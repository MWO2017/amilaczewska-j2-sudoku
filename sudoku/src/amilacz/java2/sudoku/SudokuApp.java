package amilacz.java2.sudoku;

import java.io.File;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SudokuApp {
	public static void main(String[] args) {

		try {
			Workbook workbook = WorkbookFactory.create(new File("D:\\Documents\\podyplomowka\\cwiczenia\\java2-2\\cwiczenia\\sudoku\\sudoku-m.xlsx"));
			SudokuBoardChecker sbc =new SudokuBoardChecker(workbook);
			boolean checkIfOK = sbc.verifyBoardStructure(0);
			System.out.println("board has right structure: "+checkIfOK);
			if (checkIfOK) {
				if(sbc.verifyBoardCorrectness(0)) {
					System.out.println("board has right values");
				}else {
					System.out.println("some values are repeated");
				}
			}
			
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
