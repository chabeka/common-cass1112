package fr.urssaf.image.sae.dfce.admin.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Cette classe contient la liste des applications sources suite à la
 * désérialisation du fichier xml [applications.xml].
 * 
 * @author akenore
 * 
 */
@XStreamAlias("applications")

public class Applications {
	@XStreamImplicit(itemFieldName = "application")	
	@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
	 private List<String> applications ;

	/**
	 * @param applications : L'application source
	 */
	public final void setApplications(final List<String> applications) {
		this.applications = applications;
	}

	/**
	 * @return L'application source
	 */
	public final List<String> getApplications() {
		return applications;
	}
}
