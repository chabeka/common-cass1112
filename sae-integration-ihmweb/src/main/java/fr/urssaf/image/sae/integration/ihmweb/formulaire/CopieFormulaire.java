package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

public class CopieFormulaire extends GenericForm{

   /**
    * Resultat
    */
   private ResultatTest resultats = new ResultatTest();

   /**
    * Identifiant GED
    */
   private String idGed;

   /**
    * Liste des métadonnées
    */
   private MetadonneeValeurList listeMetadonnees = new MetadonneeValeurList();
   
   /**
    * Mode de consultation
    */
   private ModeConsultationEnum modeConsult = ModeConsultationEnum.AncienServiceSansMtom;

   /**
    * Constructeur.
    * 
    * @param parent
    *           formulaire parent.
    */
   public CopieFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   /**
    * Constructeur.
    * 
    */
   public CopieFormulaire() {
      super();
   }

   /**
    * Les résultats de l'appel à l'opération.
    * 
    * @return Les résultats de l'appel à l'opération
    */
   public final ResultatTest getResultats() {
      return this.resultats;
   }

   /**
    * Les résultats de l'appel à l'opération.
    * 
    * @param resultats
    *           Les résultats de l'appel à l'opération
    */
   public final void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }

   /**
    * Le mode d'utilisation de la consultation
    * 
    * @return Le mode d'utilisation de la consultation
    */
   public final ModeConsultationEnum getModeConsult() {
      return modeConsult;
   }

   /**
    * Le mode d'utilisation de la consultation
    * 
    * @param modeConsult
    *           Le mode d'utilisation de la consultation
    */
   public final void setModeConsult(ModeConsultationEnum modeConsult) {
      this.modeConsult = modeConsult;
   }

   /**
    * L'identifiant d'archivage de l'archive que l'on souhaite consulter
    * 
    * @return L'identifiant d'archivage de l'archive que l'on souhaite consulter
    */
   public String getIdGed() {
      return idGed;
   }

   /**
    * L'identifiant d'archivage de l'archive que l'on souhaite consulter
    * 
    * @param idArchivage
    *           L'identifiant d'archivage de l'archive que l'on souhaite
    *           consulter
    */
   public void setIdGed(String idGed) {
      this.idGed = idGed;
   }

   /**
    * La liste des métadonnées que l'on souhaite dans la consultation
    * 
    * @return La liste des métadonnées que l'on souhaite dans la consultation
    * 
    */
   public MetadonneeValeurList getListeMetadonnees() {
      return listeMetadonnees;
   }

   /**
    * La liste des métadonnées que l'on souhaite dans la consultation
    * 
    * @param codeMetadonnees
    *           La liste des métadonnées que l'on souhaite dans la consultation
    */
   public void setListeMetadonnees(MetadonneeValeurList listeMetadonnees) {
      this.listeMetadonnees = listeMetadonnees;
   }

}
