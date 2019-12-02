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

import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.rnd.dao.cql.IRndDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Rnd Thrift<-> Cql
 */
@Component
public class MigrationRnd implements IMigration {

  @Autowired
  private IRndDaoCql rndDaoCql;

  @Autowired
  private RndSupport rndSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationRnd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_RND - migrationFromThriftToCql- start ");

    final List<TypeDocument> typeDocumentsThrift = rndSupport.findAll();

    if (!typeDocumentsThrift.isEmpty()) {
      rndDaoCql.saveAll(typeDocumentsThrift);
    }
    final List<TypeDocument> typeDocumentsCql = new ArrayList<>();
    final Iterator<TypeDocument> rndsIterator = rndDaoCql.findAllWithMapper();
    rndsIterator.forEachRemaining(typeDocumentsCql::add);
    compareRnds(typeDocumentsThrift, typeDocumentsCql);
    LOGGER.info(" MIGRATION_RND - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_RND - migrationFromCqlTothrift- start ");


    final Iterator<TypeDocument> typeDocumentsIterator = rndDaoCql.findAllWithMapper();
    final List<TypeDocument> rndsCql = new ArrayList<>();
    while (typeDocumentsIterator.hasNext()) {
      final TypeDocument typeDocument = typeDocumentsIterator.next();
      rndsCql.add(typeDocument);
      rndSupport.ajouterRnd(typeDocument, new Date().getTime());
    }
    final List<TypeDocument> rndsThrift = rndSupport.findAll();
    compareRnds(rndsThrift, rndsCql);

    LOGGER.info(" MIGRATION_RND - migrationFromCqlTothrift- end ");
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param typeDocumentsThrift
   * @param typeDocumentsCql
   */
  private void compareRnds(final List<TypeDocument> typeDocumentsThrift, final List<TypeDocument> typeDocumentsCql) {

    LOGGER.info("MIGRATION_RND -- SizeThriftrnd=" + typeDocumentsThrift.size());
    LOGGER.info("MIGRATION_RND -- SizeCqlrnd=" + typeDocumentsCql.size());
    if (CompareUtils.compareListsGeneric(typeDocumentsThrift, typeDocumentsCql)) {
      LOGGER.info("MIGRATION_RND -- Les listes rnd sont identiques");
    } else {
      LOGGER.warn("MIGRATION_RND -- ATTENTION: Les listes rnd sont différentes ");
    }
  }

}
