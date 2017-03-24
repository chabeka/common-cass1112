package fr.urssaf.image.commons.controller.spring3.exemple.controller.form;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.commons.controller.spring3.exemple.controller.base.BaseExempleController;
import fr.urssaf.image.commons.controller.spring3.exemple.formulaire.FormFormulaire;
import fr.urssaf.image.commons.controller.spring3.exemple.service.DocumentService;

@Controller
@RequestMapping(value = "/form")
public class FormController extends BaseExempleController<FormFormulaire> {

	@Autowired
	private DocumentService documentService;

	@RequestMapping(method = RequestMethod.GET)
	protected String getDefaultView(Model model) {

		FormFormulaire formFormulaire = new FormFormulaire();
		model.addAttribute(formFormulaire);
		
		return this.defaultView();
	}

	@RequestMapping(method = RequestMethod.POST)
	protected String save(@Valid FormFormulaire formFormulaire,
			BindingResult result) {
		formFormulaire.validate(result);
		
		String view;
		if (result.hasErrors()) {
			view =  defaultView();
		}
		else{
			documentService.save(formFormulaire);
			view = "redirect:/table.do";
		}
		
		return view;

	}

	@RequestMapping(value = "/{titre}/{level}/populate", method = RequestMethod.GET)
	protected String populate(Model model,@PathVariable("titre") String title,
			@PathVariable int level) {
		FormFormulaire formFormulaire = new FormFormulaire();
		formFormulaire.setTitre(title);
		formFormulaire.setLevel(level);

		model.addAttribute(formFormulaire);
		return this.defaultView();
	}
	
	@RequestMapping(value = "table", method = RequestMethod.GET)
	protected String table() {

		return "redirect:/table.do";
	}

	public String defaultView() {
		return "form/formView";
	}

}
