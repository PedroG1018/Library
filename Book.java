/**
 * Name: Pedro Gutierrez Jr.
 * Date: 13 Feb 2020
 * Assignment: Project 01 Part 01/04: Book.java
 */

import java.time.LocalDate;

public class Book {
    public static final int ISBN_ = 0;
    public static final int TITLE_ = 1;
    public static final int SUBJECT_ = 2;
    public static final int PAGE_COUNT_ = 3;
    public static final int AUTHOR_ = 4;
    public static final int DUE_DATE_ = 5;

    private String mISBN;
    private String mTitle;
    private String mSubject;
    private int mPageCount;
    private String mAuthor;
    private LocalDate mDueDate;

    public Book(String mISBN, String mTitle, String mSubject, int mPageCount, String mAuthor, LocalDate mDueDate) {
        this.mISBN = mISBN;
        this.mTitle = mTitle;
        this.mSubject = mSubject;
        this.mPageCount = mPageCount;
        this.mAuthor = mAuthor;
        this.mDueDate = mDueDate;
    }

    public String getISBN() {
        return mISBN;
    }

    public void setISBN(String mISBN) {
        this.mISBN = mISBN;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int mPageCount) {
        this.mPageCount = mPageCount;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public LocalDate getDueDate() {
        return mDueDate;
    }

    public void setDueDate(LocalDate mDueDate) {
        this.mDueDate = mDueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (mPageCount != book.mPageCount) return false;
        if (!mISBN.equals(book.mISBN)) return false;
        if (!mTitle.equals(book.mTitle)) return false;
        if (!mSubject.equals(book.mSubject)) return false;
        return mAuthor.equals(book.mAuthor);
    }

    @Override
    public int hashCode() {
        int result = mISBN.hashCode();
        result = 31 * result + mTitle.hashCode();
        result = 31 * result + mSubject.hashCode();
        result = 31 * result + mPageCount;
        result = 31 * result + mAuthor.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return mTitle + " by " + mAuthor + " ISBN: " + mISBN;
    }
}
