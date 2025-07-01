package ru.neoflex.deal.util;

public class StringPatterns {
    public static final String EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String PASSPORT_SERIES = "[\\d]{4}";
    public static final String PASSPORT_NUMBER = "[\\d]{6}";
    public static final String ACCOUNT_NUMBER = "[\\d]{20}";
    public static final String INN = "^([\\d]{10}|[\\d]{12})$";
}
