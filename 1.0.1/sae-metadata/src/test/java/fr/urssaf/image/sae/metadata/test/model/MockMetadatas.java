package fr.urssaf.image.sae.metadata.test.model;

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
public class MockMetadatas {
	@XStreamImplicit(itemFieldName = "metadata")
	private List<MockMetadata> metadatas;

	/**
	 * @return La liste des metadonnées.
	 */

	public final List<MockMetadata> getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas
	 *            : La liste des metadonnées.
	 */
	public final void setMetadatas(final List<MockMetadata> metadatas) {
		this.metadatas = metadatas;
	}

}
