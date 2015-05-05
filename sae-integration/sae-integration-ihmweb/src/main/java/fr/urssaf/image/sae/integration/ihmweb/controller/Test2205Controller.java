package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * 400-Consultation-TestLibre
 */
@Controller
@RequestMapping(value = "test2205")
public class Test2205Controller extends AbstractTestWsController<TestWsConsultationFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2205";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsConsultationFormulaire getFormulairePourGet() {
      
      TestWsConsultationFormulaire formulaire = new TestWsConsultationFormulaire();
      ConsultationFormulaire formConsult = formulaire.getConsultation();
      formConsult.getResultats().setStatus(TestStatusEnum.NonLance);
      
      //-- Les codes des métadonnées souhaitées
      CodeMetadonneeList codesMetas = formConsult.getCodeMetadonnees();
      codesMetas.add("DomaineComptable");
      codesMetas.add("DomaineCotisant");
      codesMetas.add("DomaineTechnique");
      codesMetas.add("DomaineRH");
      codesMetas.add("CodeRND");
      codesMetas.add("ContratDeService");

      return formulaire;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsConsultationFormulaire formulaire) {
      consultation(formulaire.getUrlServiceWeb(), formulaire.getConsultation());
   }
   
   /**
    * Appel du web service de consultation
    * @param urlWebService
    * @param formulaire
    */
   private void consultation(String urlWebService, ConsultationFormulaire formConsult) {
      
      //-- La liste des codes des métadonnées attendues
      CodeMetadonneeList codesMetasAttendues = new CodeMetadonneeList();
      codesMetasAttendues.add("DomaineComptable");
      codesMetasAttendues.add("DomaineCotisant");
      codesMetasAttendues.add("DomaineTechnique");
      codesMetasAttendues.add("DomaineRH");
      codesMetasAttendues.add("CodeRND");
      codesMetasAttendues.add("ContratDeService");

      //-- Les valeurs des métadonnées attendues
      List<MetadonneeValeur> metaAttendues = new ArrayList<MetadonneeValeur>();
      metaAttendues.add(new MetadonneeValeur("DomaineComptable", ""));
      metaAttendues.add(new MetadonneeValeur("DomaineCotisant", ""));
      metaAttendues.add(new MetadonneeValeur("DomaineTechnique", "true"));
      metaAttendues.add(new MetadonneeValeur("DomaineRH", ""));
      metaAttendues.add(new MetadonneeValeur("CodeRND", "7.7.8.8.1"));
      metaAttendues.add(new MetadonneeValeur("ContratDeService", "SAE"));

      //-- Lancement du test
      getConsultationTestService()
         .appelWsOpConsultationReponseCorrecteAttendue(urlWebService, formConsult, null, codesMetasAttendues, metaAttendues);
      
      
      //-- La répose attendue est correcte on passe le test à succès 
      if (TestStatusEnum.AControler.equals(formConsult.getResultats().getStatus())) {
         formConsult.getResultats().setStatus(TestStatusEnum.Succes);
      }
   }

}
