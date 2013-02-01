import java.util.Collection;
import java.util.Map;

/**
 */
public class TestBean {
    private String string;
    private TestBean childA;
    private TestBean childB;
    private Map<String, Object> map;
    private Collection<Object> list;

    public TestBean() {}

    public TestBean(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public TestBean getChildA() {
        return childA;
    }

    public void setChildA(TestBean childA) {
        this.childA = childA;
    }

    public TestBean getChildB() {
        return childB;
    }

    public void setChildB(TestBean childB) {
        this.childB = childB;
    }

    public Collection<Object> getList() {
        return list;
    }

    public void setList(Collection<Object> list) {
        this.list = list;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}

