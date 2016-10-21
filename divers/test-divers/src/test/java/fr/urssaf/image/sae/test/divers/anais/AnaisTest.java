package fr.urssaf.image.sae.test.divers.anais;

import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import fr.urssaf.image.sae.anais.framework.service.SaeAnaisService;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;

@RunWith(BlockJUnit4ClassRunner.class)
public class AnaisTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(AnaisTest.class);
   
   private static final String ANAIS_INTEG = "anaisapi.giin.recouv";
   
   private static final String ANAIS_PROD = "anaisapi.cnp.recouv";
   
   @Test
   public void habilitationsAnais() {
      
      SaeAnaisConfig config = new SaeAnaisConfig();
      
      // TODO : choisir le host
      //config.setHostname(ANAIS_INTEG);
      config.setHostname(ANAIS_PROD);
      
      // TODO : choisir l'application
      /*config.setCodeapp("RECHERCHE-DOCUMENTAIRE");
      config.setPasswd("rechercheDoc");
      config.setAppdn("cn=USR_READ_NAT_APP_RECHERCHE-DOCUMENTAIRE,OU=RECHERCHE-DOCUMENTAIRE,OU=Applications,OU=Technique,dc=recouv");*/
      config.setCodeapp("EXPLOIT_SAE");
      config.setPasswd("esae69pi85");
      config.setAppdn("cn=USR_READ_NAT_APP_EXPLOIT-SAE,ou=EXPLOIT-SAE,ou=Applications,ou=Technique,dc=recouv");
      
      config.setCodeenv("PROD");
      config.setPort(Integer.valueOf(389));
      config.setTimeout("60000");
      config.setUsetls(false);
      config.setComptePortail("");
      config.setDroitsDirect(false);
      
      LOGGER.info("Creation du service anais sur l'host {}", config.getHostname());
      SaeAnaisService service = new SaeAnaisService(config);
      
      String user = "CER6900493";
      String password = "cle201001";
      LOGGER.info("Recuperation de l'habilitation pour le user/password {}/{}", new String[]{ user, password });
      try {
         SaeAnaisAuth saeAnaisAuth = service.habilitationsAnais(user, password, null, null);
         LOGGER.info("L'utilisateur est : {} {}", new String[] { saeAnaisAuth.getNom(), saeAnaisAuth.getPrenom() });
         LOGGER.info("L'utilisateur a {} habilitation(s)", new Integer[] { saeAnaisAuth.getHabilitations().size() });
         LOGGER.info("Liste des habilitations : {}", convertToString(saeAnaisAuth.getHabilitations()));
      } catch (AucunDroitException e) {
         LOGGER.error("Aucun droit n'a ete trouve : {}", e.getMessage());
      }
   }
   
   @Test
   public void test() throws AnaisExceptionServerCommunication {
      AnaisLdap connection = AnaisLdapProvider.getUniqueInstance();

      try {
         connection.init(ANAIS_PROD, Integer.valueOf(389),
               false, "cn=USR_READ_NAT_APP_EXPLOIT-SAE,ou=EXPLOIT-SAE,ou=Applications,ou=Technique,dc=recouv", 
               "esae69pi85", "EXPLOIT_SAE", "PROD", "60000", "", false);

      } catch (AnaisExceptionServerAuthentication e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionFailure e) {
         throw new SaeAnaisApiException(e);
      } catch (AnaisExceptionServerCommunication e) {
         throw new SaeAnaisApiException(e);
      }
      
      SearchControls scopeSec = new SearchControls();
      scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
      String baseDN = "ou=recouvrement,dc=recouv";
      /*String filter = new String("(&(objectClass=anaisUser)(uid=" + userLogin
            + "))");*/
      String filter = "";
      int count = 0;
      String DN = null;

      try {

         NamingEnumeration<?> users = connection.getAnais_ldap().search(baseDN,
               filter, scopeSec);

         while (users != null && users.hasMore()) {
            count++;
            SearchResult user = (SearchResult) users.next();

            DN = new String(user.getName() + "," + baseDN);
         }

         /*if (count > 1) {
            throw new AnaisExceptionAuthMultiUid();
         }

         if (count == 0) {
            throw new AnaisExceptionNoObject(
                  "Aucun utilisateur dont l'attribut uid=" + userLogin
                        + "est trouvé");
         }*/
      } catch (NamingException e) {
         if (LOGGER.isErrorEnabled()) {
            LOGGER
                  .error("AnaisUser-->GetDNFromLogin - Erreur1 : impossible de recuperer le dn de l'utilisateur");
         }

         //throw new AnaisExceptionNoObject(e.toString());
      } 

      connection.close();
   }
   
   /**
    * Methode permettant de convertir en chaine la liste des habilitations.
    * @param habilitations liste des habilitations
    * @return String
    */
   private String convertToString(List<SaeAnaisAuthHabilitation> habilitations) {
      StringBuffer buffer = new StringBuffer();
      boolean firstProfil = true;
      for (SaeAnaisAuthHabilitation profil : habilitations) {
         if (!firstProfil) {
            buffer.append(", ");
         }
         buffer.append(profil.getCode());
         firstProfil = false;
      }
      return buffer.toString();
   }
}
