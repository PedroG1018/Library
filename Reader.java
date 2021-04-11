/**
 * Name: Pedro Gutierrez Jr.
 * Date: 24 Feb 2020
 * Assignment: Project 01 Part 02/04: Reader.java
 */

import java.util.ArrayList;
import java.util.List;

public class Reader {
    public static final int CARD_NUMBER_ = 0;
    public static final int NAME_= 1;
    public static final int PHONE_ = 2;
    public static final int BOOK_COUNT_ = 3;
    public static final int BOOK_START_ = 4;

    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books = new ArrayList<Book>();

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
    }

    public Code addBook(Book book) {
        if(hasBook(book)) {
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        } else {
            books.add(book);
            return Code.SUCCESS;
        }
    }

    public Code removeBook(Book book) {
        try {
            if (!hasBook(book)) {
                return Code.READER_DOESNT_HAVE_BOOK_ERROR;
            } else {
                books.remove(book);
                return Code.SUCCESS;
            }
        } catch (Exception e) {
            return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
        }
    }

    public boolean hasBook(Book book) {
        if (books.contains(book)) {
            return true;
        } else {
            return false;
        }
    }

    public int getBookCount() {
        return books.size();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;

        if (getCardNumber() != reader.getCardNumber()) return false;
        if (!getName().equals(reader.getName())) return false;
        return getPhone().equals(reader.getPhone());
    }

    @Override
    public int hashCode() {
        int result = getCardNumber();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getPhone().hashCode();
        return result;
    }

    @Override
    public String toString() {
        String titles = "";

        for(int i = 0; i < books.size(); i++) {
            if(i < books.size()-1) {
                titles += books.get(i).toString() + ", ";
                //titles += books.get(i).getTitle() + ", ";
            } else {
                titles += books.get(i).toString();
                //titles += books.get(i).getTitle();
            }
        }

        return name + " (#" + cardNumber + ") has checked out [" + titles + "]";
    }
}
