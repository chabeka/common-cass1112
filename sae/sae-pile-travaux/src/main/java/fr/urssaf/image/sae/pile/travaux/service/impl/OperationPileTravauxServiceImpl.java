/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.pile.travaux.service.OperationPileTravauxService;
import fr.urssaf.image.sae.pile.travaux.service.cql.OperationPileTravauxCqlService;
import fr.urssaf.image.sae.pile.travaux.service.thrift.OperationPileTravauxThriftService;

/**
 * @author AC75007648
 */
@Service
public class OperationPileTravauxServiceImpl implements OperationPileTravauxService {

  private final String cfName = "jobsqueue";

  private final OperationPileTravauxCqlService operationPileTravauxCqlService;

  private final OperationPileTravauxThriftService operationPileTravauxThriftService;

  @Autowired
  public OperationPileTravauxServiceImpl(final OperationPileTravauxCqlService operationPileTravauxCqlService,
                                         final OperationPileTravauxThriftService operationPileTravauxThriftService) {
    super();
    this.operationPileTravauxCqlService = operationPileTravauxCqlService;
    this.operationPileTravauxThriftService = operationPileTravauxThriftService;
  }

  @Override
  public void purger(final Date dateMax) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      operationPileTravauxCqlService.purger(dateMax);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      operationPileTravauxThriftService.purger(dateMax);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      operationPileTravauxCqlService.purger(dateMax);
      operationPileTravauxThriftService.purger(dateMax);
    }
  }

}
