package fr.urssaf.image.sae.documents.executable.aspect;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;
import fr.urssaf.image.sae.documents.executable.utils.messages.SaeDocumentsExecutableMessageHandler;

/**
 * Classe de validation des paramètres obligatoires.
 */
@Aspect
public class ParamFormatFichier {

   /********************************************************* SERVICE *********************************************************************************/
   private static final String FORMAT_SERVICE_IDENTIFIERFICHIER = "execution(* fr.urssaf.image.sae.documents.executable.service.FormatFichierService.identifierFichier(*,*,*,*))"
         + "&& args(idFormat,stream,document,metadonnees)";

   private static final String FORMAT_SERVICE_VALIDERFICHIER = "execution(* fr.urssaf.image.sae.documents.executable.service.FormatFichierService.validerFichier(*,*))"
         + "&& args(idFormat,stream)";

   /**
    * Vérification des paramètres de la méthode "identifierFichier" de la classe
    * FormatFichierService. Vérification du String idFormat donné en paramètre<br>
    * Vérification du InputStream stream donné en paramètre<br>
    * Vérification du Document document donné en paramètre<br>
    * 
    * @param idFormat
    *           identifiant du format
    * @param stream
    *           contenu du fichier
    * @param Document
    *           document DFCE
    */
   @Before(FORMAT_SERVICE_IDENTIFIERFICHIER)
   public final void validIdentifierFichierFromFormatFichierService(
         String idFormat, InputStream stream, Document document, List<String> metadonnees) {
      List<String> param = new ArrayList<String>();

      if (StringUtils.isBlank(idFormat)) {
         param.add(Constantes.IDFORMAT);
      }
      if (stream == null) {
         param.add(Constantes.STREAM);
      }
      if (document == null) {
         param.add(Constantes.DOCUMENT);
      }
      if (metadonnees == null) {
         param.add(Constantes.METADONNEES);
      }

      if (!param.isEmpty()) {
         throw new ParametreRuntimeException(
               SaeDocumentsExecutableMessageHandler.getMessage(
                     Constantes.PARAM_OBLIGATOIRE, param.toString()));
      } else {
         // verifie la liste des metadonnees
         List<String> metasNonAutorisees = MetadataUtils.checkMetadonneesNonAutorisees(metadonnees);
         if (!metasNonAutorisees.isEmpty()) {
            throw new ParametreRuntimeException(SaeDocumentsExecutableMessageHandler.getMessage(
                  Constantes.PARAM_METADONNEES_NON_AUTORISEES, metasNonAutorisees.toString()));
         }
      }
   }

   /**
    * Vérification des paramètres de la méthode "validerFichier" de la classe
    * FormatFichierService. Vérification du String idFormat donné en paramètre<br>
    * Vérification du InputStream stream donné en paramètre<br>
    * 
    * @param idFormat
    *           identifiant du format
    * @param stream
    *           contenu du fichier
    */
   @Before(FORMAT_SERVICE_VALIDERFICHIER)
   public final void validValiderFichierFromFormatFichierService(
         String idFormat, InputStream stream) {
      List<String> param = new ArrayList<String>();

      if (StringUtils.isBlank(idFormat)) {
         param.add(Constantes.IDFORMAT);
      }
      if (stream == null) {
         param.add(Constantes.STREAM);
      }

      if (!param.isEmpty()) {
         throw new ParametreRuntimeException(
               SaeDocumentsExecutableMessageHandler.getMessage(
                     Constantes.PARAM_OBLIGATOIRE, param.toString()));
      }
   }
}
