package com.servet.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Basit bir kütüphane: kitap ekle, üye kaydet, ödünç al, iade et. */
public class Library {

    public static final int MAX_BORROWED_BOOKS = 3;

    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, String> members = new HashMap<>(); // e-posta → isim
    private final Map<String, String> borrowedBy = new HashMap<>(); // isbn → e-posta

    public void addBook(Book book) {
        if (books.putIfAbsent(book.isbn(), book) != null) {
            throw new IllegalStateException("Kitap zaten kayıtlı: " + book.isbn());
        }
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public void registerMember(String name, String email) {
        if (email == null || !email.matches("[^@\\s]+@[^@\\s]+\\.[a-z]{2,}")) {
            throw new IllegalArgumentException("Geçersiz e-posta: " + email);
        }
        if (members.putIfAbsent(email, name) != null) {
            throw new IllegalStateException("Üye zaten kayıtlı: " + email);
        }
    }

    public boolean isMember(String email) {
        return members.containsKey(email);
    }

    public void borrow(String email, String isbn) {
        if (!isMember(email)) {
            throw new IllegalStateException("Üye değil: " + email);
        }
        if (!books.containsKey(isbn)) {
            throw new IllegalArgumentException("Kitap bulunamadı: " + isbn);
        }
        if (borrowedBy.containsKey(isbn)) {
            throw new IllegalStateException("Kitap zaten ödünç verilmiş: " + isbn);
        }
        if (borrowedBooks(email).size() >= MAX_BORROWED_BOOKS) {
            throw new IllegalStateException("Ödünç alma limiti (" + MAX_BORROWED_BOOKS + ") doldu: " + email);
        }
        borrowedBy.put(isbn, email);
    }

    public void giveBack(String email, String isbn) {
        if (!email.equals(borrowedBy.get(isbn))) {
            throw new IllegalStateException(email + " bu kitabı ödünç almamış: " + isbn);
        }
        borrowedBy.remove(isbn);
    }

    public List<String> borrowedBooks(String email) {
        List<String> result = new ArrayList<>();
        borrowedBy.forEach((isbn, borrower) -> {
            if (borrower.equals(email)) {
                result.add(isbn);
            }
        });
        return result;
    }

    public List<Book> availableBooks() {
        return books.values().stream().filter(b -> !borrowedBy.containsKey(b.isbn())).toList();
    }
}