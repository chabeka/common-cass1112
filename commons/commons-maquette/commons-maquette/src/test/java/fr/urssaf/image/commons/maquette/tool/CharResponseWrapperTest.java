package fr.urssaf.image.commons.maquette.tool;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests unitaires de la classe {@link CharResponseWrapper}
 *
 */
@SuppressWarnings("PMD")
public class CharResponseWrapperTest {

   
   /**
    * Tests unitaires des getter/setter du champ <code>status</code><br>
    */
   @Test
   public void status_gettersSetters() {
      
      
      // Création des objets
      MockHttpServletResponse response = new MockHttpServletResponse();
      CharResponseWrapper wrapper = new CharResponseWrapper(response);
      
      int iExpected;
      int iActual;
      
      // Valeur initiale
      iActual = wrapper.getStatus();
      iExpected = CharResponseWrapper.DEFAULT_STATUS;
      assertEquals("Le statut initial n'est pas bon",iExpected,iActual);
      
      // Définition d'une nouvelle valeur avec un 1er setter
      wrapper.setStatus(HttpServletResponse.SC_FORBIDDEN);
      iActual = wrapper.getStatus();
      iExpected = HttpServletResponse.SC_FORBIDDEN;
      assertEquals("Le setter de status n'a pas fonctionné",iExpected,iActual);
      
      // Définition d'une nouvelle valeur avec un 2ème setter
      wrapper.setStatus(HttpServletResponse.SC_BAD_REQUEST,"un_message");
      iActual = wrapper.getStatus();
      iExpected = HttpServletResponse.SC_BAD_REQUEST;
      assertEquals("Le setter de status n'a pas fonctionné",iExpected,iActual);
      
   }
   
   
   /**
    * Test unitaire de sendError<br>
    * <br>
    * Test de la surcharge {@link CharResponseWrapper#sendError(int)}
    * 
    * @throws IOException 
    */
   @Test
   public void sendError_Test1() throws IOException {
      
      // Création des objets
      MockHttpServletResponse response = new MockHttpServletResponse();
      CharResponseWrapper wrapper = new CharResponseWrapper(response);
      
      // Positionne une erreur
      wrapper.sendError(HttpServletResponse.SC_FORBIDDEN);
      int iActual = wrapper.getStatus();
      int iExpected = HttpServletResponse.SC_FORBIDDEN;
      assertEquals("sendError n'a pas fonctionné",iExpected,iActual);
      
   }
   
   
   /**
    * Test unitaire de sendError<br>
    * <br>
    * Test de la surcharge {@link CharResponseWrapper#sendError(int, String)}
    * 
    * @throws IOException 
    */
   @Test
   public void sendError_Test2() throws IOException {
      
      // Création des objets
      MockHttpServletResponse response = new MockHttpServletResponse();
      CharResponseWrapper wrapper = new CharResponseWrapper(response);
      
      // Positionne une erreur
      wrapper.sendError(HttpServletResponse.SC_CONFLICT,"un_message");
      int iActual = wrapper.getStatus();
      int iExpected = HttpServletResponse.SC_CONFLICT;
      assertEquals("sendError n'a pas fonctionné",iExpected,iActual);
      
   }
   
   
   /**
    * Test d'écriture dans le wrapper
    */
   @Test
   public void testEcritureLecture() {
      
      // Création des objets
      MockHttpServletResponse response = new MockHttpServletResponse();
      CharResponseWrapper wrapper = new CharResponseWrapper(response);
      
      // Ecrit dans le wrapper
      String sTest = "test d'écriture";
      PrintWriter printerWriter = wrapper.getWriter();
      try {
         printerWriter.write(sTest);
      }
      finally {
         printerWriter.close();
      }
      
      // Vérifie le résultat
      String sExpected = sTest;
      String sActual = wrapper.toString();
      assertEquals("Echec d'écriture (ou de lecture) dans le wrapper",sExpected,sActual);
      
   }
   
   
}
