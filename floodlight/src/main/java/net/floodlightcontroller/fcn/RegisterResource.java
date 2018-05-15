package net.floodlightcontroller.fcn;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import net.floodlightcontroller.staticentry.web.SFPEntryMap;

public class RegisterResource extends ServerResource {

	@Get("json")
	public SFPEntryMap register() {
		System.out.println("helloooooooooooooooooooo");

		return null;
	}

}
