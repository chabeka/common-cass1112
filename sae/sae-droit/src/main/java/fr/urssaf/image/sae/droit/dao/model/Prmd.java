/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.List;
import java.util.Map;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;

/**
 * Classe de modèle d'un PRMD
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "droitprmdcql")
public class Prmd implements Comparable<Prmd> {

  /** identifiant unique du PRMD */
  @PartitionKey
  @Column(name = "code")
  private String code;

  /** description du PRMD */
  @Column(name = "description")
  private String description;

  /** requête LUCENE pour le filtrage de la recherche */
  @Column(name = "lucene")
  private String lucene;

  /** liste de clé/valeur pour un PRMD */
  @Column(name = "metadata")
  private Map<String, List<String>> metadata;

  /**
   * Nom du qualifier de la classe d'implémentation du bean de vérification de
   * l'appartenance à un PRMD
   */
  private String bean;

  /**
   * @return l'identifiant unique du PRMD
   */
  public final String getCode() {
    return code;
  }

  /**
   * @param code
   *           identifiant unique du PRMD
   */
  public final void setCode(final String code) {
    this.code = code;
  }

  /**
   * @return la description du PRMD
   */
  public final String getDescription() {
    return description;
  }

  /**
   * @param description
   *           description du PRMD
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @return la requête LUCENE pour le filtrage de la recherche
   */
  public final String getLucene() {
    return lucene;
  }

  /**
   * @param lucene
   *           requête LUCENE pour le filtrage de la recherche
   */
  public final void setLucene(final String lucene) {
    this.lucene = lucene;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean equals(final Object obj) {

    boolean areEquals = false;

    if (obj instanceof Prmd) {
      final Prmd prmd = (Prmd) obj;

      areEquals = code.equals(prmd.getCode())
          && description.equals(prmd.getDescription())
          && Objects.equal(lucene, prmd.getLucene())
          && Objects.equal(bean, prmd.getBean())
          && metadata.keySet().size() == prmd.getMetadata().keySet()
          .size()
          && metadata.keySet().containsAll(prmd.getMetadata().keySet());
    }

    return areEquals;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int hashCode() {
    return super.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String toString() {

    final StringBuffer buffer = new StringBuffer("\nliste des metadonnees :");
    for (final String key : metadata.keySet()) {
      buffer.append("\nclé = " + key + " / valeur = " + metadata.get(key));
    }

    return "code : " + code + "\ndescription : " + description
        + "\nlucene : " + lucene + "\nbean : " + bean + buffer.toString();
  }

  /**
   * @return la liste de clé/valeur pour un PRMD
   */
  public final Map<String, List<String>> getMetadata() {
    return metadata;
  }

  /**
   * @param metadata
   *           liste de clé/valeur pour un PRMD
   */
  public final void setMetadata(final Map<String, List<String>> metadata) {
    this.metadata = metadata;
  }

  /**
   * @return le Nom du qualifier de la classe d'implémentation du bean de
   *         vérification de l'appartenance à un PRMD
   */
  public final String getBean() {
    return bean;
  }

  /**
   * @param bean
   *           Nom du qualifier de la classe d'implémentation du bean de
   *           vérification de l'appartenance à un PRMD
   */
  public final void setBean(final String bean) {
    this.bean = bean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final Prmd o) {

    return getCode().compareTo(o.getCode());
  }

}
