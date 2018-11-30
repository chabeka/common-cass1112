package fr.urssaf.image.sae.integration.ihmweb.formulaire;



/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "archivageMasse" du WS SaeService
 */
public class Test1150Formulaire extends TestWsParentFormulaire {

   
   private final CaptureMasseFormulaire captureMasse = new CaptureMasseFormulaire(this) ;
   
   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   private final RechercheFormulaire rechercheFormulaire = new RechercheFormulaire(this);
   
   private final ConsultationFormulaire consultFormulaire = new ConsultationFormulaire(this);
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    */
   public final CaptureMasseFormulaire getCaptureMasse() {
      return this.captureMasse;
   }

   /**
    * Le sous formulaire de la capture unitaire
    * @return le sous formulaire de capture unitaire
    */

   public CaptureUnitaireFormulaire getCaptureUnitaire() {
      return captureUnitaire;
   }

   /**
    * Le sous formulaire de la recherche
    * @return le sous formulaire de la recherche
    */

   public RechercheFormulaire getRechercheFormulaire() {
      return rechercheFormulaire;
   }

   /**
    * Le sous formulaire de la consultation
    * @return le sous formulaire de la consultation
    */

   public ConsultationFormulaire getConsultFormulaire() {
      return consultFormulaire;
   }


}
