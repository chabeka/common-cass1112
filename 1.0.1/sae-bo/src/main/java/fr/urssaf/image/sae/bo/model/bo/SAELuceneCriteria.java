package fr.urssaf.image.sae.bo.model.bo;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.AbstractSAECriteria;

/**
 * Classe représentant le critère de recherche par requête lucène.<br/>
 * Elle contient les attributs :
 * <ul>
 * <li>limit : Le nombre de SAEDocument à retourner après la recherche.</li>
 * <li>luceneQuery : La requête lucene.</li>
 * </ul>
 * 
 * @author akenore
 * 
 */
public class SAELuceneCriteria extends AbstractSAECriteria {
	private String luceneQuery;
	private int limit;

	/**
	 * @return Le nombre de SAEDocument à retourner après la recherche.
	 */
	public final int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            : Le nombre de SAEDocument à retourner après la recherche.
	 */
	public final void setLimit(final int limit) {
		this.limit = limit;
	}

	/**
	 * @return La requête lucene
	 */
	public final String getLuceneQuery() {
		return luceneQuery;
	}

	/**
	 * @param luceneQuery
	 *            : La requête lucene
	 */
	public final void setLuceneQuery(final String luceneQuery) {
		this.luceneQuery = luceneQuery;
	}

	/**
	 * Construit un objet de type {@link SAELuceneCriteria}
	 */
	public SAELuceneCriteria() {
		super();
	}

	/**
	 * Construit un objet de type {@link SAELuceneCriteria}
	 * 
	 * @param luceneQuery
	 *            : La requête lucene.
	 * @param metadatas
	 *            : La liste des métadonnées désirées.
	 */
	public SAELuceneCriteria(final String luceneQuery,
			final List<SAEMetadata> metadatas) {
		super(metadatas);
		this.luceneQuery = luceneQuery;

	}

	/**
	 * Construit un objet de type {@link SAELuceneCriteria}
	 * 
	 * @param luceneQuery
	 *            : La requête lucene
	 * @param limit
	 *            : Le nombre de SAEDocument à retourner après la recherche
	 * @param metadatas
	 *            : La liste des métadonnées désirées.
	 */
	public SAELuceneCriteria(final String luceneQuery, final int limit,
			final List<SAEMetadata> metadatas) {
		this(luceneQuery, metadatas);
		this.limit = limit;
	}

	/**
	 * Construit un objet de type {@link SAELuceneCriteria}
	 * 
	 * @param luceneQuery
	 *            : La requête lucene
	 */
	public SAELuceneCriteria(final String luceneQuery) {
		this(luceneQuery, null);

	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() {
		final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		toStrBuilder.append("luceneQuery", luceneQuery);
		if (getDesiredSAEMetadatas() != null) {
			for (SAEMetadata metadata : getDesiredSAEMetadatas()) {
				toStrBuilder.append(" code long", metadata.getLongCode());
			}
		}
		return toStrBuilder.toString();

	}

}
