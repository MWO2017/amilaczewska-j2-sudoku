package amilacz.java2.sudoku;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class SudokuBoardChecker {
	private Workbook workbook;

	public SudokuBoardChecker(Workbook workbook) {
		this.workbook = workbook;
	}

	// metoda sprawdzajaca poprawnosc typow w planszy sudoku
	public boolean verifyBoardStructure(int sheetIndex) {
		Sheet sheet = this.workbook.getSheetAt(sheetIndex);
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
							double value = cell.getNumericCellValue();
							if (value != 1.0 && value != 2.0 && value != 3.0 && value != 4.0 && value != 5.0
									&& value != 6.0 && value != 7.0 && value != 8.0 && value != 9.0) {
								isOK = false;
								System.out.println("not integer value found in board");
							}
							break;
						case STRING:
						case FORMULA:
						case ERROR:
						case BOOLEAN:
							System.out.println("incorrect value found in board: "+cellType);
							isOK = false;
							break;
						default:
							System.out.println("default");
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
		boolean isCorrect = true;
		boolean areRowsOK = checkRows(sheet);
		boolean areColumnsOK = checkColumns(sheet);
		boolean areSquaresOK = checkSquares(sheet);
		// if (checkRows(sheet)==false || checkColumns(sheet)==false) --> nie spr 2.
		// czesci alternatywy, jak 1. juz jest true
		if (areRowsOK == false || areColumnsOK == false || areSquaresOK == false) {
			isCorrect = false;
		}
		return isCorrect;
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
								System.out.println("in row " + (i + 1) + ", in column " + (j + 1)); // jesli (i+1) bez nawiasow, to nastepuje konkatenacja
								// napisow
								isOK = false;
							}
						}
					} 
				}
			}
		}
		return isOK;
	}

	// sprawdzanie wielokrotonosci wystapien liczb w kolumnach
	private boolean checkColumns(Sheet sheet) {
		boolean isOK = true;

		for (int j = 0; j < 9; j++) {
			HashMap<Double, Integer> columnMap = new HashMap<>();
			for (int i = 0; i < 9; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					Cell cell_col = row.getCell(j);
					if (cell_col != null) {
						CellType cellType = cell_col.getCellTypeEnum();
						if (cellType.equals(CellType.NUMERIC)) {
							double value = cell_col.getNumericCellValue();
							if (countUniquenessOfValues(columnMap, value) == false) {
								System.out.println("in column " + (j + 1) + ", in row " + (i + 1));
								isOK = false;
							}
						}
					} 
				}
			}
		}
		return isOK;
	}

	// sprawdzanie wielokrotonosci wystapien liczb w kwadratach 3x3
	private boolean checkSquares(Sheet sheet) {
		boolean isOK = true;

		for (int j = 0; j < 9; j = j + 3) {
			for (int i = 0; i < 9; i = i + 3) {
				if (checkOneSquares(sheet, i, j) == false) {
					isOK = false;
				}
			}
		}
		return isOK;
	}

	// sprawdzanie wielokrotonosci wystapien liczb w wybranym kwadracie 3x3
	private boolean checkOneSquares(Sheet sheet, int c, int r) {
		boolean isOK = true;
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
							if (countUniquenessOfValues(squareMap, value) == false) {
								System.out.println("in square in row " + (j + 1) + ", in column " + (i + 1));
								isOK = false;
							}
						}
					} 
				}
			}
		}
		return isOK;
	}

	// zliczanie wielokrotonosci wystapien liczb w danym secie
	private boolean countUniquenessOfValues(HashMap<Double, Integer> map, double value) {
		boolean isOK = true;
		if (map.containsKey(value)) {
			map.put(value, map.get(value) + 1);
			System.out.print(value + " repeated ");
			isOK = false;
		} else {
			map.put(value, 1);
		}
		return isOK;
	}

}
