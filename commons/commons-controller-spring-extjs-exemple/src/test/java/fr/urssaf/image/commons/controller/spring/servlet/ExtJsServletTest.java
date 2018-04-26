package fr.urssaf.image.commons.controller.spring.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import fr.urssaf.image.commons.controller.spring.exceptions.RessourceNonSpecifieeException;
import fr.urssaf.image.commons.controller.spring.exceptions.RessourceNonTrouveeException;
import fr.urssaf.image.commons.controller.spring.exceptions.TypeMimeInconnuException;
import fr.urssaf.image.commons.util.resource.ResourceUtil;

/**
 * Tests unitaires de la classe ExtJsServlet
 *
 */
@SuppressWarnings("PMD")
public class ExtJsServletTest {

   
   /**
    * Test de la méthode {@link ExtJsServlet#doGet}<br>
    * <br>
    * Cas de test : le paramètre "name" n'est pas envoyé à la requête<br>
    * <br>
    * Résultat attendu : levée d'une exception {@link RessourceNonSpecifieeException}
    *  
    * @throws IOException en cas d'erreur d'E/S
    */
   @Test(expected = RessourceNonSpecifieeException.class)
   public void test_doGet_1() throws IOException
   {
      
      // Création des objets Mock
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();

      // Création de l'objet Servlet
      ExtJsServlet servlet = new ExtJsServlet();
      
      // Appel de la servlet
      servlet.doGet(request, response);
      
      // Si l'exception n'est pas levée, le test sera en échec
      
   }
   
   
   /**
    * Test de la méthode {@link ExtJsServlet#doGet}<br>
    * <br>
    * Cas de test : le paramètre "name" est envoyée à la requête, avec la valeur <code>null</code><br>
    * <br>
    * Résultat attendu : levée d'une exception {@link RessourceNonSpecifieeException}
    *  
    * @throws IOException en cas d'erreur d'E/S
    */
   @Test(expected = RessourceNonSpecifieeException.class)
   public void test_doGet_2() throws IOException
   {
      
      // Création des objets Mock
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();

      // Création de l'objet Servlet
      ExtJsServlet servlet = new ExtJsServlet();
      
      // Ajoute à la Request d'un nom de ressource inexistant
      String nomRessource = null;
      request.addParameter("name", nomRessource);
      
      // Appel de la servlet
      servlet.doGet(request, response);
      
      // Si l'exception attendue n'est pas levée, le test passera en échec
      
   }
   
 
   /**
    * Test de la méthode {@link ExtJsServlet#doGet}<br>
    * <br>
    * Cas de test : le paramètre "name" contient un nom de ressource inexistante<br>
    * <br>
    * Résultat attendu : levée d'une exception {@link RessourceNonSpecifieeException} avec
    * le nom de la ressource demandée
    * 
    * @throws ServletException exception levée par {@link javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)}
    * @throws IOException en cas d'erreur d'E/S 
    */
   @Test
   public void test_doGet_3() throws ServletException, IOException
   {
      
      // Création des objets Mock
      MockServletConfig config = new MockServletConfig();
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      
      // Création de l'objet Servlet
      ExtJsServlet servlet = new ExtJsServlet();
      servlet.init(config);
      
      // Ajoute à la Request d'un nom de ressource inexistant
      String nomRessource = "toto";
      request.addParameter("name", nomRessource);
      
      // Appel de la servlet. On attend une RessourceNonTrouveeException
      try
      {
         servlet.doGet(request, response);
      }
      catch (RessourceNonTrouveeException ex)
      {
         
         // Vérifie l'exception
         String expected = nomRessource;
         String actual = ex.getNomRessource();
         assertEquals("L'exception levée n'est pas correcte",expected,actual);
         
         // Vérifie que la réponse soit vide
         // On vérifie que rien n'a été écrit dans la réponse
         // D'abord la taille
         int iExpected = 0;
         int iActual = response.getContentLength();
         assertEquals("L'objet HttpServletResponse n'est pas vide !",iExpected,iActual);
         // Puis le contenu
         String sExpected = "";
         String sActual = response.getContentAsString();
         assertEquals("L'objet HttpServletResponse n'est pas vide !",sExpected,sActual);
         
         // Si on arrive jusque là, le test est OK
         return;
         
      }
      
      // Si on arrive jusque là, c'est que l'exception n'a pas été levée
      // Le test est donc en échec
      fail("L'exception attendue RessourceNonTrouveeException n'a pas été levée");
      
   }
   
