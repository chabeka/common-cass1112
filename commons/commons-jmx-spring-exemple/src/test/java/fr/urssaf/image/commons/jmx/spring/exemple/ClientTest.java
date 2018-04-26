package fr.urssaf.image.commons.jmx.spring.exemple;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.jmx.spring.exemple.mbean.IPremierBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
      "/applicationContext-jmx-server.xml", 
      "/applicationContext-jmx-client.xml" })
public class ClientTest {

   
   @Autowired
   private IPremierBean proxyPremierBean;
   
   
   @Test
   public void ClientExemple() {
      
      // Lecture de l'attribut HelloWord
      String helloWord = proxyPremierBean.getHelloWord();
      System.out.println("proxyPremierBean.getHelloWord() : " + helloWord);
      assertEquals("Vérification de l'attribut HelloWord","Hello test",helloWord);
      
      // Lecture/Ecriture de l'attribut Valeur
      int valeur = proxyPremierBean.getValeur();
      System.out.println("proxyPremierBean.getValeur() : " + valeur);
      assertEquals("Vérification de l'attribut Valeur avant modif",1,valeur);
      proxyPremierBean.setValeur(150);
      valeur = proxyPremierBean.getValeur();
      System.out.println("proxyPremierBean.getValeur() : " + valeur);
      assertEquals("Vérification de l'attribut Valeur après modif",150,valeur);
      
      // Invocation de l'opération sayHello
      proxyPremierBean.sayHello();
      
   }
   
   
}
