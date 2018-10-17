package mandelbrot;

/**
 * Static class that can be used to validate whether a string is an integer, a double or even
 * a ComplexNumber (in the form a+bi). This is used within SettingsFrame to validate whether
 * the values that have been entered are suitable.
 */

public class RegexManager {

    /**Regex matching with a positive integer*/
    private static final String unsignedInteger = "[0-9]+";
    /**Regex matching with a signed integer*/
    private static final String signedInteger = "[+-]?" + unsignedInteger;
    /**Regex matching with a positive double*/
    private static final String unsignedDouble = unsignedInteger + "(\\.[0-9]+)?(E" + signedInteger + ")?";
    /**Regex matching with a signed double*/
    private static final String signedDouble = "[+-]?" + unsignedDouble;
    /**Regex matching with a complex number*/
    private static final String complexNumber = "(" + signedDouble + "[+-]" + unsignedDouble + "i|" +  signedDouble + "i?)";

    /**Decided whether the string is a valid positive integer*/
    public static boolean matchesUnsignedInteger(String input) {

        return input.matches(unsignedInteger);

    }

    /**Decides whether the string is a valid integer*/
    public static boolean matchesSignedInteger(String input) {

        return input.matches(signedInteger);

    }

    /**Decides whether the string is a valid positive double*/
    public static boolean matchesUnsignedDouble(String input) {

        return input.matches(unsignedDouble);

    }

    /**Decides whether the string is a valid double*/
    public static boolean matchesSignedDouble(String input) {

        return input.matches(signedDouble);

    }

    /**Decides whether the string is a valid complex number*/
    public static boolean matchesComplexNumber(String input) {

        return input.matches(complexNumber);

    }

}
