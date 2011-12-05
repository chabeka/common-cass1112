package fr.urssaf.image.sae.anais.framework.component;

import anaisJavaApi.AnaisConnection_Application;
import anaisJavaApi.AnaisExceptionFailure;
import anaisJavaApi.AnaisExceptionServerAuthentication;
import anaisJavaApi.AnaisExceptionServerCommunication;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;

/**
 * Cette classe permet de créer des connexions à ANAIS<br>
 * Il est nécessaire au préalable d'instancier un objet {@link DataSource} pour
 * spécifier les paramètres des connexion<br>
 * Ce paramétrage est donc identique pour chaque connexion créée.<br>
 * Cette connection s'appuie sur la méthode $
 * {@link AnaisConnection_Application#init(String, Integer, boolean, String, String, String, String, String)
 * ) <br> La classe est utilisé en argument dans {@link AnaisConnectionSupport} pour
 * configurer chaque classe de type DAO<br>
 * <br>
 * Voici un exemple tiré du code source de {@link AnaisConnectionSupport}<br>
 * <code>
 * 
 * private final AnaisConnection_Application connection;<br>
   <br>
   public AnaisConnectionSupport(ConnectionFactory connectionFactory) {<br>
      <br>
   &nbsp;&nbsp;if (connectionFactory == null) {<br>
   &nbsp;&nbsp;&nbsp;throw new IllegalStateException("'connectionFactory' is required");<br>
   &nbsp;&nbsp;}<br>
      <br>
   &nbsp;&nbsp;this.connection = connectionFactory.createConnection();<br>
   }<br>
   </code>
 * 
 * @see {@link DataSource}
 * @see {@link AnaisConnectionSupport}
 * @see {@link AnaisConnection_Application#init(String, Integer, boolean, String, String, String, String, String)}
 * 
 */
public class ConnectionFactory {

   private final DataSource dataSource;

   /***
    * Initialisation du data source
    * 
    * @param dataSource
    *           paramétrage de la connexion
    */
   public ConnectionFactory(DataSource dataSource) {

      if (dataSource == null) {
         throw new IllegalStateException("'dataSource' is required");
      }
      this.dataSource = dataSource;
   }

   /**
    * Création d'une connexion à ANAIS<br>
    * La méthode instancie un objet {@link AnaisConnection_Application}<br>
    * La méthode appelle
    * {@link AnaisConnection_Application#init(String, Integer, boolean, String, String, String, String, String)}
    * avec les paramètres du {@link DataSource} de l'objet<br><br>
    * Les exceptions {@link AnaisExceptionFailure},
    * {@link AnaisExceptionServerCommunication} et
    * {@link AnaisExceptionServerAuthentication}
    * sont encapsulées dans une exception {@link SaeAnaisApiException}
    * 
    * @see SaeAnaisApiException
    * @return connexion initialisée à ANAIS
    * @throws SaeAnaisApiException
    */
   public final AnaisConnection_Application createConnection() {

      AnaisConnection_Application connection = new AnaisConnection_Application();

      try {
         connection.init(dataSource.getHostname(), dataSource.getPort(),
               dataSource.isUsetls(), dataSource.getAppdn(),

               dataSource.getPasswd(), dataSource.getCodeapp(), dataSource
                     .getCodeenv(), dataSource.getTimeout());

         return connection;

      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionServerAuthentication e) {
         throw new SaeAnaisApiException(e);
      }

   }
}
