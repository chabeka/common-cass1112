/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.rnd;

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
import fr.urssaf.image.sae.rnd.dao.cql.IRndDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

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
  public Diff migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_RND - migrationFromThriftToCql- start ");

    final List<TypeDocument> typeDocumentsThrift = rndSupport.findAll();

    if (!typeDocumentsThrift.isEmpty()) {
      rndDaoCql.saveAll(typeDocumentsThrift);
    }
    final List<TypeDocument> typeDocumentsCql = new ArrayList<>();
    final Iterator<TypeDocument> rndsIterator = rndDaoCql.findAllWithMapper();
    rndsIterator.forEachRemaining(typeDocumentsCql::add);
    final Diff diff = compareRnds(typeDocumentsThrift, typeDocumentsCql);
    LOGGER.info(" MIGRATION_RND - migrationFromThriftToCql- end ");
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_RND - migrationFromCqlTothrift- start ");


    final Iterator<TypeDocument> typeDocumentsIterator = rndDaoCql.findAllWithMapper();
    final List<TypeDocument> rndsCql = new ArrayList<>();
    while (typeDocumentsIterator.hasNext()) {
      final TypeDocument typeDocument = typeDocumentsIterator.next();
      rndsCql.add(typeDocument);
      rndSupport.ajouterRnd(typeDocument, new Date().getTime());
    }
    final List<TypeDocument> rndsThrift = rndSupport.findAll();

    final Diff diff = compareRnds(rndsThrift, rndsCql);
    LOGGER.info(" MIGRATION_RND - migrationFromCqlTothrift- end ");
    return diff;
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param typeDocumentsThrift
   * @param typeDocumentsCql
   */
  public Diff compareRnds(final List<TypeDocument> typeDocumentsThrift, final List<TypeDocument> typeDocumentsCql) {

    Collections.sort(typeDocumentsThrift);
    Collections.sort(typeDocumentsCql);
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE) 
        .build();
    final Diff diff = javers.compareCollections(typeDocumentsThrift, typeDocumentsCql, TypeDocument.class);
    return diff;
  }

}
