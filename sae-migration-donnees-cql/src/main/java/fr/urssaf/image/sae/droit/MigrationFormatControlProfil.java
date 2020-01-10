package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IFormatControlProfilDaoCql;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.model.FormatControlProfilM;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration pour formatControlProfil
 */
@Component
public class MigrationFormatControlProfil implements IMigrationR {

  @Autowired
  private IFormatControlProfilDaoCql formatcontrolprofildaocql;

  @Autowired
  private FormatControlProfilSupport formatControlProfilSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationFormatControlProfil.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromThriftToCql- start ");

    final List<FormatControlProfil> formatControlProfilsThrift = formatControlProfilSupport.findAll();
    if (!formatControlProfilsThrift.isEmpty()) {
      formatcontrolprofildaocql.saveAll(formatControlProfilsThrift);
    }
    final List<FormatControlProfil> formatControlProfilsCql = new ArrayList<>();
    final Iterator<FormatControlProfil> formatControlProfilsIterator = formatcontrolprofildaocql.findAllWithMapper();
    formatControlProfilsIterator.forEachRemaining(formatControlProfilsCql::add);
    final boolean result = compareFormatControlProfil(formatControlProfilsThrift, formatControlProfilsCql);
    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromThriftToCql- end ");
    return result;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromCqlTothrift- start ");

    final Iterator<FormatControlProfil> formatControlProfils = formatcontrolprofildaocql.findAllWithMapper();
    final List<FormatControlProfil> formatControlProfilsCql = new ArrayList<>();
    while (formatControlProfils.hasNext()) {
      final FormatControlProfil formatControlProfil = formatControlProfils.next();
      formatControlProfilsCql.add(formatControlProfil);
      formatControlProfilSupport.create(formatControlProfil, new Date().getTime());
    }
    final List<FormatControlProfil> formatControlProfilThrift = formatControlProfilSupport.findAll();
    final boolean result = compareFormatControlProfil(formatControlProfilThrift, formatControlProfilsCql);
    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromCqlTothrift- end ");
    return result;
  }

  /**
   * Logs: Comparer les listes de formatControlProfil
   * 
   * @param formatControlProfilsThrift
   * @param formatControlProfilsCql
   */
  public boolean compareFormatControlProfil(final List<FormatControlProfil> formatControlProfilsThrift,
                                            final List<FormatControlProfil> formatControlProfilsCql) {

    final List<FormatControlProfilM> formatControlProfilsThriftM = convertList(formatControlProfilsThrift);
    final List<FormatControlProfilM> formatControlProfilsCqlM = convertList(formatControlProfilsCql);
    formatControlProfilsCqlM.get(0).setDescription("M");
    final boolean result = CompareUtils.compareListsGeneric(formatControlProfilsThriftM, formatControlProfilsCqlM);
    if (result) {
      LOGGER.info("MIGRATION_FORMAT_CONTROL_PROFIL -- Les listes FormatControlProfil sont identiques");
    } else {
      LOGGER.info("MIGRATION_FORMAT_CONTROL_PROFIL -- NbThrift=" + formatControlProfilsThrift.size());
      LOGGER.info("MIGRATION_FORMAT_CONTROL_PROFIL -- NbCql=" + formatControlProfilsCql.size());
      LOGGER.warn("MIGRATION_FORMAT_CONTROL_PROFIL -- ATTENTION: Les listes FormatControlProfil sont différentes ");
    }
    return result;

  }

  private List<FormatControlProfilM> convertList(final List<FormatControlProfil> formatControlProfils) {


    final List<FormatControlProfilM> formatControlProfilsM = new ArrayList<>();
    for (final FormatControlProfil formatControlProfil : formatControlProfils) {
      final FormatControlProfilM formatControlProfilM = new FormatControlProfilM(formatControlProfil);
      formatControlProfilsM.add(formatControlProfilM);
    }
    return formatControlProfilsM;
  }
}
