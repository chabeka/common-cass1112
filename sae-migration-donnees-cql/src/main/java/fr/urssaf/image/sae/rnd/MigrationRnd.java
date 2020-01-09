/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.rnd;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.rnd.dao.cql.IRndDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Rnd Thrift<-> Cql
 */
@Component
public class MigrationRnd implements IMigrationR {

  @Autowired
  private IRndDaoCql rndDaoCql;

  @Autowired
  private RndSupport rndSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationRnd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_RND - migrationFromThriftToCql- start ");

    final List<TypeDocument> typeDocumentsThrift = rndSupport.findAll();

    if (!typeDocumentsThrift.isEmpty()) {
      rndDaoCql.saveAll(typeDocumentsThrift);
    }
    final List<TypeDocument> typeDocumentsCql = new ArrayList<>();
    final Iterator<TypeDocument> rndsIterator = rndDaoCql.findAllWithMapper();
    rndsIterator.forEachRemaining(typeDocumentsCql::add);
    LOGGER.info(" MIGRATION_RND - migrationFromThriftToCql- end ");
    return compareRnds(typeDocumentsThrift, typeDocumentsCql);
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_RND - migrationFromCqlTothrift- start ");


    final Iterator<TypeDocument> typeDocumentsIterator = rndDaoCql.findAllWithMapper();
    final List<TypeDocument> rndsCql = new ArrayList<>();
    while (typeDocumentsIterator.hasNext()) {
      final TypeDocument typeDocument = typeDocumentsIterator.next();
      rndsCql.add(typeDocument);
      rndSupport.ajouterRnd(typeDocument, new Date().getTime());
    }
    final List<TypeDocument> rndsThrift = rndSupport.findAll();


    LOGGER.info(" MIGRATION_RND - migrationFromCqlTothrift- end ");
    return compareRnds(rndsThrift, rndsCql);
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param typeDocumentsThrift
   * @param typeDocumentsCql
   */
  private boolean compareRnds(final List<TypeDocument> typeDocumentsThrift, final List<TypeDocument> typeDocumentsCql) {

    final boolean result = CompareUtils.compareListsGeneric(typeDocumentsThrift, typeDocumentsCql);
    if (result) {
      LOGGER.info("MIGRATION_RND -- Les listes rnd sont identiques");
    } else {
      LOGGER.info("MIGRATION_RND -- NbThrift=" + typeDocumentsThrift.size());
      LOGGER.info("MIGRATION_RND -- NbCql=" + typeDocumentsCql.size());
      LOGGER.warn("MIGRATION_RND -- ATTENTION: Les listes rnd sont différentes ");
    }
    return result;
  }

}
