package org.kuali.rice.krtrain.component;

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.view.View;

import java.util.List;

/**
 */
public class BookGroup extends Group {
    private static final long serialVersionUID = -339646333256501392L;

    private Booklet booklet;

    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> list = super.getComponentsForLifecycle();
        list.add(booklet);
        return list;
    }

    public void setBooklet(Booklet booklet) {
        this.booklet = booklet;
    }

    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);
        List<String> l = getLayoutManager().getCssClasses();
        l.add("b-load");
        for (Component component: getItems()) {
            component.getCssClasses().add("b-loadPage");
        }
        setOnDocumentReadyScript("jQuery('#" +   getId() + "').booklet(" + booklet.getTemplateOptionsJSString() + ");");
    }
}
