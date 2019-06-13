import java.util.Timer;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AutomationBase {
	
	protected static WebDriver driver;
	protected static Actions action;
	protected static WebDriverWait wait;
	protected static boolean IsTimerSet = false;
	
	protected static WebElement WaitUntillClickable(By selector) throws InterruptedException
	{
		IsPageLoaded(driver);
		
		return wait.until(ExpectedConditions.elementToBeClickable(selector));
			 		
	} 
	
	protected static void highlightElement(WebDriver driver, WebElement element) { 

        JavascriptExecutor js = ((JavascriptExecutor) driver); 
        System.out.println("INSIDE HIGHLIGHT");
        
        js.executeScript("arguments[0]" + ".setAttribute('style','BACKGROUND-COLOR: black; BORDER: 3px solid red;')",
                element);
        //element.sendKeys(text);
        //
        

        try { 
            Thread.sleep(1000); 
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        } 
         
    } 

	

	
	
	protected static WebElement WaitUntillDisplayed(By selector) throws InterruptedException
	{
		IsPageLoaded(driver);
		
		return wait.until(ExpectedConditions.presenceOfElementLocated(selector));
			 		
	} 
	
		
	public static  boolean IsPageLoaded(WebDriver driver ) throws InterruptedException
	{
		        boolean readyState = false;
		        Timer timer = new Timer();
		         
	            while(!readyState)
	            {
	            	if (IsTimerSet==false)
	            	{
	            	 timer.schedule(new TimerTask() {
			        	  @Override
			        	  public void run() {
			        	    driver.navigate().refresh();
			        	    IsTimerSet=true;
			        	  }
			        	}, 5*1000);
	            	}
	            	 
	                JavascriptExecutor execute = (JavascriptExecutor)driver;
	                execute.executeScript("window.scrollTo(0, document.body.offsetHeight)");
	                String s1 = execute.executeScript("return document.readyState").toString();
	                 
	                                
	                readyState = s1.equals("complete") ;
	                if (readyState== true)
	                {
	                	timer.cancel();
	                
	                } 
	            }
	            IsTimerSet= false;
	            
	            return readyState;
	}
	
	public static void WaitforElement(By locator) 
	{
		try{
		new WebDriverWait(driver, 15).ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
		.until(ExpectedConditions.visibilityOfElementLocated(locator));
		}
		catch(Exception e)
		{
			driver.navigate().refresh();
			new WebDriverWait(driver, 5).ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
			.until(ExpectedConditions.visibilityOfElementLocated(locator));
		}
		
	}
	
	public static void isElementDisplayed() throws InterruptedException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver,10000);
	        Thread.sleep(2000);
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-bar-spinner")));
	        
	    } 
	    catch (org.openqa.selenium.NoSuchElementException
	            | org.openqa.selenium.StaleElementReferenceException
	            | org.openqa.selenium.TimeoutException e) {
	        
	    }
	}
	
	public static void EnterText(WebElement element, String text)
	{
		System.out.print("ENTERING - "+ text);
		 element.sendKeys(text);
		 
		 String actualText = element.getText();
		 if(!actualText.equalsIgnoreCase(text))
		 {
			 element.clear();
			 element.sendKeys(text);
			 
		 }
		
		////Actions action =  new Actions(driver);
		
		////action.click(element).build().perform();
		
		////action.sendKeys(element,text).perform();
		//action.build();
		//action.perform();
		
		
	}

}
