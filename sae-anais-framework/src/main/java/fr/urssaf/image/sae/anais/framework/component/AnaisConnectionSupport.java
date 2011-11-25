package fr.urssaf.image.sae.anais.framework.component;

import anaisJavaApi.AnaisConnection_Application;
import anaisJavaApi.AnaisExceptionAuthAccountLocked;
import anaisJavaApi.AnaisExceptionAuthFailure;
import anaisJavaApi.AnaisExceptionAuthMultiUid;
import anaisJavaApi.AnaisExceptionFailure;
import anaisJavaApi.AnaisExceptionNoObject;
import anaisJavaApi.AnaisExceptionPwdExpired;
import anaisJavaApi.AnaisExceptionPwdExpiring;
import anaisJavaApi.AnaisExceptionServerCommunication;
import anaisJavaApi.AnaisHabilitationList;
import anaisJavaApi.AnaisUserInfo;
import anaisJavaApi.AnaisUserResult;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;

/**
 * La classe encapsule les méthode de la classe
 * {@link AnaisConnection_Application} <br>
 * Les méthodes concernées sont
 * <ul>
 * <li>{@link AnaisConnection_Application#checkUserCredential(String, String)}</li>
 * <li>{@link AnaisConnection_Application#close()}</li>
 * <li>{@link AnaisConnection_Application#getUserInfo(String)}</li>
 * <li>
 * {@link AnaisConnection_Application#getUserHabilitations(String, String, String)}
 * </li>
 * </ul>
 * L'instianciation de cette classe a recours à un objet de type
 * {@ConnectionFactory}<br>
 * La classe est la classe mère des classe DAO<br>
 * Voici un exemple<br>
 * <code>
    <br>
    public class AuthentificationDAO extends AnaisConnectionSupport {<br>
    <br>
    &nbsp;public AuthentificationDAO(ConnectionFactory connectionFactory) {<br>
    &nbsp;&nbsp;&nbsp;super(connectionFactory);<br>
    &nbsp;}<br>
 
 * </code><br>
 * Les exceptions levées par ANAIS sont encapsulées dans
 * {@link SaeAnaisApiException} :
 * <ul>
 * <li>{@link AnaisExceptionServerCommunication}</li>
 * <li>{@link AnaisExceptionAuthFailure}</li>
 * <li>{@link AnaisExceptionPwdExpired}</li>
 * <li>{@link AnaisExceptionPwdExpiring}</li>
 * <li>{@link AnaisExceptionAuthAccountLocked}</li>
 * <li>{@link AnaisExceptionAuthMultiUid}</li>
 * <li>{@link AnaisExceptionFailure}</li>
 * <li>{@link AnaisExceptionNoObject}</li>
 * </ul>
 * 
 */
public class AnaisConnectionSupport {

   private final AnaisConnection_Application connection;
   
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
    * Encapsule
    * {@link anaisJavaApi.AnaisConnection_Application#checkUserCredential(String, String)}
    * 
    * @param userLogin
    *           param login
    * @param userPassword
    *           param passwd
    * @return {@link AnaisUserResult}
    * @throws SaeAnaisApiException
    */
   public final AnaisUserResult checkUserCredential(String userLogin,
         String userPassword) {

      try {
         return connection.checkUserCredential(userLogin, userPassword);
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionAuthFailure e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionPwdExpired e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionPwdExpiring e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionAuthAccountLocked e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionAuthMultiUid e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      }
   }

   /**
    * Encapsule {@link anaisJavaApi.AnaisConnection_Application#close()}
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
    * Encapsule
    * {@link anaisJavaApi.AnaisConnection_Application#getUserInfo(String)}
    * 
    * @param userDn
    *           param userdn
    * @return {@link AnaisUserInfo}
    * @throws SaeAnaisApiException
    */
   public final AnaisUserInfo getUserInfo(String userDn) {
      try {
         return connection.getUserInfo(userDn);
      } catch (AnaisExceptionNoObject e) {
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
   public final AnaisHabilitationList getUserHabilitations(String userDn,
         String codeInterRegion, String codeOrganisme) {
      try {
         return connection.getUserHabilitations(userDn, codeInterRegion,
               codeOrganisme);
      } catch (AnaisExceptionNoObject e) {
         throw new SaeAnaisApiException(e);
      }
   }
   
   

}
