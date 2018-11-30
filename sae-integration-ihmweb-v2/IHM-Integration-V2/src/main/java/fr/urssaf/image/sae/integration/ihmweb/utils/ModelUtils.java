package fr.urssaf.image.sae.integration.ihmweb.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import fr.urssaf.image.sae.integration.ihmweb.modele.CasTest;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielCasTestService;

/**
 * Bean singleton contenant des méthodes utilitaires pour le Model
 */
@Service
public class ModelUtils {

   @Autowired
   private ReferentielCasTestService casTestService;
   
   
   /**
    * Ajoute dans le Model l'objet CasTest décrivant le cas de test en cours
    * 
    * @param idTest l'identifiant unique du cas de test
    * @param model le Model
    */
   public final void ajouteCasTestDansModele(
         String idTest,
         Model model) {
      
      // Récupère les propriétés du cas de test
      CasTest casTest = casTestService.getCasTest(idTest);
      
      // Ajout la description du cas de test au modèle
      model.addAttribute("casTest",casTest);
      
      
   }
   
}
