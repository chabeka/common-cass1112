package fr.urssaf.image.commons.springsecurity.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.commons.springsecurity.web.form.FormFormulaire;
import fr.urssaf.image.commons.springsecurity.web.service.FormService;

@Controller
@RequestMapping(value = "/simpleForm")
public class SimpleFormController {

   private final FormService formService;

   @Autowired
   public SimpleFormController(FormService formService) {

      Assert.notNull(formService, "'formService' is required");
      this.formService = formService;
   }

   @RequestMapping(method = RequestMethod.GET)
   protected String init(Model model) {

      FormFormulaire formFormulaire = new FormFormulaire();
      formService.populate(formFormulaire);
      
      model.addAttribute(formFormulaire);

      return defaultView();
   }

   @RequestMapping(method = RequestMethod.POST)
   protected String save(@Valid FormFormulaire formFormulaire,
         BindingResult result, Model model) {

      String view;
      if (result.hasErrors()) {
         view = defaultView();
         model.addAttribute("message", "form.failure");
      } else {

         formService.save(formFormulaire);
         model.addAttribute("message", "form.success");
         view = defaultView();
      }

      return view;

   }

   private String defaultView() {
      return "simpleForm";
   }

}
