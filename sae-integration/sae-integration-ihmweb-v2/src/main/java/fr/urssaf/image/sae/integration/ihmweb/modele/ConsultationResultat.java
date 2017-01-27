package fr.urssaf.image.sae.integration.ihmweb.modele;

import javax.activation.DataHandler;

import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ListeMetadonneeType;

/**
 * Résultat d'un appel au service de consultation (ou consultationMTOM)
 */
public class ConsultationResultat {

   private ListeMetadonneeType metadonnees;
   private DataHandler contenu;

   /**
    * Constructeur
    */
   public ConsultationResultat() {

   }

   /**
    * Constructeur
    * 
    * @param metadonnees
    *           la liste des métadonnées
    * @param contenu
    *           le contenu du document
    */
   public ConsultationResultat(ListeMetadonneeType metadonnees,
         DataHandler contenu) {
      this.metadonnees = metadonnees;
      this.contenu = contenu;
   }

   /**
    * Les métadonnées
    * 
    * @return Les métadonnées
    */
   public final ListeMetadonneeType getMetadonnees() {
      return metadonnees;
   }

   /**
    * Les métadonnées
    * 
    * @param metadonnees
    *           Les métadonnées
    */
   public final void setMetadonnees(ListeMetadonneeType metadonnees) {
      this.metadonnees = metadonnees;
   }

   /**
    * Les métadonnées
    * 
    * @return Les métadonnées
    */
   public final DataHandler getContenu() {
      return contenu;
   }

   /**
    * Les métadonnées
    * 
    * @param contenu
    *           Les métadonnées
    */
   public final void setContenu(DataHandler contenu) {
      this.contenu = contenu;
   }

}
