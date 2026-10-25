import java.util.Random;
import java.util.Scanner;

public class Monster {
    private String image;
    private final int x, y;

    Monster(int sizeBoard) {
        this(sizeBoard, "\uD83E\uDDDF");
    }

    Monster(int sizeBoard, String image) {
        this.image = image;
        Random random = new Random();
        x = 1 + random.nextInt(sizeBoard);
        y = 1 + random.nextInt(sizeBoard - 1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getImage() {
        return image;
    }

    public boolean conflictPerson(int personX, int personY) {
        return personX == x && personY == y;
    }

    public boolean taskMonster(int difficultGame, Scanner scanner) {
        System.out.println("-- Решите задачу --");

        // Сложность определяют тип уравнения
        Random random = new Random();
        int trueAnswer = 0;
        String example = "";

        if (difficultGame == 1) {
            int a = random.nextInt(21);
            int b = random.nextInt(21);
            trueAnswer = a + b;
            example = a + " + " + b;
        } else if (difficultGame == 2) {
            int a = random.nextInt(51);
            int b = random.nextInt(a + 1);
            trueAnswer = a - b;
            example = a + " - " + b;
        } else if (difficultGame == 3) {
            int a = 1 + random.nextInt(10);
            int b = 1 + random.nextInt(10);
            trueAnswer = a * b;
            example = a + " * " + b;
        } else if (difficultGame == 4) {
            int a = 10 + random.nextInt(91);
            int b = 10 + random.nextInt(91);
            int action = random.nextInt(3);

            if (action == 0) {
                trueAnswer = a + b;
                example = a + " + " + b;
            } else if (action == 1) {
                trueAnswer = a - b;
                example = a + " - " + b;
            } else {
                trueAnswer = a * b;
                example = a + " * " + b;
            }
        } else if (difficultGame == 5) {
            int a = 2 + random.nextInt(9);
            int b = 2 + random.nextInt(9);
            int c = 1 + random.nextInt(20);
            trueAnswer = a * b + c;
            example = a + " * " + b + " + " + c;
        }

        System.out.println("-- Реши пример: " + example + " = ? --");
        int answer = scanner.nextInt();

        if (trueAnswer == answer) {
            System.out.println("-- Верно! Ты победил монстра --");
            return true;
        } else {
            System.out.println("-- Ты проиграл эту битву! Правильный ответ: " + trueAnswer + " --");
            return false;
        }
    }
}
