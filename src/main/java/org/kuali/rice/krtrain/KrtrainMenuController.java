package org.kuali.rice.krtrain;

import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for the training menu view
 *
 * @author KRAD Training
 */
@Controller
@RequestMapping(value = "/menu")
public class KrtrainMenuController extends UifControllerBase {

    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new KrtrainMenuForm();
    }
}
