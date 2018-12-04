package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeStockageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "stockageUnitaire"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "stockageUnitaire.tag" (attribut
 * "objetFormulaire")
 */
public class StockageUnitaireFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private MetadonneeValeurList metadonnees = new MetadonneeValeurList();

   private String urlEcde;
   
   private String urlEcdeOrig;

   private ModeStockageUnitaireEnum modeStockage = ModeStockageUnitaireEnum.stockageUnitaireAvecUrlEcde;

   private String nomFichier;
   
   private String nomFichierOrig;
   
   private String dernierIdArchiv;
   
   private String dernierSha1;
  
    

   /**
    * constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public StockageUnitaireFormulaire(TestWsParentFormulaire parent) {
      super(parent);
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
    * L'URL ECDE du document que l'on veut archiver
    * 
    * @return L'URL ECDE du document que l'on veut archiver
    */
   public final String getUrlEcde() {
      return this.urlEcde;
   }

   /**
    * L'URL ECDE du document origine que l'on veut archiver
    * 
    * @return L'URL ECDE du document origine que l'on veut archiver
    */
   public final String getUrlEcdeOrig() {
      return this.urlEcdeOrig;
   }

   /**
    * L'URL ECDE du document que l'on veut archiver
    * 
    * @param urlEcde
    *           L'URL ECDE du document que l'on veut archiver
    */
   public final void setUrlEcde(String urlEcde) {
      this.urlEcde = urlEcde;
   }

   /**
    * L'URL ECDE du document origine que l'on veut archiver
    * 
    * @param urlEcdeOrig
    *           L'URL ECDE du document origine que l'on veut archiver
    */
   public final void setUrlEcdeOrig(String urlEcdeOrig) {
      this.urlEcdeOrig = urlEcdeOrig;
   }
   
   /**
    * La liste des métadonnées décrivant le document que l'on souhaite archiver
    * 
    * @return La liste des métadonnées décrivant le document que l'on souhaite
    *         archiver
    */
   public final MetadonneeValeurList getMetadonnees() {
      return metadonnees;
   }

   /**
    * La liste des métadonnées décrivant le document que l'on souhaite archiver
    * 
    * @param metadonnees
    *           La liste des métadonnées décrivant le document que l'on souhaite
    *           archiver
    */
   public final void setMetadonnees(MetadonneeValeurList metadonnees) {
      this.metadonnees = metadonnees;
   }

   /**
    * Le mode d'appel à la stockage unitaire
    * 
    * @return Le mode d'appel au stockage unitaire
    */
   public final ModeStockageUnitaireEnum getModeStockage() {
      return modeStockage;
   }

   /**
    * Le mode d'appel à la stockage unitaire
    * 
    * @param modeStockage
    *           Le mode d'appel au stockage unitaire
    */
   public final void setModeStockage(ModeStockageUnitaireEnum modeStockage) {
      this.modeStockage = modeStockage;
   }

   /**
    * Le nom du fichier dans le cas où le mode d'appel à la stockage unitaire est
    * "envoi du contenu"
    * 
    * @return Le nom du fichier dans le cas où le mode d'appel à la stockage
    *         unitaire est "envoi du contenu"
    */
   public final String getNomFichier() {
      return nomFichier;
   }

   /**
    * Le nom du fichier dans le cas où le mode d'appel à la stockage unitaire est
    * "envoi du contenu"
    * 
    * @param nomFichier
    *           Le nom du fichier dans le cas où le mode d'appel à la stockage
    *           unitaire est "envoi du contenu"
    */
   public final void setNomFichier(String nomFichier) {
      this.nomFichier = nomFichier;
   }
   
   /**
    * Le nom du fichier origine dans le cas où le mode d'appel à la stockage unitaire est
    * "envoi du contenu"
    * 
    * @return Le nom du fichier origine dans le cas où le mode d'appel à la stockage
    *         unitaire est "envoi du contenu"
    */
   public final String getNomFichierOrig() {
      return nomFichierOrig;
   }

   /**
    * Le nom du fichier origine dans le cas où le mode d'appel à la stockage unitaire est
    * "envoi du contenu"
    * 
    * @param nomFichierOrig
    *           Le nom du fichier origine dans le cas où le mode d'appel à la stockage
    *           unitaire est "envoi du contenu"
    */
   public final void setNomFichierOrig(String nomFichierOrig) {
      this.nomFichierOrig = nomFichierOrig;
   }
   
   /**
    * Le dernier id d'archivage obtenu en réponse de la capture unitaire
    * 
    * @return Le dernier id d'archivage obtenu en réponse de la capture unitaire
    */
   public final String getDernierIdArchivage() {
      return dernierIdArchiv;
   }

   
   /**
    * Le dernier id d'archivage obtenu en réponse de la capture unitaire
    * 
    * @param dernierIdArchiv Le dernier id d'archivage obtenu en réponse de la capture unitaire
    */
   public final void setDernierIdArchivage(String dernierIdArchiv) {
      this.dernierIdArchiv = dernierIdArchiv;
   }


   /**
    * Le SHA-1 du dernier document envoyé lors de la capture unitaire
    * 
    * @return Le SHA-1 du dernier document envoyé lors de la capture unitaire
    */
   public final String getDernierSha1() {
      return dernierSha1;
   }


   /**
    * Le SHA-1 du dernier document envoyé lors de la capture unitaire
    * 
    * @param dernierSha1 Le SHA-1 du dernier document envoyé lors de la capture unitaire
    */
   public final void setDernierSha1(String dernierSha1) {
      this.dernierSha1 = dernierSha1;
   }
   
    
}
