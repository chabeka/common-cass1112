package sae.integration.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.MetadonneeType;

/**
 * Classe facilitant la création d'un sommaire.xml pour la modification de masse
 */
public class ModificationSommaireBuilder {

   public boolean restitutionUUID = true;

   public String batchMode = "PARTIEL";

   private final List<String> uuidList = new ArrayList<>();

   private final List<ListeMetadonneeType> metadatasList = new ArrayList<>();

   public void addDocument(final String uuid, final ListeMetadonneeType metas) {
      uuidList.add(uuid);
      metadatasList.add(metas);
   }

   public String build() {
      final String debut = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<som:sommaire xmlns:som=\"http://www.cirtil.fr/sae/sommaireXml\"\r\n" + 
            "   xmlns:somres=\"http://www.cirtil.fr/sae/commun_sommaire_et_resultat\"\r\n" + 
            "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
            "\r\n" + 
            "   <som:batchMode>" + batchMode + "</som:batchMode>\r\n" +
            "\r\n" + 
            "   <som:restitutionUuids>" + restitutionUUID +"</som:restitutionUuids>\r\n" + 
            "\r\n" + 
            "   <som:documents>\r\n";
      final String fin = "   </som:documents>\r\n" + 
            "   <som:documentsVirtuels />\r\n" + 
            "</som:sommaire>";

      final StringBuilder str = new StringBuilder();
      str.append(debut);
      for (int i = 0; i < uuidList.size(); i++) {
         final String uuid = uuidList.get(i);
         str.append(getXmlForDocument(uuid, metadatasList.get(i)));
      }
      str.append(fin);
      return str.toString();
   }

   /**
    * @param string
    * @param listeMetadonneeType
    * @return
    */
   private Object getXmlForDocument(final String uuid, final ListeMetadonneeType listeMetadonneeType) {
      final String debut = "      <somres:document>\r\n" +
            "         <somres:objetNumerique>\r\n" +
            "            <somres:UUID>" + StringEscapeUtils.escapeXml(uuid) + "</somres:UUID>\r\n" +
            "         </somres:objetNumerique>\r\n" +
            "         <somres:metadonnees>\r\n";
      final String fin = "         </somres:metadonnees>\r\n" +
            "      </somres:document>\r\n";
      final StringBuilder str = new StringBuilder();
      str.append(debut);
      final List<MetadonneeType> metas = listeMetadonneeType.getMetadonnee();
      for (int i = 0; i < metas.size(); i++) {
         final MetadonneeType meta = metas.get(i);
         str.append("            <somres:metadonnee>\r\n");
         str.append("               <somres:code>" + StringEscapeUtils.escapeXml(meta.getCode()) + "</somres:code>\r\n");
         str.append("               <somres:valeur>" + StringEscapeUtils.escapeXml(meta.getValeur()) + "</somres:valeur>\r\n");
         str.append("            </somres:metadonnee>\r\n");
      }
      str.append(fin);
      return str.toString();
   }

}
