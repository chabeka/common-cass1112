/**
 * 
 */
package fr.urssaf.image.sae.droit.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;

public class ServiceContractM extends ServiceContract implements Comparable<ServiceContractM> {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ServiceContractM.class);


  @Override
  public final boolean equals(final Object obj) {
    boolean areEquals = false;

    if (obj instanceof ServiceContract) {
      final ServiceContract contract = (ServiceContract) obj;

      areEquals = getCodeClient().equals(contract.getCodeClient())
          && getDescription().equals(contract.getDescription())
          && getLibelle().equals(contract.getLibelle())
          && getViDuree().equals(contract.getViDuree())
          && getListPki().equals(contract.getListPki())
          && getListCertifsClient().equals(contract.getListCertifsClient())
          ;
      if (!areEquals) {
        LOGGER.warn("codeClient:" + getCodeClient() + "/" + contract.getCodeClient()
        + ", description:" + getDescription() + "/" + contract.getDescription()
        + ", libelle:" + getLibelle() + "/" + contract.getLibelle()
        + ", viDuree:" + getViDuree() + "/" + contract.getViDuree()
        + ", listPki:" + getListPki() + "/" + contract.getListPki()
        + ", listCertifsClient:" + getListCertifsClient() + "/" + contract.getListCertifsClient());
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

  @Override
  public int compareTo(final ServiceContractM o) {

    return getCodeClient().compareTo(o.getCodeClient());
  }

}
