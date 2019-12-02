/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.format;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.format.referentiel.dao.cql.IReferentielFormatDaoCql;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration ReferentielFormat Thrift<-> Cql
 */
@Component
public class MigrationReferentielFormat implements IMigration {

  @Autowired
  private IReferentielFormatDaoCql referentielFormatDaoCql;

  @Autowired
  private ReferentielFormatSupport formatSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationReferentielFormat.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromThriftToCql- start ");

    final List<FormatFichier> formatFichiersThrift = formatSupport.findAll();

    if (!formatFichiersThrift.isEmpty()) {
      referentielFormatDaoCql.saveAll(formatFichiersThrift);
    }
    final List<FormatFichier> formatFichiersCql = new ArrayList<>();
    final Iterator<FormatFichier> formatFichiersIterator = referentielFormatDaoCql.findAllWithMapper();
    formatFichiersIterator.forEachRemaining(formatFichiersCql::add);
    compareformatFichiers(formatFichiersThrift, formatFichiersCql);
    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromCqlTothrift- start ");


    final Iterator<FormatFichier> formatFichiersIterator = referentielFormatDaoCql.findAllWithMapper();
    final List<FormatFichier> formatFichiersCql = new ArrayList<>();

    while (formatFichiersIterator.hasNext()) {
      final FormatFichier formatFichier = formatFichiersIterator.next();
      formatFichiersCql.add(formatFichier);
      formatSupport.create(formatFichier, new Date().getTime());
    }
    final List<FormatFichier> formatFichiersThrift = formatSupport.findAll();
    compareformatFichiers(formatFichiersThrift, formatFichiersCql);

    LOGGER.info(" MIGRATION_REFERENTIEL_FORMAT - migrationFromCqlTothrift- end ");
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param formatFichiersThrift
   * @param formatFichiersCql
   */
  private void compareformatFichiers(final List<FormatFichier> formatFichiersThrift, final List<FormatFichier> formatFichiersCql) {

    LOGGER.info("MIGRATION_REFERENTIEL_FORMAT -- SizeThriftformatFichier=" + formatFichiersThrift.size());
    LOGGER.info("MIGRATION_REFERENTIEL_FORMAT -- SizeCqlformatFichier=" + formatFichiersCql.size());
    if (CompareUtils.compareListsGeneric(formatFichiersThrift, formatFichiersCql)) {
      LOGGER.info("MIGRATION_REFERENTIEL_FORMAT -- Les listes formatFichier sont identiques");
    } else {
      LOGGER.warn("MIGRATION_REFERENTIEL_FORMAT -- ATTENTION: Les listes formatFichier sont différentes ");
    }
  }
}
