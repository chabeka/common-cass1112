package fr.urssaf.image.commons.controller.spring3.jndi.exemple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.commons.controller.spring3.jndi.exemple.dao.DocumentDao;

@Controller
@RequestMapping(value = "/document")
public class DocumentController {

   @Autowired
   private DocumentDao documentDao;

   @RequestMapping(method = RequestMethod.GET)
   protected String getDefaultView(Model model) {

      model.addAttribute("documents", documentDao.allDocuments());

      return "documents";
   }

}
