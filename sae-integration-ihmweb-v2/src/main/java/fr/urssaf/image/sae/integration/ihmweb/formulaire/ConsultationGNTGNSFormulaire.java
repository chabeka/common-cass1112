package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

public class ConsultationGNTGNSFormulaire extends GenericForm{
   private ResultatTest resultats = new ResultatTest();

   private String idArchivage;

   private CodeMetadonneeList codeMetadonnees = new CodeMetadonneeList();
   
   private ModeConsultationEnum modeConsult = ModeConsultationEnum.AncienServiceSansMtom;

   /**
    * @param parent
    */
   public ConsultationGNTGNSFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   /**
    * Constructeur
    * 
    */
   public ConsultationGNTGNSFormulaire() {
      super();
   }

   /**
    * Les résultats de l'appel à l'opération
    * 
    * @return Les résultats de l'appel à l'opération
    */
   public final ResultatTest getResultats() {
      return this.resultats;
   }

   /**
    * Les résultats de l'appel à l'opération
    * 
    * @param resultats
    *           Les résultats de l'appel à l'opération
    */
   public final void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }

   /**
    * L'identifiant d'archivage de l'archive que l'on souhaite consulter
    * 
    * @return L'identifiant d'archivage de l'archive que l'on souhaite consulter
    */
   public final String getIdArchivage() {
      return idArchivage;
   }

   /**
    * L'identifiant d'archivage de l'archive que l'on souhaite consulter
    * 
    * @param idArchivage
    *           L'identifiant d'archivage de l'archive que l'on souhaite
    *           consulter
    */
   public final void setIdArchivage(String idArchivage) {
      this.idArchivage = idArchivage;
   }

   /**
    * La liste des codes des métadonnées que l'on souhaite dans la consultation
    * 
    * @return La liste des codes des métadonnées que l'on souhaite dans la
    *         consultation
    * 
    */
   public final CodeMetadonneeList getCodeMetadonnees() {
      return codeMetadonnees;
   }

   /**
    * La liste des codes des métadonnées que l'on souhaite dans la consultation
    * 
    * @param codeMetadonnees
    *           La liste des codes des métadonnées que l'on souhaite dans la
    *           consultation
    */
   public final void setCodeMetadonnees(CodeMetadonneeList codeMetadonnees) {
      this.codeMetadonnees = codeMetadonnees;
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
    * @param modeConsult Le mode d'utilisation de la consultation
    */
   public final void setModeConsult(ModeConsultationEnum modeConsult) {
      this.modeConsult = modeConsult;
   }

}
