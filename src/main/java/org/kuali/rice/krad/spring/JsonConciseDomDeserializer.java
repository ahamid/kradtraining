package org.kuali.rice.krad.spring;

import com.google.common.collect.Lists;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
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

    @Override
    protected Element fromNode(Document document, JsonNode jsonNode)
            throws IOException
    {
        Element beans = document.createElementNS(NS, "beans");
        Iterator<Map.Entry<String, JsonNode>> nodes = jsonNode.getFields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> bean = nodes.next();
            JsonNode node = bean.getValue();
            assertNodeType(node, ObjectNode.class);
            beans.appendChild(parseBean(document, (ObjectNode) node, bean.getKey()));
        }
        return beans;
    }

    protected static void assertNodeType(JsonNode node, Class<? extends JsonNode> type) {
        if (!type.isAssignableFrom(node.getClass())) throw new RuntimeException("Expected " + type + " node but got " + node.getClass());
    }

    protected void setAttrib(ObjectNode parent, Map<String, String> attribs, String key, JsonNode value) {
        assertNodeType(value, ValueNode.class);
        attribs.put(key.replaceFirst("^_", ""), value.getTextValue());
        parent.remove(key);
    }

    protected Map<String, String> extractObjectAttributes(ObjectNode jsonNode, String[] fieldsToHoistArr, String attribField) {
        if (fieldsToHoistArr == null) fieldsToHoistArr = new String[0];
        if (attribField == null) attribField = "attributes";

        Map<String, String> attribs = new HashMap<String, String>();
        Collection<String> fieldsToHost = Arrays.asList(fieldsToHoistArr);

        ObjectNode attribsNode = null;

        Collection<Map.Entry<String, JsonNode>> fields = Lists.newArrayList(jsonNode.getFields());

        for (Map.Entry<String, JsonNode> entry: fields) {
            // it's really an attribute
            if (attribField.equals(entry.getKey())) {
                assertNodeType(entry.getValue(), ObjectNode.class);
                attribsNode = (ObjectNode) entry.getValue();
                jsonNode.remove(attribField);
            } else if (fieldsToHost.contains(entry.getKey())) {
                setAttrib(jsonNode, attribs, entry.getKey(), entry.getValue());
            }
        }

        if (attribsNode != null) {
            Collection<Map.Entry<String, JsonNode>> attributes = Lists.newArrayList(attribsNode.getFields());
            for (Map.Entry<String, JsonNode> attrib: attributes) {
                setAttrib(jsonNode, attribs, attrib.getKey(), attrib.getValue());
            }
        }

        return attribs;
    }

    protected Element parseBean(Document document, ObjectNode jsonNode, String beanName) {
        Element bean = document.createElementNS(NS, "bean");

        Map<String, String> attribs = extractObjectAttributes(jsonNode, new String[] { "_class", "_id", "_parent"  }, "attributes");

        // name attr overrides key
        beanName = StringUtils.defaultString(attribs.get("name"), beanName);
        beanName = StringUtils.defaultString(attribs.get("id"), beanName);
        if (!StringUtils.isBlank(beanName)) bean.setAttribute("id", beanName);
        if (!StringUtils.isBlank(attribs.get("class"))) bean.setAttribute("class", attribs.get("class"));
        if (!StringUtils.isBlank(attribs.get("parent"))) bean.setAttribute("parent", attribs.get("parent"));

        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.getFields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> property = fields.next();
            bean.appendChild(parseProperty(document, property.getValue(), property.getKey()));
        }

        return bean;
    }

    protected Element createInjectedElement(Document document, JsonNode node, String elementName) {
        Element e = document.createElementNS(NS, elementName);
        e.appendChild(parseNode(document, node, null));
        return e;
    }

    protected Element createValueElement(Document document, ValueNode node) {
        Element e = document.createElementNS(NS, "value");
        e.appendChild(document.createTextNode(node.getTextValue()));
        return e;
    }

    protected Element parseProperty(Document document, JsonNode jsonNode, String key) {
        if ("constructor-arg".equals(key)) {
            return createInjectedElement(document, jsonNode, "constructor-arg");
        } else {
            Element property = createInjectedElement(document, jsonNode, "property");
            property.setAttribute("name", key);
            return property;
        }
    }

    protected Element parseNode(Document document, JsonNode jsonNode, String name) {
        if (jsonNode instanceof ValueNode) {
            return createValueElement(document, (ValueNode) jsonNode);
        } else if (jsonNode instanceof ObjectNode) {
            return parseObject(document, (ObjectNode) jsonNode, name);
        } else if (jsonNode instanceof ArrayNode) {
            return parseArray(document, (ArrayNode) jsonNode);
        } else {
            throw new RuntimeException("Unknown node type: " + jsonNode.getClass());
        }
    }

    protected Element parseArray(Document document, JsonNode node) {
       return parseArray(document, node, false);
    }

    protected Element parseArray(Document document, JsonNode node, boolean merge) {
        Element list = document.createElementNS(NS, "list");

        if (node instanceof ObjectNode) {
            if (node.get("merge") != null) {
                merge = Boolean.valueOf(node.get("merge").getTextValue());
            }
            node = node.get("values");
        }

        if (merge) {
            list.setAttribute("merge", "true");
        }

        assertNodeType(node, ArrayNode.class);

        Iterator<JsonNode> items = node.getElements();
        while (items.hasNext()) {
            list.appendChild(parseNode(document, items.next(), null));
        }
        return list;
    }

    protected Element parseMap(Document document, ObjectNode node) {
        return parseMap(document, node, false);
    }

    protected Element parseMap(Document document, ObjectNode node, boolean merge) {
        Element map = document.createElementNS(NS, "map");

        Collection<Map.Entry<String, JsonNode>> entries = Lists.newArrayList(node.getFields());
        if (entries.size() == 2 && node.get("values") != null && node.get("merge") != null) {
            merge = Boolean.valueOf(node.get("merge").getTextValue());
            entries = Lists.newArrayList(node.get("values").getFields());
        }

        if (merge) {
            map.setAttribute("merge", "true");
        }

        for (Map.Entry<String, JsonNode> entryField: entries) {
            Element entry = document.createElementNS(NS, "entry");
            entry.setAttribute("key", entryField.getKey());
            entry.appendChild(parseNode(document, entryField.getValue(), entryField.getKey()));
            map.appendChild(entry);
        }
        return map;
    }

    protected Element parseObject(Document document, ObjectNode jsonNode, String name) {
        // object could be a ref, list, map or a bean
        List<Map.Entry<String, JsonNode>> fields = Lists.newArrayList(jsonNode.getFields());
        if (fields.size() == 1 && jsonNode.get("_ref") != null) {
            // it's a ref
            JsonNode ref = jsonNode.get("_ref");
            assertNodeType(ref, ValueNode.class);
            Element e = document.createElementNS(NS, "ref");
            e.setAttribute("bean", ref.getTextValue());
            return e;
        } else if (fields.size() == 1 && jsonNode.get("map") != null) {
            JsonNode node = jsonNode.get("map");
            assertNodeType(node, ObjectNode.class);
            return parseMap(document, (ObjectNode) node);
        } else if (fields.size() == 1 && jsonNode.get("merge-map") != null) {
            JsonNode node = jsonNode.get("map");
            assertNodeType(node, ObjectNode.class);
            return parseMap(document, (ObjectNode) node, true);
        } else if (fields.size() == 1 &&  jsonNode.get("list") != null) {
            JsonNode node = jsonNode.get("list");
            return parseArray(document, node);
        } else if (fields.size() == 1 &&  jsonNode.get("merge-list") != null) {
            JsonNode node = jsonNode.get("list");
            return parseArray(document, node, true);
        } else {
            if (jsonNode.get("_class") != null || jsonNode.get("_parent") != null) {
                return parseBean(document, jsonNode, name);
            } else {
                return parseMap(document, jsonNode);
            }


        }
    }
}
