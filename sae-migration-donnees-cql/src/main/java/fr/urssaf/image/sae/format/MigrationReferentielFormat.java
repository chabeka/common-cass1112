/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.format;

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
import fr.urssaf.image.sae.format.referentiel.dao.cql.IReferentielFormatDaoCql;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * (AC75095351) Classe de migration ReferentielFormat Thrift<-> Cql
 */
@Component
public class MigrationReferentielFormat implements IMigrationR {

  @Autowired
  private IReferentielFormatDaoCql referentielFormatDaoCql;

  @Autowired
  private ReferentielFormatSupport formatSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationReferentielFormat.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public Diff migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromThriftToCql- start ");

    final List<FormatFichier> formatFichiersThrift = formatSupport.findAll();

    if (!formatFichiersThrift.isEmpty()) {
      referentielFormatDaoCql.saveAll(formatFichiersThrift);
    }
    final List<FormatFichier> formatFichiersCql = new ArrayList<>();
    final Iterator<FormatFichier> formatFichiersIterator = referentielFormatDaoCql.findAllWithMapper();
    formatFichiersIterator.forEachRemaining(formatFichiersCql::add);
    final Diff diff = compareformatFichiers(formatFichiersThrift, formatFichiersCql);
    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromThriftToCql- end ");
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromCqlTothrift- start ");


    final Iterator<FormatFichier> formatFichiersIterator = referentielFormatDaoCql.findAllWithMapper();
    final List<FormatFichier> formatFichiersCql = new ArrayList<>();

    while (formatFichiersIterator.hasNext()) {
      final FormatFichier formatFichier = formatFichiersIterator.next();
      formatFichiersCql.add(formatFichier);
      formatSupport.create(formatFichier, new Date().getTime());
    }
    final List<FormatFichier> formatFichiersThrift = formatSupport.findAll();
    final Diff diff = compareformatFichiers(formatFichiersThrift, formatFichiersCql);

    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromCqlTothrift- end ");
    return diff;
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param formatFichiersThrift
   * @param formatFichiersCql
   */
  private Diff compareformatFichiers(final List<FormatFichier> formatFichiersThrift, final List<FormatFichier> formatFichiersCql) {

    Collections.sort(formatFichiersThrift);
    Collections.sort(formatFichiersCql);
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
        .build();
    final Diff diff = javers.compareCollections(formatFichiersThrift, formatFichiersCql, FormatFichier.class);
    return diff;
  }

}
