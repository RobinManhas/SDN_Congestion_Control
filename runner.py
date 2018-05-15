#!/usr/bin/python

"""
Create a network where different switches are connected to
different controllers, by creating a custom Switch() subclass.
"""

from mininet.net import Mininet
from mininet.node import OVSSwitch, Controller, RemoteController
from mininet.topolib import TreeTopo
from mininet.log import setLogLevel
from mininet.cli import CLI
from mininet.link import TCLink
from topo import MyTopo
setLogLevel( 'info' )

# Two local and one "external" controller (which is actually c0)
# Ignore the warning message that the remote isn't (yet) running

mytopo = MyTopo()
net = Mininet( topo=mytopo, link=TCLink, build=False )
controller = net.addController(name='c0',
controller=RemoteController,ip='127.0.0.1',port=6653)
#rightSwitch = net.get('vs')
#_intf = Intf( 'eth0', node=rightSwitch)

net.build()
net.start()
CLI( net )
net.stop()
