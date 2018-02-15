package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.controller.springsecurity.exemple.form.FormFormulaire;

@Service
@Qualifier("jsr250")
public class FormServiceImplJSR250 extends AbstractFormServiceImpl {

   @Override
   @RolesAllowed( { "ROLE_USER" })
   public void save(FormFormulaire form) {

      LOG.info("JSR250 SAVE title:" + form.getTitle() + " text:"
            + form.getText());

   }

}
