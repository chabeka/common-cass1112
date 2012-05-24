package fr.urssaf.image.sae.anais.framework.component;

import recouv.cirti.anais.api.source.AnaisExceptionFailure;
import recouv.cirti.anais.api.source.AnaisExceptionServerAuthentication;
import recouv.cirti.anais.api.source.AnaisExceptionServerCommunication;
import recouv.cirti.anais.api.source.AnaisLdap;
import recouv.cirti.anais.api.source.AnaisLdapProvider;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;

/**
 * Cette classe permet de créer des connexions à ANAIS<br>
 * <br>
 * Il est nécessaire au préalable d'instancier un objet {@link DataSource} pour
 * spécifier les paramètres des connexion. Ce paramétrage est donc identique
 * pour chaque connexion créée.<br>
 * <br>
 * Cette classe est utilisée en argument dans {@link AnaisConnectionSupport}
 * pour configurer chaque classe de type DAO<br>
 * <br>
 * 
 * @see {@link DataSource}
 * @see {@link AnaisConnectionSupport}
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
    * <br>
    * La méthode instancie un objet {@link AnaisLdap} en s'appuyant sur les
    * paramètres du {@link DataSource} de l'objet.<br>
    * <br>
    * Les exceptions levées par l'API ANAIS sont encapsulées dans une exception
    * {@link SaeAnaisApiException}.
    * 
    * @see SaeAnaisApiException
    * @return connexion initialisée à ANAIS
    * @throws SaeAnaisApiException
    */
   public final AnaisLdap createConnection() {

      AnaisLdap connection = AnaisLdapProvider.getUniqueInstance();

      try {
         connection.init(dataSource.getHostname(), dataSource.getPort(),
               dataSource.isUsetls(), dataSource.getAppdn(), dataSource
                     .getPasswd(), dataSource.getCodeapp(), dataSource
                     .getCodeenv(), dataSource.getTimeout(), dataSource
                     .getComptePortail(), dataSource.isDroitsDirect());

      } catch (AnaisExceptionServerAuthentication e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      }

      return connection;

   }
}
