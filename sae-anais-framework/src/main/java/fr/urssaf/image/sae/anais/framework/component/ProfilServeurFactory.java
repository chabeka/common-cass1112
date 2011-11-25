package fr.urssaf.image.sae.anais.framework.component;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import fr.urssaf.image.sae.anais.framework.modele.ObjectFactory;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAdresseServeur;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCodesEnvironnement;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilServeur;

/**
 * Classe d'instanciation de profil serveur pour ANAIS<br>
 * La configuration s'appuie sur le fichier sae-anais-framework.properties dans
 * le classpath<br>
 * les paramètres de ce fichier sont:
 * <ul>
 * <li>anais.port : Numéro de port TCP sur lequel écoute le serveur ANAIS</li>
 * <li>anais.tls : Activation de TLS pour la communication avec le serveur ANAIS
 * </li>
 * <li>anais.timeout : Le temps de réponse maximal d'un serveur lors d'une
 * connexion</li>
 * <li>anais.host.Developpement : Liste des noms d'hôte ou adresses IP du serveur ANAIS en
 * mode « Tests pour les développements »</li>
 * <li>anais.host.Validation : Liste des noms d'hôte ou adresses IP du serveur ANAIS en
 * mode « Validation »</li>
 * <li>anais.host.Production : Liste des noms d'hôte ou adresses IP du serveur ANAIS
 * en mode « Production »</li>
 * </ul>
 * Les adresses IP sont séparées par des '|', ex: host1|host2|host3 <br>
 * <br>
 * La récupération des paramètres dans le fichier de properties s'appuie sur le
 * Framework <a href='http://commons.apache.org/configuration/'>Apache commons
 * configuration</a><br>
 * 
 * @see SaeAnaisProfilServeur
 */
public class ProfilServeurFactory {

   private static final Logger LOG = Logger
         .getLogger(ProfilServeurFactory.class);

   /**
    * Méthode d'instanciation d'un profil serveur à partir d'un code
    * d'environnement
    * 
    * @see SaeAnaisEnumCodesEnvironnement
    * 
    * @param environnement
    *           code d'environnement de ANAIS
    * @return profil ANAIS spécifique au code environnement
    */
   public final SaeAnaisProfilServeur createProfil(
         SaeAnaisEnumCodesEnvironnement environnement) {

      SaeAnaisProfilServeur profil = null;

      switch (environnement) {
      case Developpement:
         profil = new DevProfil();
         break;
      case Validation:
         profil = new ValidProfil();
         break;
      case Production:
         profil = new ProdProfil();
         break;
      default:
         throw new IllegalArgumentException("'SaeAnaisEnumCodesEnvironnement' "
               + environnement.name() + " is not used ");
      }

      profil.setCodeEnvironnement(environnement);

      SaeAnaisAdresseServeur profilServeur = profil.getServeurs().get(0);
      LOG.debug("INFO SERVEUR");
      LOG.debug("host : " + profilServeur.getHote());
      LOG.debug("port : " + profilServeur.getPort());
      LOG.debug("timeout : " + profilServeur.getTimeout());
      LOG.debug("tls : " + profilServeur.isTls());

      return profil;
   }

   private abstract class AbstractProfil extends SaeAnaisProfilServeur {

      protected AbstractProfil(SaeAnaisEnumCodesEnvironnement codeEnvironnement) {
         super();

         try {
            AbstractConfiguration.setDefaultListDelimiter("|".charAt(0));

            Configuration config = new PropertiesConfiguration(
                  AnaisConnectionSupport.ANAIS_CONNECTION);

            int port = config.getInt("anais.port");
            int timeout = config.getInt("anais.timeout");
            boolean tls = config.getBoolean("anais.tls");
            String[] hosts = config.getStringArray("anais.host."
                  + codeEnvironnement.name());

            for (String host : hosts) {
               SaeAnaisAdresseServeur serveur = ObjectFactory
                     .createSaeAnaisAdresseServeur();
               serveur.setHote(host);
               serveur.setPort(port);
               serveur.setTimeout(timeout);
               serveur.setTls(tls);

               this.getServeurs().add(serveur);
            }

         } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
         }
      }
   }

   private class DevProfil extends AbstractProfil {

      protected DevProfil() {
         super(SaeAnaisEnumCodesEnvironnement.Developpement);
      }

   }

   private class ValidProfil extends AbstractProfil {

      protected ValidProfil() {
         super(SaeAnaisEnumCodesEnvironnement.Validation);
      }

   }

   private class ProdProfil extends AbstractProfil {

      protected ProdProfil() {
         super(SaeAnaisEnumCodesEnvironnement.Production);
      }

   }
}
