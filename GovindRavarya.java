import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GovindRavarya extends AutomationBase {
	
	

	public static void main(String[] args) throws IOException, InterruptedException 
	{
					
		 StartBrowser();
		 driver.get("https://org.gem.gov.in/search/listproduct/listofsupliers");
		 
		
		 List allSellerlist =  new ArrayList<String>();
		 
		 for (char c1 = 'B'; c1<='Z'; c1++)
		 {
			 driver.findElement(By.xpath("//ul/li/a[text()='"+c1+"']")).click();
			try
			 {
		 for(int i=1; i<=500; i++)
		 {
			 IsPageLoaded(driver);
			 isElementDisplayed();
			 driver.findElement(By.xpath("//ul/li/a[text()='"+i+"']")).click(); 
			 
			List<WebElement> list =  driver.findElements(By.xpath("//ul/li[@id='pro']//a"));
			
			for (WebElement element : list) 
			{
				//allSellerlist.add(element.getText());
				
				System.out.print(element.getText()+ " | ");
			}
			
			System.out.print("\n");
		 }
			 }
			 catch (Exception e) {
				continue;
			}
		 }
	
		
		
	
	}
	
	public static void StartBrowser() throws IOException  
	{
		System.out.println("launching acitvity");
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
		
		System.out.println(System.getProperty("user.dir"));
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\lib\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-infobars");
		options.addArguments("start-maximized");
		driver = new ChromeDriver(options);
		
		wait = new WebDriverWait(driver, 30);
		driver.get("https://sso.gem.gov.in/ARXSSO/oauth/doLogin");
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			
	}

	private static void Login(String UertNameTxt, String PasswordText) throws InterruptedException {
		
		WebElement userName = WaitUntillClickable(By.id("loginid"));
		WebElement password =  WaitUntillClickable(By.id("password"));
		WebElement captcha =  WaitUntillClickable(By.id("captcha_math"));
		WebElement submit =  WaitUntillClickable(By.xpath("//button[text()='Submit']"));
		
//		Actions action = new Actions(driver);
//		
//		action.moveToElement(captcha);
//		action.contextClick(captcha);
//		action.sendKeys(Keys.DOWN);
//		action.sendKeys(Keys.DOWN);
//		action.sendKeys(Keys.RETURN);
		
		
		
		EnterText(userName, UertNameTxt);
		EnterText(password, PasswordText);
				
		captcha.click();
		Thread.sleep(15000);
		
		submit.click();
			
	}

	private static void GoToProductList() throws InterruptedException
	{
		// catalog - //a[contains(.,'Catalog') and @id='dLabel']
		// product - (//a[text()='Products'])[2]
		// search - (//a[text()='Search'])[1]
		
		//WebElement catelog = driver.findElement(By.xpath("//div[@class='row logwrp']//a[contains(.,'Catalog')]"));
		IsPageLoaded(driver);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(.,'Catalog') and @id='dLabel']")));
		WebElement catelog = WaitUntillClickable(By.xpath("//a[contains(.,'Catalog') and @id='dLabel']"));
		WebElement Product = driver.findElement(By.xpath("(//a[contains(.,'Products')])[2]"));

		Actions ac = new Actions(driver);
		ac.click(catelog).pause(1000).moveToElement(Product).pause(1000).build().perform();
		ac.pause(1000);
		ac.click(driver.findElement(By.xpath("(//a[.='Search'])[2]"))).pause(1000).build().perform();
		
		IsPageLoaded(driver);
			
		 
	}
	
	
	
	private static float GetMinPrice(int pageIndex) throws InterruptedException 
	{
		//div[@id='other_sellers']/div/a
		
		IsPageLoaded(driver);
		
		// other sellers
		WaitUntillClickable(By.xpath("//div[@id='other_sellers']/div/a")).click();
		
		List<WebElement> allPrices = driver.findElements(By.xpath("//div[@id='sellers-table-wrap']/table/tbody/tr/td[2]/span/span"));
		ArrayList<Float> allPriceFloatVal = new ArrayList<Float>();
		
		for (WebElement onePriceElement : allPrices)
		{
			Thread.sleep(1500);
			
			String price =onePriceElement.getText().split("Rs.")[1];
			allPriceFloatVal.add(Float.parseFloat(price));
			System.out.print(price+ " | ");
		}
	    float val = Collections.min(allPriceFloatVal);
		System.out.println("\t\t\tMinimum Value :"+val);
		
		// gets back to first page of product list
		driver.navigate().back();
		//IsPageLoaded(driver);
		//isElementDisplayed();
		
		Thread.sleep(5000);
		 WaitUntillClickable(By.xpath("//ul/li[@role='menuitem']/a[text()="+ pageIndex +"]"));
		 
		 WebElement currentPage = driver.findElement(By.xpath("//ul/li[@role='menuitem']/a[text()="+ pageIndex +"]"));
		 
		currentPage.click();
		return val;
		
	}
	
	private static String sendPost(String apiKey, boolean isOverlayRequired, String imageUrl, String language) throws Exception 
	{
		 String url = "https://api.ocr.space/parse/image";
		 
        URL obj = new URL(url); // OCR API Endpoints
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        
        

        JSONObject postDataParams = new JSONObject();
        
        postDataParams.put("apikey", apiKey);//TODO Add your Registered API key
        postDataParams.put("isOverlayRequired", isOverlayRequired);
        postDataParams.put("url", imageUrl);
        postDataParams.put("language", language);


        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(getPostDataString(postDataParams));
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result
        return String.valueOf(response);
    }
	
	public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
	}
}
