package fr.urssaf.image.commons.controller.spring3.exemple.formulaire;

import java.util.Date;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Errors;

import fr.urssaf.image.commons.controller.spring3.exemple.modele.Document;
import fr.urssaf.image.commons.controller.spring3.exemple.modele.Etat;
import fr.urssaf.image.commons.controller.spring3.exemple.validator.NotEmpty;

public class FormFormulaire {

	private String titre;

	private Date openDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date closeDate;

	private Etat etat;

	private Integer level;

	private boolean flag;

	private Set<Etat> etats;
	
	public void init(Document document){
		this.setCloseDate(document.getCloseDate());
		this.setEtat(document.getEtat());
		this.setEtats(document.getEtats());
		this.setFlag(document.isFlag());
		this.setLevel(document.getLevel());
		this.setOpenDate(document.getOpenDate());
		this.setTitre(document.getTitre());
		this.getInterneFormulaire().setComment(document.getComment());
	}
	

	@NotEmpty
	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	@NotNull
	@Past
	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	@NotNull
	@Past
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	@NotNull
	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public void setEtats(Set<Etat> etats) {
		this.etats = etats;
	}

	public Set<Etat> getEtats() {
		return etats;
	}

	@NotNull
	@Range(min = 1, max = 3)
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void validate(Errors errors) {
		
		RuleFormUtil.DateRule dateRule = new RuleFormUtil.DateRule(this.closeDate,
				this.openDate);
		
		if (!dateRule.isValid()) {
			errors.rejectValue("closeDate","validDate");
		}
		
		
	}

	public InterneFormulaire interneFormulaire = new InterneFormulaire();

	@Valid
	public InterneFormulaire getInterneFormulaire() {
		return this.interneFormulaire;
	}

	public class InterneFormulaire {

		private String comment;

		public void setComment(String comment) {
			this.comment = StringUtils.trim(comment);
		}

		@NotEmpty
		public String getComment() {
			return comment;
		}
	}

}
