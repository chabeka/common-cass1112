package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeConsultationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "consultation"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "consultation.tag" (attribut
 * "objetFormulaire")
 */
public class ConsultationAffichableFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private String idArchivage;

   private CodeMetadonneeList codeMetadonnees = new CodeMetadonneeList();
   
   private String optimisationMTOM;
   
   /**
    * @return the optimisationMTOM
    */
   public String getOptimisationMTOM() {
      return optimisationMTOM;
   }

   /**
    * @param optimisationMTOM the optimisationMTOM to set
    */
   public void setOptimisationMTOM(String optimisationMTOM) {
      this.optimisationMTOM = optimisationMTOM;
   }

   private Integer numeroPage;
   
   private Integer nombrePages;
   


   /**
    * @return the numeroPage
    */
   public Integer getNumeroPage() {
      return numeroPage;
   }

   /**
    * @param numeroPage the numeroPage to set
    */
   public void setNumeroPage(Integer numeroPage) {
      this.numeroPage = numeroPage;
   }

   /**
    * @return the nombrePages
    */
   public Integer getNombrePages() {
      return nombrePages;
   }

   /**
    * @param nombrePages the nombrePages to set
    */
   public void setNombrePages(Integer nombrePages) {
      this.nombrePages = nombrePages;
   }


    /**
    * @param parent
    */
   public ConsultationAffichableFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   /**
    * Constructeur
    * 
    */
   public ConsultationAffichableFormulaire() {
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

   
}
