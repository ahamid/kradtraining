package org.kuali.rice.krad.spring;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.ValueNode;
import org.codehaus.jackson.xc.DomElementJsonDeserializer;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.*;

/**
 * Converts JSON into DOM
 */
public class JsonConciseDomDeserializer extends DomElementJsonDeserializer {
    private static final String NS = null; //"fake namespace";

    private static interface JsonNodeMapping {
        public Collection<String> getChildFieldNames();
        public Collection<String> getAttributeFieldNames();
        public String getDefaultChildElementName();
        public String getKeyAttribute();
    }

    public static class JsonNodeMappingImpl implements JsonNodeMapping {
        private Collection<String> childFieldNames;
        private Collection<String> attributeFieldNames;
        private String defaultChildElementName;
        private String keyAttribute;

        public JsonNodeMappingImpl(String[] childFieldNames, String[] attributeFieldNames,
                                   String defaultChildElementName, String keyAttribute) {
            this.childFieldNames = Arrays.asList(childFieldNames);
            this.attributeFieldNames = Arrays.asList(attributeFieldNames);
            this.defaultChildElementName = defaultChildElementName;
            this.keyAttribute = keyAttribute;
        }

        public Collection<String> getAttributeFieldNames() {
            return attributeFieldNames;
        }

        public Collection<String> getChildFieldNames() {
            return childFieldNames;
        }

        public String getDefaultChildElementName() {
            return defaultChildElementName;
        }

        public String getKeyAttribute() {
            return keyAttribute;
        }

    }

    private static final JsonNodeMapping BEAN_NODE_MAPPING = new JsonNodeMappingImpl(
        new String[] { "constructor-arg" },
        new String[] { "_class" },
        "property",
        "id"
    );

    private static final JsonNodeMapping PROPERTY_NODE_MAPPING = new JsonNodeMappingImpl(
            new String[] { },
            new String[] { "_ref" },
            "bean",
            "name"
    );

    private static class MappedNode {
        Element element;
        Map<String, JsonNode> attributes = new LinkedHashMap<String, JsonNode>();
        Map<String, JsonNode> defaultChildren = new LinkedHashMap<String, JsonNode>();
        Map<String, JsonNode> children = new LinkedHashMap<String, JsonNode>();
    }

    protected MappedNode convertNode(Document document, JsonNode jsonNode, String elementName, String beanName, JsonNodeMapping mapping) {
        MappedNode mapped = new MappedNode();

        JsonNode attributesNode = null;

        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.getFields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            // it's really an attribute
            if ("attributes".equals(entry.getKey())) {
                attributesNode = entry.getValue();
            } else if (mapping.getAttributeFieldNames().contains(entry.getKey())) {
                mapped.attributes.put(entry.getKey().replaceFirst("^_", ""), entry.getValue());
            } else if (mapping.getChildFieldNames().contains(entry.getKey())) {
                mapped.children.put(entry.getKey(), entry.getValue());
            } else {
                mapped.defaultChildren.put(entry.getKey(), entry.getValue());
            }
        }

        if (attributesNode != null) {
            Iterator<Map.Entry<String, JsonNode>> attributes = attributesNode.getFields();
            while (attributes.hasNext()) {
                Map.Entry<String, JsonNode> attribute = attributes.next();
                if (!(attribute.getValue() instanceof ValueNode)) {
                    throw new RuntimeException("attributes must have simple values");
                }
                mapped.attributes.put(attribute.getKey(), attribute.getValue());
            }
        }

        mapped.element = document.createElementNS(NS, elementName);
        mapped.element.setAttribute(mapping.getKeyAttribute(), beanName);

        JsonNode nameNode = mapped.attributes.get(mapping.getKeyAttribute());
        if (nameNode != null) {
            mapped.element.setAttribute(mapping.getKeyAttribute(), nameNode.getTextValue());
        }

        for (Map.Entry<String, JsonNode> attr: mapped.attributes.entrySet()) {
            mapped.element.setAttribute(attr.getKey(), attr.getValue().getTextValue());
        }

        return mapped;
    }

    @Override
    protected Element fromNode(Document document, JsonNode jsonNode)
            throws IOException
    {
        Element beans = document.createElementNS(NS, "beans");
        Iterator<Map.Entry<String, JsonNode>> nodes = jsonNode.getFields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> bean = nodes.next();
            beans.appendChild(parseBean(document, bean.getValue(), bean.getKey()));
        }
        return beans;
    }

    protected Element parseProperty(Document document, String name, JsonNode jsonNode) throws IOException {
        MappedNode mapped = convertNode(document, jsonNode, "property", name, PROPERTY_NODE_MAPPING);

        if (jsonNode instanceof ValueNode) {
            mapped.element.setAttribute("value", ((ValueNode) jsonNode).getTextValue());
        } else if (jsonNode instanceof ObjectNode) {
            mapped.element.appendChild(parseBean(document, jsonNode, name));
        } else {
            // automatic conversions to list and map would be nice
            // but not today.
            throw new RuntimeException("invalid type");
        }
        return mapped.element;
    }

    protected Element parseBean(Document document, JsonNode jsonNode, String beanName)
            throws IOException
    {
        MappedNode node = convertNode(document, jsonNode, "bean", beanName, BEAN_NODE_MAPPING);

        for (Map.Entry<String, JsonNode> property: node.defaultChildren.entrySet()) {
            node.element.appendChild(parseProperty(document, property.getKey(), property.getValue()));
        }

        JsonNode constructor_arg = node.children.get("constructor-arg");
        if (constructor_arg != null) {
            Element e = document.createElementNS(NS, "constructor-arg");
            e.setAttribute("value", constructor_arg.getTextValue());
            node.element.appendChild(e);
        }

        return node.element;
    }
}
