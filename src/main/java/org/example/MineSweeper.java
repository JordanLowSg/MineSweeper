package org.example;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MineSweeper {

    public static final int MIN_GRID_SIZE = 3;
    public static final int MAX_GRID_SIZE = 9;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int gridSize = getGridSizeFromUser(scanner);
        int numMines = getNumberOfMinesFromUser(scanner, gridSize);

        char[][] grid = generateGrid(gridSize);
        placeMines(grid, random, numMines);

        playGame(scanner, grid);

        scanner.close();
    }

    public static int getGridSizeFromUser(Scanner scanner) {
        int size = 0;
        while (size < MIN_GRID_SIZE || size > MAX_GRID_SIZE) {
            System.out.println("Welcome to Minesweeper!");
            System.out.print("Enter the size of the grid (e.g. 4 for a 4x4 grid): ");
            try {
                size = scanner.nextInt();
                if (size < MIN_GRID_SIZE) {
                    System.out.println("Minimum size of grid is " + MIN_GRID_SIZE + ".");
                } else if (size > MAX_GRID_SIZE) {
                    System.out.println("Maximum size of grid is " + MAX_GRID_SIZE + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input.");
                scanner.nextLine();
                size = -1;
            }
        }
        return size;
    }

    public static int getNumberOfMinesFromUser(Scanner scanner, int gridSize) {
        int numMines = 0;
        int maxMines = (int) Math.floor(gridSize * gridSize * 0.35);
        while (numMines > maxMines || numMines < 1) {
            System.out.print("Enter the number of mines to place on the grid (maximum is " + maxMines + "): ");
            try {
                numMines = scanner.nextInt();
                if (numMines > maxMines) {
                    System.out.println("Maximum number is 35% of total squares.");
                } else if (numMines < 1) {
                    System.out.println("There must be at least 1 mine.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input.");
                scanner.nextLine();
                numMines = -1;
            }
        }
        return numMines;
    }

    public static char[][] generateGrid(int size) {
        char[][] grid = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '_';
            }
        }
        return grid;
    }

    public static void placeMines(char[][] grid, Random random, int numMines) {
        for (int i = 0; i < numMines; i++) {
            int row;
            int col;
            do {
                row = random.nextInt(grid.length);
                col = random.nextInt(grid[0].length);
            } while (grid[row][col] == 'X');

            grid[row][col] = 'X';
        }
    }

    public static void playGame(Scanner scanner, char[][] minedGrid) {
        int revealedSquares = 0;
        int gridSize = minedGrid.length;

        while (revealedSquares < gridSize * gridSize - getNumMines(minedGrid)) {
            //remove the below for testing
//            printGridWithMineLocation(minedGrid);
            printGrid(minedGrid);
            String selection = getSelection(scanner);
            int row = getRow(selection);
            int col = getCol(selection);

            if (minedGrid[row][col] == 'X') {
                System.out.println("Oh no, you detonated a mine! Game over.");
                break;
            } else if (minedGrid[row][col] != '_') {
                System.out.println("Square already revealed! Please select another square:");
            } else {
                revealSquare(minedGrid, row, col);
                revealedSquares++;
                System.out.println("\nHere is your updated minefield:");
            }
        }

        if (revealedSquares == gridSize * gridSize - getNumMines(minedGrid)) {
            System.out.println("Congratulations, you have won the game!");
        }
        System.out.println("Press any key to play again...");
        scanner.nextLine();
        restartGame();
    }

    public static void restartGame() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int gridSize = getGridSizeFromUser(scanner);
        int numMines = getNumberOfMinesFromUser(scanner, gridSize);

        char[][] grid = generateGrid(gridSize);
        placeMines(grid, random, numMines);

        playGame(scanner, grid);
    }

    public static void printGridWithMineLocation(char[][] grid) {
        //For testing, comment out when done. Can be used as a help feature
        System.out.print("  ");
        for (int i = 0; i < grid.length; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();

        for (int i = 0; i < grid.length; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printGrid(char[][] grid) {
        System.out.print("  ");
        for (int i = 0; i < grid.length; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();

        for (int i = 0; i < grid.length; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'X') {
                    //hide location of mines
                    System.out.print("_ ");
                } else {
                    //if not a mine, show what is in grid
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public static String getSelection(Scanner scanner) {
        System.out.print("Select a square to reveal (e.g. A1): ");
        String selection;
        do {
            selection = scanner.nextLine().toUpperCase();
            if (selection.length() != 2 || !Character.isAlphabetic(selection.charAt(0)) || checkDigit(selection)) {
                System.out.println("Incorrect input. Please use format (e.g. A1).");
            }
        } while (selection.length() != 2 || !Character.isAlphabetic(selection.charAt(0)) || checkDigit(selection));
        return selection;
    }

    private static boolean checkDigit(String selection) {
        return selection.charAt(1) == '0' || !Character.isDigit(selection.charAt(1));
    }

    public static int getRow(String selection) {
        return selection.charAt(0) - 'A';
    }

    public static int getCol(String selection) {
        return Integer.parseInt(selection.substring(1)) - 1;
    }

    public static void revealSquare(char[][] grid, int row, int col) {
        char adjacentMines = (char) (calculateAdjacentMines(grid, row, col) + '0');
        grid[row][col] = adjacentMines;
        System.out.println("This square contains " + adjacentMines + " adjacent mines.");
    }

    public static int calculateAdjacentMines(char[][] grid, int row, int col) {
        int numMines = 0;
        int gridSize = grid.length;

        for (int i = Math.max(0, row - 1); i <= Math.min(gridSize - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(gridSize - 1, col + 1); j++) {
                if (i != row || j != col) {
                    if (grid[i][j] == 'X') {
                        numMines++;
                    }
                }
            }
        }
        return numMines;
    }

    public static int getNumMines(char[][] grid) {
        int numMines = 0;
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == 'X') {
                    numMines++;
                }
            }
        }
        return numMines;
    }
}