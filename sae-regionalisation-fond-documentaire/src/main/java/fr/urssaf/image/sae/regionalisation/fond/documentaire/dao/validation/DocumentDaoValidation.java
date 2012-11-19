package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.validation;

import java.util.UUID;

import net.docubase.toolkit.model.document.Document;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * fr.urssaf.image.sae.regionalisation.fond.documentaire.daoDocumentDao.<br>
 * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class DocumentDaoValidation {

   private static final String CLASS = "fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocumentDao.";

   private static final String GET_METHOD = "execution(net.docubase.toolkit.model.document.Document "
         + CLASS + "getDocument(*))" + "&& args(uuid)";

   private static final String SAVE_METHOD = "execution(void " + CLASS
         + "updateDocument(*))" + "&& args(document)";

   /**
    * Validation de la méthode de getDocument <br>
    * 
    * @param uuid
    *           identifiant unique du document
    */
   @Before(GET_METHOD)
   public final void getDocument(UUID uuid) {

      if (uuid == null) {
         throw new IllegalArgumentException(
               "le paramètre uuid doit être renseigné");
      }
   }

   /**
    * Validation de la méthode de updateDocument <br>
    * 
    * @param document
    *           document à mettre à jour
    */
   @Before(SAVE_METHOD)
   public final void update(Document document) {

      if (document == null) {
         throw new IllegalArgumentException(
               "le paramètre document doit être renseigné");
      }
   }

}
