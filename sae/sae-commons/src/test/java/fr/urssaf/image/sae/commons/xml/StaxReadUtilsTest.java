package fr.urssaf.image.sae.commons.xml;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class StaxReadUtilsTest {

   @Test
   public void testLecture() throws FileNotFoundException, XMLStreamException,
         IOException {
      ClassPathResource resource = new ClassPathResource(
            "xml/fichierLecture.xml");
      int nombreElements = StaxReadUtils.compterElements(resource.getFile(),
            "nom");
      Assert.assertEquals("le nombre d'éléments doit etre correct", 5,
            nombreElements);
   }

   @Test
   public void testGetElement() throws FileNotFoundException,
         XMLStreamException, IOException {
      ClassPathResource resource = new ClassPathResource(
            "xml/fichierLecture.xml");
      String value = StaxReadUtils.getElementValue(resource.getFile(), "nom");
      Assert.assertEquals("la valeur doit etre correcte", "nom 1", value);
   }

   @Test
   public void testGetElementNull() throws FileNotFoundException,
         XMLStreamException, IOException {
      ClassPathResource resource = new ClassPathResource(
            "xml/fichierLecture.xml");
      String value = StaxReadUtils.getElementValue(resource.getFile(),
            "element");
      Assert.assertNull("la valeur doit etre nulle", value);
   }
   
   @Test
   public void testGetElementNotInXml() throws FileNotFoundException,
         XMLStreamException, IOException {
      ClassPathResource resource = new ClassPathResource(
            "xml/fichierLecture.xml");
      String value = StaxReadUtils.getElementValue(resource.getFile(),
            "balise");
      Assert.assertNull("la valeur doit etre nulle", value);
   }

}
