package com.servet.library;

/** ISBN-13 doğrulama: 13 rakam + son hane kontrol hanesi (checksum). */
public final class IsbnValidator {

    private IsbnValidator() {
    }

    public static boolean isValid(String isbn) {
        if (isbn == null) {
            return false;
        }
        String digits = isbn.replace("-", "");
        if (!digits.matches("\\d{13}")) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = digits.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - sum % 10) % 10;
        return checkDigit == digits.charAt(12) - '0';
    }
}