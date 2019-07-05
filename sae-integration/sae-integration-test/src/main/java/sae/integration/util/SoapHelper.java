package sae.integration.util;

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
      final DetailEntry detailEntry = (DetailEntry) e.getFault().getDetail().getDetailEntries().next();
      final Node detailNode = detailEntry.getFirstChild();
      final String detailAsString = detailNode.getTextContent();
      return detailAsString;
   }
}
