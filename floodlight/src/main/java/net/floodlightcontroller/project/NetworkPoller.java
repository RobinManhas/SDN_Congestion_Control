package net.floodlightcontroller.project;

import java.io.IOException;

public class NetworkPoller extends Thread{

    public static String execCmd(String cmd) throws java.io.IOException {
        java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void run(){
        while(NetworkStatisticsCollector.statsEnabled){
            System.out.println("Network poller running");

            // query each link present to get information
            try {
                System.out.println("value:"+execCmd("sudo tc -s qdisc ls dev s1-eth1"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // store that information in circular buffer

            // sleep for the desired time (50 milliseconds)
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            NetworkStatisticsCollector.statsEnabled = false;
        }
    }
}