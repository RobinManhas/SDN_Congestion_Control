package net.floodlightcontroller.project;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class StatsEnabler extends ServerResource {
    protected static Logger log = LoggerFactory.getLogger(StatsEnabler.class);

    @Get("json")
    public Object handleRequest() {
        NWStatsService statisticsService = (NWStatsService) getContext().getAttributes().get(NWStatsService.class.getCanonicalName());

        if (getReference().getPath().contains(StatsWebRoutable.ENABLE_STR)) {
            statisticsService.enableStats();
            return "{\"Stat module status\":\"Enabled\"}";
        }

        if (getReference().getPath().contains(StatsWebRoutable.DISABLE_STR)) {
            statisticsService.disableStats();
            return "{\"Stat module status\":\"Disabled\"}";
        }

        return "{\"Request type\":\"Unknown, proper type: /wm/project/action/{enable/disable}\"}";
    }
}
