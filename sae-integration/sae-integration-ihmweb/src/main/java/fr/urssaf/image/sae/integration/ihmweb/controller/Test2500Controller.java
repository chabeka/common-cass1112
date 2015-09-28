package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.AjoutNoteFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsAjoutNoteFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * 2500-Note-TestLibre
 */
@Controller
@RequestMapping(value = "test2500")
public class Test2500Controller extends AbstractTestWsController<TestWsAjoutNoteFormulaire> {

   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2500";
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final TestWsAjoutNoteFormulaire getFormulairePourGet() {
      
      TestWsAjoutNoteFormulaire formulaire = new TestWsAjoutNoteFormulaire();
      AjoutNoteFormulaire formAjoutNote = formulaire.getAjoutNote();
      formAjoutNote.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      formAjoutNote.setNote("Ma note ");
      return formulaire;
      
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(TestWsAjoutNoteFormulaire formulaire) {
      
      //-- Appel au ws
      String urlServiceWeb = formulaire.getUrlServiceWeb();
      AjoutNoteFormulaire ajoutNoteForm = formulaire.getAjoutNote();
      getAjoutNoteTestService().appelWsOpAjoutNoteTestLibre(urlServiceWeb, null, ajoutNoteForm);
      
   }
 
}
