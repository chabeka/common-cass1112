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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.droit.dao.ContratServiceDao;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.serializer.ListSerializer;

/**
 * Classe de support de la classe {@link ContratServiceDao}
 * 
 */
@Component
public class ContratServiceSupport {

   private final ContratServiceDao dao;

   /**
    * constructeur
    * @param csDao DAO associée au Constrat de service
    */
   @Autowired
   public ContratServiceSupport(ContratServiceDao csDao){
      this.dao = csDao;
   }

   
   /**
    * Méthode de création d'une ligne
    * 
    * @param contratService
    *           propriétés du contrat de service à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(ServiceContract contratService, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<String, String> updaterJobRequest = dao
            .getContratServiceTmpl().createUpdater(
                  contratService.getCodeClient());

      // écriture des colonnes
      dao.ecritLibelle(updaterJobRequest, contratService.getLibelle(), clock);
      dao.ecritDescription(updaterJobRequest, contratService.getDescription(),
            clock);
      dao.ecritViDuree(updaterJobRequest, contratService.getViDuree(), clock);

      if (StringUtils.isNotEmpty(contratService.getIdPki())) {
         dao.ecritIdPki(updaterJobRequest, contratService.getIdPki(), clock);
      }

      if (contratService.getIdCertifClient() != null) {
         dao.ecritCert(updaterJobRequest, contratService.getIdCertifClient(),
               clock);
      }

      if (CollectionUtils.isNotEmpty(contratService.getListCertifsClient())) {
         dao.ecritListeCert(updaterJobRequest, contratService
               .getListCertifsClient(), clock);
      }

      if (CollectionUtils.isNotEmpty(contratService.getListPki())) {
         dao.ecritListePki(updaterJobRequest, contratService.getListPki(),
               clock);
      }

      dao.ecritFlagControlNommage(updaterJobRequest, contratService
            .isVerifNommage(), clock);

      // écriture en base
      dao.getContratServiceTmpl().update(updaterJobRequest);

   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param libelle
    *           identifiant du contrat de service
    * @param clock
    *           horloge de la suppression
    */
   public final void delete(String libelle, long clock) {
      // Création du Mutator
      Mutator<String> mutator = dao.createMutator();

      // suppression
      dao.mutatorSuppressionContratService(mutator, libelle, clock);

      // Execution de la commande
      mutator.execute();
   }

   /**
    * Lecture d'une ligne
    * 
    * @param libelle
    *           identifiant du contrat de service
    * @return le contrat de service correspondant à l'identifiant fournit
    */
   public final ServiceContract find(String libelle) {

      ColumnFamilyResult<String, String> result = dao.getContratServiceTmpl()
            .queryColumns(libelle);

      ServiceContract contratService = getContratServiceFromResult(result);

      return contratService;
   }

   private ServiceContract getContratServiceFromResult(
         ColumnFamilyResult<String, String> result) {

      ServiceContract contract = null;

      if (result != null && result.hasResults()) {
         contract = new ServiceContract();
         contract.setCodeClient(result.getKey());
         contract.setLibelle(result.getString(ContratServiceDao.CS_LIBELLE));
         contract.setDescription(result
               .getString(ContratServiceDao.CS_DESCRIPTION));
         contract.setViDuree(result.getLong(ContratServiceDao.CS_VI_DUREE));

         if (result.getString(ContratServiceDao.CS_PKI) != null) {
            contract.setIdPki(result.getString(ContratServiceDao.CS_PKI));
         }

         if (result.getString(ContratServiceDao.CS_CERT) != null) {
            contract.setIdCertifClient(result
                  .getString(ContratServiceDao.CS_CERT));
         }

         if (result.getByteArray(ContratServiceDao.CS_LISTE_CERT) != null) {
            byte[] bytes = result.getByteArray(ContratServiceDao.CS_LISTE_CERT);
            contract
                  .setListCertifsClient(ListSerializer.get().fromBytes(bytes));
         }

         if (result.getByteArray(ContratServiceDao.CS_LISTE_PKI) != null) {
            byte[] bytes = result.getByteArray(ContratServiceDao.CS_LISTE_PKI);
            contract.setListPki(ListSerializer.get().fromBytes(bytes));
         }

         if (result.getBoolean(ContratServiceDao.CS_VERIF_NOMMAGE) != null) {
            contract.setVerifNommage(result
                  .getBoolean(ContratServiceDao.CS_VERIF_NOMMAGE));
         }
      }

      return contract;
   }

   /**
    * Lecture de toutes les lignes (attention aux performances)
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à récupérer
    * @return la liste de toutes les contrats de service
    */
   public final List<ServiceContract> findAll(int maxKeysToRead) {
      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(ContratServiceDao.CS_CFNAME);
      rangeSlicesQuery.setRange("", "", false,
            ContratServiceDao.MAX_CS_ATTIBUTS);
      rangeSlicesQuery.setRowCount(maxKeysToRead);
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
      List<ServiceContract> list = new ArrayList<ServiceContract>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         ServiceContract contract = getContratServiceFromResult(row);

         if (contract != null)
            list.add(contract);
      }
      return list;
   }

}
