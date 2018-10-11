package mandelbrot;

/**
 * Static class that can be used to validate whether a string is an integer, a double or even
 * a ComplexNumber (in the form a+bi). This is used within SettingsFrame to validate whether
 * the values that have been entered are suitable.
 */

public class RegexManager {

    /**Regex constants that can be matched with an input string*/
    private static final String validUnsignedInteger = "[0-9]+";
    private static final String validSignedInteger = "[+-]?" + validUnsignedInteger;
    private static final String validUnsignedDouble = validUnsignedInteger + "(\\.[0-9]+)?";
    private static final String validSignedDouble = "[+-]?" + validUnsignedDouble;
    private static final String validComplexNumber = "(" + validSignedDouble + "[+-]" + validUnsignedInteger + "i \\|" + validSignedDouble + "\\|" + validSignedDouble+ "i)";

    /**Decided whether the string is a valid positive integer*/
    public static boolean matchesUnsignedInteger(String input) {

        return input.matches(validUnsignedInteger);

    }

    /**Decides whether the string is a valid integer*/
    public static boolean matchesSignedInteger(String input) {

        return input.matches(validSignedInteger);

    }

    /**Decides whether the string is a valid double*/
    public static boolean matchesUnsignedDouble(String input) {

        return input.matches(validUnsignedDouble);

    }

    /**Decides whether the string is a valid double*/
    public static boolean matchesSignedDouble(String input) {

        return input.matches(validSignedDouble);

    }

    /**Decides whether the string is a valid complex number*/
    public static boolean matchesComplexNumber(String input) {

        return input.matches(validComplexNumber);

    }

}
