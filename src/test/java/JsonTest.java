import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.spring.JsonApplicationContext;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

   /* private static String[] KRAD_DD = {
        "classpath:BootStrapSpringBeans.xml",
        "classpath:org/kuali/rice/kns/bo/datadictionary/DataDictionaryBaseTypes.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/AdHocRoutePerson.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/AdHocRouteWorkgroup.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/Attachment.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/AttributeReferenceDummy.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/AttributeReferenceElements.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/BusinessObjectAttributeEntry.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/DataDictionaryBaseTypes.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/DocumentHeader.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/Note.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/NoteType.xml",
        "classpath:org/kuali/rice/krad/bo/datadictionary/PessimisticLock.xml",
        "classpath:org/kuali/rice/krad/uif/UifActionDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifViewPageDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifHeaderFooterDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifGroupDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifLayoutManagerDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifMaintenanceDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifDocumentDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifInquiryDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifLookupDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifIncidentReportDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifFieldDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifControlDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifWidgetDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifConfigurationDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifRiceDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifElementDefinitions.xml",
        "classpath:org/kuali/rice/krad/uif/UifInitiateDocumentInfoDefinitions.xml",
    };  */

    @Test
    public void test_krad_dom() {
        ApplicationContext krad = new ClassPathXmlApplicationContext("classpath:BootStrapSpringBeans.xml");

        /*ApplicationContext krad = new ClassPathXmlApplicationContext(KRAD_DD);
        JsonApplicationContext ac = new JsonApplicationContext(krad);
        ac.setFormat(JsonApplicationContext.JsonFormat.CONCISE);
        ac.setConfigLocation("classpath:org/kuali/rice/krtrain/book/BookEntryViewTest.json");
        ac.refresh();

        Assert.assertEquals(InputField.class, ac.getBean("averageReview").getClass());   */
        DataDictionaryService dds = (DataDictionaryService) GlobalResourceLoader.getService("dataDictionaryService");
        View view = dds.getViewById("Krtrain-BookEntryViewMenu");
        Assert.assertNotNull(view);
        System.err.println(view);
        Assert.assertEquals("Book Entry Menu IN JSON", view.getHeaderText());
    }
}
