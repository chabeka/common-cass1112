package fr.urssaf.image.sae.ecde.modele.resultats;

import java.util.List;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentVirtuelType;


/**
 * Objet représentant le fichier sommaire.xml 
 * <br>
 * Ce fichier se situe dans le répertoire ecdeDirectory.
 *
 */
@SuppressWarnings("PMD.LongVariable")
public class Resultats {

   private String batchMode;
   private String ecdeDirectory;
   private int initDocCount;
   private int initVDocsCount;
   private int integDocsCount;
   private int integVDocsCount;
   private int nonIntegDocsCount;
   private int nonIntegVDocsCount;
   
   private List<UntypedDocumentOnError> nonIntegratedDocuments;
   private List<DocumentVirtuelType> nonIntegratedVDocuments;
   
   
   
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
    * 
    * @return int nb initial de document
    */
   public final int getInitialDocumentsCount() {
      return initDocCount;
   }
   /**
    * @param initialDocumentsCount nb initial a de document
    */
   public final void setInitialDocumentsCount(int initialDocumentsCount) {
      this.initDocCount = initialDocumentsCount;
   }
   /**
    * 
    * @return nb initial de document virtuel
    */
   public final int getInitialVirtualDocumentsCount() {
      return initVDocsCount;
   }
   /**
    * 
    * @param initialVirtualDocumentsCount nb initial de document virtuel
    */
   public final void setInitialVirtualDocumentsCount(int initialVirtualDocumentsCount) {
      this.initVDocsCount = initialVirtualDocumentsCount;
   }
   /**
    * 
    * @return nb de document a intégré
    */
   public final int getIntegratedDocumentsCount() {
      return integDocsCount;
   }
   /**
    * 
    * @param integDocsCount nb de document a intégré
    */
   public final void setIntegratedDocumentsCount(int integDocsCount) {
      this.integDocsCount = integDocsCount;
   }
   /**
    * 
    * @return nb de document virtuel a integrer
    */
   public final int getIntegratedVirtualDocumentsCount() {
      return integVDocsCount;
   }
   /**
    * 
    * @param integVDocsCount nb de document virtuel a integrer
    */
   public final void setIntegratedVirtualDocumentsCount(
         int integVDocsCount) {
      this.integVDocsCount = integVDocsCount;
   }
   /**
    * 
    * @return nb de non document a integrer
    */
   public final int getNonIntegratedDocumentsCount() {
      return nonIntegDocsCount;
   }
   /**
    * 
    * @param nonIntegDocsCount nb de non document a integrer
    */
   public final void setNonIntegratedDocumentsCount(int nonIntegDocsCount) {
      this.nonIntegDocsCount = nonIntegDocsCount;
   }
   /**
    * 
    * @return nb de non document virtuel a integrer
    */
   public final int getNonIntegratedVirtualDocumentsCount() {
      return nonIntegVDocsCount;
   }
   /**
    * 
    * @param nonIntegVDocsCount nb de non document virtuel a integrer
    */
   public final void setNonIntegratedVirtualDocumentsCount(
         int nonIntegVDocsCount) {
      this.nonIntegVDocsCount = nonIntegVDocsCount;
   }
   /**
    * 
    * @return list des untypeddocument en error
    */
   public final List<UntypedDocumentOnError> getNonIntegratedDocuments() {
      return nonIntegratedDocuments;
   }
   /**
    * 
    * @param nonIntegratedDocuments list des untypeddocument en error
    */
   public final void setNonIntegratedDocuments(
         List<UntypedDocumentOnError> nonIntegratedDocuments) {
      this.nonIntegratedDocuments = nonIntegratedDocuments;
   }
   /**
    * @return the nonIntegratedVDocuments
    */
   public final List<DocumentVirtuelType> getNonIntegratedVDocuments() {
      return nonIntegratedVDocuments;
   }
   /**
    * @param nonIntegratedVDocuments the nonIntegratedVDocuments to set
    */
   public final void setNonIntegratedVDocuments(List<DocumentVirtuelType> nonIntegratedVDocuments) {
      this.nonIntegratedVDocuments = nonIntegratedVDocuments;
   }
   
   
}
