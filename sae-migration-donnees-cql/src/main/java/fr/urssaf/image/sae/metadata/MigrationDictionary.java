/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IDictionaryDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;

/**
 * (AC75095351) Classe de migration Dictionary Thrift<-> Cql
 */
@Component
public class MigrationDictionary implements IMigrationR {

  @Autowired
  private IDictionaryDaoCql dictionaryDaoCql;

  @Autowired
  private DictionarySupport dictionarySupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationDictionary.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public Diff migrationFromThriftToCql(final Javers javers) {

    MigrationDictionary.LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- start ");

    final List<Dictionary> dictionarysThrift = dictionarySupport.findAll();

    if (!dictionarysThrift.isEmpty()) {
      dictionaryDaoCql.saveAll(dictionarysThrift);
    }
    final List<Dictionary> dictionarysCql = new ArrayList<>();
    final Iterator<Dictionary> dictionarysIterator = dictionaryDaoCql.findAllWithMapper();
    dictionarysIterator.forEachRemaining(dictionarysCql::add);
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- end ");
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- nbThrift= {}", dictionarysThrift.size());
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- nbCql= {} ", dictionarysCql.size());
    final Diff diff = compareDictionarys(dictionarysThrift, dictionarysCql, javers);
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift(final Javers javers) {

    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromCqlTothrift- start ");


    final Iterator<Dictionary> dictionarysIterator = dictionaryDaoCql.findAllWithMapper();
    final List<Dictionary> dictionarysCql = new ArrayList<>();
    while (dictionarysIterator.hasNext()) {
      final Dictionary dictionary = dictionarysIterator.next();
      dictionarysCql.add(dictionary);
      for (final String element : dictionary.getEntries()) {
        dictionarySupport.addElement(dictionary.getIdentifiant(), element, new Date().getTime());
      }
    }
    final List<Dictionary> dictionarysThrift = dictionarySupport.findAll();
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromCqlTothrift- end ");
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromCqlTothrift- nbThrift= {}", dictionarysThrift.size());
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromCqlTothrift- nbCql= {} ", dictionarysCql.size());
    final Diff diff = compareDictionarys(dictionarysThrift, dictionarysCql, javers);
    return diff;
  }

  /**
   * Logs:Comparaison des listes en taille et en contenu
   * 
   * @param dictionarysThrift
   * @param dictionarysCql
   */
  public Diff compareDictionarys(final List<Dictionary> dictionarysThrift, final List<Dictionary> dictionarysCql, final Javers javers) {
    Collections.sort(dictionarysThrift);
    Collections.sort(dictionarysCql);

    final Diff diff = javers.compareCollections(dictionarysThrift, dictionarysCql, Dictionary.class);
    return diff;
  }
}
