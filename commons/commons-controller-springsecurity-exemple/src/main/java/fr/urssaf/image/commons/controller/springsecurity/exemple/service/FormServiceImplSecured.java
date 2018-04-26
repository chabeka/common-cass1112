package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.controller.springsecurity.exemple.form.FormFormulaire;

@Service
@Qualifier("secured")
public class FormServiceImplSecured extends AbstractFormServiceImpl {

   @Override
   @Secured( { "ROLE_USER" })
   public void save(FormFormulaire form) {

      LOG.info("SECURED SAVE title:" + form.getTitle() + " text:"
            + form.getText());

   }

}
