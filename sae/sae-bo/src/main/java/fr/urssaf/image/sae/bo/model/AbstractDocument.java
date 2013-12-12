package fr.urssaf.image.sae.bo.model;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;

/**
 * Classe abstraite contenant les éléments communs des objets métiers et objets
 * conteneurs.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>content : Le contenu d’un document.</li>
 * <li>uuid :L'identifiant unique d’un document.</li>
 * <li>filePath :Le chemin absolu du fichier.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractDocument {
   // Les attributs
   private byte[] content;
   private UUID uuid;
   private String filePath;
   private String fileName;

   /**
    * @return Le contenu d’un document
    */
   @SuppressWarnings("PMD.MethodReturnsInternalArray")
   public final byte[] getContent() {
      // Pas de clone pour des raisons de performance.
      return getByteArrayCopy(content);
   }

   /**
    * @param fileContent
    *           : Le contenu d’un document.
    */
   @SuppressWarnings("PMD.ArrayIsStoredDirectly")
   public final void setContent(final byte[] fileContent) {
      // Pas de clone pour des raisons de performance.
      this.content = getByteArrayCopy(fileContent);

   }

   /**
    * @return L'identifiant unique d’un document
    */
   public final UUID getUuid() {
      return uuid;
   }

   /**
    * @param uuidDoc
    *           : L'identifiant unique d’un document.
    */
   public final void setUuid(final UUID uuidDoc) {
      this.uuid = uuidDoc;
   }

   /**
    * Construit un objet de type {@link AbstractDocument}.
    */
   public AbstractDocument() {
      // Ici on fait rien
   }

   /**
    * Construit un objet de type {@link AbstractDocument}.
    * 
    * @param fileContent
    *           : Le contenu d’un document.
    */
   @SuppressWarnings("PMD.ArrayIsStoredDirectly")
   public AbstractDocument(final byte[] fileContent) {
      // Pas de clone pour des raisons de performance.
      this.content = getByteArrayCopy(fileContent);

   }

   /**
    * Construit un objet de type {@link AbstractDocument}.
    * 
    * @param fileContent
    *           : Le contenu d’un document.
    * @param filePath
    *           : Le chemin absolu du fichier.
    */
   @SuppressWarnings("PMD.ArrayIsStoredDirectly")
   public AbstractDocument(final byte[] fileContent, final String filePath) {
      // Pas de clone pour des raisons de performance.
      this.content = getByteArrayCopy(fileContent);
      this.filePath = filePath;

   }

   /**
    * Construit un objet de type {@link AbstractDocument}.
    * 
    * @param fileContent
    *           : Le contenu d’un document.
    * @param filePath
    *           : Le chemin absolu du fichier.
    * @param fileName
    *           : Le nom du fichier.
    */
   @SuppressWarnings("PMD.ArrayIsStoredDirectly")
   public AbstractDocument(final String filePath, final String fileName,
         final byte[] fileContent) {
      // Pas de clone pour des raisons de performance.
      this.filePath = filePath;
      this.content = getByteArrayCopy(fileContent);
      this.fileName = fileName;

   }

   /**
    * Construit un objet de type {@link AbstractDocument}.
    * 
    * @param fileContent
    *           : Le contenu d’un document.
    * @param fileName
    *           : Le nom du fichier.
    */
   @SuppressWarnings("PMD.ArrayIsStoredDirectly")
   public AbstractDocument(String fileName, final byte[] fileContent) {
      // Pas de clone pour des raisons de performance.
      this.content = getByteArrayCopy(fileContent);
      this.fileName = fileName;
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
    * @return le nom du fichier
    */
   public final String getFileName() {
      return fileName;
   }

   /**
    * Initialise le nom du fichier
    * 
    * @param fileName
    *           : Le nom du fichier
    */
   public final void setFileName(String fileName) {
      this.fileName = fileName;
   }

   /**
    * Retourne un nouveau tableau de byte à partir de celui passé en paramètre
    * 
    * @param fileContent
    *           le contenu
    * @return le nouvel objet contenant les mêmes données que l'objet d'origine
    */
   private byte[] getByteArrayCopy(byte[] fileContent) {
      byte[] tContent = null;
      if (ArrayUtils.isNotEmpty(fileContent)) {
         tContent = Arrays.copyOf(fileContent, fileContent.length);
      }

      return tContent;
   }
}
