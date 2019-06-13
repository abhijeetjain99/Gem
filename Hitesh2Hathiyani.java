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
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;


public class Hitesh2Hathiyani  extends AutomationBase{

	public static void main(String[] args) throws IOException, InterruptedException 
	{
					
		String productNameStr = null;
		String soldByNameText = null;
		int count = 0;
		int count2= 0;
		
		// start browser - DONE
		StartBrowser();
		
		// go to site
		// enter credentials - DONE
		
		
		//Login("hitesh2_hathiyani", "Hitesh@5151");
		
		//Login("govind_ravriya", "Hitesh@9898");
		Login("hitesh2_hathiyani", "Hitesh@5151");
		//Login("vinod1_patel", "Hitesh@1010");
		// go to product list - DONE
		GoToProductList();
		
		 /*
			 * 1.get all pages count
			 * 2. click each product from every page
			 * 		-> if seller = "HR" -> skip the updation of value for this product
			 * 		-> else -> go to other seller link 
			 * 				-> from the all seller present get the minimum value 
			 * 					and add the new minimum value against the "HR" seller 
			 * 					and save the form
			 *
			 */
		// get all pages count
		Thread.sleep(2000);
		WaitUntillClickable(By.xpath("//ul/li[@role='menuitem']/a[text()>0]"));
		int pages = driver.findElements(By.xpath("//ul/li[@role='menuitem']/a[text()>0]")).size();
		
		System.out.println("Total Pages: "+ pages);
		//for(int pageIndex = 403 ; pageIndex >= 1; pageIndex-- )
		for(int pageIndex = 54 ; pageIndex<=pages; pageIndex++ )
		{
			//System.out.println("\tPage: "+ pageIndex);
		
			// current page click
			 WaitUntillClickable(By.xpath("//ul/li[@role='menuitem']/a[text()="+ pageIndex +"]"));
			 
			 String xpath = "//ul/li[@role='menuitem']/a[text()="+ pageIndex +"]";
			// System.out.println("Current Page Xpath: "+ xpath);
			 WebElement currentPage = driver.findElement(By.xpath(xpath));
			 
			 IsPageLoaded(driver);
			 isElementDisplayed();
			  
			currentPage.click();
			
			// 
			int tableRow= wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//tbody/tr"))).size();
			
			//System.out.println("\tTotal rows: "+ tableRow);
			JavascriptExecutor js1 = (JavascriptExecutor) driver;
			// 10 PRODUCTS FROM THE CURRENT PAGE
			//int a[] = {4,5,7,9,10};
			//for (int row : a) 
			for (int row=1; row <=tableRow; row++)
			
			{
				// productName - 
								 
				Thread.sleep(500);
				
				 IsPageLoaded(driver);
				 isElementDisplayed();
				 
				 WaitforElement(By.xpath("//tbody/tr["+ row +"]/td[1]/a"));
				 
				 WebElement productName = driver.findElement(By.xpath("//tbody/tr["+ row +"]/td[1]/a"));
				 
					productNameStr= productName.getText();
					
					String ourOfferedValue = driver.findElement(By.xpath("//tbody/tr["+ row +"]/td[9]")).getText();
					
					float ourOfferedValueStringReplaced = Float.parseFloat(ourOfferedValue.replace(",","")) ;
					
					
				 wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr["+ row +"]/td[1]/a"))).click();
				 
								 				
				//Thread.sleep(2000);
				
				IsPageLoaded(driver);	
				WebElement soldByName = null;
				
				// current price
				String price = WaitUntillClickable(By.xpath("(//div[@id='pricing_summary']/descendant::span/span)[1]")).getText().split("Rs.")[1];
				// IF PRICE CONTAINS COMMAS THEN REMOVE IT E.X. 1,000 -> 1000
				float floatPrice = Float.parseFloat(price.replace(",","")) ;
				
				// reducing 0.01 from price till 2 decimal digit 
				String newPrice = String.format("%.2f",floatPrice-00.01);
				
				if( Float.compare(ourOfferedValueStringReplaced , floatPrice)==0 )////soldByNameText.equalsIgnoreCase("H R Imaging Solution")
				{
					System.out.println("- "+ (++count2) +" | Page Number- " 
							+ "( "+ pageIndex +"- " + row +" ) | Product Name- " 
							+ productNameStr +" |");
					
					try{
						driver.navigate().back();
					}
					catch(Exception e)
					{
						 driver.navigate().refresh();
						 WaitforElement(By.xpath("//ul/li[@role='menuitem']/a[text()="+ pageIndex +"]"));
					}
					
				   Thread.sleep(2000);
				 
				   isElementDisplayed();			   
				  // WaitUntillClickable(By.xpath("//ul/li[@role='menuitem']/a[text()="+ pageIndex +"]")).click();
				   currentPage = WaitUntillClickable(By.xpath("//ul/li[@role='menuitem']/a[text()="+ pageIndex +"]"));
					
					js1.executeScript("arguments[0].scrollIntoView(false);",currentPage);
					
					currentPage.click();
				}
				else
				{
					// back to all pages list for changing stack value
					driver.navigate().back();
					//System.out.print(pageIndex +" - "+ row +" -->"+ newPrice);
					
					System.out.println(
							" Auth error on "+ 
							"Page Number ("+ pageIndex + "-"+ row + ") "+ price +
							" -> "+ newPrice +
							" | Product Name: "+ productNameStr +
							" | on :" + new java.util.Date());
					
					//// FROM THIS
				
					
					//// TILL THEI
				}
				 
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
		highlightElement(driver, submit);
		
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
