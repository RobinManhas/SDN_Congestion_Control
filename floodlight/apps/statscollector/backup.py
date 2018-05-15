#! /usr/bin/python

import sys
import os
import re
import time
import json
import simplejson #used to process policies and encode/decode requests
import subprocess #spawning subprocesses
import argparse
import threading
from time import sleep
############################################################################
# Robin: Current topology
############################################################################
#
#	vs                    
#	|2                     
#	S1(1)---(1)S3(2)-<< Delay added here >>-(2)S4(1)---(1)S2(2)---(1)vc
#	|3                                |3
#	pc                                ps
#
#############################################################################

# CPs for Application: (Other logic present in projectTopology.py)

# create sample topology as per the paper (done)
# add ovs switches and attach them to controller (done)
# check ingress/ egress logic based on L2 (done)
# remove previous qos or interactive queues from each switch interface to avoid issues (done)
# add ARP broadcast flooding provision on ovs switches for discoverability (done)
# a way to get switch info where vs and vc are attached (done)
# find all switches between that vs and vc switch via rest API (done)
# set the default queue bandwidth values for all links (done)
# create an interactive queue for path between vs and vc (done for path between vs and vc)
# add flow-entries between vs and vc to prioritize video traffic via queue 1 (done)
# check if video can be transferred between vs and vc (done using ffmpeg video)
# check if iperf tcp/udp possible between client and server (done, checked rate limit too)
# add new stats module in floodlight to fetch stats (done, but tc stats are 0)
# fetch bytes transmitted/queued stats from switches, delay and min data rate known (done)
# congestion control implementation ()

NST_Table = {}
NST_Table_Size = 0
NST_Table_Read_Index = -1

CURRENT_DELAY = 50 #milliseconds
SWITCH_MIN_RATES = [5,10,10,10] # in Mbits/sec

CONTROLLER_IP_PORT = 'localhost:8080'
SOURCE_IP = '10.0.0.2'
DEST_IP = '10.0.0.5'

stopFlag = False

def find_all(a_str, sub_str):
    	start = 0
    	b_starts = []
   	while True:
        	start = a_str.find(sub_str, start)
        	if start == -1: return b_starts
        	#print start
        	b_starts.append(start)
        	start += 1

def networkPoller():
	global NST_Table
	global NST_Table_Size
	global CURRENT_DELAY
	global SWITCH_MIN_RATES
	
	nodeList = ['s1','s2','s3','s4']
	ifaceList = ['s1-eth1','s2-eth2','s3-eth2','s4-eth1']
	while not stopFlag:
		# get stats of switch
		counter = 0
		link_entry_list = [4] #fmt: [no. of links (4),link-s1,link-s2,link-s3,link-s4 stats]

		while counter < 4:
			interface_stats = [50] #fmt: [dur (50 ms) , bytes tx, bytes queued, min rate, delay] 
			queuecmd = "sudo ovs-ofctl queue-stats %s %s"%((nodeList[counter]),(ifaceList[counter]))
			queueNum = 0
			q_res = os.popen(queuecmd).read().split('\n') 
			for line in q_res:
				spaceTok = line.split(' ')
				if len(spaceTok) > 6:
					bytes = spaceTok[6].split('=')[1].rstrip(",")
					if queueNum == 2:
						if counter == 2: # switch 3 - switch 4 link has 50 msec delay
							interface_stats.extend([bytes,0,SWITCH_MIN_RATES[counter],50])
						else:
							interface_stats.extend([bytes,0,SWITCH_MIN_RATES[counter],0])
					print "Switch %s sent %s bytes from queue %s"%((nodeList[counter]),(bytes),(queueNum))
				queueNum+=1
			print "appending: ",interface_stats
			link_entry_list.append(interface_stats)	
			counter+=1
			
		NST_Table[NST_Table_Size] = link_entry_list
		NST_Table_Size += 1		
		sleep(2)

def stopPoller():
	global stopFlag;
	stopFlag = True

def fetchInterfaces():
	return
