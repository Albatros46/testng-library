package com.servet.library;

public record Book(String isbn, String title, String author) {

    public Book {
        if (!IsbnValidator.isValid(isbn)) {
            throw new IllegalArgumentException("Geçersiz ISBN: " + isbn);
        }
    }
}