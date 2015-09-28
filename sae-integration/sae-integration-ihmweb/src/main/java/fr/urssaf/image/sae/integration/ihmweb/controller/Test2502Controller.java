package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.AjoutNoteFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2502Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ConsultationResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.Note;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectExtractor;


/**
 * 2502-Note-Ajout-OK
 */
@Controller
@RequestMapping(value = "test2502")
public class Test2502Controller extends AbstractTestWsController<Test2502Formulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2502";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test2502Formulaire getFormulairePourGet() {
      
      Test2502Formulaire formulaire = new Test2502Formulaire();
      
      
      //-- capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptureUnitaire();

      //-- L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Note-2502-Note-Ajout-OK/documents/doc1.PDF"));
      
      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-01");
      metadonnees.add("Denomination", "Test 2502-Note-Ajout-OK");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");
        
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    * @throws IntegrationException 
    */
   @Override
   protected final void doPost(Test2502Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if (etape.equals("1")){
         //-- Appel du ws de capture uintaire
         etape1captureUnitaireAppelWs(formulaire);
      } else
      {  if (("2".equals(etape))){
            //-- Appel du ws de capture uintaire
            etape2ajoutNoteAppelWS(formulaire);
         } else if ("3".equals(etape)) {
         //-- Appel du ws de consultation
         try {
            etape3consultationAppelWS(formulaire);
         } catch (IntegrationException e) {            
            formulaire.getConsultation().getResultats().setStatus(TestStatusEnum.Echec);
         }
       } 
      }
   }
   
 
   private void etape1captureUnitaireAppelWs(Test2502Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Lance le test
      CaptureUnitaireResultat res = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
      
      ResultatTest resultatTest = formCaptureEtp1.getResultats();
      
    if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
     // initialise l'UUID du document pour l'étape 2
       AjoutNoteFormulaire formAjoutNote = formulaire.getAjoutNote();
       formAjoutNote.setIdArchivage(res.getIdArchivage());
       formAjoutNote.setNote("Note classique test 2502");
       
      //-- On sauvegarde le sha1 du document
      formulaire.setDernierSha1(res.getSha1());
    }
   }
   
   
   private void etape2ajoutNoteAppelWS(Test2502Formulaire formulaire) {

      // Lance le WS d'Ajout de note 
      String urlServiceWeb = formulaire.getUrlServiceWeb();
      AjoutNoteFormulaire ajoutNoteForm = formulaire.getAjoutNote();
      getAjoutNoteTestService().appelWsOpAjoutNoteTestLibre(urlServiceWeb, null, ajoutNoteForm);
         
      // initialise l'UUID du document pour l'étape 2
      ConsultationFormulaire formConsultation = formulaire.getConsultation();
      formConsultation.setIdArchivage(ajoutNoteForm.getIdArchivage());
    
      // Les méta souhaitées
      CodeMetadonneeList metadonnees = new CodeMetadonneeList();
      metadonnees.add("Note");
      formConsultation.setCodeMetadonnees(metadonnees);

   }
   
   private void etape3consultationAppelWS(Test2502Formulaire formulaire) throws IntegrationException {

      // Lance le WS de consultation
      ConsultationFormulaire formCons = formulaire.getConsultation();
      String formUrlWS = formulaire.getUrlServiceWeb();
      String sha1attendu = formulaire.getDernierSha1();
      
      //-- On définit la liste des méta attendues (controlées)
      CodeMetadonneeList codesMetaAttendues = new CodeMetadonneeList();
      codesMetaAttendues.add("Note");
      
      ConsultationResultat res = getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formUrlWS, formCons , sha1attendu, codesMetaAttendues, null);
      
      MetadonneeValeurList metaObtenues = SaeServiceObjectExtractor
      .extraitMetadonnees(res.getMetadonnees());
      
      if(metaObtenues.size() > 0){
         ObjectMapper mapper = new ObjectMapper();
         String laNote = "{\"contenu\":\"Note classique test 2502\",\"dateCreation\":\"2015-09-14 13:16:22\",\"auteur\":\"_ADMIN\"}";
         try {
            Note note = mapper.readValue(laNote, Note.class);
            
            if(note.getContenu().equals("Note classique test 2502")){
               
            }else{
               this.getConsultationTestService().
               appelWsOpConsultationSoapFault(
                     formUrlWS, 
                     formCons,
                     ViStyle.VI_OK,
                     "sae_ErreurInterneAjoutNote",
                     new String[] {""});
            }
         } catch (JsonParseException e) {
            throw new IntegrationException("Format de la note n'est pas dans un Json correct");
         } catch (JsonMappingException e) {
            throw new IntegrationException("Format de la note n'est pas dans un Json correct");
         } catch (IOException e) {
            throw new IntegrationException("Format de la note n'est pas dans un Json correct");
         }
      }
      
   }
}
