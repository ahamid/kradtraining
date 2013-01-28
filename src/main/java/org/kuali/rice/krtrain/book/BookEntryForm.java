package org.kuali.rice.krtrain.book;

import org.kuali.rice.krad.web.form.UifFormBase;

/**
 */
public class BookEntryForm extends UifFormBase {
    private static final long serialVersionUID = -5429620882125163299L;

    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
