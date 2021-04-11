import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @Test
    void testConstructor() {
        Reader reader = null;
        assertNull(reader);
        reader = new Reader(0, "", "");
        assertNotNull(reader);
    }

    @Test
    void testGetterAndField() {
        int cardNumber = 1247;
        String name = "Cole Phelps";
        String phone = "555-555-5555";
        Reader reader = new Reader(cardNumber, name, phone);
        assertEquals(cardNumber, reader.getCardNumber());
        assertEquals(name, reader.getName());
        assertEquals(phone, reader.getPhone());
    }

    @Test
    void testEquals() {
        Reader reader1 = new Reader(1247, "Cole Phelps", "555-555-5555");
        Reader reader2 = new Reader(007, "James Bond", "777-777-7777");
        assertNotEquals(reader1, reader2);
        reader1 = new Reader(007, "James Bond", "777-777-7777");
        assertEquals(reader1, reader2);
    }

    @Test
    void testSetter() {
        int cardNumber1 = 1247;
        String name1 = "Cole Phelps";
        String phone1 = "555-555-5555";
        Reader reader1 = new Reader(cardNumber1, name1, phone1);

        int cardNumber2 = 007;
        String name2 = "James Bond";
        String phone2 = "777-777-7777";

        reader1.setCardNumber(cardNumber2);
        reader1.setName(name2);
        reader1.setPhone(phone2);

        assertNotEquals(cardNumber2, cardNumber1);
        assertNotEquals(name2, name1);
        assertNotEquals(phone2, phone1);

        assertEquals(cardNumber2, reader1.getCardNumber());
        assertEquals(name2, reader1.getName());
        assertEquals(phone2, reader1.getPhone());
    }

    @Test
    void addBook_test() {
        Reader reader = new Reader(1247, "Cole Phelps", "555-555-5555");
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        assertEquals(reader.addBook(book), Code.SUCCESS);
        assertNotEquals(reader.addBook(book), Code.SUCCESS);
        assertEquals(reader.addBook(book), Code.BOOK_ALREADY_CHECKED_OUT_ERROR);
    }


    @Test
    void getBookCount_Test() {
        Reader reader = new Reader(1247, "Cole Phelps", "555-555-5555");
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        assertEquals(reader.getBookCount(), 0);
        reader.addBook(book);
        assertEquals(reader.getBookCount(), 1);
        reader.removeBook(book);
        assertEquals(reader.getBookCount(), 0);
    }

    @Test
    void hasBook_test() {
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        Reader reader = new Reader(0, "", "");
        assertFalse(reader.hasBook(book));
        reader.addBook(book);
        assertTrue(reader.hasBook(book));
    }

    @Test
    void removeBook_test() {
        Reader reader = new Reader(1247, "Cole Phelps", "555-555-5555");
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        assertEquals(reader.removeBook(book), Code.READER_DOESNT_HAVE_BOOK_ERROR);
        reader.addBook(book);
        assertEquals(reader.removeBook(book), Code.SUCCESS);
    }
}
