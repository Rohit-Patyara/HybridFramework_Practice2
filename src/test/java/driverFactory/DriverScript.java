package driverFactory;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {

	WebDriver driver;
	String inputPath = ".\\FileInput\\DataEnginePrac2.xlsx";
	String outputPath = ".\\FileOutput\\HybridFrameworkResultsPrac2.xlsx";
	ExtentReports report;
	ExtentTest logger;
	String testCases = "MasterTestCases";

	public void startTest() throws Throwable {

		ExcelFileUtil xl = new ExcelFileUtil(inputPath);
		// Iterate all test cases in testCases
		for (int i = 1; i <= xl.rowCount(testCases); i++) {
			String TCstatus = "True";
			if (xl.getCellData(testCases, i, 2).equalsIgnoreCase("Y")) {
				// Read corresponding sheet or test cases
				String TcModule = xl.getCellData(testCases, i, 1);
				// Define path of HTML report
				report = new ExtentReports(
						".\\target\\ExtentReports\\" + TcModule + " " + FunctionLibrary.generateDate() + ".html");
				logger = report.startTest(TcModule);
				logger.assignAuthor("Rohit Patyara");
				// Iterate all rows in TcModule sheet
				for (int j = 1; j <= xl.rowCount(TcModule); j++) {
					String description = xl.getCellData(TcModule, j, 0);
					String objectType = xl.getCellData(TcModule, j, 1);
					String locatorName = xl.getCellData(TcModule, j, 2);
					String locatorValue = xl.getCellData(TcModule, j, 3);
					String testData = xl.getCellData(TcModule, j, 4);
					try {
						if (objectType.equalsIgnoreCase("startBrowser")) {
							driver = FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("openUrl")) {
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("waitForElement")) {
							FunctionLibrary.waitForElement(locatorName, locatorValue, testData);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("typeAction")) {
							FunctionLibrary.typeAction(locatorName, locatorValue, testData);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("clickAction")) {
							FunctionLibrary.clickAction(locatorName, locatorValue);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("validateTitle")) {
							FunctionLibrary.validateTitle(testData);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("closeBrowser")) {
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("mouseHoverAction")) {
							FunctionLibrary.mouseHoverAction(locatorName, locatorValue);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("categoryTable")) {
							FunctionLibrary.categoryTable(testData);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("dropDownAction")) {
							FunctionLibrary.dropDownAction(locatorName, locatorValue, testData);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("captureStock")) {
							FunctionLibrary.captureStock(locatorName, locatorValue);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("stockTable")) {
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("captureSupplier")) {
							FunctionLibrary.captureSupplier(locatorName, locatorValue);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("supplierTable")) {
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("captureCustomer")) {
							FunctionLibrary.captureCustomer(locatorName, locatorValue);
							logger.log(LogStatus.INFO, description);
						}
						if (objectType.equalsIgnoreCase("customerTable")) {
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, description);
						}
						// write as Pass into TCModule sheet in status cell if keyword/method executed
						// successfully
						xl.setCellData(TcModule, j, 5, "Pass", outputPath);
						logger.log(LogStatus.PASS, description);
					} catch (Exception e) {
						Reporter.log(e.getMessage(), true);
						// write as Fail into TCModule sheet in status cell if keyword/method fails to
						// execute
						xl.setCellData(TcModule, j, 5, "Fail", outputPath);
						logger.log(LogStatus.FAIL, description);
						TCstatus = "False";
					}
					// report.endTest(logger);
					// report.flush();
				}
				report.endTest(logger);
				report.flush();
				if (TCstatus.equalsIgnoreCase("True")) {
					// write as Pass into TestCases sheet
					xl.setCellData(testCases, i, 3, "Pass", outputPath);
				} else {
					// write as Fail into TestCases sheet
					xl.setCellData(testCases, i, 3, "Fail", outputPath);
				}
			} else {
				// write as blocked for test cases flag to N in TestCases sheet
				xl.setCellData(testCases, i, 3, "Blocked", outputPath);
			}
		}

	}
}
