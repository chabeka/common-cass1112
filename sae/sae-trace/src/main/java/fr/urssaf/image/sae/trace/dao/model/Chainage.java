package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.UUID;

/**
 * Objet de chaînage des journaux
 * 
 * 
 */
public class Chainage {

   /**
    * Hash du journal
    */
   private String hash;

   /**
    * Algorithme du hash
    */
   private String algoHash;

   /**
    * Date de fin
    */
   private Date dateFin;

   /**
    * Identifiant unique du journal précédent
    */
   private UUID uuidPrecedentJournal;

   /**
    * Hash recalculé du journal
    */
   private String hashRecalcule;

   /**
    * Constructeur
    */
   public Chainage() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param hash
    *           Hash du journal
    * @param algoHash
    *           Algorithme du hash
    * @param dateFin
    *           Date de fin
    * @param uuidPrecedentJournal
    *           Identifiant unique du journal précédent
    * @param hashRecalcule
    *           Hash recalculé du journal
    */
   public Chainage(String hash, String algoHash, Date dateFin,
         UUID uuidPrecedentJournal, String hashRecalcule) {
      super();
      this.hash = hash;
      this.algoHash = algoHash;
      this.dateFin = new Date(dateFin.getTime());
      this.uuidPrecedentJournal = uuidPrecedentJournal;
      this.hashRecalcule = hashRecalcule;
   }

   /**
    * @return the hash
    */
   public final String getHash() {
      return hash;
   }

   /**
    * @param hash
    *           the hash to set
    */
   public final void setHash(String hash) {
      this.hash = hash;
   }

   /**
    * @return the algoHash
    */
   public final String getAlgoHash() {
      return algoHash;
   }

   /**
    * @param algoHash
    *           the algoHash to set
    */
   public final void setAlgoHash(String algoHash) {
      this.algoHash = algoHash;
   }

   /**
    * @return the dateFin
    */
   public final Date getDateFin() {
      return dateFin;
   }

   /**
    * @param dateFin
    *           the dateFin to set
    */
   public final void setDateFin(Date dateFin) {
      this.dateFin = new Date(dateFin.getTime());
   }

   /**
    * @return the uuidPrecedentJournal
    */
   public final UUID getUuidPrecedentJournal() {
      return uuidPrecedentJournal;
   }

   /**
    * @param uuidPrecedentJournal
    *           the uuidPrecedentJournal to set
    */
   public final void setUuidPrecedentJournal(UUID uuidPrecedentJournal) {
      this.uuidPrecedentJournal = uuidPrecedentJournal;
   }

   /**
    * @return the hashRecalcule
    */
   public final String getHashRecalcule() {
      return hashRecalcule;
   }

   /**
    * @param hashRecalcule
    *           the hashRecalcule to set
    */
   public final void setHashRecalcule(String hashRecalcule) {
      this.hashRecalcule = hashRecalcule;
   }

}
