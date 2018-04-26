package fr.urssaf.image.commons.springsecurity.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.springsecurity.service.OtherService;
import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

@Service
public class OtherServiceImpl implements OtherService {

   private static final Logger LOG = Logger.getLogger(OtherService.class);

   @Override
   public void save(Modele modele) {

      LOG.info("SAVE title:" + modele.getTitle() + " text:" + modele.getText());

   }

   @Override
   public Modele load(int identifiant) {

      Modele modele = new Modele();

      if (identifiant == 0) {

         modele
               .setText("Ainsi, dans les temps des fables, \naprès les inondations et les déluges \nil sortit de la terre des hommes armés qui s'exterminèrent");
         modele.setTitle("Montesquieu");

      } else {

         modele
               .setText("Je n'aime pas le travail, \nnul ne l'aime ; \nmais j'aime ce qui est dans le travail l'occasion de se découvrir soi-même.");
         modele.setTitle("Conrad");
      }

      return modele;
   }
}
