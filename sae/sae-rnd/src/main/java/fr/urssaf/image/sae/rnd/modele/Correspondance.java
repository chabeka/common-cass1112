package fr.urssaf.image.sae.rnd.modele;

import java.util.Date;

import org.javers.core.metamodel.annotation.Id;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.ComparisonChain;

/**
 * Objet représentant la correspondance entre un code rnd temporaire et un code
 * définitif
 * 
 * 
 */
@Table(name = "correspondancesrndcql")
public class Correspondance implements Comparable<Correspondance> {

  /**
   * Le code temporaire
   */
  @PartitionKey(0)
  @Column(name = "codeTemporaire")
  @Id
  private String codeTemporaire;

  /**
   * Le code définitif
   */
  @Column(name = "codeDefinitif")
  private String codeDefinitif;

  /**
   * La version en cours durant l'existence de ce code temporaire
   */
  @PartitionKey(1)
  @Column(name = "versionCourante")
  @Id
  private String versionCourante;

  /**
   * La date de début de mise à jour du traitement de cette correspondance
   */
  @Column(name = "dateDebutMaj")
  private Date dateDebutMaj;

  /**
   * La date de fin de mise à jour du traitement de cette correspondance
   */
  @Column(name = "dateFinMaj")
  private Date dateFinMaj;

  /**
   * L'état de la mise à jour
   */
  @Column(name = "etat")
  private EtatCorrespondance etat;

  /**
   * @return the codeTemporaire
   */

  public final String getCodeTemporaire() {
    return codeTemporaire;
  }

  /**
   * @param codeTemporaire
   *           the codeTemporaire to set
   */
  public final void setCodeTemporaire(final String codeTemporaire) {
    this.codeTemporaire = codeTemporaire;
  }

  /**
   * @return the codeDefinitif
   */
  public final String getCodeDefinitif() {
    return codeDefinitif;
  }

  /**
   * @param codeDefinitif
   *           the codeDefinitif to set
   */
  public final void setCodeDefinitif(final String codeDefinitif) {
    this.codeDefinitif = codeDefinitif;
  }

  /**
   * @return the dateDebutMaj
   */
  public final Date getDateDebutMaj() {
    return getDateCopy(dateDebutMaj);
  }

  /**
   * @param dateDebutMaj
   *           the dateDebutMaj to set
   */
  public final void setDateDebutMaj(final Date dateDebutMaj) {
    this.dateDebutMaj = getDateCopy(dateDebutMaj);
  }

  /**
   * @return the dateFinMaj
   */
  public final Date getDateFinMaj() {
    return getDateCopy(dateFinMaj);
  }

  /**
   * @param dateFinMaj
   *           the dateFinMaj to set
   */
  public final void setDateFinMaj(final Date dateFinMaj) {
    this.dateFinMaj = getDateCopy(dateFinMaj);
  }

  /**
   * @return the etat
   */
  public final EtatCorrespondance getEtat() {
    return etat;
  }

  /**
   * @param etat
   *           the etat to set
   */
  public final void setEtat(final EtatCorrespondance etat) {
    this.etat = etat;
  }

  /**
   * @return the versionCourante
   */
  public final String getVersionCourante() {
    return versionCourante;
  }

  /**
   * @param versionCourante
   *           the versionCourante to set
   */
  public final void setVersionCourante(final String versionCourante) {
    this.versionCourante = versionCourante;
  }

  private Date getDateCopy(final Date date) {
    Date tDate = null;
    if (date != null) {
      tDate = new Date(date.getTime());
    }

    return tDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final Correspondance o) {

    return ComparisonChain.start()
        .compare(getCodeTemporaire(), o.getCodeTemporaire())
        .compare(getVersionCourante(), o.getVersionCourante())
        .result();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (codeDefinitif == null ? 0 : codeDefinitif.hashCode());
    result = prime * result + (codeTemporaire == null ? 0 : codeTemporaire.hashCode());
    result = prime * result + (dateDebutMaj == null ? 0 : dateDebutMaj.hashCode());
    result = prime * result + (dateFinMaj == null ? 0 : dateFinMaj.hashCode());
    result = prime * result + (etat == null ? 0 : etat.hashCode());
    result = prime * result + (versionCourante == null ? 0 : versionCourante.hashCode());
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
    final Correspondance other = (Correspondance) obj;
    if (codeDefinitif == null) {
      if (other.codeDefinitif != null) {
        return false;
      }
    } else if (!codeDefinitif.equals(other.codeDefinitif)) {
      return false;
    }
    if (codeTemporaire == null) {
      if (other.codeTemporaire != null) {
        return false;
      }
    } else if (!codeTemporaire.equals(other.codeTemporaire)) {
      return false;
    }
    if (dateDebutMaj == null) {
      if (other.dateDebutMaj != null) {
        return false;
      }
    } else if (!dateDebutMaj.equals(other.dateDebutMaj)) {
      return false;
    }
    if (dateFinMaj == null) {
      if (other.dateFinMaj != null) {
        return false;
      }
    } else if (!dateFinMaj.equals(other.dateFinMaj)) {
      return false;
    }
    if (etat != other.etat) {
      return false;
    }
    if (versionCourante == null) {
      if (other.versionCourante != null) {
        return false;
      }
    } else if (!versionCourante.equals(other.versionCourante)) {
      return false;
    }
    return true;
  }
}
