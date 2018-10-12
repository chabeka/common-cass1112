package fr.urssaf.image.sae.trace.dao.modelcql;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.dao.model.TraceIndex;

@Table(name = "traceregtechniqueindexcql")
public class TraceRegTechniqueIndexCql extends TraceIndex {

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
   * constructeur par défaut
   */
  public TraceRegTechniqueIndexCql() {
    super();
  }

  /**
   * Constructeur
   *
   * @param exploitation
   *          trace technique
   */
  public TraceRegTechniqueIndexCql(final TraceRegTechniqueCql exploitation) {
    super(exploitation);
    this.contexte = exploitation.getContexte();
    this.contrat = exploitation.getContratService();
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
