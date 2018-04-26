package fr.urssaf.image.sae.pile.travaux.ihmweb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.zookeeper.ZookeeperClientFactory;
import fr.urssaf.image.sae.pile.travaux.ihmweb.exception.PileTravauxRuntimeException;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraEtZookeeperConfig;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.ConfigEtClients;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.JobHistory;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.JobRequest;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.JobState;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.droit.VIContenuExtrait;
import fr.urssaf.image.sae.pile.travaux.ihmweb.serializer.VISerializer;

@Service
public class PileTravauxService {

   // Column Family JobRequest
   private static final String JOBREQUEST_CFNAME = "JobRequest";
   private static final String JR_TYPE_COLUMN = "type";
   private static final String JR_PARAMETERS_COLUMN = "parameters";
   private static final String JR_STATE_COLUMN = "state";
   private static final String JR_RESERVED_BY_COLUMN = "reservedBy";
   private static final String JR_CREATION_DATE_COLUMN = "creationDate";
   private static final String JR_RESERVATION_DATE_COLUMN = "reservationDate";
   private static final String JR_STARTING_DATE_COLUMN = "startingDate";
   private static final String JR_ENDING_DATE_COLUMN = "endingDate";
   private static final String JR_MESSAGE = "message";
   private static final String JR_SAE_HOST = "saeHost";
   private static final String JR_CLIENT_HOST = "clientHost";
   private static final String JR_DOC_COUNT = "docCount";
   private static final String JR_PID = "pid";
   public static final String JR_TO_CHECK_FLAG = "toCheckFlag";
   public static final String JR_TO_CHECK_FLAG_RAISON = "toCheckFlagRaison";
   public static final String JR_VI = "vi";

   
   // Column Family JobHistory
   private static final String JOBHISTORY_CFNAME = "JobHistory";
   
   
   
   // Autres constantes
   private static final int MAX_JOB_ATTIBUTS = 100;

   private List<ConfigEtClients> clients = new ArrayList<ConfigEtClients>();

   /**
    * Constructeur de la DAO
    * 
    * @param keyspace
    *           Keyspace cassandra à utiliser
    */
   // @Autowired
   // public PileTravauxService(Keyspace keyspace) {
   // this.keyspace = keyspace;
   // }

