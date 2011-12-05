/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.data.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author akenore
 * 
 */
@XStreamAlias("luceneQueries")
public class LuceneQueries {
	@XStreamImplicit(itemFieldName = "luceneQuery")
	private List<LuceneQuery> luceneQuery;

	/**
	 * @param luceneQuery
	 *            : Les réquêtes lucenes
	 */
	public final void setLuceneQuery(final List<LuceneQuery> luceneQuery) {
		this.luceneQuery = luceneQuery;
	}

	/**
	 * @return Les réquêtes lucenes.
	 */
	public final List<LuceneQuery> getLuceneQuery() {
		return luceneQuery;
	}

}
