package fr.urssaf.image.sae.integration.ihmweb.modele;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Log des résultats d'un test
 */
public class ResultatTestLog {

   private final List<String> log = new ArrayList<String>();
   
   
   /**
    * Le log
    * 
    * @return Le log
    */
   public final List<String> getLog() {
      return this.log;
   }
   
   
   /**
    * Ajoute une ligne au log (sans retour charriots)
    * 
    * @param log la ligne
    */
   public final void appendLog(String log) {
      this.log.add(log);
   }
   
   
   /**
    * Ajoute une ligne au log, avec un retour charriot
    * 
    * @param log la ligne
    */
   public final void appendLogLn(String log) {
      this.log.add(log);
      this.log.add("\r\n");
   }
   
   
   /**
    * Ajoute un retour charriot
    */
   public final void appendLogNewLine() {
      this.log.add("\r\n");
   }
   
   
   /**
    * Ajoute un tableau de lignes (sans ajout de retour charriots entre les lignes)
    * 
    * @param lines les lignes
    */
   public final void appendArrayLn(String[] lines) {
      if (lines!=null) {
         for(String ligne: lines) {
            appendLogLn(ligne);
         }
      }
   }
   
   
   /**
    * Insère le timestamp en 1ère ligne
    */
   public final void insertTimestamp() {
      
      String pattern = "dd/MM/yyyy HH'h'mm ss's'";
      Date maintenant = new Date();
      DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.FRENCH);
      String dateFormatee = dateFormat.format(maintenant); 
      
      this.log.add(0, dateFormatee + "\r\n\r\n");
      
   }
   
   
   
}
