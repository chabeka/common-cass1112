/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.List;

import org.javers.core.metamodel.annotation.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de modèle d'un PAGMa
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "droitpagmacql")
public class Pagma implements Comparable<Pagma> {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(Pagma.class);
  /** code unique du PAGMa */
  @PartitionKey
  @Column(name = "code")
  @Id
  private String code;

  /** liste des codes des actions unitaires du PAGMa */
  @Column(name = "actionunitaires")
  private List<String> actionUnitaires;

  /**
   * @return le code unique du PAGMa
   */
  public final String getCode() {
    return code;
  }

  /**
   * @param code
   *           code unique du PAGMa
   */
  public final void setCode(final String code) {
    this.code = code;
  }

  /**
   * @return la liste des codes des actions unitaires du PAGMa
   */
  public final List<String> getActionUnitaires() {
    return actionUnitaires;
  }

  /**
   * @param actionUnitaires
   *           liste des codes des actions unitaires du PAGMa
   */
  public final void setActionUnitaires(final List<String> actionUnitaires) {
    this.actionUnitaires = actionUnitaires;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean equals(final Object obj) {
    boolean areEquals = false;

    if (obj instanceof Pagma) {
      final Pagma pagma = (Pagma) obj;
      areEquals = code.equals(pagma.getCode())
          && actionUnitaires.size() == pagma.getActionUnitaires().size()
          && actionUnitaires.containsAll(pagma.getActionUnitaires());
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
    final StringBuffer buffer = new StringBuffer();
    for (final String action : actionUnitaires) {
      buffer.append("action = " + action + "\n");
    }

    return "code : " + code + "\nactions :\n" + buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final Pagma o) {
    return getCode().compareTo(o.getCode());
  }

}
