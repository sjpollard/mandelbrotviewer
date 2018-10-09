package mandelbrot;

/**
 * Static class that can be used to validate whether a string is an integer, a double or even
 * a ComplexNumber (in the form a+bi). This is used within SettingsFrame to validate whether
 * the values that have been entered are suitable.
 */

public class RegexManager {

    /**Regex constants that can be matched with an input string*/
    private static final String validInteger = "-?[0-9]+";
    private static final String validDouble = validInteger + "\\.[0-9]+";
    private static final String validComplexNumber = validDouble + "(\\+|-)[0-9]+\\.[0-9]+i";

    /**Decides whether the string is a valid integer*/
    public static boolean matchesInteger(String input) {

        return input.matches(validInteger);

    }

    /**Decides whether the string is a valid double*/
    public static boolean matchesDouble(String input) {

        return input.matches(validDouble);

    }

    /**Decides whether the string is a valid complex number*/
    public static boolean matchesComplexNumber(String input) {

        return input.matches(validComplexNumber);

    }

}
