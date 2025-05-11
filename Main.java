import java.io.*;
import java.util.*;

public class Main {
    private static final String CONFIG_FILE = "config.txt";
    private static final String STATS_FILE = "stats.txt";
    private static int size = 3;
    private static char[][] game;
    private static String playerX = "Гравець X";
    private static String playerO = "Гравець O";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadConfig();
        gameMenu();
    }

    private static void gameMenu() {
        boolean run = true;
        while (run) {
            System.out.println("\nГоловне меню:");
            System.out.println("1. Грати");
            System.out.println("2. Налаштування");
            System.out.println("3. Статистика");
            System.out.println("4. Вихід");
            String input = scanner.nextLine();

            switch (input) {
                case "1": startGame(); break;
                case "2": changeSettings(); break;
                case "3": showStats(); break;
                case "4": run = false; break;
                default: System.out.println("Невірний вибір.");
            }
        }
    }

    private static void changeSettings() {
        System.out.println("Оберiть розмiр гри: 3 (1), 5 (2), 7 (3), 9 (4)");
        switch (scanner.nextLine()) {
            case "1": size = 3; break;
            case "2": size = 5; break;
            case "3": size = 7; break;
            case "4": size = 9; break;
            default: System.out.println("Невірний вибір.");
        }
        System.out.print("Iм'я гравця X: ");
        playerX = scanner.nextLine();
        System.out.print("Iм'я гравця O: ");
        playerO = scanner.nextLine();
        saveConfig();
    }

    private static void startGame() {
        game = new char[size * 2 - 1][size * 2 - 1];
        initBoard();
        char player = 'X';
        int moves = 0;

        while (true) {
            printBoard();
            if (!playerMove(player)) continue;
            moves++;
            if (checkWin(player)) {
                printBoard();
                System.out.println("Переможець: " + (player == 'X' ? playerX : playerO));
                saveStat((player == 'X' ? playerX : playerO));
                return;
            }
            if (moves == size * size) {
                System.out.println("Нiчия");
                saveStat("Нічия");
                return;
            }
            player = (player == 'X') ? 'O' : 'X';
        }
    }

    private static void initBoard() {
        for (int i = 0; i < game.length; i++)
            for (int j = 0; j < game.length; j++)
                game[i][j] = (i % 2 == 0 && j % 2 == 0) ? ' ' : (i % 2 == 0 ? '|' : (j % 2 == 0 ? '-' : '+'));
    }

    private static void printBoard() {
        for (char[] row : game) {
            for (char cell : row) System.out.print(cell + " ");
            System.out.println();
        }
    }

    private static boolean playerMove(char player) {
        System.out.println("Хiд: " + (player == 'X' ? playerX : playerO) + " (" + player + ")");
        System.out.print("Введiть рядок (1-" + size + "): ");
        int row = getIntInput();
        System.out.print("Введiть стовпець (1-" + size + "): ");
        int col = getIntInput();

        if (row < 1 || row > size || col < 1 || col > size || game[(row - 1) * 2][(col - 1) * 2] != ' ') {
            System.out.println("Неправильний хiд.");
            return false;
        }
        game[(row - 1) * 2][(col - 1) * 2] = player;
        return true;
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    private static boolean checkWin(char player) {
        for (int i = 0; i < size; i++) {
            boolean row = true, col = true;
            for (int j = 0; j < size; j++) {
                if (game[i * 2][j * 2] != player) row = false;
                if (game[j * 2][i * 2] != player) col = false;
            }
            if (row || col) return true;
        }
        boolean diag1 = true, diag2 = true;
        for (int i = 0; i < size; i++) {
            if (game[i * 2][i * 2] != player) diag1 = false;
            if (game[i * 2][(size - 1 - i) * 2] != player) diag2 = false;
        }
        return diag1 || diag2;
    }

    private static void saveConfig() {
        try {
            FileWriter w = new FileWriter(CONFIG_FILE);
            w.write(size + "\n" + playerX + "\n" + playerO + "\n");
            w.close();
        } catch (IOException e) {
            System.out.println("Помилка збереження конфігурації.");
        }
    }

    private static void loadConfig() {
        try {
            BufferedReader r = new BufferedReader(new FileReader(CONFIG_FILE));
            size = Integer.parseInt(r.readLine());
            playerX = r.readLine();
            playerO = r.readLine();
            r.close();
        } catch (Exception e) {
        }
    }

    private static void saveStat(String winner) {
        try {
            FileWriter w = new FileWriter(STATS_FILE, true);
            w.write(new Date().toString() + " | " + winner + " | " + size + "x" + size + "\n");
            w.close();
        } catch (IOException e) {
            System.out.println("Не вдалося зберегти статистику.");
        }
    }

    private static void showStats() {
        try {
            BufferedReader r = new BufferedReader(new FileReader(STATS_FILE));
            String line;
            System.out.println("Історія ігор:");
            while ((line = r.readLine()) != null) System.out.println(line);
            r.close();
        } catch (IOException e) {
            System.out.println("Немає статистики.");
        }
    }
}
