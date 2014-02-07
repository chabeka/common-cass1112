package fr.urssaf.image.sae.documents.executable.aspect;

import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.documents.executable.utils.messages.SaeDocumentsExecutableMessageHandler;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
public class ParamDfce {

   /********************************************************* SERVICE *********************************************************************************/
   private static final String DFCE_SERVICE_EXECUTERREQUETE = "execution(* fr.urssaf.image.sae.documents.executable.service.DfceService.executerRequete(*))"
         + "&& args(requeteLucene)";

   private static final String DFCE_SERVICE_RECUPERERCONTENU = "execution(* fr.urssaf.image.sae.documents.executable.service.DfceService.recupererContenu(*))"
         + "&& args(document)";

   /**
    * Vérification des paramètres de la méthode "executerRequete" de la classe
    * DfceService. Vérification du String requeteLucene donné en paramètre<br>
    * 
    * @param requeteLucene
    *           requête lucène
    */
   @Before(DFCE_SERVICE_EXECUTERREQUETE)
   public final void validExecuterRequeteFromDfceService(String requeteLucene) {
      List<String> param = new ArrayList<String>();

      if (StringUtils.isBlank(requeteLucene)) {
         param.add(Constantes.REQUETELUCENE);
      }

      if (!param.isEmpty()) {
         throw new ParametreRuntimeException(
               SaeDocumentsExecutableMessageHandler.getMessage(
                     Constantes.PARAM_OBLIGATOIRE, param.toString()));
      }
   }

   /**
    * Vérification des paramètres de la méthode "recupererContenu" de la classe
    * DfceService. Vérification du Document document donné en paramètre<br>
    * 
    * @param document
    *           Document Dfce
    */
   @Before(DFCE_SERVICE_RECUPERERCONTENU)
   public final void validRecupererContenuFromDfceService(Document document) {
      List<String> param = new ArrayList<String>();

      if (document == null) {
         param.add(Constantes.DOCUMENT);
      }

      if (!param.isEmpty()) {
         throw new ParametreRuntimeException(
               SaeDocumentsExecutableMessageHandler.getMessage(
                     Constantes.PARAM_OBLIGATOIRE, param.toString()));
      }
   }
}
