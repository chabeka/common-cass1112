package fr.urssaf.image.sae.ecde.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.service.ResultatService;
import fr.urssaf.image.sae.ecde.service.strategy.impl.JaxbResultatSerializer;

/**
 * Classe implémentant le service ResultatService
 * 
 */
@Service
@Qualifier("resultatService")
public class ResultatServiceImpl implements ResultatService {

   @Autowired
   private JaxbResultatSerializer jaxbRSerializer;
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void persistResultat(Resultats resultats) throws EcdeXsdException, IOException {
      jaxbRSerializer.serializeResultat(resultats);
   }

}
