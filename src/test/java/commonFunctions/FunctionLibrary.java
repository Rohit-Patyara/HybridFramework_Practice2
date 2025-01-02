package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {

	public static WebDriver driver;
	public static Properties conpro;

	// Method for launching browser
	public static WebDriver startBrowser() throws Throwable {
		conpro = new Properties();
		conpro.load(new FileInputStream(".\\PropertyFiles\\Environment.properties"));
		if (conpro.getProperty("Browser").equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (conpro.getProperty("Browser").equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (conpro.getProperty("Browser").equalsIgnoreCase("MsEdge")) {
			driver = new EdgeDriver();
		} else {
			Reporter.log("Browser key value is not matching", true);
		}
		driver.manage().window().maximize();
		return driver;
	}

	// Method for launching application url
	public static void openUrl() {
		driver.get(conpro.getProperty("Url"));
	}

	// Method for webelement to wait
	public static void waitForElement(String locatorName, String locatorValue, String testData) {
		WebDriverWait myWait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(testData)));
		if (locatorName.equalsIgnoreCase("xpath")) {
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue)));
		}
		if (locatorName.equalsIgnoreCase("id")) {
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorValue)));
		}
		if (locatorName.equalsIgnoreCase("name")) {
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorValue)));
		}
	}

	// Method for type action
	public static void typeAction(String locatorName, String locatorValue, String testData) {
		if (locatorName.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(locatorValue)).clear();
			driver.findElement(By.xpath(locatorValue)).sendKeys(testData);
		}
		if (locatorName.equalsIgnoreCase("id")) {
			driver.findElement(By.id(locatorValue)).clear();
			driver.findElement(By.id(locatorValue)).sendKeys(testData);
		}
		if (locatorName.equalsIgnoreCase("name")) {
			driver.findElement(By.name(locatorValue)).clear();
			driver.findElement(By.name(locatorValue)).sendKeys(testData);
		}
	}

	// Method for click action
	public static void clickAction(String locatorName, String locatorValue) {
		if (locatorName.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(locatorValue)).click();
		}
		if (locatorName.equalsIgnoreCase("id")) {
			driver.findElement(By.id(locatorValue)).sendKeys(Keys.ENTER);
		}
		if (locatorName.equalsIgnoreCase("name")) {
			driver.findElement(By.name(locatorValue)).click();
		}
	}

	// Method for page title validation
	public static void validateTitle(String expectedTitle) {
		String actualTitle = driver.getTitle();
		try {
			Assert.assertEquals(actualTitle, expectedTitle, "Titles are not matching");
		} catch (AssertionError e) {
			Reporter.log(e.getMessage(), true);
		}
	}

	// Method for closing browser
	public static void closeBrowser() {
		driver.quit();
	}

	// Method for date generation
	public static String generateDate() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
		return df.format(date);
	}

	// Method for mouse hover
	public static void mouseHoverAction(String locatorName, String locatorValue) {
		Actions act = new Actions(driver);
		if (locatorName.equalsIgnoreCase("xpath")) {
			act.moveToElement(driver.findElement(By.xpath(locatorValue))).build().perform();
		}
		if (locatorName.equalsIgnoreCase("id")) {
			act.moveToElement(driver.findElement(By.id(locatorValue))).build().perform();
		}
		if (locatorName.equalsIgnoreCase("name")) {
			act.moveToElement(driver.findElement(By.name(locatorValue))).build().perform();
		}
	}

	// Method for category table
	public static void categoryTable(String exp_Data) {
		// If search text box is not displayed click search panel
		if (!driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).isDisplayed())
			// click search panel button
			driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).sendKeys(exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("Search_Button"))).click();
		String act_Data = driver
				.findElement(By.xpath("//table[@id='tbl_a_stock_categorieslist']/tbody/tr[1]/td[4]/div/span/span"))
				.getText();
		Reporter.log("Actual Category: " + act_Data + "      " + "Expected Category: " + exp_Data, true);
		try {
			Assert.assertEquals(act_Data, exp_Data, "Category name is not matching");
		} catch (AssertionError e) {
			Reporter.log(e.getMessage(), true);
		}
	}

	// Method for list boxes
	public static void dropDownAction(String locatorName, String locatorValue, String testData) {
		int value = Integer.parseInt(testData);
		if (locatorName.equalsIgnoreCase("xpath")) {
			Select sel = new Select(driver.findElement(By.xpath(locatorValue)));
			sel.selectByIndex(value);
		}
		if (locatorName.equalsIgnoreCase("id")) {
			Select sel = new Select(driver.findElement(By.id(locatorValue)));
			sel.selectByIndex(value);
		}
		if (locatorName.equalsIgnoreCase("name")) {
			Select sel = new Select(driver.findElement(By.name(locatorValue)));
			sel.selectByIndex(value);
		}
	}

	// Method to capture stock number into notepad
	public static void captureStock(String locatorName, String locatorValue) throws Throwable {
		String stockNumber = "";
		if (locatorName.equalsIgnoreCase("xpath")) {
			stockNumber = driver.findElement(By.xpath(locatorValue)).getAttribute("value");
		}
		if (locatorName.equalsIgnoreCase("id")) {
			stockNumber = driver.findElement(By.id(locatorValue)).getAttribute("value");
		}
		if (locatorName.equalsIgnoreCase("name")) {
			stockNumber = driver.findElement(By.name(locatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter(".\\CaptureData\\stocknumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stockNumber);
		bw.flush();
		fw.close();
	}

	// Method for stock table
	public static void stockTable() throws Throwable {
		// read stock number from note pad
		FileReader fr = new FileReader(".\\CaptureData\\stocknumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_Data = br.readLine();
		// If search text box is not displayed click search panel
		if (!driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).isDisplayed())
			// click search panel button
			driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).sendKeys(exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("Search_Button"))).click();
		String act_Data = driver
				.findElement(By.xpath("//table[@id='tbl_a_stock_itemslist']/tbody/tr[1]/td[8]/div/span/span"))
				.getText();
		Reporter.log("Actual Stock Number :" + act_Data + "   " + "Expected Stock Number :" + exp_Data);
		try {
			Assert.assertEquals(act_Data, exp_Data, "Stock Number is not matching");
		} catch (AssertionError e) {
			Reporter.log(e.getMessage(), true);
		}
		br.close();
	}

	// Method to capture supplier number into notepad
	public static void captureSupplier(String locatorName, String locatorValue) throws Throwable {
		String supplierNumber = "";
		if (locatorName.equalsIgnoreCase("xpath")) {
			supplierNumber = driver.findElement(By.xpath(locatorValue)).getAttribute("value");
		}
		if (locatorName.equalsIgnoreCase("id")) {
			supplierNumber = driver.findElement(By.id(locatorValue)).getAttribute("value");
		}
		if (locatorName.equalsIgnoreCase("name")) {
			supplierNumber = driver.findElement(By.name(locatorValue)).getAttribute("value");
		}
		// create note pad and write supplier number
		FileWriter fw = new FileWriter(".\\CaptureData\\suppliernumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(supplierNumber);
		bw.flush();
		bw.close();
	}

	// Method to validate supplier table
	public static void supplierTable() throws Throwable {
		// Read supplier number from notepad
		FileReader fr = new FileReader(".\\CaptureData\\suppliernumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_Data = br.readLine();
		// If search text box is not displayed click search panel
		if (!driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).isDisplayed())
			// click search panel button
			driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).sendKeys(exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("Search_Button"))).click();
		String act_Data = driver
				.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log("Actual supplier number :" + act_Data + "    " + "Expected supplier number " + exp_Data, true);
		try {
			Assert.assertEquals(act_Data, exp_Data, "Supplier number is not matching");
		} catch (AssertionError e) {
			Reporter.log(e.getMessage(), true);
		}
		br.close();
	}

	// Method to capture customer number into notepad
	public static void captureCustomer(String locatorName, String locatorValue) throws Throwable {
		String customerNumber = "";
		if (locatorName.equalsIgnoreCase("xpath")) {
			customerNumber = driver.findElement(By.xpath(locatorValue)).getAttribute("value");
		}
		if (locatorName.equalsIgnoreCase("id")) {
			customerNumber = driver.findElement(By.id(locatorValue)).getAttribute("value");
		}
		if (locatorName.equalsIgnoreCase("name")) {
			customerNumber = driver.findElement(By.name(locatorValue)).getAttribute("value");
		}
		// create note pad and write supplier number
		FileWriter fw = new FileWriter(".\\CaptureData\\customernumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(customerNumber);
		bw.flush();
		bw.close();
	}

	// Method to validate customer table
	public static void customerTable() throws Throwable {
		// Read supplier number from notepad
		FileReader fr = new FileReader(".\\CaptureData\\customernumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_Data = br.readLine();
		// If search text box is not displayed click search panel
		if (!driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).isDisplayed())
			// click search panel button
			driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).sendKeys(exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("Search_Button"))).click();
		String act_Data = driver
				.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log("Actual customer number :" + act_Data + "    " + "Expected customer number " + exp_Data, true);
		try {
			Assert.assertEquals(act_Data, exp_Data, "Customer number is not matching");
		} catch (AssertionError e) {
			Reporter.log(e.getMessage(), true);
		}
		br.close();
	}
}
