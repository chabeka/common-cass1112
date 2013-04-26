package fr.urssaf.image.sae.rnd.dao.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.rnd.dao.CorrespondancesDao;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;

/**
 * Support permettant d'effectuer les opérations d'écriture sur la CF
 * CorrespondancesRnd
 * 
 * 
 */
@Component
public class CorrespondancesRndSupport {

   private final CorrespondancesDao correspondancesDao;

   /**
    * Constructeur
    * 
    * @param correspondancesDao
    *           DAO d'accès à la CF CorrespondancesRnd
    */
   @Autowired
   public CorrespondancesRndSupport(CorrespondancesDao correspondancesDao) {
      this.correspondancesDao = correspondancesDao;
   }

   private static final int MAX_FIND_RESULT = 5000;

   /**
    * Création d'une ligne dans la CF CorrespondancesRnd
    * 
    * @param correspondance
    *           Correspondance à ajouter
    * @param clock
    *           Horloge de la création
    */
   public final void ajouterCorrespondance(Correspondance correspondance,
         long clock) {

      ColumnFamilyUpdater<String, String> updater = correspondancesDao
            .getCfTmpl().createUpdater(correspondance.getCodeTemporaire());

      correspondancesDao.ecritCodeDefinitif(correspondance.getCodeDefinitif(),
            updater, clock);

      if (correspondance.getDateDebutMaj() != null) {
         correspondancesDao.ecritDateDebutMaj(correspondance.getDateDebutMaj(),
               updater, clock);
      }
      if (correspondance.getDateFinMaj() != null) {
         correspondancesDao.ecritDateFinMaj(correspondance.getDateFinMaj(),
               updater, clock);
      }
      if (correspondance.getEtat() != null) {
         correspondancesDao.ecritEtat(correspondance.getEtat().toString(),
               updater, clock);
      }

      correspondancesDao.getCfTmpl().update(updater);
   }

   /**
    * Renvoie toutes les correspondances entre code temporaire et code définitif
    * 
    * @return Une liste de {@link Correspondance}
    */
   public final List<Correspondance> getAllCorrespondances() {

      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(correspondancesDao.getKeyspace(),
                  StringSerializer.get(), StringSerializer.get(),
                  bytesSerializer);
      rangeSlicesQuery
            .setColumnFamily(correspondancesDao.getColumnFamilyName());
      rangeSlicesQuery.setRange(StringUtils.EMPTY, StringUtils.EMPTY, false,
            MAX_FIND_RESULT);
      QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
            .execute();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<String, String, byte[]>();
      ColumnFamilyResultWrapper<String, String> result = converter
            .getColumnFamilyResultWrapper(queryResult, StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);

      // On itère sur le résultat
      HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
            result);

      List<Correspondance> listeCorrespondances = new ArrayList<Correspondance>();

      for (ColumnFamilyResult<String, String> row : resultIterator) {

         Correspondance correspondance = new Correspondance();
         correspondance.setCodeTemporaire(row.getKey());
         correspondance.setCodeDefinitif(row
               .getString(CorrespondancesDao.CORR_CODE_DEFINITIF));
         correspondance.setDateDebutMaj(row
               .getDate(CorrespondancesDao.CORR_DATE_DEBUT_MAJ));
         correspondance.setDateFinMaj(row
               .getDate(CorrespondancesDao.CORR_DATE_FIN_MAJ));
         correspondance.setEtat(EtatCorrespondance.valueOf(row
               .getString(CorrespondancesDao.CORR_ETAT)));

         listeCorrespondances.add(correspondance);
      }
      return listeCorrespondances;
   }

   /**
    * Méthode permettant de récupérer les informations pour une correspondance
    * spÃ©cifique
    * 
    * @param codeTemporaire
    *           Code temporaire de la correspondance
    * @return {@link Correspondance}
    */
   public final Correspondance find(String codeTemporaire) {

      ColumnFamilyResult<String, String> result = correspondancesDao
            .getCfTmpl().queryColumns(codeTemporaire);

      if (result != null && result.hasResults()) {
         Correspondance correspondance = new Correspondance();
         correspondance.setCodeTemporaire(result.getKey());
         correspondance.setCodeDefinitif(result
               .getString(CorrespondancesDao.CORR_CODE_DEFINITIF));
         correspondance.setDateDebutMaj(result
               .getDate(CorrespondancesDao.CORR_DATE_DEBUT_MAJ));
         correspondance.setDateFinMaj(result
               .getDate(CorrespondancesDao.CORR_DATE_FIN_MAJ));
         if (result.getString(CorrespondancesDao.CORR_ETAT) != null) {
            correspondance.setEtat(EtatCorrespondance.valueOf(result
                  .getString(CorrespondancesDao.CORR_ETAT)));
         }

         return correspondance;
      }

      return null;
   }
}
