package com.swe.EduMeter.model;

// https://docs.oracle.com/en/java/javase/17/language/records.html
public record Token(String userHash, long expiresAt, boolean isAdmin) {}