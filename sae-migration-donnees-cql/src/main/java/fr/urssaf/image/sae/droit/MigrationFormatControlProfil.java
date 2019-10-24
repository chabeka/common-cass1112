/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.cql.IFormatControlProfilDaoCql;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;

/**
 * (AC75095351) Classe de migration pour formatControlProfil
 */
@Component
public class MigrationFormatControlProfil {

  @Autowired
  private IFormatControlProfilDaoCql formatcontrolprofildaocql;

  @Autowired
  private FormatControlProfilSupport formatControlProfilSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationFormatControlProfil.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationFormatControlProfil - migrationFromThriftToCql- start ");

    final List<FormatControlProfil> formatControlProfils = formatControlProfilSupport.findAll();
    if (!formatControlProfils.isEmpty()) {
      formatcontrolprofildaocql.saveAll(formatControlProfils);
    }

    LOGGER.info(" MigrationFormatControlProfil - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationActionUnitaire - migrationFromCqlTothrift- start ");

    final Iterator<FormatControlProfil> formatControlProfils = formatcontrolprofildaocql.findAllWithMapper();
    while (formatControlProfils.hasNext()) {
      final FormatControlProfil formatControlProfil = formatControlProfils.next();
      formatControlProfilSupport.create(formatControlProfil, new Date().getTime());
    }

    LOGGER.info(" MigrationActionUnitaire - migrationFromCqlTothrift- end ");
  }
}
