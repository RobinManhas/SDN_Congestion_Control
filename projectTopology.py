__author__ = 'Robin'
from mininet.node import CPULimitedHost
from mininet.topo import Topo
from mininet.net import Mininet
from mininet.log import setLogLevel, info
from mininet.node import RemoteController
from mininet.cli import CLI
from mininet.link import TCLink, TCIntf
from mininet.util import custom

CONTROLLER_IP = "127.0.0.1"
CONTROLLER_PORT = 6653 

def projectTopology():
    #intf = custom( TCIntf, bw=10 ) # 10 Mbps
    net = Mininet(topo=None,controller=None)#,intf=intf)
    controller = net.addController(name='c0',
    	controller=RemoteController,ip=CONTROLLER_IP,port=CONTROLLER_PORT)
    # Add hosts and switches
    videoServer = net.addHost('vs', ip='10.0.0.2', mac='00:00:00:00:00:01')
    perfServer = net.addHost('ps', ip='10.0.0.3')
    perfClient = net.addHost('pc', ip='10.0.0.4')
    videoClient = net.addHost('vc', ip='10.0.0.5', mac='00:00:00:00:00:02')
    switch1 = net.addSwitch('s1')
    switch2 = net.addSwitch('s2')
    switch3 = net.addSwitch('s3')
    switch4 = net.addSwitch('s4')

    # Add links

    net.addLink(switch1,switch3)
    net.addLink(switch1,videoServer)
    net.addLink(switch1,perfClient)

    net.addLink(switch2,switch4)
    net.addLink(switch2,videoClient)
    net.addLink(switch2,perfServer)
    #net.addLink(switch2,perfClient); # switch 2 with sdn controller and dhcp server

    net.addLink(switch3,switch4)
    net.start()

    # clear pre existing qos and queues RM TODO: modify to a loop later
    videoServer.cmd('ovs-vsctl --all destroy qos')
    videoServer.cmd('ovs-vsctl --all destroy queue')
    videoClient.cmd('ovs-vsctl --all destroy qos')
    videoClient.cmd('ovs-vsctl --all destroy queue')
    perfServer.cmd('ovs-vsctl --all destroy qos')
    perfServer.cmd('ovs-vsctl --all destroy queue')
    perfClient.cmd('ovs-vsctl --all destroy qos')
    perfClient.cmd('ovs-vsctl --all destroy queue')
    switch1.cmd('ovs-vsctl --all destroy qos')
    switch1.cmd('ovs-vsctl --all destroy queue')
    switch2.cmd('ovs-vsctl --all destroy qos')
    switch2.cmd('ovs-vsctl --all destroy queue')
    switch3.cmd('ovs-vsctl --all destroy qos')
    switch3.cmd('ovs-vsctl --all destroy queue')
    switch4.cmd('ovs-vsctl --all destroy qos')
    switch4.cmd('ovs-vsctl --all destroy queue')
    print "Previous qos and queues removed successfully"

    CLI(net)
    net.stop()    

# if the script is run directly (sudo custom/optical.py):
if __name__ == '__main__':
    setLogLevel('info')
    projectTopology()
