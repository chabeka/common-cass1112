package fr.urssaf.image.commons.controller.spring.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.urssaf.image.commons.controller.spring.exceptions.RessourceNonSpecifieeException;
import fr.urssaf.image.commons.controller.spring.exceptions.RessourceNonTrouveeException;
import fr.urssaf.image.commons.controller.spring.exceptions.TypeMimeInconnuException;


/**
 * Servlet permettant le téléchargement de la ressource Javascript fr.urssaf.image.commons.js.extjs.js
 * aux applications clientes.<br>
 * <br>
 * Voici le lien à inclure dans la vue de l'application cliente :
 * <pre>
 * &lt;script type="text/javascript" src="getResourceExtJs.do?name=/js/fr.urssaf.image.commons.js.extjs.js"&gt;image extjs&lt;/script&gt;
 * </pre>
 * Et voici ce qu'il faut ajouter au web.xml :<br>
 * <pre> 
   &lt;!-- Servlet pour extjs --&gt;
   &lt;servlet&gt;
      &lt;description&gt;&lt;/description&gt;
      &lt;display-name&gt;ExtJsServlet&lt;/display-name&gt;
      &lt;servlet-name&gt;ExtJsServlet&lt;/servlet-name&gt;
      &lt;servlet-class&gt;fr.urssaf.image.commons.controller.spring.servlet.ExtJsServlet&lt;/servlet-class&gt;
   &lt;/servlet&gt;
   &lt;servlet-mapping&gt;
      &lt;servlet-name&gt;ExtJsServlet&lt;/servlet-name&gt;
      &lt;url-pattern&gt;/getResourceExtJs.do&lt;/url-pattern&gt;
   &lt;/servlet-mapping&gt;
   </pre>
 */
public final class ExtJsServlet extends HttpServlet {
	
   
   private static final long serialVersionUID = -553585589874184028L;
   
   
   /**
    * Taille d'un buffer pour la lecture d'un fichier (en octets)
    */
   private static final int BUFFER_READ_SIZE = 1024;
   
	/**
	 * {@inheritDoc}
	 * 
	 * <br><br>
	 * <b>Lève également les exceptions suivantes, dérivées de IOException :</b><br>
	 * <ul>
	 * <li>{@link RessourceNonSpecifieeException} si le nom de la ressource n'est pas spécifiée
	 * (paramètre <code>name</code> manquant dans l'objet <code>HttpServletRequest</code>)</li>
	 * <li>{@link RessourceNonTrouveeException} si le nom de la ressource demandée ne correspond
	 * pas à une ressource existante (paramètre <code>name</code> de l'objet <code>HttpServletRequest</code>)</li>
	 * <li>{@link TypeMimeInconnuException} si le type MIME de la ressource demandée ne peut pas
	 * être déterminé</li>
	 * </ul>
	 */
   @Override
   public void doGet(
         HttpServletRequest request,
         HttpServletResponse response)
   throws IOException {
		
      // Récupère le nom de la ressource demandée
      String requestedFile = request.getParameter("name");
      if( requestedFile == null )
      {
         throw new RessourceNonSpecifieeException();
      }

      // Récupère la ressource sous la forme d'un InputStream
      ServletContext servletContext = getServletContext();
      String filename = requestedFile;
      InputStream inputStream = getClass().getResourceAsStream( filename );
      try {

         // Vérifie que la ressource a été trouvée
         if (inputStream==null)
         {
            throw new RessourceNonTrouveeException(null, filename);
         }

         // Ecrit la ressource dans la réponse HTTP
         printFileContentWithResponse( response, servletContext, inputStream, requestedFile ) ;

      }
      finally {
         if (inputStream!=null) {
            inputStream.close();
         }
      }
		
	}
	
   @SuppressWarnings("PMD.AssignmentInOperand")
	private void printFileContentWithResponse(
	      HttpServletResponse response,
	      ServletContext servletContext,
	      InputStream inputStream,
	      String requestedFile ) throws IOException
   {
       if( inputStream != null )
       {
          
          // Récupére le type MIME
          String mimeType = getTypeMime(requestedFile,servletContext); 
                   
          // Copie le contenu de la ressource dans la réponse HTTP
          OutputStream out = response.getOutputStream();
          int length = 0 ;
          try {
             byte[] buf = new byte[BUFFER_READ_SIZE];
             int count = 0;
             while ((count = inputStream.read(buf)) >= 0) {
                length += count ; 
                out.write(buf, 0, count);
             }
          }
          finally {
             out.close();
          }
          
          // Set content type
          response.setContentType(mimeType);
          
          // Set content size
          response.setContentLength(length);
          
       }
   }
   
   
   /**
    * Renvoie le type MIME de la ressource demandée
    * @param requestedFile la ressource demandée
    * @param servletContext l'objet ServletContext
    * @return le type MIME
    * @throws TypeMimeInconnuException si le type MIME n'est pas trouvé
    */
   protected String getTypeMime(
         String requestedFile,
         ServletContext servletContext) throws TypeMimeInconnuException
   {
      String mimeType = servletContext.getMimeType(requestedFile);
      if (mimeType == null) {
         throw new TypeMimeInconnuException(null,requestedFile);
         // NB : cette levée d'exception va être difficile à tester unitairement
         // car la méthode servletContext.getMimeType() renvoie TOUJOURS un type MIME, 
         // en tout cas pour le type javax.activation.MimetypesFileTypeMap
         // En effet, si la méthode ne retrouve pas le type MIME, elle renvoie 
         // "application/octet-stream"
      }
      return mimeType;
   }

}
