/**
 * Name: Pedro Gutierrez Jr.
 * Date: 7 March 2021
 * Assignment: Project 01 Part 03/04: Shelf.java
 */

import java.time.LocalDate;
import java.util.HashMap;

public class Shelf {
    public static final int SHELF_NUMBER_= 0;
    public static final int SUBJECT_ = 1;

    private int shelfNumber;
    private String subject;
    private HashMap<Book, Integer> books = new HashMap<>();

    public Shelf() {

    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (getShelfNumber() != shelf.getShelfNumber()) return false;
        return getSubject().equals(shelf.getSubject());
    }

    @Override
    public int hashCode() {
        int result = getShelfNumber();
        result = 31 * result + getSubject().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getShelfNumber() + " : " + getSubject();
    }

    public int getBookCount(Book book) {
        if(!books.containsKey(book)) {
            return -1;
        } else {
            return books.get(book);
        }
    }

    public Code addBook(Book book) {
        if(books.containsKey(book)) {
            books.replace(book, books.get(book), books.get(book) + 1);
            System.out.println(book + " added to shelf " + toString());
            return Code.SUCCESS;
        } else if(!books.containsKey(book) && getSubject().equals(book.getSubject())) {
            books.put(book, 1);
            System.out.println(book + " added to shelf " + toString());
            return Code.SUCCESS;
        } else {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    public Code removeBook(Book book) {
        if(!books.containsKey(book)){
            System.out.println(book.getTitle() + " is not on shelf " + getSubject());
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if (books.containsKey(book) && books.get(book) == 0) {
            System.out.println("No copies of " + book.getTitle() + " remain on shelf " + getSubject());
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else {
            books.replace(book, books.get(book), books.get(book) - 1);
            System.out.println(book + " successfully removed from shelf " + getSubject());
            return Code.SUCCESS;
        }
    }

    public String listBooks() {
        String bookList = "on shelf: " + toString() + "\n";
        int count = 0;

        for(Book book : getBooks().keySet()) {
            if(getBookCount(book) > 0) {
                bookList = bookList + book + " " + getBookCount(book) + "\n";
                count += getBookCount(book);
            }
        }

        if(count == 1) {
            String finalBookList = count + " book " + bookList;
            return finalBookList;
        } else {
            String finalBookList = count + " books " + bookList;
            return finalBookList;
        }
    }
}
