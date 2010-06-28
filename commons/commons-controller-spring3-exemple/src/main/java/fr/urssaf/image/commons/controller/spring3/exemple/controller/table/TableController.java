package fr.urssaf.image.commons.controller.spring3.exemple.controller.table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.urssaf.image.commons.controller.spring3.exemple.controller.base.AbstractExempleController;
import fr.urssaf.image.commons.controller.spring3.exemple.formulaire.TableFormulaire;
import fr.urssaf.image.commons.controller.spring3.exemple.service.DocumentService;

@Controller
@RequestMapping(value = "/table")
@SessionAttributes("formulaire")
public class TableController extends AbstractExempleController {

	@Autowired
	private DocumentService documentService;
	
	@RequestMapping(method = RequestMethod.GET)
	protected String getDefaultView(Model model) {

		TableFormulaire tableFormulaire = new TableFormulaire();
		tableFormulaire.initDocuments(documentService.allDocuments());
		model.addAttribute("formulaire",tableFormulaire);
		return this.defaultView();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String update(@ModelAttribute("formulaire") @Valid TableFormulaire tableFormuaire,BindingResult result) {

		tableFormuaire.validate(result);
		if (result.hasErrors()) {
			return this.defaultView();
		}
		
		documentService.update(tableFormuaire);
		return "redirect:/table.do";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, HttpServletResponse reponse) {

		return "redirect:/form.do";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "closeDates",
				new CustomDateEditor(dateFormat, true));
	}

	private String defaultView() {
		return "table/tableView";

	}

}
