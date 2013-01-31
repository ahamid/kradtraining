package org.kuali.rice.krtrain.component;

import org.apache.commons.lang.ObjectUtils;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.modifier.ComponentModifier;
import org.kuali.rice.krad.uif.modifier.ComponentModifierBase;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krtrain.book.BookEntryForm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class AuditFieldModifier extends ComponentModifierBase {

    private static final long serialVersionUID = 322532509158853421L;

    @Override
    public Set<Class<? extends Component>> getSupportedComponents() {
        Set<Class<? extends Component>> set = new HashSet<Class<? extends Component>>();
        set.add(Group.class);
        return set;
    }

    @Override
    public void performModification(View view, Object o, Component component) {
        // Create a Map<String, Object> that will hold initial values for input fields.
        // Store this Map in the model (form) property ‘extensionData’ with the key ‘AuditData’. If the
        // map already exists in the model property, pull the map for comparing against
        // updated values
        BookEntryForm form = (BookEntryForm) o;
        Map<String, Object> existing = (Map<String, Object>) form.getExtensionData().get("AuditData");
        if (existing == null) {
            existing = new HashMap<String, Object>();
            form.getExtensionData().put("AuditData", existing);
        }

        // b. Check if the given component is a group
        if (component instanceof Group) {
            // and if so get all its nested input fields with
            // the call: ComponentUtils.getComponentsOfTypeDeep(group, InputField.class)
            // c. Iterate through each nested input field,
            for (InputField field: ComponentUtils.getComponentsOfTypeDeep((Group) component, InputField.class)) {
                // then get its property path and current value
                // (use ObjectPropertyUtils.getPropertyValue(model, propertyPath), and check the
                // property can be read using ObjectPropertyUtils.isReadableProperty(model,propertyPath))
                String propertyPath = field.getBindingInfo().getBindingPath(); // is this right??
                Object model = o; // is this right??
                Object currentValue = ObjectPropertyUtils.getPropertyValue(model, propertyPath);

                boolean isReadable = ObjectPropertyUtils.isReadableProperty(model, propertyPath);

                if (!isReadable) continue;

                // d. If the initial values map does not contain an entry for the property path, create an
                // entry in the initial values map, using the property path as the key and the property
                // value as the entry value

                if (!existing.containsKey(propertyPath)) {
                    existing.put(propertyPath, currentValue);
                } else {
                    // e. If the initial values map does contain an entry, get the initial value and compare to
                    // the current value (if the property is type String
                    // (ObjectPropertyUtils.getPropertyType(model, propertyPath)) treat nulls and empty
                    // strings as the same)

                    Object curValue = currentValue;
                    Object previousValue = existing.get(propertyPath);
                    if (ObjectPropertyUtils.getPropertyType(model, propertyPath) == String.class) {
                        if (((String) curValue) == null) {
                            curValue = "";
                        }
                        if (((String) previousValue) == null) {
                            previousValue = "";
                        }
                    }

                    // f. If the value has changed, add the CSS class ‘audit-changed’ to the control for the
                    // input field
                    if (!ObjectUtils.equals(previousValue, curValue)) {
                        // New Value is different than Previous Value!
                        // highlight it
                        field.getControl().getCssClasses().add("audit-changed");
                    }
                }

            }
        }
    }
}
