package fr.urssaf.image.commons.spring.batch.model.flat;

public class Livre {

   private String titre;

   private String auteur;

   private Integer identifiant;

   /**
    * @return the titre
    */
   public String getTitre() {
      return titre;
   }

   /**
    * @param titre
    *           the titre to set
    */
   public void setTitre(String titre) {
      this.titre = titre;
   }

   /**
    * @return the auteur
    */
   public String getAuteur() {
      return auteur;
   }

   /**
    * @param auteur
    *           the auteur to set
    */
   public void setAuteur(String auteur) {
      this.auteur = auteur;
   }

   /**
    * @return the identifiant
    */
   public Integer getIdentifiant() {
      return identifiant;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setIdentifiant(Integer identifiant) {
      this.identifiant = identifiant;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Livre [auteur=").append(auteur).append(", identifiant=")
            .append(identifiant).append(", titre=").append(titre).append("]");
      return builder.toString();
   }

}
