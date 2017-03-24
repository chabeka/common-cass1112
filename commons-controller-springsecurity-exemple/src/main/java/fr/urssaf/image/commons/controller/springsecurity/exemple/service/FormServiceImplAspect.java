package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.controller.springsecurity.exemple.form.FormFormulaire;

@Service
@Qualifier("aspect")
public class FormServiceImplAspect extends AbstractFormServiceImpl {

   @Override
   public void save(FormFormulaire form) {

      LOG.info("ASPECT SAVE title:" + form.getTitle() + " text:"
            + form.getText());

   }

}
