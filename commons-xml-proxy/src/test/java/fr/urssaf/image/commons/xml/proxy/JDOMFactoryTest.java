package fr.urssaf.image.commons.xml.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.CharEncoding;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.Test;

import fr.urssaf.image.commons.xml.JDOMUtil;
import fr.urssaf.image.commons.xml.proxy.modele.Bibliotheque;
import fr.urssaf.image.commons.xml.proxy.modele.Livre;



/**
 * Tests unitaires de la classe {@link JDOMFactory} 
 */
@SuppressWarnings("PMD")
public class JDOMFactoryTest {

   
   private static final String FILE = "bibliotheque_jdom.xml";
   
   @Test
   public void populate() throws JDOMException, IOException {

      File file = new File("src/test/resources/" + FILE);
      Document document = JDOMUtil.readFromFile(file,CharEncoding.UTF_8);
      Element element = document.getRootElement();

      JDOMFactory<Bibliotheque> xmlFactory = new JDOMFactory<Bibliotheque>(
            element, 
            Bibliotheque.class);

      Bibliotheque bibliotheque = xmlFactory.getInstance();

      assertNotNull("Noeud racine non trouvé",bibliotheque);

      Livre[] livres = bibliotheque.getLivre();

      assertLivre(livres[0], "romantisme du XXe", "Madame Bovary",
            "Gustave Flaubert");

      assertLivre(livres[1], "naturalisme", "l'argent", "Emile Zola");

      assertEquals("il y a plus de livres que prévus", 2, livres.length);

   }


   private void assertLivre(Livre livre, String style, String titre,
         String auteur) {

      assertEquals("Mauvais attribut","poche", livre.getType());
      assertEquals("Valeur incorrecte pour l'attribut style",style, livre.getStyle());
      assertEquals("Valeur incorrecte pour le titre",titre, livre.getTitre().getValue());
      assertEquals("Valeur incorrecte pour l'auteur du livre",auteur, livre.getAuteur().getValue());
      assertEquals("Valeur incorrecte pour la nationalité de l'auteur","FR", livre.getAuteur().getNat());

   }
   
}
