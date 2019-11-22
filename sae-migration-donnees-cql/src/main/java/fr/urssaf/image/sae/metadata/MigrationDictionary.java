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

import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IDictionaryDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Dictionary Thrift<-> Cql
 */
@Component
public class MigrationDictionary implements IMigration {

  @Autowired
  private IDictionaryDaoCql dictionaryDaoCql;

  @Autowired
  private DictionarySupport dictionarySupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationDictionary.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- start ");

    final List<Dictionary> dictionarysThrift = dictionarySupport.findAll();

    if (!dictionarysThrift.isEmpty()) {
      dictionaryDaoCql.saveAll(dictionarysThrift);
    }
    final List<Dictionary> dictionarysCql = new ArrayList<>();
    final Iterator<Dictionary> dictionarysIterator = dictionaryDaoCql.findAllWithMapper();
    dictionarysIterator.forEachRemaining(dictionarysCql::add);
    compareDictionarys(dictionarysThrift, dictionarysCql);
    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

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
    compareDictionarys(dictionarysThrift, dictionarysCql);

    LOGGER.info(" MIGRATION_DICTIONARY - migrationFromCqlTothrift- end ");
  }

  /**
   * Comparaison des liste en taille et en contenu
   * 
   * @param dictionarysThrift
   * @param dictionarysCql
   */
  private void compareDictionarys(final List<Dictionary> dictionarysThrift, final List<Dictionary> dictionarysCql) {

    LOGGER.info("MIGRATION_DICTIONARY -- SizeThriftdictionary=" + dictionarysThrift.size());
    LOGGER.info("MIGRATION_DICTIONARY -- SizeCqldictionary=" + dictionarysCql.size());
    if (CompareUtils.compareListsGeneric(dictionarysThrift, dictionarysCql)) {
      LOGGER.info("MIGRATION_DICTIONARY -- Les listes dictionary sont identiques");
    } else {
      LOGGER.warn("MIGRATION_DICTIONARY -- ATTENTION: Les listes dictionary sont différentes ");
    }
  }
}
