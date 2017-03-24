package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import org.apache.log4j.Logger;

import fr.urssaf.image.commons.controller.springsecurity.exemple.form.FormFormulaire;

public abstract class AbstractFormServiceImpl implements FormService {

   protected static final Logger LOG = Logger.getLogger(AbstractFormServiceImpl.class);

   @Override
   public void display(FormFormulaire form) {

      LOG.info("title:" + form.getTitle() + " text:" + form.getText());

   }

   @Override
   public void populate(FormFormulaire form) {

      form
            .setText("Ainsi, dans les temps des fables, \naprès les inondations et les déluges \nil sortit de la terre des hommes armés qui s'exterminèrent");
      form.setTitle("Montesquieu");

   }

}
