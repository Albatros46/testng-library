package com.servet.library;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Uçtan uca ödünç alma akışı: priority + dependsOnMethods.
 *
 * DİKKAT: TestNG, JUnit'ten farklı olarak bir sınıftaki tüm testler için TEK bir instance kullanır.
 * Bu yüzden 'library' alanı testler arasında paylaşılır ve akış adım adım ilerler.
 */
public class BorrowFlowTest {

    private static final String EMAIL = "servet@example.com";
    private static final String ISBN = "9780134685991";

    private Library library;

    @BeforeClass(alwaysRun = true)
    public void setUpLibrary() {
        library = new Library();
        library.addBook(new Book(ISBN, "Effective Java", "Joshua Bloch"));
    }

    @Test(priority = 1, groups = { "smoke", "regression" })
    public void registerMember() {
        library.registerMember("Servet", EMAIL);

        assertTrue(library.isMember(EMAIL));
    }

    @Test(priority = 2, dependsOnMethods = "registerMember", groups = { "smoke", "regression" })
    public void borrowBook() {
        library.borrow(EMAIL, ISBN);

        assertEquals(library.borrowedBooks(EMAIL).size(), 1);
        assertTrue(library.availableBooks().isEmpty());
    }

    @Test(priority = 3, dependsOnMethods = "borrowBook", groups = "regression",
            expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = ".*zaten ödünç verilmiş.*")
    public void sameBookCannotBeBorrowedTwice() {
        library.borrow(EMAIL, ISBN);
    }

    @Test(priority = 4, dependsOnMethods = "borrowBook", groups = { "smoke", "regression" })
    public void giveBackBook() {
        library.giveBack(EMAIL, ISBN);

        assertTrue(library.borrowedBooks(EMAIL).isEmpty());
        assertEquals(library.availableBooks().size(), 1);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        library = null;
    }
}
