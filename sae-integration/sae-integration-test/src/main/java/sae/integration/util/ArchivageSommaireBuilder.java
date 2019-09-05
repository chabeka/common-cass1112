package sae.integration.util;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.MetadonneeType;

/**
 * Classe facilitant la création d'un sommaire.xml pour l'archivage de masse
 */
public class ArchivageSommaireBuilder {

   public String batchMode = "PARTIEL";

   public boolean restitutionUUID = true;

   private final List<String> filePaths = new ArrayList<>();

   private final List<String> fileTargetNames = new ArrayList<>();

   private final List<ListeMetadonneeType> metadatasList = new ArrayList<>();

   public void addDocument(final String filePath, final ListeMetadonneeType metas) {
      filePaths.add(filePath);
      final int fileIndex = filePaths.size();
      final String fileName = Paths.get(filePath).getFileName().toString();
      final String fileTargetName = fileName.replace(".", fileIndex + ".");
      fileTargetNames.add(fileTargetName);
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
      for (int i = 0; i < filePaths.size(); i++) {
         final String fileName = fileTargetNames.get(i);
         str.append(getXmlForDocument(fileName, metadatasList.get(i)));
      }
      str.append(fin);
      return str.toString();
   }

   /**
    * @param string
    * @param listeMetadonneeType
    * @return
    */
   private Object getXmlForDocument(final String filename, final ListeMetadonneeType listeMetadonneeType) {
      final String debut = "      <somres:document>\r\n" +
            "         <somres:objetNumerique>\r\n" +
            "            <somres:cheminEtNomDuFichier>" + StringEscapeUtils.escapeXml(filename) + "</somres:cheminEtNomDuFichier>\r\n" +
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

   public List<String> getFilePaths() {
      return filePaths;
   }

   public List<String> getFileTargetNames() {
      return fileTargetNames;
   }

}
