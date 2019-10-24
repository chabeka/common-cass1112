/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.format;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.format.referentiel.dao.cql.IReferentielFormatDaoCql;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * (AC75095351) Classe de migration ReferentielFormat Thrift<-> Cql
 */
@Component
public class MigrationReferentielFormat {

  @Autowired
  private IReferentielFormatDaoCql referentielFormatDaoCql;

  @Autowired
  private ReferentielFormatSupport formatSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationReferentielFormat.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationReferentielFormat - migrationFromThriftToCql- start ");

    final List<FormatFichier> formatFichiers = formatSupport.findAll();

    if (!formatFichiers.isEmpty()) {
      referentielFormatDaoCql.saveAll(formatFichiers);
    }

    LOGGER.info(" MigrationReferentielFormat - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationReferentielFormat - migrationFromCqlTothrift- start ");


    final Iterator<FormatFichier> formatFichiers = referentielFormatDaoCql.findAllWithMapper();

    while (formatFichiers.hasNext()) {
      final FormatFichier formatFichier = formatFichiers.next();
      formatSupport.create(formatFichier, new Date().getTime());
    }


    LOGGER.info(" MigrationReferentielFormat - migrationFromCqlTothrift- end ");
  }
}
