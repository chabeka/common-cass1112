package fr.urssaf.image.sae.webdemo.security;

import static org.junit.Assert.assertEquals;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@SuppressWarnings("PMD")
@Ignore
public class AuthenticationTest {

   private WebDriver driver;

   @Before
   public void before() throws ServletException {

      driver = new HtmlUnitDriver();

   }

   @After
   public void after() {

      driver.close();
   }

   @Test
   public void connectFailure_title() throws Exception {

      driver.get("http://localhost:8080/sae-webdemo/");

      assertEquals("Erreur 403", driver.getTitle());

   }

   @Test
   public void connectFailure_copyright() throws Exception {

      driver.get("http://localhost:8080/sae-webdemo/");

      WebElement webElement = driver.findElement(By.id("copyright"));
      assertEquals("Copyright CIRTIL 2010", webElement.getText());
     
   }

}
