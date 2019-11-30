/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.exception;

/**
 * Exception levée lorsqu'une balise n'est pas retrouvée dans le sommaire.xml.
 */
public class ResultatEchecLectureXMLException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Constructeur
    * 
    * @param balise
    *           balise du sommaire
    */
   public ResultatEchecLectureXMLException(final String balise) {
      super("La balise XML " + balise
            + " n'a pas été trouvée dans le fichier sommaire.xml");
   }

}
