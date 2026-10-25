import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String castle = "\uD83C\uDFF0";
        String treasure = "\uD83D\uDCB0";
        String potion = "\uD83E\uDDEA";
        String trap = "\uD83D\uDCA5";

        Scanner scanner = new Scanner(System.in);

        System.out.println("-- Привет! Ты готов начать играть в игру? (Напиши: ДА или НЕТ) --");
        String answer = scanner.nextLine().toUpperCase();
        System.out.println("-- Ваш ответ: " + answer + " --");

        switch (answer) {
            case "ДА":
                System.out.println("-- Выберите размер игрового поля (от 5 до 9): --");
                int sizeBoard = scanner.nextInt();

                while (sizeBoard < 5 || sizeBoard > 9) {
                    System.out.println("-- Размер поля должен быть от 5 до 9: --");
                    sizeBoard = scanner.nextInt();
                }

                System.out.println("-- Размер игрового поля: " + sizeBoard + " x " + sizeBoard + " --");

                int step = 0;
                int score = 0;
                Person person = new Person(sizeBoard);

                Random random = new Random();
                int castleX = 1 + random.nextInt(sizeBoard);
                int castleY = 1;

                // Сначала создаём пустое игровое поле.
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

                // Зелья появляются только на свободных клетках.
                int countPotion = 1 + sizeBoard / 4;
                for (int i = 0; i < countPotion; i++) {
                    int potionX;
                    int potionY;

                    do {
                        potionX = 1 + random.nextInt(sizeBoard);
                        potionY = 1 + random.nextInt(sizeBoard);
                    } while (!board[potionY - 1][potionX - 1].equals("  ")
                            || potionX == person.getX() && potionY == person.getY());

                    board[potionY - 1][potionX - 1] = potion;
                }

                // Ловушки тоже не должны попадать на другие предметы.
                int countTrap = sizeBoard / 2;
                for (int i = 0; i < countTrap; i++) {
                    int trapX;
                    int trapY;

                    do {
                        trapX = 1 + random.nextInt(sizeBoard);
                        trapY = 1 + random.nextInt(sizeBoard);
                    } while (!board[trapY - 1][trapX - 1].equals("  ")
                            || trapX == person.getX() && trapY == person.getY());

                    board[trapY - 1][trapX - 1] = trap;
                }

                int countMonster = sizeBoard * 2 - 2;
                Monster[] monsters = new Monster[countMonster];

                for (int i = 0; i < countMonster; i++) {
                    Monster monster;

                    do {
                        if (random.nextBoolean()) {
                            monster = new Monster(sizeBoard);
                        } else {
                            monster = new BigMonster(sizeBoard);
                        }
                    } while (!board[monster.getY() - 1][monster.getX() - 1].equals("  ")
                            || monster.getX() == person.getX() && monster.getY() == person.getY());

                    monsters[i] = monster;
                    board[monster.getY() - 1][monster.getX() - 1] = monster.getImage();
                }

                System.out.println("-- Выбери сложность игры (от 1 до 5): --");
                int difficultGame = scanner.nextInt();

                while (difficultGame < 1 || difficultGame > 5) {
                    System.out.println("-- Нужно выбрать число от 1 до 5: --");
                    difficultGame = scanner.nextInt();
                }

                System.out.println("-- Выбранная сложность: " + difficultGame + " --");
                System.out.println("-- Собирайте сокровища: каждое даёт 10 очков. --");
                System.out.println("-- Зелье восстанавливает жизнь, а ловушка отнимает её. --");
                System.out.println("-- Обычный монстр даёт 20 очков, большой — 40. --");
                System.out.println("-- Найдите замок и пройдите игру! --");

                while (true) {
                    board[person.getY() - 1][person.getX() - 1] = person.getImage();
                    outputBoard(board, person.getLive(), score);

                    char personLetter = (char) ('A' + person.getX() - 1);
                    System.out.println("-- Введите клетку, например D4, или кнопку W, A, S, D. --");
                    System.out.println("-- Ход возможен только по вертикали или горизонтали на одну клетку. --");
                    System.out.println("-- Клетка персонажа: " + personLetter + person.getY() + " --");

                    String command = scanner.next().toUpperCase();
                    int x = person.getX();
                    int y = person.getY();

                    // W, A, S, D изменяют координаты на одну клетку.
                    if (command.equals("W")) {
                        y--;
                    } else if (command.equals("S")) {
                        y++;
                    } else if (command.equals("A")) {
                        x--;
                    } else if (command.equals("D")) {
                        x++;
                    } else if (command.length() == 2) {
                        char letter = command.charAt(0);
                        char number = command.charAt(1);
                        x = letter - 'A' + 1;
                        y = number - '0';
                    } else {
                        System.out.println("-- Введите W, A, S, D или название клетки, например D4 --");
                        continue;
                    }

                    if (x < 1 || x > sizeBoard || y < 1 || y > sizeBoard) {
                        System.out.println("-- Такой клетки нет на игровом поле --");
                        continue;
                    }

                    if (person.isMoveCorrect(x, y)) {
                        if (board[y - 1][x - 1].equals("  ")) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            step++;

                            char newLetter = (char) ('A' + person.getX() - 1);
                            System.out.println("-- Ход корректный; новая клетка: " + newLetter + person.getY()
                                    + "\nХод номер: " + step + " --");
                        } else if (board[y - 1][x - 1].equals(castle)) {
                            step++;
                            System.out.println("-- Вы прошли игру за " + step + " ходов! --");
                            System.out.println("-- Итоговый счёт: " + score + " --");
                            break;
                        } else if (board[y - 1][x - 1].equals(treasure)) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            step++;
                            score += 10;
                            System.out.println("-- Вы нашли сокровище! +10 очков --");
                        } else if (board[y - 1][x - 1].equals(potion)) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            step++;

                            if (person.upLive()) {
                                System.out.println("-- Вы выпили зелье и восстановили одну жизнь! --");
                            } else {
                                System.out.println("-- У вас уже максимальное количество жизней --");
                            }
                        } else if (board[y - 1][x - 1].equals(trap)) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            person.downLive();
                            step++;
                            System.out.println("-- Вы попали в ловушку и потеряли одну жизнь! --");
                        } else {
                            for (Monster monster : monsters) {
                                if (monster.conflictPerson(x, y)) {
                                    if (monster.taskMonster(difficultGame, scanner)) {
                                        board[person.getY() - 1][person.getX() - 1] = "  ";
                                        person.move(x, y);
                                        step++;
                                        if (monster instanceof BigMonster) {
                                            score += 40;
                                            System.out.println("-- За победу над большим монстром вы получили 40 очков! --");
                                        } else {
                                            score += 20;
                                            System.out.println("-- За победу над монстром вы получили 20 очков! --");
                                        }
                                    } else {
                                        person.downLive();
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("-- Некорректный ход --");
                    }

                    if (person.getLive() <= 0) {
                        break;
                    }
                }

                if (person.getLive() <= 0) {
                    System.out.println("-- Закончились жизни. Итоговый счёт: " + score + " --");
                }
                break;

            case "НЕТ":
                System.out.println("-- Жалько, приходи ещё! --");
                break;

            default:
                System.out.println("-- Данные введены некорректно --");
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
