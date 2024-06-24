import org.junit.Test;

import java.util.Random;

import static org.example.MineSweeper.*;
import static org.junit.Assert.assertEquals;

public class MineSweeperTest {

    @Test
    public void testGenerateGrid() {
        int gridSize = 5;
        char[][] grid = generateGrid(gridSize);

        // Assertions
        assertEquals(gridSize, grid.length); // Check number of rows
        assertEquals(gridSize, grid[0].length); // Check number of columns
    }

    @Test
    public void testPlaceMines() {
        int gridSize = 5;
        int mines = 3;
        char[][] grid = generateGrid(gridSize);
        Random random = new Random();
        placeMines(grid, random, mines);

        int finalMines = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == 'X') {
                    finalMines++;
                }
            }
        }

        // Assertions
        assertEquals(mines, finalMines); // Check the number of miens
    }

    @Test
    public void testCalculateAdjacentMines() {
        int gridSize = 5;
        char[][] grid = generateGrid(gridSize);

        grid[1][1] = 'X';
        grid[0][1] = 'X';
        grid[1][0] = 'X';

        int result = calculateAdjacentMines(grid, 0, 0);
        int expectedResult = 3;

        assertEquals(result, expectedResult); // Check the number adjacent mines

        grid[1][0] = '_';

        result = calculateAdjacentMines(grid, 0, 0);
        expectedResult = 2;

        assertEquals(result, expectedResult); // Check the number adjacent mines
    }

    @Test
    public void testGetNumMines() {
        int gridSize = 5;
        int mines = 3;
        char[][] grid = generateGrid(gridSize);
        Random random = new Random();
        placeMines(grid, random, mines);

        int numMines = getNumMines(grid);
        assertEquals(numMines, mines);
    }
}