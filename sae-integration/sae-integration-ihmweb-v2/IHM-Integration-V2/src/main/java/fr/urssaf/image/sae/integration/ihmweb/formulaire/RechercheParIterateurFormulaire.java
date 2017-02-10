package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.IdentifiantPage;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "rechercheParIterateur"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "rechercheParIterateur.tag" (attribut
 * "objetFormulaire")
 */
public class RechercheParIterateurFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private MetadonneeValeurList metaFixes = new MetadonneeValeurList();
   
   private MetadonneeRangeValeur metaVariable = new MetadonneeRangeValeur();
   
   private MetadonneeValeurList equalFilter = new MetadonneeValeurList();
   
   private MetadonneeValeurList notEqualFilter = new MetadonneeValeurList();
   
   private MetadonneeRangeValeurList rangeFilter = new MetadonneeRangeValeurList();
   
   private int nbDocParPage;
   
   private IdentifiantPage idPage = new IdentifiantPage();
   
   private CodeMetadonneeList codeMetadonnees = new CodeMetadonneeList();

   /**
    * Constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public RechercheParIterateurFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   
   /**
    * Constructeur
    * 
    */
   public RechercheParIterateurFormulaire() {

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


   /**
    * La liste des codes des métadonnées que l'on souhaite dans les résultats de
    * recherche
    * 
    * @return La liste des codes des métadonnées que l'on souhaite dans les
    *         résultats de recherche
    */
   public final CodeMetadonneeList getCodeMetadonnees() {
      return codeMetadonnees;
   }

   /**
    * La liste des codes des métadonnées que l'on souhaite dans les résultats de
    * recherche
    * 
    * @param codeMetadonnees
    *           La liste des codes des métadonnées que l'on souhaite dans les
    *           résultats de recherche
    */
   public final void setCodeMetadonnees(CodeMetadonneeList codeMetadonnees) {
      this.codeMetadonnees = codeMetadonnees;
   }


   /**
    * @return the metaFixes
    */
   public MetadonneeValeurList getMetaFixes() {
      return metaFixes;
   }


   /**
    * @param metaFixes the metaFixes to set
    */
   public void setMetaFixes(MetadonneeValeurList metaFixes) {
      this.metaFixes = metaFixes;
   }


   /**
    * @return the metaVariable
    */
   public MetadonneeRangeValeur getMetaVariable() {
      return metaVariable;
   }


   /**
    * @param metaVariable the metaVariable to set
    */
   public void setMetaVariable(MetadonneeRangeValeur metaVariable) {
      this.metaVariable = metaVariable;
   }


   /**
    * @return the equalFilter
    */
   public MetadonneeValeurList getEqualFilter() {
      return equalFilter;
   }


   /**
    * @param equalFilter the equalFilter to set
    */
   public void setEqualFilter(MetadonneeValeurList equalFilter) {
      this.equalFilter = equalFilter;
   }
   
   /**
    * @return the notEqualFilter
    */
   public MetadonneeValeurList getNotEqualFilter() {
      return notEqualFilter;
   }
   
   /**
    * @param equalFilter the notEqualFilter to set
    */
   public void setNotEqualFilter(MetadonneeValeurList notEqualFilter) {
      this.notEqualFilter = notEqualFilter;
   }


   


   /**
    * @return the rangeFilter
    */
   public MetadonneeRangeValeurList getRangeFilter() {
      return rangeFilter;
   }


   /**
    * @param rangeFilter the rangeFilter to set
    */
   public void setRangeFilter(MetadonneeRangeValeurList rangeFilter) {
      this.rangeFilter = rangeFilter;
   }


   /**
    * @return the nbDocParPage
    */
   public int getNbDocParPage() {
      return nbDocParPage;
   }


   /**
    * @param nbDocParPage the nbDocParPage to set
    */
   public void setNbDocParPage(int nbDocParPage) {
      this.nbDocParPage = nbDocParPage;
   }


   /**
    * @return the idPage
    */
   public IdentifiantPage getIdPage() {
      return idPage;
   }


   /**
    * @param idPage the idPage to set
    */
   public void setIdPage(IdentifiantPage idPage) {
      this.idPage = idPage;
   }



}