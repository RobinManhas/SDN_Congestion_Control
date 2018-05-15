package net.floodlightcontroller.fcn;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NSEntryListSerializer extends JsonSerializer<NSEntryList> {
    @Override
    public void serialize(NSEntryList em, JsonGenerator jGen, SerializerProvider serializer)
            throws IOException, JsonProcessingException {
        jGen.configure(Feature.WRITE_NUMBERS_AS_STRINGS, true);
 
        if (em == null) {
            jGen.writeStartObject();
            jGen.writeString("No flows have been added to the Static Entry Pusher.");
            jGen.writeEndObject();
            return;
        }
 
        List<NSEntry> list = em.getList();
 
        jGen.writeStartObject();
//        for (NSEntry ns : list) {
//               
//                    jGen.writeArrayFieldStart(dpid);
//                    for (String name : theMap.get(dpid).keySet()) {
//                        jGen.writeStartObject();
//                        jGen.writeFieldName(name);
//                        if (theMap.get(dpid).get(name) instanceof OFFlowMod) {
//                            OFFlowModSerializer.serializeFlowMod(jGen, (OFFlowMod) theMap.get(dpid).get(name));
//                        } else if (theMap.get(dpid).get(name) instanceof OFGroupMod) {
//                            OFGroupModSerializer.serializeGroupMod(jGen, (OFGroupMod) theMap.get(dpid).get(name));
//                        }
//                        jGen.writeEndObject();
//                    }   
//                    jGen.writeEndArray();
//                }
            
        
        jGen.writeEndObject();
    }
}
