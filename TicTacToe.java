import java.util.Random;
import java.util.Scanner;

/**
 * Created by Alexander Tsupko on 29.05.16.
 * Александр Цупко, 29 мая 2016 (с) Все права защищены.
 * Версия 1.0
 */
public class TicTacToe {
    private static final int ROW = 3; // число рядов (минимум 3)
    private static final int COL = 3; // число столбцов (минимум 3)
    private static final int WIN = 3; // число одинаковых символов в ряд (минимум 3,
                                      // максимум - максимальное из ROW и COL)
    private static final char PLAYER = '\u0078'; // латинская строчная буква x
    private static final char AI     = '\u006F'; // латинская строчная буква o
    private static final char EMPTY  = '\u00B7'; // точка по центру

    private static char field[][] = new char[ROW][COL]; // игровое поле размером ROW x COL
    private static Scanner scanner = new Scanner(System.in); // создание сканера стандартного ввода
    private static Random random = new Random(); // создание объекта для генерации случайных чисел

    /**
     * В методе main() инициализируется игровое поле точками по центру,
     * выводится в консоль и запускается игровой цикл.
     *
     * В игровом цикле вызываются поочерёдно методы ходов игрока и компьютера,
     * с поочерёдными проверками на выигрыш одной из сторон и заполнения поля.
     *
     * Если используется вариант игры 3х3х3, запускается метод aiSmartTurn(),
     * работающий в данном варианте, согласно теоретически продуманной стратегии.
     * Для изменения варианта игры, просто измените константы ROW, COL и WIN выше.
     *
     * Для произвольной игры, компьютер играет достаточно разумно для того,
     * чтобы игра казалась пользователю увлекательной. Багов пока не обнаружено.
     * @param args аргументы командной строки не используются
     */
    public static void main(String[] args) {
        initField();
        printField();
        while (true) {
            playerTurn();
            printField();
            if (isWinning(PLAYER, WIN)) {
                System.out.println("ИГРОК ПОБЕЖДАЕТ");
                break;
            }
            if (isFieldFull()) {
                System.out.println("НИЧЬЯ");
                break;
            }
            if (ROW == 3 && COL == 3 && WIN == 3) aiSmartTurn();
            else                                  aiTurn();
            printField();
            if (isWinning(AI, WIN)) {
                System.out.println("КОМПЬЮТЕР ПОБЕЖДАЕТ");
                break;
            }
            if (isFieldFull()) {
                System.out.println("НИЧЬЯ");
                break;
            }
        }
        scanner.close();
        System.out.println("ИГРА ОКОНЧЕНА");
    }

    /**
     * Инициализирует игровое поле точками по центру.
     */
    private static void initField() {
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                field[i][j] = EMPTY;
    }

