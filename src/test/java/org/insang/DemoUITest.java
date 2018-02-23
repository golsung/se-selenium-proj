package org.insang;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.insang.web.DemoController;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


public class DemoUITest {
	
		WebDriver driver;
		
		@Before
		public void setUp() {
			System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
			driver=new ChromeDriver();

	        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			
		}
		@After
		public void tearDown() {
			driver.findElement(By.id("delall")).click();
			driver.quit();
		}
		@Test
		public void shouldCreateNewMemberWhenValidMemberIsEntered() throws IOException {
			driver.get("http://localhost:8080");
	        driver.findElement(By.id("name1")).clear();
		    driver.findElement(By.id("name1")).sendKeys("insang");
		    driver.findElement(By.id("email1")).clear();
		    driver.findElement(By.id("email1")).sendKeys("insang@hansung.ac.kr");
		    driver.findElement(By.id("score1")).clear();
		    driver.findElement(By.id("score1")).sendKeys("30");
		   
		    driver.findElement(By.id("btnCreateMember")).click();
		    
		    List<WebElement> editBtns = driver.findElements(By.xpath("(//input[@value='편집'])")); 
		    List<WebElement> delBtns = driver.findElements(By.xpath("(//input[@value='삭제'])"));
		    assertTrue((editBtns.size()==1 && delBtns.size()==1));
		    assertEquals("insang", driver.findElement(By.xpath("//table[2]/tbody/tr/td")).getText());
		    assertEquals("insang@hansung.ac.kr", driver.findElement(By.xpath("//table[2]/tbody/tr/td[2]")).getText());
		    assertEquals("30", driver.findElement(By.xpath("//table[2]/tbody/tr/td[3]")).getText());
		    
//		    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//            String fileName = UUID.randomUUID().toString();
//            File targetFile = new File("C:\\tmp\\" + fileName + ".png");
//            FileUtils.copyFile(scrFile, targetFile);
		   
		}
		
		@Test
		public void shouldEditMemberWhenEditButtonIsClicked() {
			driver.get("http://localhost:8080");
	        driver.findElement(By.id("name1")).clear();
		    driver.findElement(By.id("name1")).sendKeys("insang");
		    driver.findElement(By.id("email1")).clear();
		    driver.findElement(By.id("email1")).sendKeys("insang@hansung.ac.kr");
		    driver.findElement(By.id("score1")).clear();
		    driver.findElement(By.id("score1")).sendKeys("30");
		    driver.findElement(By.id("btnCreateMember")).click();
		    
		    driver.findElement(By.xpath("(//input[@value='편집'])[1]")).click();

		    
		    assertEquals("Edit page", driver.getTitle());
		    
		    assertEquals("insang", driver.findElement(By.name("name")).getAttribute("value"));
		    assertEquals("insang@hansung.ac.kr", driver.findElement(By.name("email")).getAttribute("value"));
		    assertEquals("30", driver.findElement(By.name("score")).getAttribute("value"));

		    driver.findElement(By.name("score")).clear();
		    driver.findElement(By.name("score")).sendKeys("40");
		    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		    
		    assertEquals("New Member Page", driver.getTitle());
		    List<WebElement> editBtns = driver.findElements(By.xpath("(//input[@value='편집'])")); 
		    List<WebElement> delBtns = driver.findElements(By.xpath("(//input[@value='삭제'])"));
		    assertTrue((editBtns.size()==1 && delBtns.size()==1));
		    assertEquals("insang", driver.findElement(By.xpath("//table[2]/tbody/tr/td[1]")).getText());
		    assertEquals("insang@hansung.ac.kr", driver.findElement(By.xpath("//table[2]/tbody/tr/td[2]")).getText());
		    assertEquals("40", driver.findElement(By.xpath("//table[2]/tbody/tr/td[3]")).getText());
		}
		

}
