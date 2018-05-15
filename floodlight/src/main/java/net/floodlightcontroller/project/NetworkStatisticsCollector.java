package net.floodlightcontroller.project;

import net.floodlightcontroller.core.*;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.restserver.IRestApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/*
register nw stats collector module to floodlight, proper initializations
rest api interface enabler, start nw poller, poll and collect stats every 50 ms.
circular buffer storing 5 seconds worth of data (100 data points) done
rest api get stats logic to parse circular buffer, serialize to json and return
 */

public class NetworkStatisticsCollector implements NWStatsService, IFloodlightModule {
    public static volatile boolean statsEnabled;
    protected IFloodlightProviderService floodlightProvider;
    protected IRestApiService restApi;
    protected static Logger logger;
    public NetworkStatisticsCollector(){
        statsEnabled = false;
    }

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleServices() {
        Collection<Class<? extends IFloodlightService>> l =
                new ArrayList<Class<? extends IFloodlightService>>();
        l.add(NWStatsService.class);
        return l;
    }

    @Override
    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
        Map<Class<? extends IFloodlightService>,
                IFloodlightService> m =
                new HashMap<Class<? extends IFloodlightService>,
                        IFloodlightService>();
        // We are the class that implements the service
        m.put(NWStatsService.class, this);
        return m;
    }

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
        //This module should depend on FloodlightProviderService, IRestApiService
        Collection<Class<? extends IFloodlightService>> l =
                new ArrayList<Class<? extends IFloodlightService>>();
        l.add(IFloodlightProviderService.class);
        l.add(IRestApiService.class);
        return l;
    }

    @Override
    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
        logger = LoggerFactory.getLogger(NetworkStatisticsCollector.class);
        restApi = context.getServiceImpl(IRestApiService.class);
        System.out.println("Robin: Init called");
    }

    @Override
    public void startUp(FloodlightModuleContext context) {
        restApi.addRestletRoutable(new StatsWebRoutable());
        System.out.println("Robin: Start called");
    }

    @Override
    public void enableStats() {
        statsEnabled = true;
        NetworkPoller poller = new NetworkPoller();
        poller.start();
    }

    @Override
    public void disableStats() {
        statsEnabled = false;
    }

    @Override
    public List<TableEntry> collectStats(String src, String dst, int idx, int max) {
        return null;
    }
}
