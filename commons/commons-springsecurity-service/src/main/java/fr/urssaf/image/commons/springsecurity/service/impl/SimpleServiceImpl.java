package fr.urssaf.image.commons.springsecurity.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.springsecurity.service.SimpleService;
import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

@Service
public class SimpleServiceImpl implements SimpleService {

   private static final Logger LOG = Logger.getLogger(SimpleService.class);

   @Override
   public void save(Modele modele) {

      LOG.info("SAVE title:" + modele.getTitle() + " text:" + modele.getText());

   }

   @Override
   public Modele load() {

      Modele modele = new Modele();

      modele
            .setText("Ainsi, dans les temps des fables, \naprès les inondations et les déluges \nil sortit de la terre des hommes armés qui s'exterminèrent");
      modele.setTitle("Montesquieu");

      return modele;
   }

}
