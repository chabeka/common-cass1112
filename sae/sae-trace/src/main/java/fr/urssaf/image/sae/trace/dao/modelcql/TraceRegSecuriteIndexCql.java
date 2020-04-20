/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.modelcql;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.dao.model.TraceIndex;

/**
 * Classe de modèle de la CF traceregsecuriteindexcql
 */
@Table(name = "traceregsecuriteindexcql")
public class TraceRegSecuriteIndexCql extends TraceIndex {

  /**
   * Clé de partitionnement associé à la colonne du même nom dans la CF traceregsecuriteindexcql
   */
  @PartitionKey
  @Column(name = "identifiantindex")
  private String identifiantIndex;

  /** Contexte de la trace */
  private String contexte;

  /**
   * Code du contrat de service
   */
  private String contrat;

  /**
   * Constructeur par défaut
   */
  public TraceRegSecuriteIndexCql() {
    super();
  }

  /**
   * Constructeur
   *
   * @param exploitation
   *          trace de sécurité
   */
  public TraceRegSecuriteIndexCql(final TraceRegSecuriteCql exploitation) {
    super(exploitation);
    contexte = exploitation.getContexte();
    contrat = exploitation.getContratService();
  }

  /**
   * @return the identifiantIndex
   */
  public String getIdentifiantIndex() {
    return identifiantIndex;
  }

  /**
   * @param identifiantIndex
   *          the identifiantIndex to set
   */
  public void setIdentifiantIndex(final String identifiantIndex) {
    this.identifiantIndex = identifiantIndex;
  }

  /**
   * @return le Contexte de la trace
   */
  public final String getContexte() {
    return contexte;
  }

  /**
   * @param contexte
   *          Contexte de la trace
   */
  public final void setContexte(final String contexte) {
    this.contexte = contexte;
  }

  /**
   * @return the contrat
   */
  public String getContrat() {
    return contrat;
  }

  /**
   * @param contrat
   *          the contrat to set
   */
  public void setContrat(final String contrat) {
    this.contrat = contrat;
  }
}
