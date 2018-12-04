package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "getDocFormatOrigine"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "getDocFormatOrigine.tag" (attribut
 * "objetFormulaire")
 */
public class GetDocFormatOrigineFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private String idArchivage;
   
   private String idArchivageOrig;

   private CodeMetadonneeList codeMetadonnees = new CodeMetadonneeList();
   
  // private ModeConsultationEnum modeConsult = ModeConsultationEnum.AncienServiceSansMtom;

   /**
    * @param parent
    */
   public GetDocFormatOrigineFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   /**
    * Constructeur
    * 
    */
   public GetDocFormatOrigineFormulaire() {
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
    * L'identifiant d'archivage du document d'origine de l'archive que l'on souhaite consulter
    * 
    * @return L'identifiant d'archivage du document d'origine de l'archive que l'on souhaite consulter
    */
   public final String getIdArchivageOrig() {
      return idArchivageOrig;
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
    * L'identifiant d'archivage du document origine de l'archive que l'on souhaite consulter
    * 
    * @param idArchivageOrig
    *           L'identifiant d'archivage du document origine de l'archive que l'on souhaite
    *           consulter
    */
   public final void setIdArchivageOrig(String idArchivageOrig) {
      this.idArchivageOrig = idArchivageOrig;
   }
   

   /**
    * La liste des codes des métadonnées que l'on souhaite dans la getDocFormatOrigine
    * 
    * @return La liste des codes des métadonnées que l'on souhaite dans la
    *         getDocFormatOrigine
    * 
    */
   public final CodeMetadonneeList getCodeMetadonnees() {
      return codeMetadonnees;
   }

   /**
    * La liste des codes des métadonnées que l'on souhaite dans la getDocFormatOrigine
    * 
    * @param codeMetadonnees
    *           La liste des codes des métadonnées que l'on souhaite dans la
    *           getDocFormatOrigine
    */
   public final void setCodeMetadonnees(CodeMetadonneeList codeMetadonnees) {
      this.codeMetadonnees = codeMetadonnees;
   }

   
//   /**
//    * Le mode d'utilisation de la consultation
//    * 
//    * @return Le mode d'utilisation de la consultation
//    */
//   public final ModeConsultationEnum getModeConsult() {
//      return modeConsult;
//   }

   
//   /**
//    * Le mode d'utilisation de la consultation
//    * 
//    * @param modeConsult Le mode d'utilisation de la consultation
//    */
//   public final void setModeConsult(ModeConsultationEnum modeConsult) {
//      this.modeConsult = modeConsult;
//   }

}
