package sae.integration.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.MetadonneeType;

/**
 * Classe facilitant la création d'un sommaire.xml pour le transfert de masse
 */
public class TransfertSommaireBuilder {

   private final List<String> uuidList = new ArrayList<>();

   private final List<String> actionList = new ArrayList<>();

   private final List<ListeMetadonneeType> metadatasList = new ArrayList<>();

   public void addDeletion(final String uuid) {
      uuidList.add(uuid);
      actionList.add("SUPPRESSION");
      metadatasList.add(null);
   }

   public void addTransfert(final String uuid, final ListeMetadonneeType metas) {
      uuidList.add(uuid);
      actionList.add("TRANSFERT");
      metadatasList.add(metas);
   }

   public String build() {
      final String debut = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<som:sommaire xmlns:som=\"http://www.cirtil.fr/sae/sommaireXml\"\r\n" + 
            "   xmlns:somres=\"http://www.cirtil.fr/sae/commun_sommaire_et_resultat\"\r\n" + 
            "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
            "\r\n" + 
            "   <som:batchMode>PARTIEL</som:batchMode>\r\n" +
            "\r\n" + 
            "   <som:documentsMultiAction>\r\n";
      final String fin = "   </som:documentsMultiAction>\r\n" +
            "   <som:documentsVirtuels />\r\n" + 
            "</som:sommaire>";

      final StringBuilder str = new StringBuilder();
      str.append(debut);
      for (int i = 0; i < uuidList.size(); i++) {
         str.append(getXmlForAction(uuidList.get(i), actionList.get(i), metadatasList.get(i)));
      }
      str.append(fin);
      return str.toString();
   }

   private Object getXmlForAction(final String uuid, final String action, final ListeMetadonneeType listeMetadonneeType) {
      final String debut = "      <somres:documentMultiAction>\r\n" +
            "         <somres:objetNumerique>\r\n" +
            "            <somres:UUID>" + StringEscapeUtils.escapeXml(uuid) + "</somres:UUID>\r\n" +
            "         </somres:objetNumerique>\r\n" +
            "         <somres:metadonnees>\r\n";
      final String fin = "         </somres:metadonnees>\r\n" +
                  "         <somres:typeAction>" + action + "</somres:typeAction>\r\n" +
                  "      </somres:documentMultiAction>\r\n";
      final StringBuilder str = new StringBuilder();
      str.append(debut);
      if (listeMetadonneeType != null) {
         final List<MetadonneeType> metas = listeMetadonneeType.getMetadonnee();
         for (int i = 0; i < metas.size(); i++) {
            final MetadonneeType meta = metas.get(i);
            str.append("            <somres:metadonnee>\r\n");
            str.append("               <somres:code>" + StringEscapeUtils.escapeXml(meta.getCode()) + "</somres:code>\r\n");
            str.append("               <somres:valeur>" + StringEscapeUtils.escapeXml(meta.getValeur()) + "</somres:valeur>\r\n");
            str.append("            </somres:metadonnee>\r\n");
         }
      }
      str.append(fin);
      return str.toString();
   }

}
