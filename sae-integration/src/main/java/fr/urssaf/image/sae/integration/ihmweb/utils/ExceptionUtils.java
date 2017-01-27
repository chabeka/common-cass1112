package fr.urssaf.image.sae.integration.ihmweb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Méthodes utilitaires pour les Exception
 */
public class ExceptionUtils {

   
   /**
    * Représente une exception en chaîne de caractères, avec le message et la StackTrace
    * @param exception l'exception
    * @return l'exception représentée en String
    */
   public static String exceptionToString(Throwable exception) {
      
      String result = exception.getMessage();
      
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw); 
      exception.printStackTrace(pw);
      
      result += "\r\n" + sw.toString();
      
      return result;
      
   }
   
   
}
