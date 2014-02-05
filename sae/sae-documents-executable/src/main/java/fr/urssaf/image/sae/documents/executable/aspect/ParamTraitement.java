package fr.urssaf.image.sae.documents.executable.aspect;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;
import fr.urssaf.image.sae.documents.executable.utils.messages.SaeDocumentsExecutableMessageHandler;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
public class ParamTraitement {

   /********************************************************* SERVICE *********************************************************************************/
   private static final String TRAITEMENT_SERVICE_IDENTIFIERVALIDERFICHIER = "execution(* fr.urssaf.image.sae.documents.executable.service.TraitementService.identifierValiderFichiers(*))"
      + "&& args(parametres)";

   /**
    * Vérification des paramètres de la méthode "identifierValiderFichiers" de la classe
    * TraitementService. Vérification du FormatValidationParametres parametres donné en paramètre<br>
    * @param parametres
    *           parametres
    */
   @Before(TRAITEMENT_SERVICE_IDENTIFIERVALIDERFICHIER)
   public final void validIdentifierValiderFichiers(FormatValidationParametres parametres) {
      List<String> param = new ArrayList<String>();

      if (parametres == null) {
         param.add(Constantes.PARAMETRES);
      }
      
      if (!param.isEmpty()) {
         throw new ParametreRuntimeException(SaeDocumentsExecutableMessageHandler.getMessage(
               Constantes.PARAM_OBLIGATOIRE, param.toString()));
      } else {
         // verifie la liste des metadonnees
         List<String> metasNonAutorisees = MetadataUtils.checkMetadonneesNonAutorisees(parametres.getMetadonnees());
         if (!metasNonAutorisees.isEmpty()) {
            throw new ParametreRuntimeException(SaeDocumentsExecutableMessageHandler.getMessage(
                  Constantes.PARAM_METADONNEES_NON_AUTORISEES, metasNonAutorisees.toString()));
         }
      }
   }
}
