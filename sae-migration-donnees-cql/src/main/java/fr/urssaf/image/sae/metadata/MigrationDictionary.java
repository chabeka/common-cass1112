/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IDictionaryDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;
import fr.urssaf.image.sae.utils.CompareUtils;

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
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- start ");

    final List<Dictionary> dictionarysThrift = dictionarySupport.findAll();

    if (!dictionarysThrift.isEmpty()) {
      dictionaryDaoCql.saveAll(dictionarysThrift);
    }
    final List<Dictionary> dictionarysCql = new ArrayList<>();
    final Iterator<Dictionary> dictionarysIterator = dictionaryDaoCql.findAllWithMapper();
    dictionarysIterator.forEachRemaining(dictionarysCql::add);
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- end ");
    return compareDictionarys(dictionarysThrift, dictionarysCql);
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

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
    return compareDictionarys(dictionarysThrift, dictionarysCql);
  }

  /**
   * Logs:Comparaison des listes en taille et en contenu
   * 
   * @param dictionarysThrift
   * @param dictionarysCql
   */
  private boolean compareDictionarys(final List<Dictionary> dictionarysThrift, final List<Dictionary> dictionarysCql) {

    final boolean result = CompareUtils.compareListsGeneric(dictionarysThrift, dictionarysCql);
    if (result) {
      LOGGER.info("MIGRATION_DICTIONARY -- Les listes dictionary sont identiques");
    } else {
      LOGGER.info("MIGRATION_DICTIONARY -- NbThrift=" + dictionarysThrift.size());
      LOGGER.info("MIGRATION_DICTIONARY -- NbCql=" + dictionarysCql.size());
      LOGGER.warn("MIGRATION_DICTIONARY -- ATTENTION: Les listes dictionary sont différentes ");
    }
    return result;
  }
}
