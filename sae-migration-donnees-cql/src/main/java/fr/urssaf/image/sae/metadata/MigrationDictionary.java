/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.referential.dao.cql.IDictionaryDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;

/**
 * (AC75095351) Classe de migration Dictionary Thrift<-> Cql
 */
@Component
public class MigrationDictionary {

  @Autowired
  private IDictionaryDaoCql dictionaryDaoCql;

  @Autowired
  private DictionarySupport dictionarySupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationDictionary.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationDictionary - migrationFromThriftToCql- start ");

    final List<Dictionary> dictionarys = dictionarySupport.findAll();

    if (!dictionarys.isEmpty()) {
      dictionaryDaoCql.saveAll(dictionarys);
    }

    LOGGER.info(" MigrationDictionary - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationDictionary - migrationFromCqlTothrift- start ");


    final Iterator<Dictionary> dictionarys = dictionaryDaoCql.findAllWithMapper();
    while (dictionarys.hasNext()) {
      final Dictionary dictionary = dictionarys.next();
      for (final String element : dictionary.getEntries()) {
        dictionarySupport.addElement(dictionary.getIdentifiant(), element, new Date().getTime());
      }
    }


    LOGGER.info(" MigrationDictionary - migrationFromCqlTothrift- end ");
  }
}
