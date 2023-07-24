package encryptdecrypt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        int actionKey = 0;
        String input = "";
        int shift = 0;
        String fileOutput = "";
        boolean error = false;
        File file = null;
        String algo = "shift";
        String fileOutputString = "";

        if (!(args.length % 2 == 0) || args.length == 0) {
            error = true;
        } else {
            for (int i = 0; i < args.length; i = i + 2) {
                switch (args[i]) {
                    case "-in" -> {
                        try {
                            input = new String(Files.readAllBytes(Paths.get(args[i + 1])));
                        } catch (IOException e) {
                            System.out.println("Cannot read file: " + e.getMessage());
                        }
                    }
                    case "-alg" -> algo = args[i + 1];
                    case "-out" -> {
                        fileOutput = args[i + 1];
                        file = new File(fileOutput);
                    }
                    case "-mode" -> {
                        switch (args[i + 1]) {
                            case "enc" -> actionKey = 1;
                            case "dec" -> actionKey = -1;
                        }
                    }
                    case "-key" -> shift = Integer.parseInt(args[i + 1]);
                    case "-data" -> input = args[i + 1];
                    default -> error = true;
                }
            }
        }

        if (!error) {
            shift = shift * actionKey;
            int letter;
            char character;
            char[] outputArray = new char[input.length()];
            for (int i = 0; i < outputArray.length; i++) {
                letter = input.charAt(i);

                if (algo.equals("unicode")) {
                    character = (char) ((char) letter + shift);
                } else if (letter > 96 && letter < 123) {
                    character = letter + shift > 122 ? (char) (letter + shift - 26) : letter + shift < 96 ? (char) (letter + shift + 26) : (char) ((char) letter + shift);
                } else if (letter > 64 && letter < 91) {
                    character = letter + shift > 90 ? (char) (letter + shift - 26) : letter + shift < 64 ? (char) (letter + shift + 26) : (char) ((char) letter + shift);
                } else {
                    character = (char) letter;
                }

                if (fileOutput.equals("")) {
                    System.out.print(character);
                } else {
                    fileOutputString = fileOutputString + character;
                }

                if (!(fileOutputString.equals(""))) {
                    try (PrintWriter printWriter = new PrintWriter(file)) {
                        printWriter.print(fileOutputString);
                    } catch (IOException e) {
                        System.out.printf("An exception occurred %s", e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("Error");
        }
    }
}