/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.exception.AdrnToRcndException;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanRNDTypeDocument;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.RNDTypeDocument;

/**
 * Conversion des beans WS en beans exploitables et inversement
 * 
 */
public class ConvertFactory {

   /**
    * Conversion d'une RNDTypeDocument en BeanTypeDocument
    * 
    * @param rndDocument
    */
   public static final BeanRNDTypeDocument WSToBeanRNDTypeDocument(
         RNDTypeDocument rndDocument, String version) {

      BeanRNDTypeDocument document = null;

      if (rndDocument != null) {

         document = new BeanRNDTypeDocument();
         document.setCodeRND(rndDocument.get_reference());

         // TODO Pourquoi refFonction n'est pas toujours renseigné ?
         String value = rndDocument.get_refFonction();
         String[] tabValues = rndDocument.get_reference().split("\\.");
         if (StringUtils.isBlank(value)) {
            value = tabValues[0];
         }
         document.setCodeFonction(value);

         // TODO Pourquoi refActivite n'est pas toujours renseigné ?
         value = rndDocument.get_refActivite();
         if (StringUtils.isBlank(value)) {
            value = tabValues[1];
         }
         document.setCodeActivite(value);

         document.setCodeLibelle(rndDocument.get_label());

         // TODO Calculer la durée de conservation
         document.setDureeConservation("1825");

         document.setVersionRND(version);

         document.setEtat(rndDocument.is_etat());
      }

      return document;
   }

   public static final List<BeanRNDTypeDocument> WSToListRNDTypeDocument(
         RNDTypeDocument[] tabRndDocuments, String version)
         throws AdrnToRcndException {

      List<BeanRNDTypeDocument> documents = null;
      if (StringUtils.isBlank(version)) {
         throw new AdrnToRcndException("Impossible de convertir les données. "
               + "Le numéro de version est null");
      }

      if (tabRndDocuments != null) {
         documents = new ArrayList<BeanRNDTypeDocument>();

         for (RNDTypeDocument rndDocument : tabRndDocuments) {
            documents.add(WSToBeanRNDTypeDocument(rndDocument, version));
         }

      }

      return documents;

   }

}
