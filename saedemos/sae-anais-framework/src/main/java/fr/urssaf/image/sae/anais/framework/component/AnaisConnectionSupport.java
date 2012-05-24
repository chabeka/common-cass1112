package fr.urssaf.image.sae.anais.framework.component;

import java.util.ArrayList;

import recouv.cirti.anais.api.source.AnaisExceptionAuthAccountLocked;
import recouv.cirti.anais.api.source.AnaisExceptionAuthFailure;
import recouv.cirti.anais.api.source.AnaisExceptionAuthMultiUid;
import recouv.cirti.anais.api.source.AnaisExceptionFailure;
import recouv.cirti.anais.api.source.AnaisExceptionNoObject;
import recouv.cirti.anais.api.source.AnaisExceptionServerCommunication;
import recouv.cirti.anais.api.source.AnaisHabilitationInstance;
import recouv.cirti.anais.api.source.AnaisLdap;
import recouv.cirti.anais.api.source.AnaisUser;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;

/**
 * La classe encapsule les méthodes de l'API ANAIS.<br>
 * <br>
 * L'instianciation de cette classe a recours à un objet de type
 * {@ConnectionFactory}<br>
 * La classe est la classe mère des classe DAO<br>
 * Voici un exemple :<br>
 * <code>
    <br>
    public class AuthentificationDAO extends AnaisConnectionSupport {<br>
    <br>
    &nbsp;public AuthentificationDAO(ConnectionFactory connectionFactory) {<br>
    &nbsp;&nbsp;&nbsp;super(connectionFactory);<br>
    &nbsp;}<br>
 
 * </code><br>
 * Les exceptions levées par l'API ANAIS sont encapsulées dans {@link SaeAnaisApiException}.
 * 
 */
public class AnaisConnectionSupport {

   private final AnaisLdap connection;

   public static final String ANAIS_CONNECTION = "sae-anais-framework.properties";

   /**
    * initialise la connection à ANAIS
    * 
    * @param connectionFactory
    *           connection factory pour ANAIS
    */
   public AnaisConnectionSupport(ConnectionFactory connectionFactory) {

      if (connectionFactory == null) {
         throw new IllegalStateException("'connectionFactory' is required");
      }

      this.connection = connectionFactory.createConnection();
   }

   /**
    * Vérification de l'authentification de l'utilisateur
    * 
    * @param userLogin
    *           param login
    * @param userPassword
    *           param passwd
    * @return {@link AnaisUserResult}
    * @throws SaeAnaisApiException
    */
   public final AnaisUser checkUserCredential(String userLogin,
         String userPassword) {

      AnaisUser user = new AnaisUser(connection);

      try {
         return user.checkUserCredential(userLogin, userPassword);
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionAuthFailure e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionAuthAccountLocked e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionAuthMultiUid e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionNoObject e) {
         throw new SaeAnaisApiException(e);
      }

   }

   /**
    * Fermeture de la connexion à ANAIS
    * 
    * @throws SaeAnaisApiException
    */
   public final void close() {
      try {
         connection.close();
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      }
   }

   /**
    * Renvoie des informations sur un utilisateur à partir de son dn
    * 
    * @param userDn
    *           userdn
    * @return les informations sur l'utilisateur
    * @throws SaeAnaisApiException
    */
   public final AnaisUser getUserInfo(String userDn) {

      AnaisUser user = new AnaisUser(connection);

      try {
         return user.GetUserInfoFromUserDN(userDn);
      } catch (AnaisExceptionNoObject e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      }

   }

   /**
    * Encapsule
    * {@link anaisJavaApi.AnaisConnection_Application#getUserHabilitations(String ,String,String)}
    * 
    * @param userDn
    *           param userdn
    * @param codeInterRegion
    *           param codeir
    * @param codeOrganisme
    *           param codeorg
    * @return {@link AnaisHabilitationList}
    * @throws SaeAnaisApiException
    */
   public final ArrayList<AnaisHabilitationInstance> getUserHabilitations(
         String userDn, String codeInterRegion, String codeOrganisme) {

      AnaisUser user = new AnaisUser(connection);

      try {
         return user.GetAllUserHabilitations(userDn, codeInterRegion,
               codeOrganisme);
      } catch (AnaisExceptionNoObject e) {
         throw new SaeAnaisApiException(e);
      }

   }

}