    /**
     * Выводит в консоль игровое поле.
     */
    private static void printField() {
        for (int i = 1; i <= COL; i++)
            System.out.print(" \t" + i);
        System.out.println();
        for (int i = 0; i < ROW; i++) {
            System.out.print(i + 1 + "\t");
            for (int j = 0; j < COL; j++)
                System.out.print(field[i][j] + "\t");
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Спрашивает у пользователя координаты из стандартного ввода.
     */
    private static void playerTurn() {
        int x, y;
        do {
            System.out.print("Введите РЯД и СТОЛБЕЦ: ");
            x = scanner.nextInt();
            y = scanner.nextInt();
        } while (!isCellEmpty(x-1, y-1));
        field[x-1][y-1] = PLAYER;
    }

    /**
     * Проверяет, лежит ли данная ячейка в игровом поле,
     * и если лежит, занята ли она или пуста.
     * @param x ряд
     * @param y столбец
     * @return истина, если ячейка валидна; ложь, если ячейка не валидна
     */
    private static boolean isCellEmpty(int x, int y) {
        return !(x < 0 || x >= ROW || y < 0 || y >= COL) && field[x][y] == EMPTY;
    }

    /**
     * Проверяет, лежат ли три одинаковых переданных элемента
     * в ряд на любой горизонтали, вертикали или диагонали.
     * @param c крестик либо нолик
     * @return истина, если лежат; ложь, если не лежат
     */
    private static boolean isWinning(char c) {
        // проверка горизонталей
        if (field[0][0] == c && field[0][1] == c && field[0][2] == c) return true;
        if (field[1][0] == c && field[1][1] == c && field[1][2] == c) return true;
        if (field[2][0] == c && field[2][1] == c && field[2][2] == c) return true;
        // проверка вертикалей
        if (field[0][0] == c && field[1][0] == c && field[2][0] == c) return true;
        if (field[0][1] == c && field[1][1] == c && field[2][1] == c) return true;
        if (field[0][2] == c && field[1][2] == c && field[2][2] == c) return true;
        // проверка диагоналей
        if (field[0][0] == c && field[1][1] == c && field[2][2] == c) return true;
        if (field[0][2] == c && field[1][1] == c && field[2][0] == c) return true;
        return false; // вернуть ложь по умолчанию
    }

    /**
     * Проверяет, лежат ли несколько одинаковых переданных элементов
     * в ряд на любой горизонтали, вертикали или диагонали для всех элементов.
     * @param c крестик либо нолик
     * @param length количество одинаковых элементов в ряд
     * @return истина, если лежат; ложь, если не лежат
     */
    private static boolean isWinning(char c, int length) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int counter; // счётчик
                // проверка горизонтали вправо от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (j+k < COL && field[i][j+k] == c) counter++;
                if (counter == length) return true;
                // проверка горизонтали влево от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (j-k > -1 && field[i][j-k] == c) counter++;
                if (counter == length) return true;
                // проверка вертикали вниз от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (i+k < ROW && field[i+k][j] == c) counter++;
                if (counter == length) return true;
                // проверка вертикали вверх от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (i-k > -1 && field[i-k][j] == c) counter++;
                if (counter == length) return true;
                // проверка главной диагонали вниз-вправо от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (i+k < ROW && j+k < COL && field[i+k][j+k] == c) counter++;
                if (counter == length) return true;
                // проверка главной диагонали вверх-влево от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (i-k > -1 && j-k > -1 && field[i-k][j-k] == c) counter++;
                if (counter == length) return true;
                // проверка побочной диагонали вверх-вправо от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (i-k > -1 && j+k < COL && field[i-k][j+k] == c) counter++;
                if (counter == length) return true;
                // проверка побочной диагонали вниз-влево от текущей ячейки на полную длину
                counter = 0;
                for (int k = 0; k < length; k++)
                    if (i+k < ROW && j-k > -1 && field[i+k][j-k] == c) counter++;
                if (counter == length) return true;
            }
        }
        return false; // вернуть ложь по умолчанию
    }

    /**
     * Проверяет, лежат ли несколько одинаковых переданных элементов
     * в ряд на горизонтали, вертикали или диагонали от данного элемента.
     * @param c крестик либо нолик
     * @param x ряд элемента
     * @param y столбец элемента
     * @param length количество одинаковых элементов в ряд
     * @return истина, если лежат; ложь, если не лежат
     */
    private static boolean isWinning(char c, int x, int y, int length) {
        int counter; // счётчик
        // проверка горизонтали вправо и влево от текущей ячейки на половину длины
        counter = 0;
        for (int k = 0; k < length/2; k++) {
            if (y+k < COL && field[x][y+k] == c) counter++;
            if ( y-k > -1 && field[x][y-k] == c) counter++;
        }
        if (counter == length) return true;
        // проверка горизонтали вправо от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (y+k < COL && field[x][y+k] == c) counter++;
        if (counter == length) return true;
        // проверка горизонтали влево от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (y-k > -1 && field[x][y-k] == c) counter++;
        if (counter == length) return true;
        // проверка вертикали вниз и вверх от текущей ячейки на половину длины
        counter = 0;
        for (int k = 0; k < length/2; k++) {
            if (x+k < ROW && field[x+k][y] == c) counter++;
            if ( x-k > -1 && field[x-k][y] == c) counter++;
        }
        if (counter == length) return true;
        // проверка вертикали вниз от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (x+k < ROW && field[x+k][y] == c) counter++;
        if (counter == length) return true;
        // проверка вертикали вверх от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (x-k > -1 && field[x-k][y] == c) counter++;
        if (counter == length) return true;
        // проверка главной диагонали вниз-вправо и вверх-влево от текущей ячейки на половину длины
        counter = 0;
        for (int k = 0; k < length/2; k++) {
            if (x+k < ROW && y+k < COL && field[x+k][y+k] == c) counter++;
            if ( x-k > -1 && y-k > -1  && field[x-k][y-k] == c) counter++;
        }
        if (counter == length) return true;
        // проверка главной диагонали вниз-вправо от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (x+k < ROW && y+k < COL && field[x+k][y+k] == c) counter++;
        if (counter == length) return true;
        // проверка главной диагонали вверх-влево от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (x-k > -1 && y-k > -1 && field[x-k][y-k] == c) counter++;
        if (counter == length) return true;
        // проверка побочной диагонали вверх-вправо и вниз-влево от текущей ячейки на половину длины
        counter = 0;
        for (int k = 0; k < length/2; k++) {
            if ( x-k > -1 && y+k < COL && field[x-k][y+k] == c) counter++;
            if (x+k < ROW && y-k > -1  && field[x+k][y-k] == c) counter++;
        }
        if (counter == length) return true;
        // проверка побочной диагонали вверх-вправо от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (x-k > -1 && y+k < COL && field[x-k][y+k] == c) counter++;
        if (counter == length) return true;
        // проверка побочной диагонали вниз-влево от текущей ячейки на полную длину
        counter = 0;
        for (int k = 0; k < length; k++)
            if (x+k < ROW && y-k > -1 && field[x+k][y-k] == c) counter++;
        if (counter == length) return true;
        return false; // вернуть ложь по умолчанию
    }

    /**
     * Проверяет, заполнено ли игровое поле крестиками и ноликами.
     * @return истина, если заполнено; ложь, если не заполнено
     */
    private static boolean isFieldFull() {
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                if (field[i][j] == EMPTY) return false;
        return true;
    }

    /**
     * Метод aiSmartTurn() вызывается только для варианта игры 3х3х3.
     * Алгоритм работает следующим образом:
     * 1. Если игрок походил НЕ в центр, компьютер ходит В ЦЕНТР.
     *    Если игрок походил В ЦЕНТР, компьютер ходит в ЛЮБОЙ ИЗ УГЛОВ.
     * 2. После первого хода, компьютер проверяет условия в следующем порядке:
     *      а) если компьютер может выиграть на текущем ходу, он выигрывает;
     *      б) если компьютер НЕ может выиграть на текущем ходу, но игрок СМОЖЕТ,
     *          компьютер МЕШАЕТ ИГРОКУ, делая ход в выигрышную для игрока точку;
     *      в) если ни одна из проверок на выигрыш не срабатывает,
     *          компьютер ходит в ЛЮБОЙ ИЗ СВОБОДНЫХ УГЛОВ;
     *         если свободных углов НЕ ОСТАЛОСЬ, компьютер ходит в СЛУЧАЙНУЮ ТОЧКУ.
     * Теоретический результат - ничья при идеальной игре сторон ИЛИ выигрыш компьютера,
     * если игрок недостаточно внимателен, что и требовалось построить.
     */
    private static void aiSmartTurn() {
//        boolean isChanged = false;
        // если игрок первым ходом походил не в центр, играть в центр
        if (field[1][1] == EMPTY) {
            field[1][1] = AI;
            System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", 2, 2);
        } else {
            // если компьютер выигрывает, завершить игру
            if (isAIWinning()) return;
//            // проверяет, может ли выиграть компьютер на данном ходу
//            for (int i = 0; i < ROW; i++)
//                for (int j = 0; j < COL; j++)
//                    if (field[i][j] == EMPTY && !isChanged) {
//                        field[i][j] = AI;
//                        if (isWinning(AI)) {
//                            isChanged = true;
//                            System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
//                        } else {
//                            field[i][j] = EMPTY;
//                        }
//                    }
            // если игрок выигрывает, завершить игру
            if (isPlayerWinning()) return;
//            // проверяет, может ли игрок выиграть на данном ходу
//            for (int i = 0; i < ROW; i++)
//                for (int j = 0; j < COL; j++)
//                    if (field[i][j] == EMPTY && !isChanged) {
//                        field[i][j] = PLAYER;
//                        if (isWinning(PLAYER)) {
//                            field[i][j] = AI;
//                            isChanged = true;
//                            System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
//                        } else {
//                            field[i][j] = EMPTY;
//                        }
//                    }
            // если никто пока не выигрывает, сделать ход в случайный угол
            // если никто пока не выигрывает, сделать ход по следующему алгоритму
            int x, y;
            do {
                // срабатывает, когда игрок ставит крестики в противоположные углы
                if (field[0][0] == PLAYER && field[2][2] == PLAYER ||
                    field[2][0] == PLAYER && field[0][2] == PLAYER) {
                    do {
                        if (random.nextBoolean()) {
                            do x = random.nextInt(3); while (x != 1); // x = 1
                            do y = random.nextInt(3); while (y == 1); // y = 0 или y = 2
                        } else {
                            do x = random.nextInt(3); while (x == 1); // x = 0 или x = 2
                            do y = random.nextInt(3); while (y != 1); // y = 1
                        }
                    } while (!isCellEmpty(x, y));
                    break;
                } //else
                // если не все углы заняты, проходимся по различным вариантам хода игрока
                if (field[0][0] == EMPTY || field[0][2] == EMPTY ||
                    field[2][0] == EMPTY || field[2][2] == EMPTY) {
                    // если игрок сыграл в ячейку (0, 0) при первом ходе в ячейку (2, 1)
                    if (field[0][0] == PLAYER) {
                        if (field[1][2] == PLAYER) {
                            x = random.nextInt(3);
                            y = 2;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x == 0);
                        }
                    } else
                    // если игрок сыграл в ячейку (0, 2) при первом ходе в ячейку (2, 1)
                    if (field[0][2] == PLAYER) {
                        if (field[1][0] == PLAYER) {
                            x = random.nextInt(3);
                            y = 0;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x == 0);
                        }
                    } else
                    // если игрок сыграл в ячейку (0, 2) при первом ходе в ячейку (1, 0)
                    if (field[0][2] == PLAYER) {
                        if (field[2][1] == PLAYER) {
                            x = 2;
                            y = random.nextInt(3);
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (y == 2);
                        }
                    } else
                    // если игрок сыграл в ячейку (2, 2) при первом ходе в ячейку (1, 0)
                    if (field[2][2] == PLAYER) {
                        if (field[0][1] == PLAYER) {
                            x = 0;
                            y = random.nextInt(3);
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (y == 2);
                        }
                    } else
                    // если игрок сыграл в ячейку (2, 0) при первом ходе в ячейку (1, 2)
                    if (field[2][0] == PLAYER) {
                        if (field[0][1] == PLAYER) {
                            x = 0;
                            y = random.nextInt(3);
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (y == 0);
                        }
                    } else
                    // если игрок сыграл в ячейку (0, 0) при первом ходе в ячейку (1, 2)
                    if (field[0][0] == PLAYER) {
                        if (field[2][1] == PLAYER) {
                            x = 2;
                            y = random.nextInt(3);
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (y == 0);
                        }
                    } else
                    // если игрок сыграл в ячейку (2, 2) при первом ходе в ячейку (0, 1)
                    if (field[2][2] == PLAYER) {
                        if (field[1][0] == PLAYER) {
                            x = random.nextInt(3);
                            y = 0;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x == 2);
                        }
                    } else
                    // если игрок сыграл в ячейку (2, 0) при первом ходе в ячейку (0, 1)
                    if (field[2][0] == PLAYER) {
                        if (field[1][2] == PLAYER) {
                            x = random.nextInt(3);
                            y = 2;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x == 2);
                        }
                    } else
                    // ДРУГИЕ СЛУЧАИ
                    // если игрок сыграл в ячейки (0, 1) и (1, 0)
                    if (field[0][1] == PLAYER && field[1][0] == PLAYER) {
                        if (random.nextBoolean()) {
                            x = 0; y = 0;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x + y != 2);
                        }
                    } else
                    // если игрок сыграл в ячейки (1, 0) и (2, 1)
                    if (field[1][0] == PLAYER && field[2][1] == PLAYER) {
                        if (random.nextBoolean()) {
                            x = 2; y = 0;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x + y != 0 && x + y != 4);
                        }
                    } else
                    // если игрок сыграл в ячейки (2, 1) и (1, 2)
                    if (field[2][1] == PLAYER && field[1][2] == PLAYER) {
                        if (random.nextBoolean()) {
                            x = 2; y = 2;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x + y != 2);
                        }
                    } else
                    // если игрок сыграл в ячейки (1, 2) и (0, 1)
                    if (field[1][2] == PLAYER && field[0][1] == PLAYER) {
                        if (random.nextBoolean()) {
                            x = 0; y = 2;
                        } else {
                            do {
                                x = random.nextInt(3);
                                y = random.nextInt(3);
                            } while (x + y != 0 && x + y != 4);
                        }
                    } else {
                        // если ни одно из условий выше не удовлетворено, выбрать случайный свободный угол
                        do x = random.nextInt(3); while (x == 1); // x = 0 или x = 2
                        do y = random.nextInt(3); while (y == 1); // y = 0 или y = 2
                    }
                } else {
                    // если не осталось свободных углов, сделать ход в случайную точку
                    x = random.nextInt(3);
                    y = random.nextInt(3);
                }
            } while (!isCellEmpty(x, y)); // проверяет на валидность
            field[x][y] = AI; // ставит нолик в выбранную ячейку
            System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", ++x, ++y);
        }
    }

    private static boolean isAIWinning() {
        // проверяет, может ли выиграть компьютер на данном ходу
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                if (field[i][j] == EMPTY) {
                    field[i][j] = AI;
                    if (isWinning(AI)) {
                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
                        return true;
                    } else {
                        field[i][j] = EMPTY;
                    }
                }
        return false;
    }

    private static boolean isPlayerWinning() {
        // проверяет, может ли игрок выиграть на данном ходу
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                if (field[i][j] == EMPTY) {
                    field[i][j] = PLAYER;
                    if (isWinning(PLAYER)) {
                        field[i][j] = AI;
                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
                        return true;
                    } else {
                        field[i][j] = EMPTY;
                    }
                }
        return false;
    }

    /**
     * Метод aiTurn() вызывается только для вариантов игры, отличных от 3х3х3.
     * Компьютер выбирает СЛУЧАЙНУЮ валидную ячейку и ставит в неё свой символ,
     * если пока не срабатывают условия для выигрыша одной из сторон.
     * Приоритет проверки выигрыша:
     * 1. Метод проверяет, может ли выиграть компьютер на данном ходу.
     * 2. Метод проверяет, может ли выиграть игрок на данном ходу.
     * 3. Метод проверяет, может ли компьютер угрожать создать выигрышную комбинацию на данном ходу.
     * 4. Метод проверяет, может ли игрок угрожать создать выигрышную комбинацию на данном ходу.
     */
    private static void aiTurn() {
//        boolean isChanged = false;
        // сначала компьютер выигрывает сам, если может
        if (isAIWinning(WIN)) return;
//        for (int i = 0; i < ROW; i++)
//            for (int j = 0; j < COL; j++)
//                if (field[i][j] == EMPTY && !isChanged) {
//                    field[i][j] = AI;
//                    if (isWinning(AI, i, j, WIN)) {
//                        isChanged = true;
//                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
//                    } else {
//                        field[i][j] = EMPTY;
//                    }
//                }
        // затем проверяет, может ли выиграть игрок, и если может, он блокирует его ход
        if (isPlayerWinning(WIN)) return;
//        for (int i = 0; i < ROW; i++)
//            for (int j = 0; j < COL; j++)
//                if (field[i][j] == EMPTY && !isChanged) {
//                    field[i][j] = PLAYER;
//                    if (isWinning(PLAYER, i, j, WIN)) {
//                        field[i][j] = AI;
//                        isChanged = true;
//                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
//                    } else {
//                        field[i][j] = EMPTY;
//                    }
//                }
        // затем компьютер проверяет, может ли он угрожать создать выигрышную комбинацию
        if (isAIWinning(WIN-1)) return;
//        for (int i = 0; i < ROW; i++)
//            for (int j = 0; j < COL; j++)
//                if (field[i][j] == EMPTY && !isChanged) {
//                    field[i][j] = AI;
//                    if (isWinning(AI, i, j, WIN-1)) {
//                        isChanged = true;
//                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
//                    } else {
//                        field[i][j] = EMPTY;
//                    }
//                }
        // затем мешает пользователю создать свою выигрышную комбинацию, блокируя её
        if (isPlayerWinning(WIN-1)) return;
//        for (int i = 0; i < ROW; i++)
//            for (int j = 0; j < COL; j++)
//                if (field[i][j] == EMPTY && !isChanged) {
//                    field[i][j] = PLAYER;
//                    if (isWinning(PLAYER, i, j, WIN-1)) {
//                        field[i][j] = AI;
//                        isChanged = true;
//                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
//                    } else {
//                        field[i][j] = EMPTY;
//                    }
//                }
        // если никто пока не выигрывает, делает случайный ход
        int x, y;
        do {
            x = random.nextInt(ROW);
            y = random.nextInt(COL);
        } while (!isCellEmpty(x, y)); // проверяет на валидность
        field[x][y] = AI; // ставит нолик в выбранную ячейку
        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", ++x, ++y);
    }

    private static boolean isAIWinning(int length) {
        // сначала компьютер выигрывает сам, если может
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                if (field[i][j] == EMPTY) {
                    field[i][j] = AI;
                    if (isWinning(AI, i, j, length)) {
                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
                        return true;
                    } else {
                        field[i][j] = EMPTY;
                    }
                }
        return false;
    }

    private static boolean isPlayerWinning(int length) {
        // затем проверяет, может ли выиграть игрок, и если может, он блокирует его ход
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                if (field[i][j] == EMPTY) {
                    field[i][j] = PLAYER;
                    if (isWinning(PLAYER, i, j, length)) {
                        field[i][j] = AI;
                        System.out.printf("Компьютер играет РЯД %d, СТОЛБЕЦ %d\n", i + 1, j + 1);
                        return true;
                    } else {
                        field[i][j] = EMPTY;
                    }
                }
        return false;
    }
}
