package com.servet.library;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.servet.library.data.TestData;

/** @DataProvider'ın 3 kullanım şekli. */
public class IsbnValidatorTest {

    // 1) Aynı sınıf içinde, Object[][] döndüren DataProvider
    @DataProvider(name = "validIsbns")
    public Object[][] validIsbns() {
        return new Object[][] {
                { "978-0-306-40615-7" },
                { "9780134685991" },
                { "978-3-16-148410-0" }
        };
    }

    @Test(dataProvider = "validIsbns", groups = { "smoke", "regression" })
    public void acceptsValidIsbn(String isbn) {
        assertTrue(IsbnValidator.isValid(isbn), isbn + " geçerli olmalı");
    }

    // 2) parallel = true → her satır ayrı bir thread'de çalışabilir
    @DataProvider(name = "invalidIsbns", parallel = true)
    public Object[][] invalidIsbns() {
        return new Object[][] {
                { null },
                { "" },
                { "9780306406158" },   // yanlış kontrol hanesi
                { "978030640615" },    // 12 hane
                { "97803064061577" },  // 14 hane
                { "978-0-306-4061X-7" } // harf
        };
    }

    @Test(dataProvider = "invalidIsbns", groups = "regression")
    public void rejectsInvalidIsbn(String isbn) {
        assertFalse(IsbnValidator.isValid(isbn), isbn + " geçersiz olmalı");
    }

    // 3) Başka bir sınıftaki, CSV dosyasından okuyan DataProvider
    @Test(dataProvider = "isbnFromCsv", dataProviderClass = TestData.class, groups = "regression")
    public void isbnFromCsvFile(String isbn, boolean expectedValid, String description) {
        assertEquals(IsbnValidator.isValid(isbn), expectedValid, description);
    }
}
