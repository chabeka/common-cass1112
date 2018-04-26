/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.utils;

import org.apache.commons.collections.Predicate;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanRNDTypeDocument;

/**
 * Classe permettant de supprimer des liste d'objet {@link BeanRNDTypeDocument}
 * ceux avec etat!=false
 * 
 */
public class PredicateEtatRNDTypeDoc implements Predicate {

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean evaluate(Object object) {

      boolean valid = false;

      if (object instanceof BeanRNDTypeDocument) {
         BeanRNDTypeDocument typeDocument = (BeanRNDTypeDocument) object;

         valid = typeDocument != null && !typeDocument.isEtat();
      }

      return valid;

   }

}
