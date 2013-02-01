package org.kuali.rice.krad.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import java.io.IOException;

/**
 */
public class JsonApplicationContext extends AbstractRefreshableConfigApplicationContext {
    public static enum JsonFormat {
        DOM,
        CONCISE
    }

    private JsonFormat format = JsonFormat.DOM;

    public JsonApplicationContext() {
        super();
    }

    public JsonApplicationContext(ApplicationContext parent) {
        super(parent);
    }

    public void setFormat(JsonFormat format) {
        this.format = format;
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        DocumentLoader loader;
        if (this.format == JsonFormat.DOM) {
            loader = new JsonNormalizedDomLoader();
        } else {
            loader = new JsonConciseDomLoader();
        }
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.setDocumentLoader(loader);
        for (String location: getConfigLocations()) {
            reader.loadBeanDefinitions(location);
        }
    }
}