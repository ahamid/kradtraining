import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.krad.spring.JsonApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

/**
 *
 */
public class JsonTest {
    @Test
    public void test_canonical_dom_parsing() {
        JsonApplicationContext ac = new JsonApplicationContext();
        ac.setConfigLocation("classpath:spring_dom.json");
        ac.refresh();
        Assert.assertEquals("Hola!", ((TestBean) ac.getBean("testBean")).getString());
    }

    @Test
    public void test_concise_dom_parsing() {
        JsonApplicationContext ac = new JsonApplicationContext();
        ac.setFormat(JsonApplicationContext.JsonFormat.CONCISE);
        ac.setConfigLocation("classpath:spring_concise_dom.json");
        ac.refresh();
        TestBean bean = (TestBean) ac.getBean("testBean");
        Assert.assertEquals("Hola!", bean.getString());
        TestBean childA = bean.getChildA();
        Assert.assertEquals("childA", childA.getString());
        TestBean childB = bean.getChildB();
        Assert.assertEquals("childB", childB.getString());
    }
}
