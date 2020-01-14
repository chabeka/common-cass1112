/**
 * AC75095351
 */
package fr.urssaf.image.sae.droit.dao.modelcql;

import java.util.Map;

import org.javers.core.metamodel.annotation.Id;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.ComparisonChain;

/**
 * Classe spécifique cql pour PAGM
 * Annotation pour Mapping avec la table cql
 */
@Table(name = "droitpagmcql")
public class PagmCql implements Comparable<PagmCql> {

  /** code contrat */
  @PartitionKey
  @Column(name = "idClient")
  @Id
  private String idClient;

  /** code intelligible du PAGM */
  @ClusteringColumn
  @Column(name = "code")
  private String code;

  /** droit d'action (PAGMa) du PAGM */
  @Column(name = "pagma")
  private String pagma;

  /** domaine d'action (PAGMp) du PAGM */
  @Column(name = "pagmp")
  private String pagmp;

  /** droit pour les formats de fichiers (PAGMf) du PAGM */
  @Column(name = "pagmf")
  private String pagmf;

  /** description du PAGM */
  @Column(name = "description")
  private String description;

  /** valeurs des paramètres dynamiques du PRMD associé */
  @Column(name = "parametres")
  private Map<String, String> parametres;

  /**
   * Flag indiquant que la compression est active.
   */
  @Column(name = "compressionPdfActive")
  private Boolean compressionPdfActive;

  /**
   * Nombre d'octet à partir duqel la compression de pdf est effectuée si la
   * compression est activée.
   */
  @Column(name = "seuilCompressionPdf")
  private Integer seuilCompressionPdf;


  /**
   * @return le code intelligible du PAGM
   */
  public final String getCode() {
    return code;
  }

  /**
   * @param code
   *          code spécifique du PAGM
   */
  public final void setCode(final String code) {
    this.code = code;
  }

  /**
   * @return le droit d'action (PAGMa) du PAGM
   */
  public final String getPagma() {
    return pagma;
  }

  /**
   * @param pagma
   *           droit d'action (PAGMa) du PAGM
   */
  public final void setPagma(final String pagma) {
    this.pagma = pagma;
  }

  /**
   * @return le droit des formats de fichiers (PAGMf) du PAGM
   */
  public final String getPagmf() {
    return pagmf;
  }

  /**
   * @param pagmf
   *           le droit des formats de fichiers (PAGMf) du PAGM
   */
  public final void setPagmf(final String pagmf) {
    this.pagmf = pagmf;
  }

  /**
   * @return le domaine d'action (PAGMp) du PAGM
   */
  public final String getPagmp() {
    return pagmp;
  }

  /**
   * @param pagmp
   *           domaine d'action (PAGMp) du PAGM
   */
  public final void setPagmp(final String pagmp) {
    this.pagmp = pagmp;
  }

  /**
   * @return la description du PAGM
   */
  public final String getDescription() {
    return description;
  }

  /**
   * @param description
   *           description du PAGM
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @return les valeurs des paramètres dynamiques du PRMD associé
   */
  public final Map<String, String> getParametres() {
    return parametres;
  }

  /**
   * @param parametres
   *           valeurs des paramètres dynamiques du PRMD associé
   */
  public final void setParametres(final Map<String, String> parametres) {
    this.parametres = parametres;
  }

  /**
   * @return le flag indiquant si la compression est active.
   */
  public final Boolean getCompressionPdfActive() {
    return compressionPdfActive;
  }

  /**
   * @param compressionPdfActive
   *           flag indiquant si la compression de pdf doit etre activee
   */
  public final void setCompressionPdfActive(final Boolean compressionPdfActive) {
    this.compressionPdfActive = compressionPdfActive;
  }

  /**
   * @return le seuil a partir duquel il faut compressé les pdf si la
   *         compression est active
   */
  public final Integer getSeuilCompressionPdf() {
    return seuilCompressionPdf;
  }

  /**
   * @param seuilCompressionPdf
   *           le seuil a partir duquel il faut compressé les pdf si la
   *           compression est active
   */
  public final void setSeuilCompressionPdf(final Integer seuilCompressionPdf) {
    this.seuilCompressionPdf = seuilCompressionPdf;
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
    final PagmCql other = (PagmCql) obj;
    if (code == null) {
      if (other.code != null) {
        return false;
      }
    } else if (!code.equals(other.code)) {
      return false;
    }
    if (compressionPdfActive == null) {
      if (other.compressionPdfActive != null) {
        return false;
      }
    } else if (!compressionPdfActive.equals(other.compressionPdfActive)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (idClient == null) {
      if (other.idClient != null) {
        return false;
      }
    } else if (!idClient.equals(other.idClient)) {
      return false;
    }
    if (pagma == null) {
      if (other.pagma != null) {
        return false;
      }
    } else if (!pagma.equals(other.pagma)) {
      return false;
    }
    if (pagmf == null) {
      if (other.pagmf != null) {
        return false;
      }
    } else if (!pagmf.equals(other.pagmf)) {
      return false;
    }
    if (pagmp == null) {
      if (other.pagmp != null) {
        return false;
      }
    } else if (!pagmp.equals(other.pagmp)) {
      return false;
    }
    if (parametres == null) {
      if (other.parametres != null) {
        return false;
      }
    } else if (!parametres.equals(other.parametres)) {
      return false;
    }
    if (seuilCompressionPdf == null) {
      if (other.seuilCompressionPdf != null) {
        return false;
      }
    } else if (!seuilCompressionPdf.equals(other.seuilCompressionPdf)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public final String toString() {
    final StringBuffer buffer = new StringBuffer();
    for (final String key : parametres.keySet()) {
      buffer.append(key + " = " + parametres.get(key) + "\n");
    }

    return "code : " + code + "\n" + "description : " + description + "\n"
    + "pagma : " + pagma + "\n" + "pagmf : " + pagmf + "\n"
    + "pagmp : " + pagmp + "\n" + "liste des parametres :\n"
    + buffer.toString() + "\n"
    + "compressionPdfActive : " + compressionPdfActive + "\n"
    + "seuilCompressionPdf : " + seuilCompressionPdf + "\n";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (code == null ? 0 : code.hashCode());
    result = prime * result + (compressionPdfActive == null ? 0 : compressionPdfActive.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + (idClient == null ? 0 : idClient.hashCode());
    result = prime * result + (pagma == null ? 0 : pagma.hashCode());
    result = prime * result + (pagmf == null ? 0 : pagmf.hashCode());
    result = prime * result + (pagmp == null ? 0 : pagmp.hashCode());
    result = prime * result + (parametres == null ? 0 : parametres.hashCode());
    result = prime * result + (seuilCompressionPdf == null ? 0 : seuilCompressionPdf.hashCode());
    return result;
  }

  /**
   * @return the idClient
   */
  public String getIdClient() {
    return idClient;
  }

  /**
   * @param idClient the idClient to set
   */
  public void setIdClient(final String idClient) {
    this.idClient = idClient;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final PagmCql o) {
    return ComparisonChain.start()
        .compare(getIdClient(), o.getIdClient())
        .compare(getCode(), o.getCode())
        .result();
  }

}
