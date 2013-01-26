package org.kuali.rice.krtrain.book;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 *
 */
public class Chapter extends PersistableBusinessObjectBase {
    private static final long serialVersionUID = 6003232280860461875L;

    private Long bookId;
    private int number;
    private String title;
    private String part;
    private String summary;
    private int numberPages;
    private boolean recap;
    private int recapPageNumber;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(int numberPages) {
        this.numberPages = numberPages;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public boolean isRecap() {
        return recap;
    }

    public void setRecap(boolean recap) {
        this.recap = recap;
    }

    public int getRecapPageNumber() {
        return recapPageNumber;
    }

    public void setRecapPageNumber(int recapPageNumber) {
        this.recapPageNumber = recapPageNumber;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
