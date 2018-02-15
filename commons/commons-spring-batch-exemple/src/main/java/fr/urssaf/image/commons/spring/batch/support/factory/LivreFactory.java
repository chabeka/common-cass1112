package fr.urssaf.image.commons.spring.batch.support.factory;

import javax.xml.bind.JAXBElement;

import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;
import fr.urssaf.image.commons.spring.batch.model.xml.livre.LivreType;


@Component
public class LivreFactory {

   public Livre createLivre(JAXBElement<LivreType> item) {

      LivreType livreType = item.getValue();

      Livre livre = new Livre();
      livre.setAuteur(livreType.getAuteur());
      livre.setIdentifiant(livreType.getId());
      livre.setTitre(livreType.getTitre());

      return livre;
   }
}
