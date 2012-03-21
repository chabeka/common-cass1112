package fr.urssaf.image.sae.ecde.service.validation;

import java.io.File;
import java.io.OutputStream;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.ecde.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;
/**
 * 
 * Classe de validation des arguments en entrée des implémentations du service
 * ResultatXmlService
 * 
 */
@Aspect
public class ResultatsXmlServiceValidation {

   private static final String ECDERESSERVICE = "fr.urssaf.image.sae.ecde.service.ResultatsXmlService.";
   
   // verification param entrée methode writeResultatsXml#1
   private static final String WRITERESXMLOUTPUT = "execution(void "+ECDERESSERVICE+"writeResultatsXml(*,*))" +
                                                 "&& args(resultatsXml, output)";
   
   // verification param entrée methode writeResultatsXml#2
   private static final String WRITERESXMLFILE = "execution(void "+ECDERESSERVICE+"writeResultatsXml(*,*))" +
                                                "&& args(resultatsXml, output)";
   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la methode writeResultatsXml de l'interface
    * service.ResultatsXmlService sont bien correct.
    * 
    * @param resultatsXml objet representant le contenu du resultats.xml
    * @param output flux dans lequel écrire resultats.xml
    * 
    */
   @Before(WRITERESXMLOUTPUT)
   public final void writeResultatsXml(ResultatsType resultatsXml, OutputStream output) {
         if (resultatsXml == null) { 
            throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessage("resultat.notExist", null));
         }
         if (output == null) { 
            throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessage("output.notExist", null));
         }
   }
   /**
    * Methode permettant l'ecriture du fichier resultats.xml
    * 
    * @param resultatsXml objet representant le contenu du resultats.xml
    * @param output fichier dans lequel écrire resultats.xml
    */
   @Before(WRITERESXMLFILE)
   public final void writeResultatsXml(ResultatsType resultatsXml, File output) {
         
         if (resultatsXml == null) { 
            throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessage("resultat.notExist", null));
         }
         if (output == null) { 
            throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessage("output.notExist", null));
         }
   }   
}