/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.lotinstallmaj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;

/**
 * TODO (AC75095028) Description du type
 *
 */
@Component
public class MajLotServiceVerificatorRouter {

  private static final String PARAMETERS_CF_NAME = "parameters";

  @Autowired
  private ModeApiCqlSupport modeApiCqlSupport;

  @Autowired
  @Qualifier("MajLotServiceVerificatorThriftImpl")
  MajLotServiceVerificator majLotServiceVerificatorThrift;

  @Autowired
  @Qualifier("MajLotServiceVerificatorCQLImpl")
  MajLotServiceVerificator majLotServiceVerificatorCQL;


  public boolean verify(final int version) {
    if (modeApiCqlSupport.isModeCql(PARAMETERS_CF_NAME)) {
      return majLotServiceVerificatorCQL.verify(version);
    } else if (modeApiCqlSupport.isModeThrift(PARAMETERS_CF_NAME)) {
      return majLotServiceVerificatorThrift.verify(version);
    }
    return false;
  }

}
