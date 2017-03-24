package fr.urssaf.image.commons.controller.springsecurity.exemple.service;

import fr.urssaf.image.commons.controller.springsecurity.exemple.form.FormFormulaire;

public interface FormService {

   void display(FormFormulaire form);

   void save(FormFormulaire form);

   void populate(FormFormulaire form);
}
