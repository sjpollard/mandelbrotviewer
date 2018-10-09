package mandelbrot;

import java.util.regex.Pattern;

public class RegexManager {

    static final String validInteger = "-?[0-9]+";
    static final String validDouble = validInteger + "\\.[0-9]+";
    static final String validComplexNumber = validDouble + "(\\+|-)[0-9]+\\.[0-9]+i";

    public static boolean matchesInteger(String input) {

        return input.matches(validInteger);

    }

    public static boolean matchesDouble(String input) {

        return input.matches(validDouble);

    }

    public static boolean matchesComplexNumber(String input) {

        return input.matches(validComplexNumber);

    }

}
