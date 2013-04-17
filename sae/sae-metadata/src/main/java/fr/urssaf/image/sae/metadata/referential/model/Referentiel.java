package fr.urssaf.image.sae.metadata.referential.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/**
 * Classe permettant de sérialiser les métadonnées du référentiel
 * 
 * @author projet
 * 
 */
@Deprecated
@XStreamAlias("referentiel")
public class Referentiel {
	@XStreamImplicit(itemFieldName = "metaDataReference")
	private List<MetadataReference> metadatas;

	/**
	 * 
	 * @param metadatas
	 *            : Les métadonnées du référentiel
	 */
	public final void setMetadatas(final List<MetadataReference> metadatas) {
		this.metadatas = metadatas;
	}

	/**
	 * 
	 * @return Les métadonnées du référentiel
	 */
	public final List<MetadataReference> getMetadatas() {
		return metadatas;
	}
	
	
}
