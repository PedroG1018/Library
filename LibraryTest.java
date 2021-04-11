import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void constructorTest() {
        Library library = null;
        assertNull(library);
        library = new Library("CSUMB");
        assertNotNull(library);
    }

    @Test
    void getterAndFieldTest() {
        int lendingLimit = 5;
        String name = "Test";
        int libraryCard = 0;
        List<Reader> readers = new ArrayList<>();
        HashMap<String, Shelf> shelves = new HashMap<>();
        HashMap<Book, Integer> books = new HashMap<>();

        Library library = new Library("Test");
        library.setName(name);
        Library.setLibraryCard(libraryCard);
        library.setReaders(readers);
        library.setShelves(shelves);
        library.setBooks(books);

        assertEquals(lendingLimit, Library.getLendingLimit());
        assertEquals(name, library.getName());
        assertEquals(libraryCard, Library.getLibraryCard());
        assertEquals(readers, library.getReaders());
        assertEquals(shelves, library.getShelves());
        assertEquals(books, library.getBooks());
    }

    @Test
    void equalsTest() {
        Library library1 = null;
        Library library2 = new Library("CSUMB");
        assertNotEquals(library1, library2);
        library1 = new Library("CSUMB");
        assertEquals(library1, library2);
    }

    @Test
    void setterTest() {
        String name1 = "Test";
        int libraryCard1 = 0;
        List<Reader> readers1 = null;
        HashMap<String, Shelf> shelves1 = null;
        HashMap<Book, Integer> books1 = null;
        Library library = new Library("Test");

        String name2 = "Test Again";
        int libraryCard2 = 1;
        List<Reader> readers2 = new ArrayList<>();
        HashMap<String, Shelf> shelves2 = new HashMap<>();
        HashMap<Book, Integer> books2 = new HashMap<>();

        library.setName(name2);
        Library.setLibraryCard(libraryCard2);
        library.setReaders(readers2);
        library.setShelves(shelves2);
        library.setBooks(books2);

        assertNotEquals(name2, name1);
        assertNotEquals(libraryCard2, libraryCard1);
        assertNotEquals(readers2, readers1);
        assertNotEquals(shelves2, shelves1);
        assertNotEquals(books2, books1);

        assertEquals(name2, library.getName());
        assertEquals(libraryCard2, Library.getLibraryCard());
        assertEquals(readers2, library.getReaders());
        assertEquals(shelves2, library.getShelves());
        assertEquals(books2, library.getBooks());
    }
    
    @Test
    void initTest() {
        Library library = new Library("CSUMB");
        assertEquals(library.init("someFile"), Code.FILE_NOT_FOUND_ERROR);
        assertEquals(library.init("Library00.csv"), Code.SUCCESS);
    }

    @Test
    void addBookTest() {
        HashMap<Book, Integer> books = new HashMap<>();
        HashMap<String, Shelf> shelves = new HashMap<>();
        Library library = new Library("CSUMB");
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        Shelf shelf = new Shelf();

        library.addBook(book);

        assertEquals(book, library.getBookByISBN(book.getISBN()));
        int count = library.getBooks().get(book);
        assertEquals(count, 1);

        library.addBook(book);

        count = library.getBooks().get(book);
        assertEquals(count, 2);

        library.addShelf(shelf);
        assertEquals(library.addBook(book), Code.SHELF_EXISTS_ERROR);

        shelf.setSubject("Memoir");
        library.addShelf(shelf.getSubject());
        assertEquals(library.addBook(book), Code.SUCCESS);
    }

    @Test
    void returnBookTest1() {
        Library library = new Library("CSUMB");
        Reader reader = new Reader(1247, "Cole Phelps", "555-555-5555");
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));

        library.addBook(book);
        library.addReader(reader);
        assertEquals(library.returnBook(reader, book), Code.READER_DOESNT_HAVE_BOOK_ERROR);

        reader.addBook(book);
        assertNotEquals(library.returnBook(reader, book), Code.READER_DOESNT_HAVE_BOOK_ERROR);
        assertNotEquals(library.returnBook(reader, book), Code.SUCCESS);
        reader.addBook(book);
        assertEquals(library.returnBook(reader, book), Code.SUCCESS);
    }

    @Test
    void returnBookTest2() {
        Library library = new Library("CSUMB");
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        Shelf shelf = new Shelf();
        shelf.setSubject("IDK");

        library.addBook(book);
        library.addShelf(shelf);
        assertEquals(library.returnBook(book), Code.SHELF_EXISTS_ERROR);

        shelf.setSubject("Memoir");
        library.addShelf(shelf.getSubject());
        assertEquals(library.returnBook(book), Code.SUCCESS);
    }

    @Test
    void listBooksTest() {
        Library library = new Library("CSUMB");
        library.init("Library00.csv");
        assertEquals(library.listBooks(), 9);
        library.getBooks().clear();
        assertEquals(library.listBooks(), 0);
    }

    @Test
    void checkOutBookTest() {
        Library library = new Library("CSUMB");
        Reader reader = new Reader(1247, "Cole Phelps", "555-555-5555");
        Book book = new Book("978-1586422397", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1));
        Shelf shelf = new Shelf();
        shelf.setSubject("IDK");

        assertEquals(library.checkOutBook(reader, book), Code.READER_NOT_IN_LIBRARY_ERROR);
        library.addReader(reader);
        assertNotEquals(library.checkOutBook(reader, book), Code.READER_NOT_IN_LIBRARY_ERROR);

        assertNotEquals(library.checkOutBook(reader, book), Code.BOOK_LIMIT_REACHED_ERROR);

        assertEquals(library.checkOutBook(reader, book), Code.BOOK_NOT_IN_INVENTORY_ERROR);
        library.addBook(book);
        assertNotEquals(library.checkOutBook(reader, book), Code.BOOK_NOT_IN_INVENTORY_ERROR);

        library.addShelf(shelf.getSubject());
        assertEquals(library.checkOutBook(reader, book), Code.SHELF_EXISTS_ERROR);
        shelf.setSubject("Memoir");
        library.addShelf(shelf.getSubject());
        assertNotEquals(library.checkOutBook(reader, book), Code.SHELF_EXISTS_ERROR);

        assertEquals(library.checkOutBook(reader, book), Code.BOOK_NOT_IN_INVENTORY_ERROR);
        shelf.addBook(book);
        assertNotEquals(library.checkOutBook(reader, book), Code.SHELF_EXISTS_ERROR);

        library.addBook(book);
        assertNotEquals(library.checkOutBook(reader, book), Code.SUCCESS);
        reader.removeBook(book);
        assertEquals(library.checkOutBook(reader, book), Code.SUCCESS);

        reader.addBook(new Book("1", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1)));
        reader.addBook(new Book("2", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1)));
        reader.addBook(new Book("3", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1)));
        reader.addBook(new Book("4", "I Heard You Paint Houses", "Memoir", 384, "Charles Brandt", LocalDate.of(2005, 1, 1)));

        assertEquals(library.checkOutBook(reader, book), Code.BOOK_LIMIT_REACHED_ERROR);
    }

    @Test
    void getBookByISBNTest() {
        Library library = new Library("CSUMB");
        library.init("Library00.csv");
        Book book = new Book("1337", "Headfirst Java", "education", 1337, "Grady Booch", LocalDate.of(1970, 01, 01));

        assertNotNull(library.getBookByISBN("1337"));
        assertNull(library.getBookByISBN("10000"));
        assertEquals(library.getBookByISBN("1337"), book);
    }

    @Test
    void listShelvesTest() {
        Library library = new Library("CSUMB");
        library.init("Library00.csv");
        HashMap<String, Shelf> shelves = new HashMap<>();

        Shelf shelf1 = new Shelf();
        Shelf shelf2 = new Shelf();
        Shelf shelf3 = new Shelf();

        shelf1.setShelfNumber(1);
        shelf2.setShelfNumber(2);
        shelf3.setShelfNumber(3);
        shelf1.setSubject("sci-fi");
        shelf2.setSubject("education");
        shelf3.setSubject("Adventure");

        shelves.put("Adventure", shelf3);
        shelves.put("education", shelf2);
        shelves.put("sci-fi", shelf1);


        assertEquals(library.getShelves(), shelves);
        assertEquals(library.listShelves(true), Code.SUCCESS);
        assertEquals(library.listShelves(false), Code.SUCCESS);

        shelf1.addBook(library.getBookByISBN("34-w-34"));
        shelf1.addBook(library.getBookByISBN("34-w-34"));
        shelf1.addBook(library.getBookByISBN("42-w-87"));
        shelf1.addBook(library.getBookByISBN("42-w-87"));
        shelf1.addBook(library.getBookByISBN("42-w-87"));
        shelf1.addBook(library.getBookByISBN("42-w-87"));
        shelf2.addBook(library.getBookByISBN("1337"));
        shelf2.addBook(library.getBookByISBN("1337"));
        shelf2.addBook(library.getBookByISBN("1337"));
        shelf3.addBook(library.getBookByISBN("5297"));
        shelf3.addBook(library.getBookByISBN("5297"));

        System.out.println(library.listShelves(true));

        assertEquals(library.getShelves().get("Adventure").listBooks(), shelves.get("Adventure").listBooks());
        assertEquals(library.getShelves().get("education").listBooks(), shelves.get("education").listBooks());
        assertEquals(library.getShelves().get("sci-fi").listBooks(), shelves.get("sci-fi").listBooks());
    }

    @Test
    void addShelfTest() {
        Library library = new Library("CSUMB");
        String shelfSubject = "IDK";

        Code code = library.addShelf(shelfSubject);

        assertNotEquals(code, Code.SHELF_EXISTS_ERROR);
        assertEquals(code, Code.SUCCESS);
        assertEquals(library.getShelves().get("IDK"), library.getShelf("IDK"));

        assertEquals(library.addShelf(library.getShelf("IDK")), Code.SHELF_EXISTS_ERROR);
        assertEquals(library.getShelves().size(), 1);
    }

    @Test
    void getShelfTest() {
        Library library = new Library("CSUMB");
        Shelf shelf = new Shelf();
        shelf.setSubject("IDK");
        shelf.setShelfNumber(1);

        assertNull(library.getShelf(shelf.getShelfNumber()));
        assertNull(library.getShelf(shelf.getSubject()));

        library.addShelf(shelf.getSubject());

        assertNotNull(library.getShelf(shelf.getShelfNumber()));
        assertNotNull(library.getShelf(shelf.getSubject()));
        assertEquals(library.getShelf(shelf.getShelfNumber()), shelf);
        assertEquals(library.getShelf(shelf.getSubject()), shelf);
    }

    @Test
    void listReadersTest() {
        Library library = new Library("CSUMB");
        library.init("Library00.csv");
        Reader reader1 = new Reader(1, "Drew Clinkenbeard", "831-582-4007");
        Reader reader2 = new Reader(2, "Jennifer Clinkenbeard", "831-555-6284");
        Reader reader3 = new Reader(3, "Monte Ray", "555-555-4444");
        Reader reader4 = new Reader(4, "Laurence Fishburn", "831-582-4007");

        Book book1 = new Book("42-w-87", "Hitchhikers Guide To the Galaxy", "sci-fi", 42, "Douglas Adams", LocalDate.of(1970, 01, 01));
        Book book2 = new Book("1337", "Headfirst Java", "education", 1337, "Grady Booch", LocalDate.of(1970, 01, 01));

        reader1.addBook(book1);
        reader1.addBook(book2);
        reader2.addBook(book1);
        reader3.addBook(book1);
        reader3.addBook(book2);
        reader4.addBook(book1);
        reader4.addBook(book2);

        assertEquals(library.getReaderByCard(1).toString(), reader1.toString());
        assertEquals(library.getReaderByCard(2).toString(), reader2.toString());
        assertEquals(library.getReaderByCard(3).toString(), reader3.toString());
        assertEquals(library.getReaderByCard(4).toString(), reader4.toString());
        assertEquals(library.listReaders(), 4);
        assertEquals(library.listReaders(false), 4);
        assertEquals(library.listReaders(true), 4);
    }

    @Test
    void getReaderByCardTest() {
        Library library = new Library("CSUMB");
        Reader reader = new Reader(1, "Drew Clinkenbeard", "831-582-4007");

        assertNull(library.getReaderByCard(1));

        library.init("Library00.csv");

        assertNotNull(library.getReaderByCard(1));
        assertEquals(library.getReaderByCard(1), reader);
    }

    @Test
    void addReaderTest() {
        Library.setLibraryCard(0);
        Library library = new Library("CSUMB");
        Reader reader = new Reader(1, "Drew Clinkenbeard", "831-582-4007");

        library.init("Library00.csv");

        assertEquals(library.addReader(reader), Code.READER_ALREADY_EXISTS_ERROR);

        reader.setName("Cole Phelps");
        reader.setPhone("555-555-5555");

        assertEquals(library.addReader(reader), Code.READER_ALREADY_EXISTS_ERROR);

        reader.setCardNumber(5);

        assertEquals(library.addReader(reader), Code.SUCCESS);
        assertEquals(library.getLibraryCardNumber(), reader.getCardNumber() + 1);
    }

    @Test
    void removeReaderTest() {
        Library library = new Library("CSUMB");
        Reader reader1 = new Reader(1247, "Cole Phelps", "555-555-5555");
        Reader reader2 = new Reader(1, "Drew Clinkenbeard", "831-582-4007");

        library.init("Library00.csv");

        assertEquals(library.removeReader(library.getReaderByCard(1)), Code.READER_STILL_HAS_BOOKS_ERROR);
        assertEquals(library.removeReader(reader1), Code.READER_NOT_IN_LIBRARY_ERROR);
        reader2.removeBook(library.getBookByISBN("1337"));
        reader2.removeBook(library.getBookByISBN("42-w-87"));

        assertEquals(library.removeReader(reader2), Code.SUCCESS);
        assertEquals(library.getReaders().size(), 3);
    }

    @Test
    void convertIntTest() {
        Library library = new Library("CSUMB");
        String recordCountString = "5";
        Code code = Code.SUCCESS;

        assertEquals(library.convertInt(recordCountString, code), Integer.parseInt(recordCountString));

        recordCountString += "w";

        code = Code.BOOK_COUNT_ERROR;
        assertEquals(library.convertInt(recordCountString, code), -2);
        code = Code.PAGE_COUNT_ERROR;
        assertEquals(library.convertInt(recordCountString, code), -8);
        code = Code.DATE_CONVERSION_ERROR;
        assertEquals(library.convertInt(recordCountString, code), -101);
    }

    @Test
    void convertDateTest() {
        Library library = new Library("CSUMB");
        String date = "0000";
        Code errorCode = Code.SUCCESS;

        assertEquals(library.convertDate(date, errorCode), LocalDate.of(1970, 01, 01));

        date = "2000-10";
        String[] localDate = date.split("-");

        assertNotEquals(localDate.length, 3);
        assertEquals(library.convertDate(date, errorCode), LocalDate.of(1970, 01, 01));

        date = "2000-10-18";
        localDate = date.split("-");

        assertEquals(localDate.length, 3);

        int year = library.convertInt(localDate[0], Code.DATE_CONVERSION_ERROR);
        int month = library.convertInt(localDate[1], Code.DATE_CONVERSION_ERROR);
        int day = library.convertInt(localDate[2], Code.DATE_CONVERSION_ERROR);

        assertNotEquals(library.convertDate(date, errorCode), LocalDate.of(1970, 01, 01));
        assertEquals(library.convertDate(date, errorCode), LocalDate.of(year, month, day));
    }

    @Test
    void getLibraryCardNumberTest() {
        Library.setLibraryCard(0);
        Library library = new Library("CSUMB");
        library.init("Library00.csv");

        System.out.println(library.getLibraryCardNumber());
        assertEquals(library.getLibraryCardNumber(), 1);

        library.addReader(new Reader(5, "Some Guy", "123-456-7890"));

        assertNotEquals(library.getLibraryCardNumber(), 1);
        assertEquals(library.getLibraryCardNumber(), 6);
    }
}