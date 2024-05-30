package utility;

import java.util.function.Function;

public class ValuePrompt {

    public static <T> T prompt(String message, Function<String, T> parser) {
        System.out.println(message);
        String input = System.console().readLine();
        try {
            return parser.apply(input);
        } catch (Exception e) {
            System.out.println("Invalid input");
            return prompt(message, parser);
        }
    }

    public static String promptPassword(String message) {
        System.out.println(message);
        char[] input = System.console().readPassword();
        return new String(input);
    }
}
