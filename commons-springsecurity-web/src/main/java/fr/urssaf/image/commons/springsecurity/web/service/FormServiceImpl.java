package fr.urssaf.image.commons.springsecurity.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.springsecurity.service.SimpleService;
import fr.urssaf.image.commons.springsecurity.service.modele.Modele;
import fr.urssaf.image.commons.springsecurity.web.form.FormFormulaire;

@Service
public class FormServiceImpl implements FormService {

   @Autowired
   private SimpleService service;

   @Override
   public void populate(FormFormulaire form) {

      Modele modele = service.load();

      form.setText(modele.getText());
      form.setTitle(modele.getTitle());
   }

   @Override
   public void save(FormFormulaire form) {

      Modele modele = new Modele();
      modele.setText(form.getText());
      modele.setTitle(form.getTitle());

      service.save(modele);

   }

}
