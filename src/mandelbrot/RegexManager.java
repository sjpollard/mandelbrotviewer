package mandelbrot;

/**
 * Static class that can be used to ate whether a string is an integer, a double or even
 * a ComplexNumber (in the form a+bi). This is used within SettingsFrame to ate whether
 * the values that have been entered are suitable.
 */

public class RegexManager {

    /**Regex constants that can be matched with an input string*/
    private static final String unsignedInteger = "[0-9]+";
    private static final String signedInteger = "[+-]?" + unsignedInteger;
    private static final String unsignedDouble = unsignedInteger + "(\\.[0-9]+)?(E" + signedInteger + ")?";
    private static final String signedDouble = "[+-]?" + unsignedDouble;
    private static final String ComplexNumber = "(" + signedDouble + "[+-]" + unsignedDouble + "i|" +  signedDouble + "i?)";

    /**Decided whether the string is a  positive integer*/
    public static boolean matchesUnsignedInteger(String input) {

        return input.matches(unsignedInteger);

    }

    /**Decides whether the string is a  integer*/
    public static boolean matchesSignedInteger(String input) {

        return input.matches(signedInteger);

    }

    /**Decides whether the string is a  positive double*/
    public static boolean matchesUnsignedDouble(String input) {

        return input.matches(unsignedDouble);

    }

    /**Decides whether the string is a  double*/
    public static boolean matchesSignedDouble(String input) {

        return input.matches(signedDouble);

    }

    /**Decides whether the string is a  complex number*/
    public static boolean matchesComplexNumber(String input) {

        return input.matches(ComplexNumber);

    }

}
