/**
 * Name: Pedro Gutierrez Jr.
 * Date: 24 March 2021
 * Assignment: Project 01 Part 04/04: Library.java
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;

public class Library {
    public static final int LENDING_LIMIT = 5;

    private String name;
    private static int libraryCard;
    private List<Reader> readers = new ArrayList<>();
    private HashMap<String, Shelf> shelves = new HashMap<>();
    private HashMap<Book, Integer> books = new HashMap<>();

    public Library(String name) {
        this.name = name;
    }

    public static int getLendingLimit() {
        return LENDING_LIMIT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getLibraryCard() {
        return libraryCard;
    }

    public static void setLibraryCard(int libraryCard) {
        Library.libraryCard = libraryCard;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public void setReaders(List<Reader> readers) {
        this.readers = readers;
    }

    public HashMap<String, Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(HashMap<String, Shelf> shelves) {
        this.shelves = shelves;
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

        Library library = (Library) o;

        if (!getName().equals(library.getName())) return false;
        if (!getReaders().equals(library.getReaders())) return false;
        if (!getShelves().equals(library.getShelves())) return false;
        return getBooks().equals(library.getBooks());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getReaders().hashCode();
        result = 31 * result + getShelves().hashCode();
        result = 31 * result + getBooks().hashCode();
        return result;
    }

    public Code init(String filename) {
        File f = new File(filename);
        Scanner scan = null;
        int bookCount = 0;
        int readerCount = 0;
        int shelfCount = 0;

        try {
            scan = new Scanner(f);
            bookCount = convertInt(scan.nextLine(), Code.BOOK_COUNT_ERROR);
            System.out.println(initBooks(bookCount, scan));
            listBooks();
            shelfCount = convertInt(scan.nextLine(), Code.SHELF_COUNT_ERROR);
            System.out.println(initShelves(shelfCount, scan));
            listShelves(true);
            readerCount = convertInt(scan.nextLine(), Code.READER_COUNT_ERROR);
            System.out.println(initReader(readerCount, scan));
            listReaders();

        } catch(FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }

        return Code.SUCCESS;
    }

    private Code initBooks(int bookCount, Scanner scan) {
        if(bookCount < 1) {
            return Code.LIBRARY_ERROR;
        }

        System.out.println("parsing " + bookCount + " books");

        String line = "";

        for(int i = 0; i < bookCount; i++) {
            line = scan.nextLine();
            System.out.println("parsing book: " + line);
            String[] bookObj = line.split(",");
            int count = convertInt(bookObj[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
            LocalDate date = convertDate(bookObj[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
            if(count <= 0) {
                return errorCode(count);
            }
            if(date == null) {
                return Code.DATE_CONVERSION_ERROR;
            }

            Book book = new Book(bookObj[Book.ISBN_], bookObj[Book.TITLE_], bookObj[Book.SUBJECT_], count, bookObj[Book.AUTHOR_], date);
            addBook(book);
        }

        return Code.SUCCESS;
    }

    private Code initShelves(int shelfCount, Scanner scan) {
        if(shelfCount < 1) {
            return Code.SHELF_COUNT_ERROR;
        }

        System.out.println("parsing " + shelfCount + " shelves");

        String line = "";

        for(int i = 0; i < shelfCount; i++) {
            line = scan.nextLine();
            System.out.println("Parsing Shelf : " + line);
            String[] shelfObj = line.split(",");
            int shelfNum = convertInt(shelfObj[Shelf.SHELF_NUMBER_], Code.SHELF_NUMBER_PARSE_ERROR);
            if(shelfNum < 0) {
                return errorCode(shelfNum);
            }

            addShelf(shelfObj[Shelf.SUBJECT_]);
        }

        for(Book book : books.keySet()) {
            for(int i = 0; i < books.get(book); i++) {
                addBookToShelf(book, shelves.get(book.getSubject()));
            }
        }

        if(shelves.size() == shelfCount) {
            return Code.SUCCESS;
        } else {
            System.out.println("Number of shelves doesn't match expected");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }
    }

    private Code initReader(int readerCount, Scanner scan) {
        if(readerCount <= 0) {
            return Code.READER_COUNT_ERROR;
        }

        String line = "";

        for(int i = 0; i < readerCount; i++) {
            line = scan.nextLine();
            String[] readerObj = line.split(",");
            int cardNum = convertInt(readerObj[Reader.CARD_NUMBER_], Code.READER_CARD_NUMBER_ERROR);
            int bookCountNum = convertInt(readerObj[Reader.BOOK_COUNT_], Code.BOOK_COUNT_ERROR);

            if(cardNum < 0) {
                return errorCode(cardNum);
            }
            if(bookCountNum < 0) {
                return errorCode(bookCountNum);
            }

            Reader reader = new Reader(cardNum, readerObj[Reader.NAME_], readerObj[Reader.PHONE_]);
            readers.add(reader);

            for(int j = Reader.BOOK_START_; j < readerObj.length; j = j + 2) {
                if(getBookByISBN(readerObj[j]) == null) {
                    System.out.println("ERROR");
                } else {
                    System.out.println(checkOutBook(reader, getBookByISBN(readerObj[j])));
                }
                convertDate(readerObj[j+1], Code.DATE_CONVERSION_ERROR);
            }
        }

        return Code.SUCCESS;
    }

    public Code addBook(Book newBook) {
        if(books.containsKey(newBook)) {
            books.replace(newBook, books.get(newBook), books.get(newBook) + 1);
            System.out.println(books.get(newBook) + " copies of " + newBook.getTitle() + " in the stacks");
        } else {
            books.put(newBook, 1);
            System.out.println(newBook + " added to the stacks.");
        }

        if(shelves.containsKey(newBook.getSubject())) {
            addBookToShelf(newBook, shelves.get(newBook.getSubject()));
            return Code.SUCCESS;
        } else {
            System.out.println("No shelf for " + newBook.getSubject() + " books");
            return Code.SHELF_EXISTS_ERROR;
        }
    }

    public Code returnBook(Reader reader, Book book) {
        if(!reader.hasBook(book)) {
            System.out.println(reader.getName() + " doesn't have " + book + " checked out");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        } else {
            System.out.println(reader.getName() + " is returning " + book);
            Code code = reader.removeBook(book);

            if(code == Code.SUCCESS) {
                returnBook(book);
                return code;
            } else {
                System.out.println("Could not return " + book);
                return code;
            }
        }
    }

    public Code returnBook(Book book) {
        for(Shelf shelf : shelves.values()) {
            if(shelf.getSubject().equals(book.getSubject())) {
                shelf.addBook(book);
                return Code.SUCCESS;
            }
        }

        System.out.println("No shelf for " + book.getTitle());
        return Code.SHELF_EXISTS_ERROR;
    }

    private Code addBookToShelf(Book book, Shelf shelf) {
        if(returnBook(book) == Code.SUCCESS) {
            return Code.SUCCESS;
        } else if (!shelf.getSubject().equals(book.getSubject())) {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }

        Code code = shelf.addBook(book);
        if(code == Code.SUCCESS) {
            System.out.println(book.getTitle() + " added to shelf");
            return Code.SUCCESS;
        } else {
            System.out.println("Could not add " + book.getTitle() + " to shelf");
            return code;
        }
    }

    public int listBooks() {
        int totalBooks = 0;
        int b = 0;

        String[] bookOutput = new String[books.size()];

        for(Book book: books.keySet()) {
            bookOutput[b] = books.get(book) + " copies of " + book;
            totalBooks += books.get(book);
            b++;
        }

        int size = bookOutput.length;
        for(int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < bookOutput.length; j++) {
                if(bookOutput[i].compareTo(bookOutput[j]) < 0) {
                    String temp = bookOutput[i];
                    bookOutput[i] = bookOutput[j];
                    bookOutput[j] = temp;
                }
            }
        }

        for(int i = 0; i < size; i++) {
            System.out.println(bookOutput[i]);
        }

        return totalBooks;
    }

    public Code checkOutBook(Reader reader, Book book) {
        if(!readers.contains(reader)) {
            System.out.println(reader.getName() + " doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(reader.getBookCount() >= LENDING_LIMIT) {
            System.out.println(reader.getName() + " has reached the lending limit, (" + LENDING_LIMIT + ")");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }
        if(!books.containsKey(book)) {
            System.out.println("ERROR: could not find " + book.getTitle());
            return Code. BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(!shelves.containsKey(book.getSubject())) {
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }
        if(shelves.get(book.getSubject()).getBookCount(book) < 1) {
            System.out.println("ERROR: no copies of " + book.getTitle() + " remain");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Code readerCode = reader.addBook(book);

        if(readerCode != Code.SUCCESS) {
            System.out.println("Couldn't checkout " + book.getTitle());
            return readerCode;
        }

        Code shelfCode = shelves.get(book.getSubject()).removeBook(book);

        if(shelfCode == Code.SUCCESS) {
            System.out.println(book + " checked out successfully");
        }

        return shelfCode;
    }

    public Book getBookByISBN(String isbn) {
        for(Book book : books.keySet()) {
            if(book.getISBN().equals(isbn)) {
                return book;
            }
        }

        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    public Code listShelves(boolean showbooks) {
        if(showbooks) {
            for(Shelf shelf : shelves.values()) {
                System.out.println(shelf.listBooks());
            }
        } else {
            String[] shelfOutput = new String[shelves.size()];
            int s = 0;

            for(Shelf shelf : shelves.values()) {
                shelfOutput[s] = shelf.toString();
                s++;
            }

            int size = shelfOutput.length;
            for(int i = 0; i < size - 1; i++) {
                for (int j = i + 1; j < shelfOutput.length; j++) {
                    if(shelfOutput[i].compareTo(shelfOutput[j]) < 0) {
                        String temp = shelfOutput[i];
                        shelfOutput[i] = shelfOutput[j];
                        shelfOutput[j] = temp;
                    }
                }
            }

            for(int i = 0; i < size; i++) {
                System.out.println(shelfOutput[i]);
            }
        }
        return Code.SUCCESS;
    }

    public Code addShelf(String shelfSubject) {
        Shelf shelf = new Shelf();
        shelf.setSubject(shelfSubject);
        shelf.setShelfNumber(shelves.size() + 1);
        addShelf(shelf);
        return Code.SUCCESS;
    }

    public Code addShelf(Shelf shelf) {
        if(shelves.containsValue(shelf)) {
            System.out.println("ERROR: Shelf already exists " + shelf.getSubject());
            return Code.SHELF_EXISTS_ERROR;
        }

        shelves.put(shelf.getSubject(), shelf);

        for(Book book : books.keySet()) {
            if(book.getSubject().equals(shelf.getSubject())) {
                for(int i = 0; i < books.get(book); i++) {
                    shelf.addBook(book);
                }
            }
        }

        return Code.SUCCESS;
    }

    public Shelf getShelf(Integer shelfNumber) {
        for(Shelf shelf : shelves.values()) {
            if(shelf.getShelfNumber() == shelfNumber) {
                return shelf;
            }
        }

        System.out.println("No shelf number " + shelfNumber + " found");
        return null;
    }

    public Shelf getShelf(String subject) {
        for(Shelf shelf : shelves.values()) {
            if(shelf.getSubject().equals(subject)) {
                return shelf;
            }
        }

        System.out.println("No shelf for " + subject + " books");
        return null;
    }

    public int listReaders() {
        int size = readers.size();

        for(int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if(readers.get(i).getCardNumber() > readers.get(j).getCardNumber()) {
                    readers.add(i, readers.get(j));
                    readers.remove(j + 1);
                }
            }
        }

        return listReaders(false);
    }

    public int listReaders(boolean showbooks) {
        if(showbooks) {
            for(int i = 0; i < readers.size(); i++) {
                System.out.println(readers.get(i).getName() + "(#" + readers.get(i).getCardNumber() + ")  has the following books:");
                System.out.println(readers.get(i).getBooks());
            }
            return readers.size();
        }

        for(int i = 0; i < readers.size(); i++) {
            System.out.println(readers.get(i).toString());
        }
        return readers.size();
    }

    public Reader getReaderByCard(int cardNumber) {
        for(int i = 0; i < readers.size(); i++) {
            if(readers.get(i).getCardNumber() == cardNumber) {
                System.out.println("Returning Reader " + readers.get(i));
                return readers.get(i);
            }
        }

        System.out.println("Could not find a reader with card #" + cardNumber);
        return null;
    }

    public Code addReader(Reader reader) {
        if(readers.contains(reader)) {
            System.out.println(reader.getName() + " already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }

        for(int i = 0; i < readers.size(); i++) {
            if(readers.get(i).getCardNumber() == reader.getCardNumber()) {
                System.out.println(readers.get(i).getName() + " and " + reader.getName() + " have the same card number!");
                return Code.READER_ALREADY_EXISTS_ERROR;
            }
        }

        readers.add(reader);
        System.out.println(reader.getName() + " added to the library!");

        if(reader.getCardNumber() > getLibraryCardNumber()) {
            libraryCard = reader.getCardNumber();
        }

        return Code.SUCCESS;
    }

    public Code removeReader(Reader reader) {
        if(readers.contains(reader) && reader.getBookCount() > 0) {
            System.out.println(reader.getName() + " must return all books!");
            return Code.READER_STILL_HAS_BOOKS_ERROR;
        } else if(!readers.contains(reader)){
            System.out.println(reader.getName() + "\nis not part of the Library");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }

        if(readers.contains(reader) && reader.getBookCount() == 0) {
            readers.remove(reader);
        }

        return Code.SUCCESS;
    }

    public int convertInt(String recordCountString, Code code) {
        try {
            return Integer.parseInt(recordCountString);

        } catch(NumberFormatException e) {
            System.out.println("Value which caused the error: " + recordCountString);
            System.out.println("Error message: " + code.getMessage());

            if(code == Code.BOOK_COUNT_ERROR) {
                System.out.println("Error: Could not read number of books");
            } else if (code == Code.PAGE_COUNT_ERROR) {
                System.out.println("Error: could not parse page count");
            } else if(code == Code.DATE_CONVERSION_ERROR) {
                System.out.println("Error: Could not parse date component");
            } else {
                System.out.println("Error: Unknown conversion error");
            }

            return code.getCode();
        }
    }

    public LocalDate convertDate(String date, Code errorCode) {
        if(date.equals("0000")) {
            return LocalDate.of(1970, 01, 01);
        }

        String[] localDate = date.split("-");

        if(localDate.length != 3) {
            System.out.println("ERROR: data conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 01, 01);
        }

        int year = convertInt(localDate[0], errorCode);
        int month = convertInt(localDate[1], errorCode);
        int day = convertInt(localDate[2], errorCode);

        if(year < 0 || month < 0 || day < 0) {
            System.out.println("Error converting date: Year " + year);
            System.out.println("Error converting date: Month " + month);
            System.out.println("Error converting date: Day " + day);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 01, 01);
        }

        return LocalDate.of(year, month, day);
    }

    public int getLibraryCardNumber() {
        return libraryCard + 1;
    }

    private Code errorCode(int codeNumber) {
        for(Code code : Code.values()) {
            if(code.getCode() == codeNumber) {
                return code;
            }
        }

        return Code.UNKNOWN_ERROR;
    }
}
