package view;

import java.util.Scanner;

public final class ConsoleInput {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleInput() {
    }

    public static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("O valor não pode ficar vazio.");
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Informe um número inteiro válido.");
            }
        }
    }

    public static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("O valor deve ser maior que zero.");
        }
    }

    public static long readPositiveLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                long value = Long.parseLong(SCANNER.nextLine().trim());
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // A mensagem abaixo também cobre entradas que não são números.
            }
            System.out.println("Informe um número maior que zero.");
        }
    }

    public static void runAction(Runnable action) {
        try {
            action.run();
        } catch (IllegalStateException e) {
            System.out.println("Operação não concluída: " + e.getMessage());
        }
    }
}
