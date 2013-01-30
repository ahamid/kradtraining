import org.codehaus.jackson.map.util.JSONPObject;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 */
public class Test {
    @org.junit.Test
    public void test() throws Exception {
        XMLStreamReader streamReader = new MappedDomDocumentParser(new JSONPObject("{\"alice\":{\"bob\": \"a\"}}"));
        XMLEventReader eventReader = new WorkingXMLEventReader(streamReader);

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        XMLEventWriter eventWriter = XMLOutputFactory.newInstance().createXMLEventWriter(new DOMResult(document));
        eventWriter.add(eventReader);
        eventWriter.close();

// test
        Source testSource = new DOMSource(document);
        Result testResult = new StreamResult(System.out);
        TransformerFactory.newInstance().newTransformer().transform(testSource, testResult);
    }
}
