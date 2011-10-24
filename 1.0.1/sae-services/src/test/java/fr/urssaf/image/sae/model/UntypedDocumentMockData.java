package fr.urssaf.image.sae.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe qui représente un document mock test
 * 
 *@@author Rhofir
 */
@XStreamAlias("document")
public class UntypedDocumentMockData {
   @XStreamAlias("metadatas")
   private List<SAEMockMetadata> metadatas;
   // Les attributs
   private String filePath;
   private String docType;

   /**
    * @return La liste des métadonnées métiers.
    */
   public final List<SAEMockMetadata> getMetadatas() {
      return metadatas;
   }

   /**
    * @param saeMetadatas
    *           : La liste des métadonnées métiers.
    */
   public final void setMetadatas(final List<SAEMockMetadata> saeMetadatas) {
      this.metadatas = saeMetadatas;
   }

   /**
    * Retourne le chemin du fichier
    * 
    * @return Le chemin du fichier
    */
   public final String getFilePath() {
      return filePath;
   }

   /**
    * Initialise le chemin du fichier
    * 
    * @param filePath
    *           : Le chemin du document
    */
   public final void setFilePath(final String filePath) {
      this.filePath = filePath;
   }

   /**
    * @return Type d'objet pour le test
    *         <ul>
    *         <li>untypedDocument</li><br>
    *         <li>saeDocument</li>
    *         </ul>
    */
   public final String getDocType() {
      return docType;
   }

   /**
    * @param docType
    *           Type d'objet pour le test
    *           <ul>
    *           <li>untypedDocument</li><br>
    *           <li>saeDocument</li>
    *           </ul>
    */
   public final void setDocType(String docType) {
      this.docType = docType;
   }

}
