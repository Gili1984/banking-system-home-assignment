package com.banking.account_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class AccountNumberGenerator {
    public static String createRandomAccountNumber() {
        LocalDateTime now = LocalDateTime.now();
        String dayPart = now.format(DateTimeFormatter.ofPattern("dd"));
        String timePart = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        Random random = new Random();
        int randomPart = random.nextInt(90) + 10;
        return dayPart + timePart + randomPart;
    }
}
