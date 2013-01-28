package org.kuali.rice.krtrain.editors;

import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 *
 */
public class EditionPropertyEditor extends PropertyEditorSupport {
    private Integer value;

    @Override
    public String getAsText() {
        Integer value = (Integer) getValue();
        String suffixed = "Unknown";
        if (value != null) {
            String suffix;
            int val = value.intValue();
            if (val >= 10 && val <= 14) {
                suffix = "th";
            } else {
                String stringValue = value.toString();
                if (stringValue.endsWith("1")) {
                    suffix = "st";
                } else if (stringValue.endsWith("2")) {
                    suffix = "nd";
                } else if (stringValue.endsWith("3")) {
                    suffix = "rd";
                } else {
                    suffix = "th";
                }
            }
            suffixed = val + suffix;
        }
        return suffixed + " Edition";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isBlank(text)) {
            setValue(null);
            return;
        }

        String integerVal = text.replaceFirst("\\p{Alpha} Edition$", "");
        setValue(Integer.parseInt(integerVal));
    }
}
