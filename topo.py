"""Custom topology example

Two directly connected switches plus a host for each switch:

   host --- switch --- switch --- host

Adding the 'topos' dict with a key/value pair to generate our newly defined
topology enables one to pass in '--topo=mytopo' from the command line.
"""

from mininet.topo import Topo
from mininet.link import TCLink

class MyTopo( Topo ):
    "Simple topology example."

    def __init__( self ):
        "Create custom topo."

        # Initialize topology
        Topo.__init__( self )

        # Add hosts and switches
    	# Add hosts and switches
	videoServer = self.addHost('vs', ip='10.0.0.2', mac='00:00:00:00:00:01')
	perfServer = self.addHost('ps', ip='10.0.0.3')
	perfClient = self.addHost('pc', ip='10.0.0.4')
	videoClient = self.addHost('vc', ip='10.0.0.5', mac='00:00:00:00:00:02')
	switch1 = self.addSwitch('s1')
	switch2 = self.addSwitch('s2')
	switch3 = self.addSwitch('s3')
	switch4 = self.addSwitch('s4')

    	# Add links

	self.addLink(switch1,switch3,use_htb=True)
	self.addLink(switch1,videoServer,use_htb=True)
	self.addLink(switch1,perfClient,use_htb=True)

	self.addLink(switch2,switch4,use_htb=True)
	self.addLink(switch2,videoClient,use_htb=True)
	self.addLink(switch2,perfServer,use_htb=True)
    	#net.addLink(switch2,perfClient); # switch 2 with sdn controller and dhcp server

	self.addLink(switch3,switch4,use_htb=True)


topos = { 'mytopo': ( lambda: MyTopo() ) }
