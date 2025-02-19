package put.edu.ctfgame.bank.util;

import java.security.SecureRandom;

public class AccountNumberGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final int ACCOUNT_NUMBER_LENGTH = 11;

    public static String generateAccountNumberWithCheckDigit() {
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }

        int checkDigit = LuhnCheckDigitGenerator.calculateLuhnCheckDigit(accountNumber.toString());
        accountNumber.append(checkDigit);

        return accountNumber.toString();
    }
}
