package org.kuali.rice.krtrain.book;

import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.view.ViewModel;

/**
 */
public class BookEntryViewHelper extends ViewHelperServiceImpl {

    private static final long serialVersionUID = -4072724616744666228L;

    public void setupAuthorBlogLink(Link link, ViewModel model) {
        link.setLinkText("Author Blog");
        link.setHref("http://www.goodreads.com/author_blogs");
    }
}