   private final List<JobRequest> getAllJobsInternal(int maxKeysToRead,
         Keyspace keyspace) {

      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(keyspace, UUIDSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(JOBREQUEST_CFNAME);
      rangeSlicesQuery.setRange("", "", false, MAX_JOB_ATTIBUTS);
      rangeSlicesQuery.setRowCount(maxKeysToRead);
      QueryResult<OrderedRows<UUID, String, byte[]>> queryResult = rangeSlicesQuery
            .execute();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<UUID, String, byte[]>();
      ColumnFamilyResultWrapper<UUID, String> result = converter
            .getColumnFamilyResultWrapper(queryResult, UUIDSerializer.get(),
                  StringSerializer.get(), bytesSerializer);

      // On itère sur le résultat
      HectorIterator<UUID, String> resultIterator = new HectorIterator<UUID, String>(
            result);
      List<JobRequest> list = new ArrayList<JobRequest>();
      for (ColumnFamilyResult<UUID, String> row : resultIterator) {
         JobRequest jobRequest = getJobRequestFromResult(row);
         // On peut obtenir un jobRequest null dans le cas d'un jobRequest
         // effacé
         if (jobRequest != null)
            list.add(jobRequest);
      }
      return list;
   }

   /**
    * Crée un objet JobRequest à partir de données lues de cassandra.
    * 
    * @param result
    *           Données de cassandra
    * @return le jobRequest
    */
   private JobRequest getJobRequestFromResult(
         ColumnFamilyResult<UUID, String> result) {

      if (result == null || !result.hasResults()) {
         return null;
      }
      Serializer<Date> dSlz = NullableDateSerializer.get();

      JobRequest jobRequest = new JobRequest();

      jobRequest.setIdJob(result.getKey());

      jobRequest.setType(result.getString(JR_TYPE_COLUMN));

      jobRequest.setParameters(result.getString(JR_PARAMETERS_COLUMN));

      String state = result.getString(JR_STATE_COLUMN);
      jobRequest.setState(JobState.valueOf(state));

      jobRequest.setReservedBy(result.getString(JR_RESERVED_BY_COLUMN));

      Date creationDate = dSlz.fromBytes(result
            .getByteArray(JR_CREATION_DATE_COLUMN));
      jobRequest.setCreationDate(creationDate);

      if (result.getByteArray(JR_RESERVATION_DATE_COLUMN) != null) {
         Date reservationDate = dSlz.fromBytes(result
               .getByteArray(JR_RESERVATION_DATE_COLUMN));
         jobRequest.setReservationDate(reservationDate);
      }

      if (result.getByteArray(JR_STARTING_DATE_COLUMN) != null) {
         Date startingDate = dSlz.fromBytes(result
               .getByteArray(JR_STARTING_DATE_COLUMN));
         jobRequest.setStartingDate(startingDate);
      }

      if (result.getByteArray(JR_ENDING_DATE_COLUMN) != null) {
         Date endingDate = dSlz.fromBytes(result
               .getByteArray(JR_ENDING_DATE_COLUMN));
         jobRequest.setEndingDate(endingDate);
      }

      jobRequest.setMessage(result.getString(JR_MESSAGE));

      jobRequest.setSaeHost(result.getString(JR_SAE_HOST));

      jobRequest.setClientHost(result.getString(JR_CLIENT_HOST));

      jobRequest.setPid(result.getInteger(JR_PID));

      jobRequest.setDocCount(result.getInteger(JR_DOC_COUNT));

      jobRequest.setToCheckFlag(result.getBoolean(JR_TO_CHECK_FLAG));

      jobRequest
            .setToCheckFlagRaison(result.getString(JR_TO_CHECK_FLAG_RAISON));
      
      if (result.getByteArray(JR_VI) != null) {
         VIContenuExtrait contenuExtrait = VISerializer.get().fromBytes(
               result.getByteArray(JR_VI));
         jobRequest.setVi(contenuExtrait);
      }

      return jobRequest;
      
   }

   /**
    * Retourne l'ensemble des jobs, quelque soit leur état, dans un ordre
    * indéfini.
    * 
    * @param connexionConfig
    *           la configuration pour la connexion à Zookeeper et Cassandra
    * @param maxKeysToRead
    *           Nombre max de clés à parcourir (attention : ça comprend les clés
    *           des jobs récemment supprimés)
    * @return Les jobs trouvés
    */
   public final List<JobRequest> getAllJobs(
         CassandraEtZookeeperConfig connexionConfig, int maxKeysToRead) {

      // Connexion à Zookeeper/Cassandra
      ConfigEtClients confEtClients = findConfigEtClient(connexionConfig);

      // Appel de la méthode de récupération des jobs
      return getAllJobsInternal(maxKeysToRead, confEtClients.getCassClient()
            .getKeyspace());

   }

   private ConfigEtClients findConfigEtClient(CassandraEtZookeeperConfig config) {

      ConfigEtClients result = null;

      // Recherche une configuration équivalente
      for (ConfigEtClients confEtCli : clients) {
         if (configEquals(confEtCli.getConfig(), config)) {
            result = confEtCli;
            break;
         }
      }

      // On n'a pas trouvé de configuration équivalente
      if (result == null) {

         // Nouvelle configuration
         ConfigEtClients newConf = new ConfigEtClients();
         newConf.setConfig(config);

         // Connexion à Zookeeper
         CuratorFramework zkClient;
         try {
            zkClient = ZookeeperClientFactory.getClient(config
                  .getZookeeperHosts(), config.getZookeeperNamespace());
         } catch (IOException e) {
            throw new PileTravauxRuntimeException(e);
         }
         newConf.setZkClient(zkClient);

         // Connexion à Cassandra
         CassandraServerBean cassandraServer = new CassandraServerBean();
         cassandraServer.setHosts(config.getCassandraHosts());
         cassandraServer.setStartLocal(false);
         cassandraServer.setDataSet(null);
         CassandraClientFactory cassandraClientFactory;
         try {
            cassandraClientFactory = new CassandraClientFactory(
                  cassandraServer, config.getCassandraKeySpace(), config
                        .getCassandraUserName(), config.getCassandraPassword());
         } catch (InterruptedException e) {
            throw new PileTravauxRuntimeException(e);
         }
         newConf.setCassClient(cassandraClientFactory);

         // Ajoute la configuration à la liste des conf
         clients.add(newConf);
         
         // Mémorise le résultat de la méthode
         result = newConf; 

      }

      // On renvoie le client Zookeeper
      return result;

   }

   private boolean configEquals(CassandraEtZookeeperConfig conf1,
         CassandraEtZookeeperConfig conf2) {

      return (StringUtils.equals(conf1.getCassandraHosts(), conf2
            .getCassandraHosts())
            && StringUtils.equals(conf1.getCassandraKeySpace(), conf2
                  .getCassandraKeySpace())
            && StringUtils.equals(conf1.getCassandraPassword(), conf2
                  .getCassandraPassword())
            && StringUtils.equals(conf1.getCassandraUserName(), conf2
                  .getCassandraUserName())
            && StringUtils.equals(conf1.getZookeeperHosts(), conf2
                  .getZookeeperHosts()) && StringUtils.equals(conf1
            .getZookeeperNamespace(), conf2.getZookeeperNamespace()));

   }
   
   
   public List<JobHistory> getJobHistory(
         CassandraEtZookeeperConfig connexionConfig,
         UUID idJob) {
      
      // Connexion à Zookeeper/Cassandra
      ConfigEtClients confEtClients = findConfigEtClient(connexionConfig);
      
      // Récupération de l'objet Keyspace
      Keyspace keyspace = confEtClients.getCassClient().getKeyspace();
      
      // Template pour requêter
      ColumnFamilyTemplate<UUID, UUID> jobHistoryTmpl = new ThriftColumnFamilyTemplate<UUID, UUID>(keyspace,
            JOBHISTORY_CFNAME, UUIDSerializer.get(), UUIDSerializer.get());
      
      // Requête
      ColumnFamilyResult<UUID, UUID> result = jobHistoryTmpl.queryColumns(idJob);

      // Mapping en objets
      Collection<UUID> colNames = result.getColumnNames();
      List<JobHistory> histories = new ArrayList<JobHistory>(colNames.size());
      StringSerializer serializer = StringSerializer.get();
      for (UUID timeUUID : colNames) {
      
         JobHistory jobHistory = new JobHistory();
      
         jobHistory.setTrace(serializer
               .fromBytes(result.getByteArray(timeUUID)));
         jobHistory.setDate(new Date(TimeUUIDUtils.getTimeFromUUID(timeUUID)));
      
         histories.add(jobHistory);
      
      }
      
      // Valeur de retour
      return histories;
      
   }

}
