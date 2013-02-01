package org.kuali.rice.krad.spring;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.xc.DomElementJsonDeserializer;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/**
 * Parses a horrible verbose normalized JSON representation of DOM
 */
public class JsonConciseDomLoader extends JsonDomLoader {
    @Override
    protected JsonDeserializer<? extends Element> getDeserializer() {
        return new JsonConciseDomDeserializer();
    }
}