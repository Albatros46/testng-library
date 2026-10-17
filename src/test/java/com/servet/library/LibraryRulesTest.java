package com.servet.library;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.servet.library.data.TestData;

/** Kurallar: expectedExceptions, assertThrows, SoftAssert, timeOut, invocationCount, enabled. */
public class LibraryRulesTest {

    private static final String EMAIL = "ayse@example.com";

    private Library library;

    /** @BeforeMethod → her testten önce yeni bir kütüphane (JUnit'teki @BeforeEach). */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        library = new Library();
        library.registerMember("Ayşe", EMAIL);
        library.addBook(new Book("978-0-306-40615-7", "Kitap 1", "Yazar A"));
        library.addBook(new Book("9780134685991", "Kitap 2", "Yazar B"));
        library.addBook(new Book("9781617294945", "Kitap 3", "Yazar C"));
        library.addBook(new Book("9780596009205", "Kitap 4", "Yazar D"));
    }

    @Test(groups = "regression")
    public void borrowLimitIsThree() {
        library.borrow(EMAIL, "978-0-306-40615-7");
        library.borrow(EMAIL, "9780134685991");
        library.borrow(EMAIL, "9781617294945");

        assertThrows(IllegalStateException.class, () -> library.borrow(EMAIL, "9780596009205"));
    }

    @Test(groups = "regression", expectedExceptions = IllegalStateException.class)
    public void nonMemberCannotBorrow() {
        library.borrow("yabanci@example.com", "9780134685991");
    }

    @Test(groups = "regression", expectedExceptions = IllegalArgumentException.class)
    public void bookWithInvalidIsbnCannotBeCreated() {
        new Book("123", "Hatalı", "Yazar");
    }

    @Test(dataProvider = "invalidEmails", dataProviderClass = TestData.class, groups = "regression",
            expectedExceptions = IllegalArgumentException.class)
    public void invalidEmailIsRejected(String email) {
        library.registerMember("Test", email);
    }

    /** SoftAssert: tüm kontrolleri çalıştırır, hataları en sonda assertAll() ile topluca raporlar. */
    @Test(groups = { "smoke", "regression" })
    public void newLibraryState() {
        SoftAssert soft = new SoftAssert();

        soft.assertEquals(library.availableBooks().size(), 4, "müsait kitap sayısı");
        soft.assertTrue(library.isMember(EMAIL), "üye kayıtlı olmalı");
        soft.assertTrue(library.borrowedBooks(EMAIL).isEmpty(), "ödünç kitap olmamalı");
        soft.assertTrue(library.findByIsbn("9780134685991").isPresent(), "kitap bulunmalı");

        soft.assertAll(); // Bu satır unutulursa hiçbir hata raporlanmaz!
    }

    /** timeOut: 500 ms'yi aşarsa test başarısız sayılır. */
    @Test(groups = "performance", timeOut = 500)
    public void findByIsbnIsFast() {
        for (int i = 0; i < 100_000; i++) {
            library.findByIsbn("9780134685991");
        }
    }

    /** invocationCount: aynı test 5 kez çalışır (flaky test avı için kullanışlı). */
    @Test(groups = "regression", invocationCount = 5)
    public void availableBooksIsStable() {
        assertEquals(library.availableBooks().size(), 4);
    }

    /** enabled = false → test atlanır ve raporlarda hiç görünmez. */
    @Test(enabled = false, description = "Rezervasyon özelliği henüz yok")
    public void reserveBook() {
    }
}
