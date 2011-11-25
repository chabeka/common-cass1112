package fr.urssaf.image.sae.anais.framework.component;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import fr.urssaf.image.sae.anais.framework.modele.ObjectFactory;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilCompteApplicatif;

/**
 * Classe d'instanciation de profil compte applicatif pour ANAIS<br>
 * La configuration s'appuie sur le fichier sae-anais-framework.properties dans
 * le classpath<br>
 * les paramètres de ce fichier sont:
 * <ul>
 * <li>anais.cn : DN du compte applicatif utilisé pour se connecter à l’annuaire
 * </li>
 * <li>anais.code-application : Code de l'application</li>
 * <li>anais.compte-applicatif-password : Mot de passe du compte applicatif pour
 * se connecter à l'annuaire</li>
 * </ul>
 * <br>
 * <br>
 * La récupération des paramètres dans le fichier de properties s'appuie sur le
 * Framework <a href='http://commons.apache.org/configuration/'>Apache commons
 * configuration</a><br>
 * 
 * @see SaeAnaisProfilCompteApplicatif
 */
public class ProfilAppliFactory {

   private static final Logger LOG = Logger.getLogger(ProfilAppliFactory.class);

   /**
    * Méthode d'instanciation d'un profil compte applicatif<br>
    * <br>
    * Si <code>profilCptAppli = Sae</code> alors instanciation à partir des
    * paramètres de configuration<br>
    * Sinon si <code>profilCptAppli</code> = <code>Autre</code> alors
    * instanciation à partir de <code>compteApplicatif</code><br>
    * Sinon la méthode lève une exception lève une exception
    * {@link IllegalArgumentException}<br>
    * <br>
    * Message de l'exception de type: 'SaeAnaisEnumCompteApplicatif' is not used
    * 
    * @param profilCptAppli
    *           choix entre (Sae/Autre)
    * @param compteApplicatif
    *           compte applicatif pour <code>Autre</code>
    * @return compte applicatif instancié
    * @throws IllegalArgumentException
    */
   public final SaeAnaisProfilCompteApplicatif createProfil(
         SaeAnaisEnumCompteApplicatif profilCptAppli,
         SaeAnaisProfilCompteApplicatif compteApplicatif) {

      SaeAnaisProfilCompteApplicatif profil = null;

      switch (profilCptAppli) {
      case Sae:
         profil = createSaeProfil();
         break;
      case Autre:
         profil = compteApplicatif;
         break;
      default:
         throw new IllegalArgumentException("'SaeAnaisEnumCompteApplicatif' "
               + profilCptAppli.name() + " is not used ");

      }

      LOG.debug("INFO APPLICATION");
      LOG.debug("appdn : " + profil.getDn());
      LOG.debug("codeapp : " + profil.getCodeApplication());
      //ne pas faire apparaitre le mot de passe
      //LOG.debug("paswd : " + profil.getPassword());

      return profil;
   }

   private SaeAnaisProfilCompteApplicatif createSaeProfil() {

      SaeAnaisProfilCompteApplicatif profil = ObjectFactory
            .createSaeAnaisProfilCompteApplicatif();

      try {
         AbstractConfiguration.setDefaultListDelimiter("|".charAt(0));
         Configuration config = new PropertiesConfiguration(
               AnaisConnectionSupport.ANAIS_CONNECTION);

         profil.setDn(config.getString("anais.cn"));
         profil.setCodeApplication(config.getString("anais.code-application"));
         profil.setPassword(config
               .getString("anais.compte-applicatif-password"));

         return profil;
      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }
   }

}
