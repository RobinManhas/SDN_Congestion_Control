package net.floodlightcontroller.project;

import net.floodlightcontroller.restserver.RestletRoutable;
import org.restlet.Context;
import org.restlet.routing.Router;

public class StatsWebRoutable implements RestletRoutable{
    /**
     * Create the Restlet router and bind to the proper resources.
     */
    protected static final String ENABLE_STR = "enable";
    protected static final String DISABLE_STR = "disable";
    protected static final String IP_STR = "myIp";
    protected static final String PEER_IP_STR = "peerIp";
    protected static final String LAST_IDX = "lastIdx";
    protected static final String MAX_ENTRY = "maxEntry";
    @Override
    public Router getRestlet(Context context) {
        Router router = new Router(context);
        router.attach("/action/enable", StatsEnabler.class); // enable disable stats
        router.attach("/stats/{"+IP_STR+"}/{"+PEER_IP_STR+"}/{"+LAST_IDX+"}/{"+MAX_ENTRY+"}", StatsResource.class);
        return router;
    }

    /**
     * Set the base path for the Quality of Service
     */
    @Override
    public String basePath() {
        return "/wm/project";
    }
}
