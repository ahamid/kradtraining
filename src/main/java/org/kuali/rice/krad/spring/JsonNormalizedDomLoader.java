package org.kuali.rice.krad.spring;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.map.ext.CoreXMLDeserializers;
import org.codehaus.jackson.map.ext.DOMDeserializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.xc.DomElementJsonDeserializer;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;

/**
 * Parses a horrible verbose normalized JSON representation of DOM
 */
public class JsonNormalizedDomLoader implements DocumentLoader {
    @Override
    public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {
        SimpleModule module = new SimpleModule("DomElementJsonDeserializer module", new Version(1, 0, 0, null));
        module.addDeserializer(Element.class, new DomElementJsonDeserializer());

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