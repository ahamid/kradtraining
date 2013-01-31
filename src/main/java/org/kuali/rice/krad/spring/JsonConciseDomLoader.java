package org.kuali.rice.krad.spring;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
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
public class JsonConciseDomLoader implements DocumentLoader {
    @Override
    public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {
        SimpleModule module = new SimpleModule("DomElementJsonDeserializer module", new Version(1, 0, 0, null));
        module.addDeserializer(Element.class, new JsonConciseDomDeserializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.registerModule(module);

        Element e = mapper.readValue(inputSource.getByteStream(), Element.class);
        // this element is not actually appended to the document
        Document doc = e.getOwnerDocument();
        doc.appendChild(e);
        return doc;
    }
}