import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String castle = "\uD83C\uDFF0";
        String treasure = "\uD83D\uDCB0";

        Scanner scanner = new Scanner(System.in);

        System.out.println("Привет! Ты готов начать играть в игру? (Напиши: ДА или НЕТ)");

        String answer = scanner.nextLine();

        System.out.println("Ваш ответ:\t" + answer);

        switch (answer) {
            case "ДА":
                System.out.println("Выберите размер игрового поля (от 5 до 9):");
                int sizeBoard = scanner.nextInt();

                while (sizeBoard < 5 || sizeBoard > 9) {
                    System.out.println("Размер поля должен быть от 5 до 9:");
                    sizeBoard = scanner.nextInt();
                }

                System.out.println("Размер игрового поля: " + sizeBoard + " x " + sizeBoard);

                int step = 0;
                int score = 0;
                Person person = new Person(sizeBoard);

                Random random = new Random();
                int castleX = 1 + random.nextInt(sizeBoard);
                int castleY = 1;

                String[][] board = new String[sizeBoard][sizeBoard];
                for (int y = 1; y <= sizeBoard; y++) {
                    for (int x = 1; x <= sizeBoard; x++) {
                        board[y - 1][x - 1] = "  ";
                    }
                }

                board[castleY - 1][castleX - 1] = castle;

                int countTreasure = sizeBoard - 2;
                for (int i = 0; i < countTreasure; i++) {
                    int treasureX;
                    int treasureY;

                    do {
                        treasureX = 1 + random.nextInt(sizeBoard);
                        treasureY = 1 + random.nextInt(sizeBoard);
                    } while (!board[treasureY - 1][treasureX - 1].equals("  ")
                            || treasureX == person.getX() && treasureY == person.getY());

                    board[treasureY - 1][treasureX - 1] = treasure;
                }

                int countMonster = sizeBoard * 2 - 2;
                Monster[] monsters = new Monster[countMonster];

                for (int i = 0; i < countMonster; i++) {
                    Monster monster;

                    do {
                        monster = new Monster(sizeBoard);
                    } while (!board[monster.getY() - 1][monster.getX() - 1].equals("  ")
                            || monster.getX() == person.getX() && monster.getY() == person.getY());

                    monsters[i] = monster;
                    board[monster.getY() - 1][monster.getX() - 1] = monster.getImage();
                }

                System.out.println("Выбери сложность игры (от 1 до 5):");
                int difficultGame = scanner.nextInt();

                while (difficultGame < 1 || difficultGame > 5) {
                    System.out.println("Нужно выбрать число от 1 до 5:");
                    difficultGame = scanner.nextInt();
                }

                System.out.println("Выбранная сложность:\t" + difficultGame);
                System.out.println("Собирайте сокровища: каждое даёт 10 очков.");
                System.out.println("Победа над монстром даёт 20 очков. Найдите замок!");

                while (true) {
                    board[person.getY() - 1][person.getX() - 1] = person.getImage();
                    outputBoard(board, person.getLive(), score);

                    char personLetter = (char) ('A' + person.getX() - 1);
                    System.out.println("Введите клетку, куда будет ходить персонаж, например D4.");
                    System.out.println("Ход возможен только по вертикали или горизонтали на одну клетку.");
                    System.out.println("Клетка персонажа: " + personLetter + person.getY());

                    String cell = scanner.next().toUpperCase();

                    if (cell.length() != 2) {
                        System.out.println("Введите название клетки из двух символов, например D4");
                        continue;
                    }

                    char letter = cell.charAt(0);
                    char number = cell.charAt(1);

                    if (letter < 'A' || letter >= 'A' + sizeBoard
                            || number < '1' || number >= '1' + sizeBoard) {
                        System.out.println("Такой клетки нет на игровом поле");
                        continue;
                    }

                    int x = letter - 'A' + 1;
                    int y = number - '0';

                    if (person.isMoveCorrect(x, y)) {
                        if (board[y - 1][x - 1].equals("  ")) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            step++;

                            System.out.println("Ход корректный; новая клетка: " + cell
                                    + "\nХод номер: " + step);
                        } else if (board[y - 1][x - 1].equals(castle)) {
                            step++;
                            System.out.println("Вы прошли игру за " + step + " ходов!");
                            System.out.println("Итоговый счёт: " + score);
                            break;
                        } else if (board[y - 1][x - 1].equals(treasure)) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            step++;
                            score += 10;
                            System.out.println("Вы нашли сокровище! +10 очков");
                        } else {
                            for (Monster monster : monsters) {
                                if (monster.conflictPerson(x, y)) {
                                    if (monster.taskMonster(difficultGame, scanner)) {
                                        board[person.getY() - 1][person.getX() - 1] = "  ";
                                        person.move(x, y);
                                        step++;
                                        score += 20;
                                        System.out.println("За победу над монстром вы получили 20 очков!");
                                    } else {
                                        person.downLive();
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("Некорректный ход");
                    }

                    if (person.getLive() <= 0) {
                        break;
                    }
                }

                if (person.getLive() <= 0) {
                    System.out.println("Закончились жизни. Итоговый счёт: " + score);
                }
                break;

            case "НЕТ":
                System.out.println("Жаль, приходи ещё!");
                break;

            default:
                System.out.println("Данные введены некорректно");
                break;
        }
    }

    static void outputBoard(String[][] board, int live, int score) {
        String wall = "";

        for (int x = 0; x < board.length; x++) {
            wall += "+----";
        }
        wall += "+";

        System.out.print("    ");
        for (int x = 0; x < board.length; x++) {
            char letter = (char) ('A' + x);
            System.out.print(letter + "    ");
        }
        System.out.println();

        for (int y = 0; y < board.length; y++) {
            System.out.println("  " + wall);
            System.out.print((y + 1) + " ");

            for (String col : board[y]) {
                if (col.equals("  ")) {
                    System.out.print("|    ");
                } else {
                    System.out.print("| " + col + " ");
                }
            }
            System.out.println("| " + (y + 1));
        }
        System.out.println("  " + wall);

        System.out.print("    ");
        for (int x = 0; x < board.length; x++) {
            char letter = (char) ('A' + x);
            System.out.print(letter + "    ");
        }
        System.out.println();

        System.out.println("Количество жизней:\t" + live);
        System.out.println("Количество очков:\t" + score + "\n");
    }
}
