package put.edu.ctfgame.bank.util;

public class LuhnCheckDigitGenerator {
    public static int calculateLuhnCheckDigit(String accountNumberWithoutCheckDigit) {
        int sum = 0;
        boolean alternate = false;

        // Process each digit starting from the rightmost
        for (int i = accountNumberWithoutCheckDigit.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(accountNumberWithoutCheckDigit.charAt(i));

            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;  // Subtract 9 if doubling results in a number > 9
                }
            }

            sum += n;
            alternate = !alternate;  // Toggle alternate
        }

        return (sum * 9) % 10;  // The check digit is the value that makes the sum a multiple of 10
    }
}
