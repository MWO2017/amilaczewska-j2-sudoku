package amilacz.java2.sudoku;

import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;

public class SudokuBoardChecker {
    private Workbook workbook;

    public SudokuBoardChecker(Workbook workbook) {
        this.workbook = workbook;
    }

    // metoda sprawdzajaca poprawnosc typow w planszy sudoku
    public boolean verifyBoardStructure(int sheetIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        boolean isOK = true;
        for (int i = 0; i < 9; i++) { // mozna zrobic petle while z isOK, zeby przy pierwszym wystapieniu false
            // i nie sprawdzac juz dalej
            Row row = sheet.getRow(i);
            if (row == null) {
                System.out.println("null row found in board");
            } else {
                for (int j = 0; j < 9; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        System.out.println("null cell found in board");
                    } else {
                        CellType cellType = cell.getCellTypeEnum();
                        switch (cellType) {
                            case BLANK:
                                break;
                            case NUMERIC:
                                break;
                            case STRING:
                                System.out.println("incorrect value found in board: " + cellType);
                                break;
                            case FORMULA:
                                System.out.println("incorrect value found in board: " + cellType);
                                break;
                            case ERROR:
                                System.out.println("incorrect value found in board: " + cellType);
                                break;
                            case BOOLEAN:
                                System.out.println("incorrect value found in board: " + cellType);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return isOK;
    }

    // metoda sprawdzajaca porawnosc planszy sudoku tzn. brak powtorzen w rzedach,
    // kolumnach i kwadratach 3x3
    public boolean verifyBoardCorrectness(int sheetIndex) {
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
       return (!checkRows(sheet) || !checkColumns(sheet) || !checkSquares(sheet));
    }

    // sprawdzanie wielokrotonosci wystapien liczb w wierszach
    private boolean checkRows(Sheet sheet) {
        boolean isOK = true;
        double value;

        for (int i = 0; i < 9; i++) {
            Row row = sheet.getRow(i);
            HashMap<Double, Integer> rowMap = new HashMap<>();
            for (int j = 0; j < 9; j++) {
                if (row != null) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        CellType cellType = cell.getCellTypeEnum();
                        if (cellType.equals(CellType.NUMERIC)) {
                            value = cell.getNumericCellValue(); // czy deklaracje lepiej dawac przed petla?
                            if (countUniquenessOfValues(rowMap, value) == false) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    // sprawdzanie wielokrotonosci wystapien liczb w kolumnach
    private boolean checkColumns(Sheet sheet) {
        HashMap<Double, Integer> columnMap = new HashMap<>();
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell_col = row.getCell(j);
                    if (cell_col != null) {
                        CellType cellType = cell_col.getCellTypeEnum();
                        if (cellType.equals(CellType.NUMERIC)) {
                            double value = cell_col.getNumericCellValue();
                            return countUniquenessOfValues(columnMap, value);
                            }
                        }
                    }
                }
            }
        return true;
    }

    // sprawdzanie wielokrotonosci wystapien liczb w kwadratach 3x3
    private boolean checkSquares(Sheet sheet) {
        for (int j = 0; j < 9; j = j + 3) {
            for (int i = 0; i < 9; i = i + 3) {
                return checkOneSquare(sheet, i, j);
            }
        }
        return true;
    }

    // sprawdzanie wielokrotonosci wystapien liczb w wybranym kwadracie 3x3
    private boolean checkOneSquare(Sheet sheet, int c, int r) {
        HashMap<Double, Integer> squareMap = new HashMap<>();
        for (int j = (r / 3) * 3; j < (r / 3) * 3 + 3; j++) {
            Row row = sheet.getRow(j);
            for (int i = (c / 3) * 3; i < (c / 3) * 3 + 3; i++) {
                if (row != null) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        CellType cellType = cell.getCellTypeEnum();
                        if (cellType.equals(CellType.NUMERIC)) {
                            double value = cell.getNumericCellValue();
                            return countUniquenessOfValues(squareMap, value);
                        }
                    }
                }
            }
        }
        return true;
    }

    // zliczanie wielokrotonosci wystapien liczb w danym secie
    private boolean countUniquenessOfValues(HashMap<Double, Integer> map, double value) {
        if (map.containsKey(value)) {
            map.put(value, map.get(value) + 1);
            System.out.print(value + " repeated ");
           return false;
        } else {
            map.put(value, 1);
        }
        return true;
    }

}
