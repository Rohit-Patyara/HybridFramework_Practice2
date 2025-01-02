package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFileUtil {

	Workbook wb;

	// Constructor for reading excel path
	public ExcelFileUtil(String excelPath) throws Throwable {
		FileInputStream fis = new FileInputStream(excelPath);
		wb = WorkbookFactory.create(fis);
	}

	// Count number of rows in a sheet
	public int rowCount(String sheetName) {
		return wb.getSheet(sheetName).getLastRowNum();
	}

	// Method for reading cell data
	public String getCellData(String sheetName, int row, int column) {
		String data;
		if (wb.getSheet(sheetName).getRow(row).getCell(column).getCellType() == CellType.NUMERIC) {
			int cellData = (int) wb.getSheet(sheetName).getRow(row).getCell(column).getNumericCellValue();
			data = String.valueOf(cellData);
		} else {
			data = wb.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
		}
		return data;
	}

	// Method for writing cell data
	public void setCellData(String sheetName, int row, int column, String status, String writeExcelpath)
			throws Throwable {
		Sheet ws = wb.getSheet(sheetName);
		Row rowNum = ws.getRow(row);
		Cell cell = rowNum.createCell(column);
		cell.setCellValue(status);
		if (status.equalsIgnoreCase("Pass")) {
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setColor(IndexedColors.GREEN.getIndex());
			font.setBold(true);
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);

		} else if (status.equalsIgnoreCase("Fail")) {
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			font.setBold(true);
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);

		} else if (status.equalsIgnoreCase("Blocked")) {
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setColor(IndexedColors.BLUE.getIndex());
			font.setBold(true);
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);
		}

		FileOutputStream fos = new FileOutputStream(writeExcelpath);
		wb.write(fos);
	}

	public static void main(String[] args) throws Throwable {
		ExcelFileUtil xl = new ExcelFileUtil("E:\\Qedge Data Files\\Sample.xlsx");
		int rc = xl.rowCount("Emp");
		System.out.println("No of Rows :" + rc);
		for (int i = 1; i <= rc; i++) {
			String fName = xl.getCellData("Emp", i, 0);
			String mName = xl.getCellData("Emp", i, 1);
			String lName = xl.getCellData("Emp", i, 2);
			String eid = xl.getCellData("Emp", i, 3);
			System.out.println(fName + "  " + mName + "  " + lName + "  " + eid);
			xl.setCellData("Emp", i, 4, "Pass", "E:\\Qedge Data Files\\abcdef12345.xlsx");
			//xl.setCellData("Emp", i, 4, "Fail", "E:\\Qedge Data Files\\abcdef123456.xlsx");
			//xl.setCellData("Emp", i, 4, "Blocked", "E:\\Qedge Data Files\\abcdef1234567.xlsx");
		}
	}
}
