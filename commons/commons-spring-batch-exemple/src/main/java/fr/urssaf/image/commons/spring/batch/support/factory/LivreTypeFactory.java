package fr.urssaf.image.commons.spring.batch.support.factory;

import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;
import fr.urssaf.image.commons.spring.batch.model.xml.livre.LivreType;

@Component
public class LivreTypeFactory {

   public LivreType createLivreType(Livre item) {

      LivreType livreType = new LivreType();

      livreType.setAuteur(item.getAuteur());
      livreType.setId(item.getIdentifiant());
      livreType.setTitre(item.getTitre());

      return livreType;
   }
}