# Robin: Enable this logic to dynamically fetch all interfaces from controller..
	#command = "curl -s http://%s/wm/device/?ipv4=%s" % (CONTROLLER_IP_PORT, SOURCE_IP)
	#result = os.popen(command).read()
	#parsedResult = json.loads(result)
	#sourceSwitch = parsedResult[0]['attachmentPoint'][0]['switchDPID']
	#sourcePort = parsedResult[0]['attachmentPoint'][0]['port']
	#print "source switch DPID: ",sourceSwitch," ,port: ",sourcePort

	#command = "curl -s http://%s/wm/device/?ipv4=%s" % ('localhost:8080', DEST_IP)
	#result = os.popen(command).read()
	#parsedResult = json.loads(result)
	#destSwitch = parsedResult[0]['attachmentPoint'][0]['switchDPID']
	#destPort = parsedResult[0]['attachmentPoint'][0]['port']
	#print "dest switch DPID: ",destSwitch," ,port: ",destPort

	# RM: fetch route between these 2 switches
	#command = "curl -s http://%s/wm/topology/route/%s/%s/%s/%s/json" % (CONTROLLER_IP_PORT, sourceSwitch, sourcePort, destSwitch, destPort)
	#result = os.popen(command).read()
	#parsedResult = json.loads(result)
	#print result

def initializeInterfacesQueues():
	# set link qos to 5 mbps between switch 1 port 1
	max5mbps = ['s1-eth1','s2-eth1','s3-eth1','s3-eth2','s4-eth1','s4-eth2']
	max10mbps = ['s1-eth2','s1-eth3','s2-eth2','s2-eth3']
	for str in max5mbps:
		queuecmd = "sudo ovs-vsctl -- set port %s qos=@defaultqos -- --id=@defaultqos create qos type=linux-htb other-config:max-rate=5000000 queues=0=@q0,1=@q1 -- --id=@q0 create queue other-config:min-rate=5000000 other-config:max-rate=5000000 -- --id=@q1 create queue other-config:max-rate=5000000 other-config:min-rate=5000000"%(str)
		q_res = os.popen(queuecmd).read()
	
	for str in max10mbps:
		queuecmd = "sudo ovs-vsctl -- set port %s qos=@defaultqos -- --id=@defaultqos create qos type=linux-htb other-config:max-rate=10000000 queues=0=@q0,1=@q1 -- --id=@q0 create queue other-config:min-rate=10000000 other-config:max-rate=10000000 -- --id=@q1 create queue other-config:max-rate=10000000 other-config:min-rate=10000000"%(str)
		q_res = os.popen(queuecmd).read()

def addVideoFlowToSwitches():

	cmd = "ovs-ofctl add-flow s1 dl_type=0x0800,nw_proto=6,nw_src=10.0.0.2,nw_dst=10.0.0.5,actions=enqueue:1:1"
	q_res = os.popen(cmd).read()
	cmd = "ovs-ofctl add-flow s2 dl_type=0x0800,nw_proto=6,nw_src=10.0.0.2,nw_dst=10.0.0.5,actions=enqueue:2:1"
	q_res = os.popen(cmd).read()
	cmd = "ovs-ofctl add-flow s3 dl_type=0x0800,nw_proto=6,nw_src=10.0.0.2,nw_dst=10.0.0.5,actions=enqueue:2:1"
	q_res = os.popen(cmd).read()
	cmd = "ovs-ofctl add-flow s4 dl_type=0x0800,nw_proto=6,nw_src=10.0.0.2,nw_dst=10.0.0.5,actions=enqueue:1:1"
	q_res = os.popen(cmd).read()

def main():
	# initialize interfaces
	initializeInterfacesQueues()
	
	# from src= video server to video client, add to interactive queue.
	addVideoFlowToSwitches()

	# start a new thread for collecting stats every 50 ms
	pollerThread = threading.Thread(target=networkPoller)
	pollerThread.start()

if  __name__ == "__main__" :
	print "calling main"
	main()
	sleep(1)
	stopPoller() # RM TODO: For now stopping thread as killing threads after process exit 				not possible

