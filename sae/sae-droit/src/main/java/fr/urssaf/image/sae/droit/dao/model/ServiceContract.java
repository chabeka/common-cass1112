/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ServiceContract.class);
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
  public final boolean equals(final Object obj) {
    boolean areEquals = false;

    if (obj instanceof ServiceContract) {
      final ServiceContract contract = (ServiceContract) obj;

      areEquals = codeClient.equals(contract.getCodeClient())
          && description.equals(contract.getDescription())
          && libelle.equals(contract.getLibelle())
          && viDuree.equals(contract.getViDuree());
      if (!areEquals) {
        LOGGER.warn("codeClient:" + codeClient + "/" + getCodeClient()
        + ", description:" + description + "/" + contract.getDescription()
        + ", libelle:" + libelle + "/" + contract.getLibelle()
        + ", description:" + description + "/" + contract.getDescription()
        + ", viDuree:" + viDuree + "/" + contract.getViDuree()
            );
      }
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

    return "code client : " + codeClient + "\n" + "description : "
        + description + "\n" + "libellé : " + libelle + "\n"
        + "durée vi : " + viDuree.toString() + "\n";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final ServiceContract o) {

    return getCodeClient().compareTo(o.getCodeClient());
  }

}