   /**
    * Test de la méthode {@link ExtJsServlet#doGet}<br>
    * <br>
    * Cas de test : le paramètre "name" contient le bon nom de ressource<br>
    * <br>
    * Résultat attendu : la ressource est écrite dans la <code>HttpServletResponse</code>
    * 
    * @throws ServletException exception levée par {@link javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)}
    * @throws IOException en cas d'erreur d'E/S 
    * @throws URISyntaxException exception levée par {@link #lectureFichierRessource}
    */
   @Test
   public void test_doGet_4() throws ServletException, IOException, URISyntaxException
   {
      
      // Création des objets Mock
      MockServletConfig config = new MockServletConfig();
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      
      // Création de l'objet Servlet
      ExtJsServlet servlet = new ExtJsServlet();
      servlet.init(config);
      
      // Ajoute à la Request du nom de la ressource à récupérer
      String nomRessource = "/js/fr.urssaf.image.commons.js.extjs.js";
      request.addParameter("name", nomRessource);
      
      // Appel de la servlet.
      servlet.doGet(request, response);
      
      // Vérifie le type MIME de la réponse
      String sExpected = "application/octet-stream";
      String sActual = response.getContentType();
      assertEquals("Le type MIME est incorrecte",sExpected,sActual);
      
      // Charge le fichier Javascript
      byte[] contenuJs = lectureFichierRessource(nomRessource);
      
      // Vérifie la taille de la réponse
      int iExpected = contenuJs.length;
      int iActual = response.getContentLength();
      assertEquals("La ressource n'a pas été correctement écrite dans l'objet HttpServletResponse",
            iExpected,iActual);
      
      // Vérifie le contenu de la réponse
      sExpected = new String(contenuJs,CharEncoding.ISO_8859_1);
      sActual = response.getContentAsString();
      assertEquals("La ressource n'a pas été correctement écrite dans l'objet HttpServletResponse",
            sExpected,sActual);

   }
   

   /**
    * Charge la ressource dans un tableau d'octets
    * @param nomRessource le nom de la resource
    * @return le contenu de la ressource dans un tableau d'octets
    * @throws URISyntaxException exception levée par {@link ResourceUtil#getResourceFullPath(Object, String)}
    * @throws IOException exception levée par {@link FileInputStream}
    */
   private byte[] lectureFichierRessource(String nomRessource) throws URISyntaxException, IOException {
      
      // Récupère le chemin complet de la ressource
      String cheminComplet = ResourceUtil.getResourceFullPath(this, nomRessource);
      
      // Lit la ressource dans un tableau d'octets
      File file = new File(cheminComplet);
      FileInputStream fileInputStream = new FileInputStream(file);
      try {
         return IOUtils.toByteArray(fileInputStream);  
      }
      finally {
         fileInputStream.close();
      }
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link ExtJsServlet#getTypeMime(String, ServletContext)}<br>
    * <br>
    * Cas de test : le nom de la ressource est un fichier javascript<br>
    * <br>
    * Résultat attendu : application/octet-stream<br>
    * 
    * @throws ServletException levée par {@link javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)}
    * @throws TypeMimeInconnuException levée par {@link ExtJsServlet#getTypeMime(String, ServletContext)}
    */
   @Test
   public void getTypeMime() throws ServletException, TypeMimeInconnuException
   {

      // Création des objets Mock
      MockServletConfig config = new MockServletConfig();
      
      // Création de l'objet Servlet
      ExtJsServlet servlet = new ExtJsServlet();
      servlet.init(config);
      
      // Configuration de la servlet
      ServletContext servletContext = servlet.getServletContext();
      
      // Appel de la méthode à tester
      String nomRessource = "/js/fr.urssaf.image.commons.js.extjs.js";
      String typeMime = servlet.getTypeMime(nomRessource, servletContext);

      // Vérifie le type MIME renvoyé
      String sExpected = "application/octet-stream";
      String sActual = typeMime;
      assertEquals("Le type MIME renvoyé est incorrect",sExpected,sActual);
      
   }
   
   
}
