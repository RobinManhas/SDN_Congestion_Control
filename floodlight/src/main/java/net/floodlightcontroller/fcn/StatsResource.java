package net.floodlightcontroller.fcn;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import net.floodlightcontroller.staticentry.web.SFPEntryMap;

public class StatsResource extends ServerResource {
    
     //"/stats/{myIp}/{peerIp}/{lastIdx}/{maxEntry}/json"
    @Get("json")
    public NSEntryList ListStaticEntries() {
//        IStaticEntryPusherService sfpService =
//                (IStaticEntryPusherService)getContext().getAttributes().
//                    get(IStaticEntryPusherService.class.getCanonicalName());

	 String srcIP = (String) getRequestAttributes().get("myIp");
     String destIP = (String) getRequestAttributes().get("peerIp");
     String lastIdx = (String) getRequestAttributes().get("lastIdx");
     String maxEntry= (String) getRequestAttributes().get("maxEntry");
     System.out.println(srcIP + destIP+lastIdx+maxEntry);
     LinkEntry l1 = new LinkEntry(1,2);
     List<LinkEntry> list = new ArrayList<>();
     list.add(l1);
     NSEntry n1 = new NSEntry(1,1,list);
     List<NSEntry> result = new ArrayList<>();
     result.add(n1) ;    
        
        return new NSEntryList(result);
    }
}
