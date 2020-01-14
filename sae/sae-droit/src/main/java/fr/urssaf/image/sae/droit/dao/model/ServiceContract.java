/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.List;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;

/**
 * Classe de modèle d'un contrat de service
 * Annotation pour Mapping avec la table cql
 * idCertifClient et idPki ont été mis en transient car ils sont dépréciés
 * et ne doivent pas être utilisés pour els tables cql
 */
@Table(name = "droitcontratservicecql")
public class ServiceContract implements Comparable<ServiceContract> {

  /** code intelligible du CS */
  @Column(name = "libelle")
  private String libelle;
  @PartitionKey
  @Column(name = "codeClient")
  /** code de l'organisme client lié au contrat de service */
  private String codeClient;

  /** durée maximum de l'habilitation exprimée en secondes */
  @Column(name = "viDuree")
  private Long viDuree;

  @Column(name = "description")
  /** description du contrat de service */
  private String description;

  /** identifiant de la PKI attendue lors de la vérification des droits */
  @Transient
  private String idPki;

  /** liste des identifiants des PKI possibles pour la vérification des droits */
  @Column(name = "listPki")
  private List<String> listPki;

  /** flag indiquant si la vérification de nommage doit être réalisée ou non */
  @Column(name = "verifNommage")
  private boolean verifNommage;

  /**
   * identifiant du certificat client attendu lors de la vérification des
   * droits
   */
  @Transient
  private String idCertifClient;

  /**
   * liste des identifiants des certificats clients attendus lors de la
   * vérification des droits
   */
  @Column(name = "listCert")
  private List<String> listCertifsClient;

  /**
   * @return le code intelligible du CS
   */
  public final String getLibelle() {
    return libelle;
  }

  /**
   * @param libelle
   *           code intelligible du CS
   */
  public final void setLibelle(final String libelle) {
    this.libelle = libelle;
  }

  /**
   * @return le code de l'organisme client lié au contrat de service
   */
  public final String getCodeClient() {
    return codeClient;
  }

  /**
   * @param codeClient
   *           code de l'organisme client lié au contrat de service
   */
  public final void setCodeClient(final String codeClient) {
    this.codeClient = codeClient;
  }

  /**
   * @return la durée maximum de l'habilitation exprimée en secondes
   */
  public final Long getViDuree() {
    return viDuree;
  }

  /**
   * @param viDuree
   *           durée maximum de l'habilitation exprimée en secondes
   */
  public final void setViDuree(final Long viDuree) {
    this.viDuree = viDuree;
  }

  /**
   * @return la description du contrat de service
   */
  public final String getDescription() {
    return description;
  }

  /**
   * @param description
   *           description du contrat de service
   */
  public final void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @deprecated utiliser listPki
   * @return l'identifiant de la PKI attendue lors de la vérification des
   *         droits
   */
  @Deprecated
  public final String getIdPki() {
    return idPki;
  }

  /**
   * @deprecated utiliser listPki
   * @param idPki
   *           l'identifiant de la PKI attendue lors de la vérification des
   *           droits
   */
  @Deprecated
  public final void setIdPki(final String idPki) {
    this.idPki = idPki;
  }

  /**
   * @return la liste des identifiants des PKI possibles pour la vérification
   *         des droits
   */
  public final List<String> getListPki() {
    return listPki;
  }

  /**
   * @param listPki
   *           la liste des identifiants des PKI possibles pour la vérification
   *           des droits
   */
  public final void setListPki(final List<String> listPki) {
    this.listPki = listPki;
  }

  /**
   * @deprecated Utiliser la liste des certificats clients
   * @return l'identifiant du certificat client attendu lors de la vérification
   *         des droits
   */
  @Deprecated
  public final String getIdCertifClient() {
    return idCertifClient;
  }

  /**
   * @deprecated Utiliser la liste des certificats clients
   * @param idCertifClient
   *           identifiant du certificat client attendu lors de la vérification
   *           des droits
   */
  @Deprecated
  public final void setIdCertifClient(final String idCertifClient) {
    this.idCertifClient = idCertifClient;
  }

  /**
   * @return le flag de vérification de nommage
   */
  public final boolean isVerifNommage() {
    return verifNommage;
  }

  /**
   * @param verifNommage
   *           le flag de vérification de nommage
   */
  public final void setVerifNommage(final boolean verifNommage) {
    this.verifNommage = verifNommage;
  }

  /**
   * @return la liste des identifiants des certificats clients attendus lors de
   *         la vérification des droits
   */
  public final List<String> getListCertifsClient() {
    return listCertifsClient;
  }

  /**
   * @param listCertifsClient
   *           la liste des identifiants des certificats clients attendus lors
   *           de la vérification des droits
   */
  public final void setListCertifsClient(final List<String> listCertifsClient) {
    this.listCertifsClient = listCertifsClient;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public final String toString() {

    return "code client : " + codeClient + "\n" + "description : "
        + description + "\n" + "libellé : " + libelle + "\n"
        + "durée vi : " + viDuree.toString() + "\n";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (codeClient == null ? 0 : codeClient.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + (idCertifClient == null ? 0 : idCertifClient.hashCode());
    result = prime * result + (idPki == null ? 0 : idPki.hashCode());
    result = prime * result + (libelle == null ? 0 : libelle.hashCode());
    result = prime * result + (listCertifsClient == null ? 0 : listCertifsClient.hashCode());
    result = prime * result + (listPki == null ? 0 : listPki.hashCode());
    result = prime * result + (verifNommage ? 1231 : 1237);
    result = prime * result + (viDuree == null ? 0 : viDuree.hashCode());
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
    final ServiceContract other = (ServiceContract) obj;
    if (codeClient == null) {
      if (other.codeClient != null) {
        return false;
      }
    } else if (!codeClient.equals(other.codeClient)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (idCertifClient == null) {
      if (other.idCertifClient != null) {
        return false;
      }
    } else if (!idCertifClient.equals(other.idCertifClient)) {
      return false;
    }
    if (idPki == null) {
      if (other.idPki != null) {
        return false;
      }
    } else if (!idPki.equals(other.idPki)) {
      return false;
    }
    if (libelle == null) {
      if (other.libelle != null) {
        return false;
      }
    } else if (!libelle.equals(other.libelle)) {
      return false;
    }
    if (listCertifsClient == null) {
      if (other.listCertifsClient != null) {
        return false;
      }
    } else if (!listCertifsClient.equals(other.listCertifsClient)) {
      return false;
    }
    if (listPki == null) {
      if (other.listPki != null) {
        return false;
      }
    } else if (!listPki.equals(other.listPki)) {
      return false;
    }
    if (verifNommage != other.verifNommage) {
      return false;
    }
    if (viDuree == null) {
      if (other.viDuree != null) {
        return false;
      }
    } else if (!viDuree.equals(other.viDuree)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(final ServiceContract o) {

    return codeClient.compareTo(o.getCodeClient());
  }


}
