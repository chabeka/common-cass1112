package fr.urssaf.image.sae.mapping.test.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe representant une liste de métadonnées.
 * 
 * @author akenore
 * 
 */
@XStreamAlias("metadatas")
public class MappingMockMetadatas {
	@XStreamImplicit(itemFieldName = "metadata")
	private List<MappingMockMetadata> metadatas;

	/**
	 * @return La liste des metadonnées.
	 */

	public final List<MappingMockMetadata> getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas
	 *            : La liste des metadonnées.
	 */
	public final void setMetadatas(final List<MappingMockMetadata> metadatas) {
		this.metadatas = metadatas;
	}

}
