package fr.urssaf.image.commons.springsecurity.web.service;

import fr.urssaf.image.commons.springsecurity.web.form.FormFormulaire;

public interface FormService {
   
   void save(FormFormulaire form);

   void populate(FormFormulaire form);
}
