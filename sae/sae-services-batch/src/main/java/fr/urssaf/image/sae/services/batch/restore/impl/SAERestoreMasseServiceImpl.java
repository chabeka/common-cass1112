package fr.urssaf.image.sae.services.batch.restore.impl;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.restore.SAERestoreMasseService;


/**
 * Implémentation du service {@link SAERestoreMasseService}
 */
@Service
public class SAERestoreMasseServiceImpl implements SAERestoreMasseService {

   /**
    * Job de la restauration de documents 
    */
   //@Autowired
   //@Qualifier("restore_masse")
   private Job jobRestore;

   private final String TRC_RESTORE = "restoreMasse()";
   
   /**
    * {@inheritDoc}
    */
   @Override
   public ExitTraitement restoreMasse(UUID idTraitement) {
      // TODO Auto-generated method stub
      
      // TODO : Appel SB pour exécuter le traitement de restore
      
      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setExitMessage("Traitement " + TRC_RESTORE + "réalisé avec succès");
      exitTraitement.setSucces(true);
      
      return exitTraitement;
   }

}
