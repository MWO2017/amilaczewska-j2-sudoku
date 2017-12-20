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
			Workbook workbook = WorkbookFactory.create(new File("sudoku.xlsx"));
			SudokuBoardChecker sbc =new SudokuBoardChecker(workbook);
			for (int i =0; i<7;i++){
				System.out.println("Board number " + (i +1) + " result: "+sbc.verifyBoardStructure(i));
			}
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
