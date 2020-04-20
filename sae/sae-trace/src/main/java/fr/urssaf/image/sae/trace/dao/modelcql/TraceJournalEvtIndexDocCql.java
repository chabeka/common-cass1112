package fr.urssaf.image.sae.trace.dao.modelcql;

import java.util.Map;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.trace.dao.model.TraceIndex;

/**
 * Classe de modèle de la CF tracejournalevtindexdoccql
 */
@Table(name = "tracejournalevtindexdoccql")
public class TraceJournalEvtIndexDocCql extends TraceIndex {

  /**
   * Clé de partitionnement associé à la colonne du même nom dans la CF tracejournalevtindexdoccql
   */
  @PartitionKey
  @Column(name = "identifiantindex")
  private UUID identifiantIndex;

  /**
   * Contexte de l'évenement
   */
  @Column(name = "contexte")
  private String contexte;

  /**
   * Code du contrat de service
   */
  @Column(name = "contratService")
  private String contratService;

  /**
   * Informations supplémentaires de la trace
   */
  @Column(name = "infos")
  private Map<String, String> infos;

  /**
   * Constructeur par défaut
   */
  public TraceJournalEvtIndexDocCql() {
    super();
  }

  /**
   * Constructeur
   *
   * @param traceJournal
   *          trace du journal des événements
   */
  public TraceJournalEvtIndexDocCql(final TraceJournalEvtCql traceJournal) {
    super(traceJournal);
    contexte = traceJournal.getContexte();
    contratService = traceJournal.getContratService();
  }

  /**
   * @return the identifiantIndex
   */
  public UUID getIdentifiantIndex() {
    return identifiantIndex;
  }

  /**
   * @param identifiantIndex
   *          the identifiantIndex to set
   */
  public void setIdentifiantIndex(final UUID identifiantIndex) {
    this.identifiantIndex = identifiantIndex;
  }

  /**
   * @return le contexte de l'événement
   */
  public final String getContexte() {
    return contexte;
  }

  /**
   * @param contexte
   *          le contexte de l'événement
   */
  public final void setContexte(final String contexte) {
    this.contexte = contexte;
  }

  /**
   * @return the contratService
   */
  public final String getContratService() {
    return contratService;
  }

  /**
   * @param contratService
   *          the contratService to set
   */
  public final void setContratService(final String contratService) {
    this.contratService = contratService;
  }

  /**
   * @return the infos
   */
  public Map<String, String> getInfos() {
    return infos;
  }

  /**
   * @param infos
   *          the infos to set
   */
  public void setInfos(final Map<String, String> infos) {
    this.infos = infos;
  }

@Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((contexte == null) ? 0 : contexte.hashCode());
	result = prime * result + ((contratService == null) ? 0 : contratService.hashCode());
	result = prime * result + ((identifiantIndex == null) ? 0 : identifiantIndex.hashCode());
	result = prime * result + ((infos == null) ? 0 : infos.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (!super.equals(obj))
		return false;
	if (!(obj instanceof TraceJournalEvtIndexDocCql))
		return false;
	TraceJournalEvtIndexDocCql other = (TraceJournalEvtIndexDocCql) obj;
	if (contexte == null) {
		if (other.contexte != null)
			return false;
	} else if (!contexte.equals(other.contexte))
		return false;
	if (contratService == null) {
		if (other.contratService != null)
			return false;
	} else if (!contratService.equals(other.contratService))
		return false;
	if (identifiantIndex == null) {
		if (other.identifiantIndex != null)
			return false;
	} else if (!identifiantIndex.equals(other.identifiantIndex))
		return false;
	if (infos == null) {
		if (other.infos != null)
			return false;
	} else if (!infos.equals(other.infos))
		return false;
	return true;
}

}
