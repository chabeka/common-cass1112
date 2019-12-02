/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.droit.dao.cql.IFormatControlProfilDaoCql;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration pour formatControlProfil
 */
@Component
public class MigrationFormatControlProfil implements IMigration {

  @Autowired
  private IFormatControlProfilDaoCql formatcontrolprofildaocql;

  @Autowired
  private FormatControlProfilSupport formatControlProfilSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationFormatControlProfil.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromThriftToCql- start ");

    final List<FormatControlProfil> formatControlProfilsThrift = formatControlProfilSupport.findAll();
    if (!formatControlProfilsThrift.isEmpty()) {
      formatcontrolprofildaocql.saveAll(formatControlProfilsThrift);
    }
    final List<FormatControlProfil> formatControlProfilsCql = new ArrayList<>();
    final Iterator<FormatControlProfil> formatControlProfilsIterator = formatcontrolprofildaocql.findAllWithMapper();
    formatControlProfilsIterator.forEachRemaining(formatControlProfilsCql::add);
    compareFormatControlProfil(formatControlProfilsThrift, formatControlProfilsCql);
    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromCqlTothrift- start ");

    final Iterator<FormatControlProfil> formatControlProfils = formatcontrolprofildaocql.findAllWithMapper();
    final List<FormatControlProfil> formatControlProfilsCql = new ArrayList<>();
    while (formatControlProfils.hasNext()) {
      final FormatControlProfil formatControlProfil = formatControlProfils.next();
      formatControlProfilsCql.add(formatControlProfil);
      formatControlProfilSupport.create(formatControlProfil, new Date().getTime());
    }
    final List<FormatControlProfil> formatControlProfilThrift = formatControlProfilSupport.findAll();
    compareFormatControlProfil(formatControlProfilThrift, formatControlProfilsCql);
    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromCqlTothrift- end ");
  }

  /**
   * Logs: Comparer les listes de formatControlProfil
   * 
   * @param formatControlProfilsThrift
   * @param formatControlProfilsCql
   */
  private void compareFormatControlProfil(final List<FormatControlProfil> formatControlProfilsThrift, final List<FormatControlProfil> formatControlProfilsCql) {

    LOGGER.info("MIGRATION_FORMAT_CONTROL_PROFIL -- SizeThriftFormatControlProfil=" + formatControlProfilsThrift.size());
    LOGGER.info("MIGRATION_FORMAT_CONTROL_PROFIL -- SizeCqlFormatControlProfil=" + formatControlProfilsCql.size());
    if (CompareUtils.compareListsGeneric(formatControlProfilsThrift, formatControlProfilsCql)) {
      LOGGER.info("MIGRATION_FORMAT_CONTROL_PROFIL -- Les listes FormatControlProfil sont identiques");
    } else {
      LOGGER.warn("MIGRATION_FORMAT_CONTROL_PROFIL -- ATTENTION: Les listes FormatControlProfil sont différentes ");
    }

  }
}
