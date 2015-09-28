package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2501Formulaire;
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
 * 2500-Note-TestLibre
 */
@Controller
@RequestMapping(value = "test2501")
public class Test2501Controller extends AbstractTestWsController<Test2501Formulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2501";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test2501Formulaire getFormulairePourGet() {
      
      Test2501Formulaire formulaire = new Test2501Formulaire();
      
      
      //-- capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptureUnitaire();

      //-- L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Note-2501-Note-OK-Standard/documents/doc1.PDF"));
      
      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-23");
      metadonnees.add("DateDebutConservation", "2011-09-01");
      metadonnees.add("Denomination", "Test 2501-Note-OK-Standard");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Note", "Note classique test 2500");
      metadonnees.add("Titre", "Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");
    

      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    * @throws IntegrationException 
    */
   @Override
   protected final void doPost(Test2501Formulaire formulaire) {
      
      String etape = formulaire.getEtape();
      if (etape.equals("1")){
         //-- Appel du ws de capture uintaire
         etape1captureUnitaireAppelWs(formulaire);
      } else if ("2".equals(etape)) {
         //-- Appel du ws de consultation
         try {
            etape2consultationAppelWS(formulaire);
         } catch (IntegrationException e) {            
            formulaire.getConsultation().getResultats().setStatus(TestStatusEnum.Echec);
         }  
      }
      
      
      
   }
   
   
   private void etape1captureUnitaireAppelWs(Test2501Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Lance le test
      CaptureUnitaireResultat res = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
      
      ResultatTest resultatTest = formCaptureEtp1.getResultats();
      
    if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
      // initialise l'UUID du document pour l'étape 2
      ConsultationFormulaire formConsultation = formulaire.getConsultation();
      formConsultation.setIdArchivage(res.getIdArchivage());
      
      //-- Les méta souhaitées
      CodeMetadonneeList metadonnees = new CodeMetadonneeList();
      metadonnees.add("Note");
      formConsultation.setCodeMetadonnees(metadonnees);
      
      //-- On sauvegarde le sha1 du document
      formulaire.setDernierSha1(res.getSha1());
      
      
   }
      //if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         // initialise l'identifiant du document archive
        // AjoutNoteFormulaire formAjoutNote = formulaire.getAjoutNote();
        // formAjoutNote.setIdArchivage(UUID.fromString(res.getIdArchivage()).toString());
      //}
   }
   
   private void etape2consultationAppelWS(Test2501Formulaire formulaire) throws IntegrationException {

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
         // String notes = metaObtenues.get(0).getValeur();
         //System.out.println(notes);
         ObjectMapper mapper = new ObjectMapper();
         String laNote = "{\"contenu\":\"Note classique test 2500\",\"dateCreation\":\"2015-09-14 13:16:22\",\"auteur\":\"_ADMIN\"}";
         try {
            Note note = mapper.readValue(laNote, Note.class);
            
            //System.out.println("obj note : " + note.getContenu());
            
            if(note.getContenu().equals("Note classique test 2500")){
               
            }else{
               this.getConsultationTestService().
               appelWsOpConsultationSoapFault(
                     formUrlWS, 
                     formCons,
                     ViStyle.VI_OK,
                     "sae_ErreurInterneAjoutNote",
                     new String[] {""});
            }
            //System.out.println(note.toString());
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
