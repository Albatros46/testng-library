package com.servet.library.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.testng.annotations.DataProvider;

/** Birden fazla test sınıfının kullanabileceği DataProvider'lar (dataProviderClass ile çağrılır). */
public final class TestData {

    private TestData() {
    }

    /** CSV dosyasındaki her satır bir test çalıştırması olur. */
    @DataProvider(name = "isbnFromCsv")
    public static Iterator<Object[]> isbnFromCsv() {
        InputStream in = Objects.requireNonNull(TestData.class.getResourceAsStream("/isbn-data.csv"),
                "isbn-data.csv bulunamadı");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            List<Object[]> rows = reader.lines()
                    .skip(1) // başlık satırı
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(",", 3))
                    .map(cols -> new Object[] { cols[0], Boolean.parseBoolean(cols[1]), cols[2] })
                    .toList();
            return rows.iterator();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Geçersiz e-posta adresleri. */
    @DataProvider(name = "invalidEmails")
    public static Object[][] invalidEmails() {
        return new Object[][] {
                { null },
                { "" },
                { "servet" },
                { "servet@" },
                { "@example.com" },
                { "servet@example" },
                { "ser vet@example.com" }
        };
    }
}
