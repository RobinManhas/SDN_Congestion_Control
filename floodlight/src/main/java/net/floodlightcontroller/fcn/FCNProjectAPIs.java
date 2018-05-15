package net.floodlightcontroller.fcn;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import net.floodlightcontroller.restserver.RestletRoutable;
import net.floodlightcontroller.staticentry.web.SFPEntryMap;

public class FCNProjectAPIs implements RestletRoutable {

	@Override
	public Restlet getRestlet(Context context) {
		Router router = new Router(context);
		router.attach("/register", RegisterResource.class);
		router.attach("/stats/{myIp}/{peerIp}/{lastIdx}/{maxEntry}/json", StatsResource.class);
		return router;
	}

	@Override
	public String basePath() {
		return "/wm/fcn";
	}		
	
}
