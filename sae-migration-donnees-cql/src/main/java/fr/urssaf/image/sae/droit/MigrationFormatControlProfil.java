package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IFormatControlProfilDaoCql;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;

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
  public Diff migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromThriftToCql- start ");

    final List<FormatControlProfil> formatControlProfilsThrift = formatControlProfilSupport.findAll();
    if (!formatControlProfilsThrift.isEmpty()) {
      formatcontrolprofildaocql.saveAll(formatControlProfilsThrift);
    }
    final List<FormatControlProfil> formatControlProfilsCql = new ArrayList<>();
    final Iterator<FormatControlProfil> formatControlProfilsIterator = formatcontrolprofildaocql.findAllWithMapper();
    formatControlProfilsIterator.forEachRemaining(formatControlProfilsCql::add);
    final Diff diff = compareFormatControlProfil(formatControlProfilsThrift, formatControlProfilsCql);
    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromThriftToCql- end ");
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromCqlTothrift- start ");

    final Iterator<FormatControlProfil> formatControlProfils = formatcontrolprofildaocql.findAllWithMapper();
    final List<FormatControlProfil> formatControlProfilsCql = new ArrayList<>();
    while (formatControlProfils.hasNext()) {
      final FormatControlProfil formatControlProfil = formatControlProfils.next();
      formatControlProfilsCql.add(formatControlProfil);
      formatControlProfilSupport.create(formatControlProfil, new Date().getTime());
    }
    final List<FormatControlProfil> formatControlProfilThrift = formatControlProfilSupport.findAll();
    final Diff diff = compareFormatControlProfil(formatControlProfilThrift, formatControlProfilsCql);
    LOGGER.info(" MIGRATION_FORMAT_CONTROL_PROFIL - migrationFromCqlTothrift- end ");
    return diff;
  }

  /**
   * Logs: Comparer les listes de formatControlProfil
   * 
   * @param formatControlProfilsThrift
   * @param formatControlProfilsCql
   */
  public Diff compareFormatControlProfil(final List<FormatControlProfil> formatControlProfilsThrift,
                                         final List<FormatControlProfil> formatControlProfilsCql) {

    Collections.sort(formatControlProfilsThrift);
    Collections.sort(formatControlProfilsCql);
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
        .build();
    final Diff diff = javers.compareCollections(formatControlProfilsThrift, formatControlProfilsCql, FormatControlProfil.class);
    return diff;
  }

}
