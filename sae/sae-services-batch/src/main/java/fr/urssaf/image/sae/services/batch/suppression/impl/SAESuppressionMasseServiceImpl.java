package fr.urssaf.image.sae.services.batch.suppression.impl;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.services.batch.common.model.ExitTraitement;
import fr.urssaf.image.sae.services.batch.suppression.SAESuppressionMasseService;

/**
 * Implémentation du service {@link SAESuppressionMasseService}
 */
@Service
public class SAESuppressionMasseServiceImpl implements SAESuppressionMasseService{
   
   /**
    * Job de la suppression de masse
    */
   //@Autowired
   //@Qualifier("suppression_masse")
   private Job jobSuppression;
   
   private final String TRC_SUPPRESSION = "suppressionMasse()";

   /**
    * {@inheritDoc}
    */
   @Override
   public ExitTraitement suppressionMasse(UUID idTraitement, String reqLucene) {
      // TODO Auto-generated method stub
      
      System.out.println("suppressionMasse" + idTraitement + " " + reqLucene);


      // TODO : Appel SB pour exécuter le traitement de restore
      
      ExitTraitement exitTraitement = new ExitTraitement();
      exitTraitement.setExitMessage("Traitement " + TRC_SUPPRESSION + "réalisé avec succès");
      exitTraitement.setSucces(true);
      
      return exitTraitement;
   }

}
