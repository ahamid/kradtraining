package org.kuali.rice.krtrain.book;

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * View helper service for the Book Entry view
 *
 * @author KRAD Training
 */
public class BookEntryViewHelper extends ViewHelperServiceImpl {
    private static final long serialVersionUID = -1266486040584339781L;

    public void setupAuthorBlogLink(Link link, ViewModel model) {
        link.setLinkText("Author Blog");
        link.setHref("http://www.goodreads.com/author_blogs");
    }

    @Override
    public void performCustomInitialization(View view, Component component) {
        System.err.println("ADDING CUSTOM SECTION");
        super.performCustomInitialization(view, component);

        // Check if the component parameter is the page with id ‘Krtrain-BookEntryPage1’
        // (make sure to do a null check on the component first), if so continue with the
        // remaining steps
        if (component == null || !"KrTrain-BookEntryPage1".equals(component.getId())) return;

        // b. Cast the component to a org.kuali.rice.krad.uif.container.Group (name the variable
        //    ‘page’)
        Group page = (Group) component;

        // c. Get a new instance of a section (group) using a vertical box layout from
        //    org.kuali.rice.krad.uif.util.ComponentFactory
        Group section = ComponentFactory.getVerticalBoxSection();

        // d. Set the header text on the new group to ‘Top Book Awards’
        section.setHeaderText("Top Book Awards");
        // e. Next obtain a new input field using ComponentFactory. Set the property name to
        //    ‘bookAwardOne’ and the label to ‘Award One’
        InputField bookAwardOne = ComponentFactory.getInputField("bookAwardOne", "Award One");

        // f. Create another input field by copying the input field created in step e (use
        //    org.kuali.rice.krad.uif.util.ComponentUtils#copy). Set the property name to
        //    ‘bookAwardTwo’ and the label to ‘Award Two’
        InputField bookAwardTwo = ComponentUtils.copy(bookAwardOne);
        bookAwardTwo.setPropertyName("bookAwardTwo");
        bookAwardTwo.setLabel("Award Two");

        List<InputField> fields = new ArrayList<InputField>();
        fields.add(bookAwardOne);
        fields.add(bookAwardTwo);

        // g. Add the two created input fields to the items list of the group created in step c
        section.setItems(fields);

        // h. Add the group created in step c to the items list of the page (at the end)
        page.setItems(Collections.singletonList(section));
    }
}
