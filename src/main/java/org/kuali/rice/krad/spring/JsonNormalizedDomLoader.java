package org.kuali.rice.krad.spring;

import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.xc.DomElementJsonDeserializer;
import org.w3c.dom.Element;

/**
 * Parses a horrible verbose normalized JSON representation of DOM
 */
public class JsonNormalizedDomLoader extends JsonDomLoader {
    @Override
    protected JsonDeserializer<? extends Element> getDeserializer() {
        return new DomElementJsonDeserializer();
    }
}