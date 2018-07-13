package fr.urssaf.image.sae.anais.framework.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import recouv.cirti.anais.api.source.AnaisExceptionAuthAccountLocked;
import recouv.cirti.anais.api.source.AnaisExceptionAuthFailure;
import recouv.cirti.anais.api.source.AnaisExceptionAuthMultiUid;
import recouv.cirti.anais.api.source.AnaisExceptionFailure;
import recouv.cirti.anais.api.source.AnaisExceptionNoObject;
import recouv.cirti.anais.api.source.AnaisExceptionServerAuthentication;
import recouv.cirti.anais.api.source.AnaisExceptionServerCommunication;
import recouv.cirti.anais.api.source.AnaisHabilitationInstance;
import recouv.cirti.anais.api.source.AnaisLdap;
import recouv.cirti.anais.api.source.AnaisLdapProvider;
import recouv.cirti.anais.api.source.AnaisUser;
import fr.urssaf.image.sae.anais.framework.config.SaeAnaisConfig;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAuth;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAuthHabilitation;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;

/**
 * Classe principale de services ANAIS.<br>
 * <br>
 * Encapsule l'API ANAIS et ses exceptions.
 */
public class SaeAnaisService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SaeAnaisService.class);

   private final SaeAnaisConfig anaisConfig;

   /**
    * Constructeur
    * 
    * @param anaisConfig
    *           la configuration d'accès à ANAIS
    */
   public SaeAnaisService(SaeAnaisConfig anaisConfig) {
      this.anaisConfig = anaisConfig;
   }

   /**
    * Récupère les habilitations ANAIS d'un agent.<br>
    * S'appuie sur la configuration d'accès à ANAIS transmise au constructeur du
    * service<br>
    * 
    * @param userLogin
    *           le code agent
    * @param userPassword
    *           le mot de passe ANAIS
    * @param codeInterRegion
    *           le code de l'inter-région où chercher les habilitations (peut
    *           être vide)
    * @param codeOrganisme
    *           le code organimse où chercher les habilitations (peut être vide)
    * @return les habilitations de l'agent
    * @throws AucunDroitException
    *            si l'agent n'a aucun droit sur l'application/orga/interIr
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final SaeAnaisAuth habilitationsAnais(String userLogin,
         String userPassword, String codeInterRegion, String codeOrganisme)
         throws AucunDroitException {

      AnaisLdap connection = createConnection();

      try {

         SaeAnaisAuth saeAnaisAuth = new SaeAnaisAuth();

         AnaisUser userResult = this.checkUserCredential(connection, userLogin,
               userPassword);

         AnaisUser user = this.getUserInfo(connection, userResult.getDn());

         List<AnaisHabilitationInstance> hablist = this.getUserHabilitations(
               connection, user.getDn(), codeInterRegion, codeOrganisme);

         saeAnaisAuth.setNom(user.getSn());
         saeAnaisAuth.setPrenom(user.getGivenname());

         LOG.debug("Info connexion : Nom={}, Prenom={}", new Object[] {
               saeAnaisAuth.getNom(), saeAnaisAuth.getPrenom() });

         LOG.debug("Nombre d'habilitations : {}", hablist.size());

         for (AnaisHabilitationInstance hab : hablist) {

            LOG.debug("Droit {} sur {}_{} déployé en {}", new Object[] {
                  hab.getCn(), hab.getCodeapp(), hab.getCodeenv(),
                  hab.getOrgcode() });

            SaeAnaisAuthHabilitation saeAnaisHab = new SaeAnaisAuthHabilitation();
            saeAnaisAuth.getHabilitations().add(saeAnaisHab);

            saeAnaisHab.setCode(hab.getCn());

            saeAnaisHab.setTypeHab(hab.getType());

            saeAnaisHab.setCodeOrga(hab.getOrgcode());

            saeAnaisHab.setCodeIr(hab.getIrcode());

         }

         if (saeAnaisAuth.getHabilitations().isEmpty()) {
            throw new AucunDroitException();
         }

         return saeAnaisAuth;

      } finally {
         this.closeConnection(connection);
      }

   }

   private AnaisLdap createConnection() {

      AnaisLdap connection = AnaisLdapProvider.getUniqueInstance();

      try {
         connection.init(anaisConfig.getHostname(), anaisConfig.getPort(),
               anaisConfig.isUsetls(), anaisConfig.getAppdn(), anaisConfig
                     .getPasswd(), anaisConfig.getCodeapp(), anaisConfig
                     .getCodeenv(), anaisConfig.getTimeout(), anaisConfig
                     .getComptePortail(), anaisConfig.isDroitsDirect());

      } catch (AnaisExceptionServerAuthentication e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      }

      return connection;

   }

   private AnaisUser checkUserCredential(AnaisLdap connection,
         String userLogin, String userPassword) {

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

   private void closeConnection(AnaisLdap connection) {
      try {
         connection.close();
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      }
   }

   private AnaisUser getUserInfo(AnaisLdap connection, String userDn) {

      AnaisUser user = new AnaisUser(connection);

      try {
         return user.GetUserInfoFromUserDN(userDn);
      } catch (AnaisExceptionNoObject e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      }

   }

   private List<AnaisHabilitationInstance> getUserHabilitations(
         AnaisLdap connection, String userDn, String codeInterRegion,
         String codeOrganisme) {

      AnaisUser user = new AnaisUser(connection);

      try {

         return user.GetAllUserHabilitations(userDn, codeInterRegion,
               codeOrganisme);

      } catch (AnaisExceptionNoObject e) {
         throw new SaeAnaisApiException(e);
      }

   }

}
