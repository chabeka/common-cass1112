package fr.urssaf.image.commons.controller.springsecurity.exemple.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.commons.controller.springsecurity.exemple.form.FormFormulaire;
import fr.urssaf.image.commons.controller.springsecurity.exemple.service.FormService;

@Controller
@RequestMapping(value = "/pageForm")
public class PageFormController {

   private final FormService formService;

   private static final Logger LOG = Logger.getLogger(PageFormController.class);

   @Autowired
   public PageFormController(FormService formService) {

      Assert.notNull(formService, "'formService' is required");

      LOG.debug("service formulaire :"
            + AopUtils.getTargetClass(formService).getAnnotation(
                  Qualifier.class).value());
      this.formService = formService;
   }

   @RequestMapping(method = RequestMethod.GET)
   protected String getDefaultView(Model model) {

      FormFormulaire formFormulaire = new FormFormulaire();
      model.addAttribute(formFormulaire);

      return defaultView();
   }

   @RequestMapping(method = RequestMethod.POST)
   protected String save(@Valid FormFormulaire formFormulaire,
         BindingResult result, Model model) {

      formService.display(formFormulaire);

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

   @RequestMapping(method = RequestMethod.POST, params = { "populate" })
   protected String populate(Model model) {

      FormFormulaire formFormulaire = new FormFormulaire();
      formService.populate(formFormulaire);
      model.addAttribute(formFormulaire);

      return defaultView();

   }

   private String defaultView() {
      return "page/pageForm";
   }

}
