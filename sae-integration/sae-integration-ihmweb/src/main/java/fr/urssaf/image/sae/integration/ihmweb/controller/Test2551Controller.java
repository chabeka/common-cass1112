package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.AjoutNoteFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsAjoutNoteFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;


/**
 * 2500-Note-TestLibre
 */
@Controller
@RequestMapping(value = "test2551")
public class Test2551Controller extends AbstractTestWsController<TestWsAjoutNoteFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2551";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsAjoutNoteFormulaire getFormulairePourGet() {
      
      TestWsAjoutNoteFormulaire formulaire = new TestWsAjoutNoteFormulaire();
      AjoutNoteFormulaire formAjoutNote = formulaire.getAjoutNote();
      formAjoutNote.getResultats().setStatus(TestStatusEnum.SansStatus);
      formAjoutNote.setIdArchivage("00000000-0000-0000-0000-000000000000");
      formAjoutNote.setNote("Ma note ");
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsAjoutNoteFormulaire formulaire) {
      
      String urlServiceWeb = formulaire.getUrlServiceWeb();
      AjoutNoteFormulaire ajoutNoteForm = formulaire.getAjoutNote();
      String idSoapFault = "sae_ArchiveNonTrouvee";
      //String idSoapFault = "sae_AjoutNoteArchiveNonTrouvee"; // Abandon du redmine 6970      
      String[] soapMsgArgs = new String[] {ajoutNoteForm.getIdArchivage()};
      
    //-- Appel au ws
      getAjoutNoteTestService().appelWsOpAjoutNoteTestSoapFault(
            urlServiceWeb, ajoutNoteForm, ViStyle.VI_OK, null, idSoapFault, soapMsgArgs);
   }
   
 
}
