import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testConstructor() {
        Book book = null;
        assertNull(book);
        book = new Book("", "", "", 0, "", LocalDate.of(0000, 1, 1));
        assertNotNull(book);
    }

    @Test
    void testGetterAndField() {
        String mISBN = "978-1586422387";
        String mTitle = "'I Heard You Paint Houses";
        String mSubject = "Memoir";
        int mPageCount = 384;
        String mAuthor = "Charles Brandt";
        LocalDate date = LocalDate.of(2005, 1, 1);
        Book book = new Book(mISBN, mTitle, mSubject, mPageCount, mAuthor, date);
        assertEquals(mISBN, book.getISBN());
        assertEquals(mTitle, book.getTitle());
        assertEquals(mSubject, book.getSubject());
        assertEquals(mPageCount, book.getPageCount());
        assertEquals(mAuthor, book.getAuthor());
        assertEquals(date, book.getDueDate());
    }

    @Test
    void testEquals() {
        Book book1 = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        Book book2 = new Book("470015866", "Nineteen Eighty-Four", "Dystopian", 328, "George Orwell", LocalDate.of(1949, 6, 8));
        assertNotEquals(book1, book2);
        book1 = new Book("470015866", "Nineteen Eighty-Four", "Dystopian", 328, "George Orwell", LocalDate.of(1949, 6, 8));
        assertEquals(book1, book2);
    }

    @Test
    void testSetter() {
        String mISBN1 = "978-1586422387";
        String mTitle1 = "'I Heard You Paint Houses";
        String mSubject1 = "Memoir";
        int mPageCount1 = 384;
        String mAuthor1 = "Charles Brandt";
        LocalDate date1 = LocalDate.of(2005, 1, 1);
        Book book1 = new Book(mISBN1, mTitle1, mSubject1, mPageCount1, mAuthor1, date1);

        String mISBN2 = "470015866";
        String mTitle2 = "Nineteen-Eighty Four";
        String mSubject2 = "Dystopian";
        int mPageCount2 = 328;
        String mAuthor2 = "George Orwell";
        LocalDate date2 = LocalDate.of(1949, 6, 8);
        book1.setISBN(mISBN2);
        book1.setTitle(mTitle2);
        book1.setSubject(mSubject2);
        book1.setPageCount(mPageCount2);
        book1.setAuthor(mAuthor2);
        book1.setDueDate(date2);

        assertNotEquals(mISBN2, mISBN1);
        assertNotEquals(mTitle2, mTitle1);
        assertNotEquals(mSubject2, mSubject1);
        assertNotEquals(mPageCount2, mPageCount1);
        assertNotEquals(mAuthor2, mAuthor1);
        assertNotEquals(date2, date1);

        assertEquals(mISBN2, book1.getISBN());
        assertEquals(mTitle2, book1.getTitle());
        assertEquals(mSubject2, book1.getSubject());
        assertEquals(mPageCount2, book1.getPageCount());
        assertEquals(mAuthor2, book1.getAuthor());
        assertEquals(date2, book1.getDueDate());
    }
}