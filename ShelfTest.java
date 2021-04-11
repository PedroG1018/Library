/**
 * Name: Pedro Gutierrez Jr.
 * Date: 7 March 2021
 * Assignment: Project 01 Part 03/04: ShelfTest.java
 */

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void testConstructor() {
        Shelf shelf = null;
        assertNull(shelf);
        shelf = new Shelf();
        assertNotNull(shelf);
    }

    @Test
    void testGetterAndField() {
        int shelfNumber = 1;
        String subject = "Test";
        HashMap<Book, Integer> books = new HashMap<>();
        Shelf shelf = new Shelf();
        shelf.setShelfNumber(shelfNumber);
        shelf.setSubject(subject);
        shelf.setBooks(books);

        assertEquals(shelfNumber, shelf.getShelfNumber());
        assertEquals(subject, shelf.getSubject());
        assertEquals(books, shelf.getBooks());
    }

    @Test
    void testEquals() {
        Shelf shelf1 = null;
        Shelf shelf2 = new Shelf();
        assertNotEquals(shelf1, shelf2);
        shelf2 = null;
        assertEquals(shelf1, shelf2);
    }

    @Test
    void testSetter() {
        int shelfNumber1 = 1;
        String subject1 = "Test";
        HashMap<Book, Integer> books1 = new HashMap<>();
        Shelf shelf = new Shelf();

        int shelfNumber2 = 2;
        String subject2 = "Test Again";
        Book book = new Book("", "", "", 0, "", LocalDate.of(0000, 1, 1));
        HashMap<Book, Integer> books2 = new HashMap<Book, Integer>();
        books2.put(book, 1);

        shelf.setShelfNumber(shelfNumber2);
        shelf.setSubject(subject2);
        shelf.setBooks(books2);

        assertNotEquals(shelfNumber2, shelfNumber1);
        assertNotEquals(subject2, subject1);
        assertNotEquals(books2, books1);

        assertEquals(shelfNumber2, shelf.getShelfNumber());
        assertEquals(subject2, shelf.getSubject());
        assertEquals(books2, shelf.getBooks());
    }

    @Test
    void getBookCountTest() {
        Shelf shelf = new Shelf();
        shelf.setShelfNumber(1);
        shelf.setSubject("Memoir");
        Book book1 = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        Book book2 = new Book("470015866", "Nineteen Eighty-Four", "Dystopian", 328, "George Orwell", LocalDate.of(1949, 6, 8));
        Random rand = new Random();
        int randNum = rand.nextInt(10)+1;

        for(int i = 0; i < randNum; i++) {
            shelf.addBook(book1);
        }

        assertEquals(shelf.getBookCount(book1), randNum);
        shelf.removeBook(book1);
        assertNotEquals(shelf.getBookCount(book1), randNum);
        randNum -= 1;

        for(int i = 0; i < randNum; i++) {
            shelf.removeBook(book1);
        }

        assertNotEquals(shelf.getBookCount(book1), randNum);
        assertEquals(shelf.getBookCount(book1), 0);
        assertEquals(shelf.getBookCount(book2), -1);
    }

    @Test
    void addBookTest() {
        Book book1 = new Book("777", "Something", "IDK", 100, "Some Guy", LocalDate.of(0000, 1, 1));
        Shelf shelf = new Shelf();
        shelf.setSubject("IDK");
        shelf.setShelfNumber(0);
        assertEquals(shelf.addBook(book1), Code.SUCCESS);
        assertEquals(shelf.getBookCount(book1), 1);
        assertEquals(shelf.addBook(book1), Code.SUCCESS);
        assertEquals(shelf.getBookCount(book1), 2);
        Book book2 = new Book("470015866", "Nineteen Eighty-Four", "Dystopian", 328, "George Orwell", LocalDate.of(1949, 6, 8));
        assertEquals(shelf.addBook(book2), Code.SHELF_SUBJECT_MISMATCH_ERROR);
    }

    @Test
    void removeBookTest() {
        Book book1 = new Book("470015866", "Nineteen Eighty-Four", "Dystopian", 328, "George Orwell", LocalDate.of(1949, 6, 8));
        Shelf shelf = new Shelf();
        shelf.setSubject("IDK");
        shelf.setShelfNumber(0);
        assertEquals(shelf.removeBook(book1), Code.BOOK_NOT_IN_INVENTORY_ERROR);
        Book book2 = new Book("777", "Something", "IDK", 100, "Some Guy", LocalDate.of(0000, 1, 1));
        assertEquals(shelf.addBook(book2), Code.SUCCESS);
        assertEquals(shelf.getBookCount(book2), 1);
        assertEquals(shelf.removeBook(book2), Code.SUCCESS);
        assertEquals(shelf.getBookCount(book2), 0);
        assertEquals(shelf.removeBook(book2), Code.BOOK_NOT_IN_INVENTORY_ERROR);
        assertEquals(shelf.getBookCount(book2), 0);
    }

    @Test
    void listBooksTest() {
        Book book1 = new Book("777", "Something", "IDK", 100, "Some Guy", LocalDate.of(0000, 1, 1));
        Shelf shelf = new Shelf();
        shelf.setSubject("IDK");
        shelf.setShelfNumber(0);
        assertEquals(shelf.addBook(book1), Code.SUCCESS);
        String testListBook = "1 book on shelf: 0 : IDK\nSomething by Some Guy ISBN: 777 1\n";
        assertEquals(shelf.listBooks(), testListBook);
        shelf.addBook(book1);
        testListBook = "2 books on shelf: 0 : IDK\nSomething by Some Guy ISBN: 777 2\n";
        assertEquals(shelf.listBooks(), testListBook);
    }
}