package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.DFCEKeyspaceConnecter;

/**
 * Classe permettant la mise à jour des donnees du keyspace Docubase dans
 * cassandra
 * 
 */
/**
 * Nom du keyspace
 */

@Component
public class DFCECqlUpdater {
  @Autowired
  protected DFCEKeyspaceConnecter dfcecf;

  /**
   * Methode d'activation d'un index composite
   * (update à true sur computed)
   * 
   * @param indexName
   * @throws IOException
   */
  public final void indexeAVideCompositeIndex(final String indexName) throws IOException {
    // On indexe l'index composite si ce n'est pas déjà le cas dans DFCE
    final StringBuffer sbf = prepareCQLUpdateTrueQuery("composite_index", indexName, "computed");
    final String query = sbf.toString();
    dfcecf.getSession().execute(query);
  }

  /**
   * Methode de création de la requete d'update de la base de données.
   * 
   * @param cFName
   * @param rowName
   * @param columnName
   * @param value
   * @return
   */
  private StringBuffer prepareCQLUpdateTrueQuery(final String cFName, final String rowName, final String columnName) {
    final String spaceString = " ";
    final StringBuffer sbf = new StringBuffer();

    sbf.append("UPDATE " + cFName + spaceString);
    sbf.append("SET " + columnName + "=" + Boolean.TRUE + spaceString);
    sbf.append("WHERE id='" + rowName + "'");
    sbf.append(" IF " + columnName + "=" + Boolean.FALSE);
    sbf.append(";");

    return sbf;
  }

}