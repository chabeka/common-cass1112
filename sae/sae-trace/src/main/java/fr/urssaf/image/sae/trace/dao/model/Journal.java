package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.UUID;

/**
 * Objet représentant un journal
 * 
 * 
 */
public class Journal {

   /**
    * Date de création du journal
    */
   private Date date;

   /**
    * Identifiant unique du journal
    */
   private UUID identifiant;

   /**
    * Nom du fichier
    */
   private String nomFichier;

   /**
    * Constructeur
    */
   public Journal() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param date
    *           La date de création du journal
    * @param identifiant
    *           L'identifiant unique du journal
    * @param nomFichier
    *           Nom du fichier contenant le journal
    */
   public Journal(Date date, UUID identifiant, String nomFichier) {
      super();
      this.date = date;
      this.identifiant = identifiant;
      this.nomFichier = nomFichier;
   }

   /**
    * @return the date
    */
   public final Date getDate() {
      return date;
   }

   /**
    * @param date
    *           the date to set
    */
   public final void setDate(Date date) {
      this.date = date;
   }

   /**
    * @return the identifiant
    */
   public final UUID getIdentifiant() {
      return identifiant;
   }

   /**
    * @param identifiant
    *           the identifiant to set
    */
   public final void setIdentifiant(UUID identifiant) {
      this.identifiant = identifiant;
   }

   /**
    * @return the nomFichier
    */
   public final String getNomFichier() {
      return nomFichier;
   }

   /**
    * @param nomFichier
    *           the nomFichier to set
    */
   public final void setNomFichier(String nomFichier) {
      this.nomFichier = nomFichier;
   }

}
