import java.util.Random;
import java.util.Scanner;

public class BigMonster extends Monster {
    BigMonster(int sizeBoard) {
        super(sizeBoard, "\uD83D\uDC79");
    }

    @Override
    public boolean taskMonster(int difficultGame, Scanner scanner) {
        // На первой сложности задание остаётся простым.
        if (difficultGame == 1) {
            return super.taskMonster(1, scanner);
        }

        System.out.println("-- Вы встретили большого монстра! Решите сложную задачу. --");

        Random random = new Random();
        int x = random.nextInt(10 * (difficultGame - 1), 10 * difficultGame);
        int y = random.nextInt(10 * (difficultGame - 1), 10 * difficultGame);
        int z = random.nextInt(100 * (difficultGame - 1), 100 * difficultGame);
        int trueAnswer = x * y - z;

        System.out.println("-- Реши пример: " + x + " * " + y + " - " + z + " = ? --");
        int answer = scanner.nextInt();

        if (trueAnswer == answer) {
            System.out.println("-- Верно! Ты победил большого монстра --");
            return true;
        } else {
            System.out.println("-- Ты проиграл эту битву! Правильный ответ: " + trueAnswer + " --");
            return false;
        }
    }
}
