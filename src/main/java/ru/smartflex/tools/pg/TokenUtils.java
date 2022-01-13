package ru.smartflex.tools.pg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TokenUtils {

    private static char CHAR_SPACE = ' ';
    private static String TOKEN_STRING_SPACE_EAT = "WS";
    private static String LPAREN = "[";
    private static String RPAREN = "]";
    private static int PAD_AMOUNT = 20;
    private static String DELIM = " :   ";
    private static String SEMI = ";";
    private static String LEFT_KEY_SPACE = " ";
    private static String LEFT_KEY_SPACE_REPLACE = "_";

    static void generateTokens(String fileName) {

        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {

            Function<Character, String> funcChar = c -> {
                if (c == CHAR_SPACE) {
                    return TOKEN_STRING_SPACE_EAT;
                } else {
                    StringBuilder sb = new StringBuilder(4);
                    sb.append(LPAREN);
                    sb.append(Character.toUpperCase(c));
                    sb.append(Character.toLowerCase(c));
                    sb.append(RPAREN);
                    return sb.toString();
                }
            };

            String line;
            do {
                line = in.readLine();

                if (line != null) {
                    System.out.print(String.format("%-" + PAD_AMOUNT + "s", line.replace(LEFT_KEY_SPACE, LEFT_KEY_SPACE_REPLACE)));
                    System.out.print(DELIM);

                    Character[] charObjectArray =
                            line.chars().mapToObj(c -> (char) c).toArray(Character[]::new);

                    Stream stream = Stream.of(charObjectArray);
                    List<String> collected = (List<String>) stream.map(funcChar).collect(toList());
                    collected.forEach(System.out::print);

                    System.out.println(SEMI);
                }

            } while (line != null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] s) {
        String fileName = System.getProperty("user.dir") + "\\src\\main\\resources\\lang.src\\control.txt";
        generateTokens(fileName);
    }
}
