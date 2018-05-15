package net.floodlightcontroller.project;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsResource extends ServerResource{
    protected static Logger log = LoggerFactory.getLogger(StatsResource.class);

    @Get("json")
    public Object handleRequest() {
        NWStatsService statisticsService = (NWStatsService) getContext().getAttributes().get(NWStatsService.class.getCanonicalName());

        String ip = (String) getRequestAttributes().get(StatsWebRoutable.IP_STR);
        String peer = (String) getRequestAttributes().get(StatsWebRoutable.PEER_IP_STR);
        String lastIndex = (String) getRequestAttributes().get(StatsWebRoutable.LAST_IDX);
        String max = (String) getRequestAttributes().get(StatsWebRoutable.MAX_ENTRY);

        statisticsService.collectStats(ip,peer,Integer.valueOf(lastIndex),Integer.valueOf(max));
        return "{\"Request type\":\"Unknown, proper type: /wm/project/stats/...\"}";
    }
}
