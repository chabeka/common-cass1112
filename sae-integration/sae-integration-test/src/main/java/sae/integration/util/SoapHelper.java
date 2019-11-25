package sae.integration.util;

import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Node;

import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.MetadonneeType;

/**
 * Classe utilitaire facilitant la manipulation des objets SOAP du SAE
 */
public class SoapHelper {

   private SoapHelper() {
      // Classe statique
   }

   public static String getMetaValue(final ListeMetadonneeType metaList, final String metaCode) {
      for (final MetadonneeType meta : metaList.getMetadonnee()) {
         if (metaCode.equals(meta.getCode())) {
            return meta.getValeur();
         }
      }
      return null;
   }

   public static String getSoapFaultDetail(final SOAPFaultException e) {
      final Detail detail = e.getFault().getDetail();
      if (detail == null) {
         return e.getMessage();
      }
      final DetailEntry detailEntry = (DetailEntry) detail.getDetailEntries().next();
      final Node detailNode = detailEntry.getFirstChild();
      final String detailAsString = detailNode.getTextContent();
      return detailAsString;
   }

   /**
    * Renvoie une chaine de caractères permettant de dumper la valeur des métadonnées. Renvoie par exemple :
    * Siret=48815777776762,DateArchivage=2016-10-11
    * 
    * @param metaList
    *           Les métadonnées à dumper
    * @return
    *         Les métadonnées sérialisées sous forme lisible
    */
   public static String getMetasAsString(final ListeMetadonneeType metaList) {
      String result = "";
      for (final MetadonneeType meta : metaList.getMetadonnee()) {
         if (!result.equals("")) {
            result += ",";
         }
         result += meta.getCode() + "=" + meta.getValeur();
      }
      return result;
   }

}
