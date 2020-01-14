package fr.urssaf.image.sae.metadata.referential.model;

import java.util.List;

import org.javers.core.metamodel.annotation.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe métier représentant un dictionnaire
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "dictionarycql")
public class Dictionary implements Comparable<Dictionary> {

  @PartitionKey
  @Column(name = "identifiant")
  @Id
  private String identifiant;

  @Column(name = "listEntries")
  private List<String> entries;

  /**
   * Constructeur de la classe Dictionary
   * 
   * @param identifiant
   *           identifiant du dictionnaire
   * @param entries
   *           Liste des valeurs autorisées
   */

  public Dictionary(final String identifiant, final List<String> entries) {
    this.identifiant = identifiant;
    this.entries = entries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (entries == null ? 0 : entries.hashCode());
    result = prime * result + (identifiant == null ? 0 : identifiant.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Dictionary other = (Dictionary) obj;
    if (entries == null) {
      if (other.entries != null) {
        return false;
      }
    } else if (!entries.equals(other.entries)) {
      return false;
    }
    if (identifiant == null) {
      if (other.identifiant != null) {
        return false;
      }
    } else if (!identifiant.equals(other.identifiant)) {
      return false;
    }
    return true;
  }

  public Dictionary() {
  }

  /**
   * 
   * @return l'identifiant du dictionnaire
   */
  public final String getIdentifiant() {
    return identifiant;
  }

  /**
   * 
   * @param identifiant
   *           identifiant du dictionnaire
   */
  public final void setIdentifiant(final String identifiant) {
    this.identifiant = identifiant;
  }

  /**
   * 
   * @return Liste des valeurs autorisées
   */
  public final List<String> getEntries() {
    return entries;
  }

  /**
   * 
   * @param entries
   *           Liste des valeurs autorisées
   */
  public final void setEntries(final List<String> entries) {
    this.entries = entries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final Dictionary o) {

    return getIdentifiant().compareTo(o.getIdentifiant());
  }

}
