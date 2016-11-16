/**
 * 
 */
package fr.urssaf.image.sae.storage.model.storagedocument;

import java.util.UUID;

import org.apache.commons.io.IOUtils;

/**
 * Objet représentant le fichier de référence une fois inséré
 * 
 */
public class StorageReferenceFile {

   private String digest;

   private String digestAlgorithm;

   private String extension;

   private String name;

   private Long size;

   private UUID uuid;
   
   private StorageContentRepository contentRepository;

   /**
    * @return le hash du fichier
    */
   public final String getDigest() {
      return digest;
   }

   /**
    * @param digest
    *           le hash du fichier
    */
   public final void setDigest(String digest) {
      this.digest = digest;
   }

   /**
    * @return le type de hashage
    */
   public final String getDigestAlgorithm() {
      return digestAlgorithm;
   }

   /**
    * @param digestAlgorithm
    *           le type de hashage
    */
   public final void setDigestAlgorithm(String digestAlgorithm) {
      this.digestAlgorithm = digestAlgorithm;
   }

   /**
    * @return l'extension du fichier
    */
   public final String getExtension() {
      return extension;
   }

   /**
    * @param extension
    *           l'extension du fichier
    */
   public final void setExtension(String extension) {
      this.extension = extension;
   }

   /**
    * @return le nom du fichier
    */
   public final String getName() {
      return name;
   }

   /**
    * @param name
    *           le nom du fichier
    */
   public final void setName(String name) {
      this.name = name;
   }

   /**
    * @return la taille du fichier
    */
   public final Long getSize() {
      return size;
   }

   /**
    * @param size
    *           la taille du fichier
    */
   public final void setSize(Long size) {
      this.size = size;
   }

   /**
    * @return l'identifiant unique du document
    */
   public final UUID getUuid() {
      return uuid;
   }

   /**
    * @param uuid
    *           l'identifiant unique du document
    */
   public final void setUuid(UUID uuid) {
      this.uuid = uuid;
   }
   
   /**
    * Getter sur le type de stockage.
    * @return StorageContentRepository
    */
   public final StorageContentRepository getContentRepository() {
      return contentRepository;
   }

   /**
    * Setter sur le type de stockage.
    * @param contentRepository type de stockage
    */
   public final void setContentRepository(
         final StorageContentRepository contentRepository) {
      this.contentRepository = contentRepository;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {

      StringBuffer buffer = new StringBuffer();
      buffer.append("uuid : ");
      buffer.append(uuid);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("hash : ");
      buffer.append(digest);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("type de hash : ");
      buffer.append(digestAlgorithm);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("nom de fichier : ");
      buffer.append(name);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("extension : ");
      buffer.append(extension);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("taille du fichier : ");
      buffer.append(size);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("type de stockage : ");
      buffer.append(contentRepository);

      return buffer.toString();
   }

}
