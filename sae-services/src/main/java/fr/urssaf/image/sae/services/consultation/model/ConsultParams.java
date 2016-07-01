/**
 * 
 */
package fr.urssaf.image.sae.services.consultation.model;

import java.util.List;
import java.util.UUID;

/**
 * Objet regroupant les paramètres nécessaires à la consultation. Dans cette
 * spécification, les paramètres définis sont l'identifiant unique du document
 * et la liste des métadonnées saisies
 * 
 */
public class ConsultParams {

   /**
    * Identifiant du document à consulter
    */
   private UUID idArchive;

   /**
    * Liste de code long de métadonnées désirées à la consultation
    */
   private List<String> metadonnees;

   /**
    * Numéro de la page à partir de laquelle on réalise le découpage du
    * document.
    */
   private Integer numeroPage;

   /**
    * Nombre de pages à extraire du document. Ce nombre peut être négatif.
    */
   private Integer nombrePages;

   /**
    * Constructeur
    * 
    * @param idArchive
    *           Identifiant du document à consulter
    */
   public ConsultParams(UUID idArchive) {
      super();
      this.idArchive = idArchive;
   }

   /**
    * Constructeur
    * 
    * @param idArchive
    *           Identifiant du document à consulter
    * @param metadonnees
    *           Liste de code long de métadonnées désirées à la consultation
    */
   public ConsultParams(UUID idArchive, List<String> metadonnees) {
      this.idArchive = idArchive;
      this.metadonnees = metadonnees;
   }

   /**
    * Constructeur
    * 
    * @param idArchive
    *           Identifiant du document à consulter
    * @param metadonnees
    *           Liste de code long de métadonnées désirées à la consultation
    * @param numeroPage
    *           Numéro de la page à partir de laquelle on réalise le découpage
    *           du document.
    * @param nombrePages
    *           Nombre de pages à extraire du document. Ce nombre peut être
    *           négatif.
    */
   public ConsultParams(UUID idArchive, List<String> metadonnees,
         Integer numeroPage, Integer nombrePages) {
      this.idArchive = idArchive;
      this.metadonnees = metadonnees;
      this.numeroPage = numeroPage;
      this.nombrePages = nombrePages;
   }

   /**
    * @return the idArchive Identifiant du document à consulter
    */
   public final UUID getIdArchive() {
      return idArchive;
   }

   /**
    * @param idArchive
    *           Identifiant du document à consulter
    */
   public final void setIdArchive(UUID idArchive) {
      this.idArchive = idArchive;
   }

   /**
    * @return Liste de code long de métadonnées désirées à la consultation
    */
   public final List<String> getMetadonnees() {
      return metadonnees;
   }

   /**
    * @param metadonnees
    *           Liste de code long de métadonnées désirées à la consultation
    */
   public final void setMetadonnees(List<String> metadonnees) {
      this.metadonnees = metadonnees;
   }

   /**
    * @return Numéro de la page à partir de laquelle on réalise le découpage du
    *         document
    */
   public final Integer getNumeroPage() {
      return numeroPage;
   }

   /**
    * @param numeroPage
    *           Numéro de la page à partir de laquelle on réalise le découpage
    *           du document
    */
   public final void setNumeroPage(Integer numeroPage) {
      this.numeroPage = numeroPage;
   }

   /**
    * @return Nombre de pages à extraire du document. Ce nombre peut être
    *         négatif.
    */
   public final Integer getNombrePages() {
      return nombrePages;
   }

   /**
    * @param nombrePages
    *           Nombre de pages à extraire du document. Ce nombre peut être
    *           négatif.
    */
   public final void setNombrePages(Integer nombrePages) {
      this.nombrePages = nombrePages;
   }

}
