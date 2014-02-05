/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.droit.dao.PagmfDao;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;
import fr.urssaf.image.sae.droit.utils.Constantes;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Classe de support permettant d'exploiter le bean et la DAO {@link PagmfDao}
 * du {@link Pagmf}.
 * 
 */
@Component
public class PagmfSupport {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(PagmfSupport.class);

   private final PagmfDao pagmfDao;
   private static final int MAX_FIND_RESULT = 2000; // limite des résultats des

   // recherches

   /**
    * constructeur
    * 
    * @param pagmfDao
    *           DAO associée au pagmf
    */
   @Autowired
   public PagmfSupport(PagmfDao pagmfDao) {
      this.pagmfDao = pagmfDao;
   }

   /**
    * Création d'un nouveau Pagmf
    * 
    * @param pagmf
    *           PAGMF à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(Pagmf pagmf, long clock) {
      String opePagmfPrefix = "ajouterPagmf";

      LOGGER.debug(DEBUT_LOG, opePagmfPrefix);
      LOGGER.debug("{} - Identifiant du PAGMF : {}", new String[] {
            opePagmfPrefix, pagmf.getCodePagmf() });

      ColumnFamilyUpdater<String, String> updater = pagmfDao.getCfTmpl()
            .createUpdater(pagmf.getCodePagmf());

      try {
         pagmfDao.addPagmf(updater, pagmf, clock);
      } catch (FormatControlProfilNotFoundException except) {
         throw new DroitRuntimeException(except);
      }

      pagmfDao.getCfTmpl().update(updater);

      LOGGER.info("{} - Ajout du PAGMF : {}", new String[] { opePagmfPrefix,
            pagmf.getCodePagmf() });
      LOGGER.debug(FIN_LOG, opePagmfPrefix);

   }

   /**
    * Méthode de suppression d'un {@link Pagmf}
    * 
    * @param code
    *           identifiant du PAGMa à supprimer - paramètre obligatoire
    * @param clock
    *           horloge de suppression - paramètre obligatoire
    */
   public final void delete(String code, Long clock) {

      if (StringUtils.isBlank(code))
         throw new IllegalArgumentException(ResourceMessagesUtils
               .loadMessage("erreur.pagmf.notnull"));

      if (clock == null || clock <= 0)
         throw new IllegalArgumentException(ResourceMessagesUtils
               .loadMessage("erreur.param"));

      Pagmf pagmf = find(code);

      if (pagmf == null) // le Pagmf renseigné n'existe pas en base
         throw new PagmfNotFoundException(ResourceMessagesUtils.loadMessage(
               "erreur.pagmf.delete", code));

      try {
         Mutator<String> mutator = pagmfDao.createMutator();
         pagmfDao.deletePagmf(mutator, code, clock);
         mutator.execute();
      } catch (Exception except) {
         throw new DroitRuntimeException(ResourceMessagesUtils
               .loadMessage("erreur.impossible.delete.pagmf"), except);
      }
   }

   
   /**
    * Méthode de suppression d'une ligne avec Murator en paramètre
    * 
    * @param code
    *           identifiant du PAGMf
    * @param clock
    *           horloge de suppression
    * @param mutator
    *           Mutator
    */
   public final void delete(String code, long clock, Mutator<String> mutator) {
      pagmfDao.mutatorSuppressionLigne(mutator, code, clock);
   }
   
   /**
    * Récupération des informations associées à un {@link Pagmf}
    * 
    * @param code
    *           identifiant du Pagmf
    * @return un Pagmf contenant les informations associées.
    */
   public final Pagmf find(String code) {

      ColumnFamilyResult<String, String> result = pagmfDao.getCfTmpl()
            .queryColumns(code);

      Pagmf pagmf = getPagmfFromResult(result);

      return pagmf;

   }

   private Pagmf getPagmfFromResult(ColumnFamilyResult<String, String> result) {

      Pagmf pagmf = null;

      if (result != null && result.hasResults()) {
         pagmf = new Pagmf();

         pagmf.setCodePagmf(result.getKey());

         pagmf.setDescription(result.getString(Constantes.COL_DESCRIPTION));

         pagmf.setCodeFormatControlProfil(result
               .getString(Constantes.COL_CODEFORMATCONTROLPROFIL));
      }

      return pagmf;
   } // Erreur levée quand le Pagmf demandé n’existe pas

   /**
    * Récupération de tous les {@link Pagmf} de la famille de colonne
    * DroitPagmf.
    * 
    * 
    * @return Une liste d’objet {@link Pagmf} représentant le contenu de CF
    *         DroitPagmf.
    */
   public final List<Pagmf> findAll() {

      try {
         BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

         RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
               .createRangeSlicesQuery(pagmfDao.getKeyspace(), StringSerializer
                     .get(), StringSerializer.get(), bytesSerializer);

         rangeSlicesQuery.setColumnFamily(pagmfDao.getColumnFamilyName());
         rangeSlicesQuery.setRange("", "", false, MAX_FIND_RESULT);
         QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
               .execute();

         // Convertion du résultat en ColumnFamilyResultWrapper pour mieux
         // l'utiliser
         QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<String, String, byte[]>();
         ColumnFamilyResultWrapper<String, String> result = converter
               .getColumnFamilyResultWrapper(queryResult, StringSerializer
                     .get(), StringSerializer.get(), bytesSerializer);

         HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
               result);

         List<Pagmf> list = new ArrayList<Pagmf>();
         for (ColumnFamilyResult<String, String> row : resultIterator) {
            if (row != null && row.hasResults()) {

               Pagmf pagmf = getPagmfFromResult(row);
               list.add(pagmf);
            }
         }

         return list;
      } catch (Exception except) {
         throw new DroitRuntimeException(ResourceMessagesUtils
               .loadMessage("erreur.impossible.recup.info"), except);
      }
   }

}
