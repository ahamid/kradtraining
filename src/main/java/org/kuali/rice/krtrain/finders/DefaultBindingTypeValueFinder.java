package org.kuali.rice.krtrain.finders;

import org.kuali.rice.krad.valuefinder.ValueFinder;

/**
 */
public class DefaultBindingTypeValueFinder implements ValueFinder {
    @Override
    public String getValue() {
        return "SPIRAL";
    }
}
