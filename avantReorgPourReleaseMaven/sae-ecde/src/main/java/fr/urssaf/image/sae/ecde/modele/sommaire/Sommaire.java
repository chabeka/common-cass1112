package fr.urssaf.image.sae.ecde.modele.sommaire;

import java.util.Date;
import java.util.List;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;

/**
 * Objet représentant le fichier sommaire.xml 
 * <br>
 * Ce fichier se situe dans le répertoire ecdeDirectory.
 *
 */
@SuppressWarnings("PMD")
public class Sommaire {

   private String batchMode;
   private Date dateCreation;
   private String description;
   private String ecdeDirectory;
   private List<UntypedDocument> documents;
  
   /**
    * Gets the value of the batchMode property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public final String getBatchMode() {
      return batchMode;
   }
   /**
    * Sets the value of the batchMode property.
    * 
    * @param batchMode
    *     allowed object is
    *     {@link String }
    *     
    */
   public final void setBatchMode(String batchMode) {
      this.batchMode = batchMode;
   }
   
   /**
    * Gets the value of the dateCreation property.
    * 
    * @return
    *     possible object is
    *     {@link Date }
    *     
    */
   public final Date getDateCreation() {
      return dateCreation;
   }
   /**
    * Sets the value of the dateCreation property.
    * 
    * @param dateCreation
    *     allowed object is
    *     {@link Date }
    *     
    */
   public final void setDateCreation(Date dateCreation) {
      this.dateCreation = dateCreation;
   }
   
   /**
    * Gets the value of the description property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public final String getDescription() {
      return description;
   }
   /**
    * Sets the value of the description property.
    * 
    * @param description
    *     allowed object is
    *     {@link String }
    *     
    */
   public final void setDescription(String description) {
      this.description = description;
   }
   
   /**
    * Gets the value of the ecdeDirectory property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public final String getEcdeDirectory() {
      return ecdeDirectory;
   }
   /**
    * Sets the value of the ecdeDirectory property.
    * 
    * @param ecdeDirectory
    *     allowed object is
    *     {@link String }
    *     
    */
   public final void setEcdeDirectory(String ecdeDirectory) {
      this.ecdeDirectory = ecdeDirectory;
   }
   
   /**
    * Gets the value of the documents property.
    * @return list de untypeddocument
    */
   public final List<UntypedDocument> getDocuments() {
      return documents;
   }
   /**
    * Sets the value of the documents property.
    * @param docs list de untypedDocument
    */
   public final void setDocuments(List<UntypedDocument> docs) {
      this.documents = docs;
   }   
   
}