package fr.urssaf.image.sae.webdemo;

import org.junit.Ignore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/test")
@SuppressWarnings("PMD")
@Ignore
public class TestController {

   public static final String SERVLET = "connection.html";

  
   @RequestMapping
   protected final String doPost() {
      return "accueil.html";
   }
}
