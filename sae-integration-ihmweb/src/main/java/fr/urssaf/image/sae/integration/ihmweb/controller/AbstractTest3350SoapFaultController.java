/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import fr.urssaf.image.sae.integration.ihmweb.constantes.PagmCodeEnum;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CopieResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;

/**
 * 
 * 
 */
public abstract class AbstractTest3350SoapFaultController extends AbstractTest3300Controller {

   /**
    * Nombre de resultat de la recherche par defaut attendu après la copie.
    */
   private final static int NB_RESULTAT_RECH_COPIE_ATTENDU = 0;

   /**
    * Nombre de resultat de la recherche par defaut attendu pour le document
    * existant.
    */
   private final static int NB_RESULTAT_RECH_EXISTANT_ATTENDU = 1;

   /**
    * {@inheritDoc}
    */
   @Override
   protected CopieResultat appelWsOpCopieService(String urlServiceWeb,
         CopieFormulaire formulaire, ViFormulaire viFormulaire) {
      return this.getCopieTestService().appelWsOpCopie(urlServiceWeb, formulaire, getSoapFaultAttendu(), getSoapFaultArguments(), viFormulaire);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected CaptureUnitaireResultat appelWsOpCaptureUnitaireService(
         String urlServiceWeb, CaptureUnitaireFormulaire formulaire,
         ViFormulaire viFormulaire) {
      return this.getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(urlServiceWeb, formulaire);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected RechercheResponse appelWsOpRechercheService(String urlServiceWeb,
         RechercheFormulaire formulaire, ViFormulaire viFormulaire) {
      return this.getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  urlServiceWeb,
                  formulaire,
                  "recherche_document_copie".equals(formulaire.getParent()
                        .getEtape()) ? NB_RESULTAT_RECH_COPIE_ATTENDU
                              : NB_RESULTAT_RECH_EXISTANT_ATTENDU, false,
                              TypeComparaison.DateCreation);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireVI(ViFormulaire viFormulaire) {
      viFormulaire.setIssuer(VI_SOAP_FAULT_ISSUER);
      PagmList pagmList = viFormulaire.getPagms();
      pagmList.clear();
      pagmList.add(PagmCodeEnum.INT_PAGM_COPIE_ALL.toString());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected MetadonneeValeurList getMetadonneesList(TypeRechercheEtape etape) {
      MetadonneeValeurList listMetas = null;
      if (TypeRechercheEtape.RECH_ETAPE_DOC_COPIE.toString().equals(
            etape.toString())) {
         listMetas = getMetadonneesListDefaut(this.getDenominationDocCopie());
      } else if (TypeRechercheEtape.RECH_ETAPE_DOC_EXISTANT.toString().equals(
            etape.toString())) {
         listMetas = getMetadonneesListDefaut(this.getDenominationDocExistant());
      }

      return listMetas;
   }

   /**
    * 
    * Methode permettant de recuperer la SOAP Fault attendu.
    * 
    * @return la SOAP Fault attendu.
    */
   abstract String getSoapFaultAttendu();

   /**
    * 
    * Methode permettant de recuperer les arguments de la SOAP Fault attendu.
    * 
    * @return
    */
   abstract Object[] getSoapFaultArguments();

   /**
    * 
    * Methode permettant de recuperer la denomination du test pour la recherche
    * du document copié.
    * 
    * @return la denomination du test pour la recherche du document copié.
    */
   abstract String getDenominationDocCopie();

   /**
    * 
    * Methode permettant de recuperer la denomination du test pour la recherche
    * du document existant.
    * 
    * @return la denomination du test pour la recherche du document existant
    */
   abstract String getDenominationDocExistant();

}
