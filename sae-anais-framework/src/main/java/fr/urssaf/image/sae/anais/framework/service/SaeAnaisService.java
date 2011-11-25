package fr.urssaf.image.sae.anais.framework.service;

import fr.urssaf.image.sae.anais.framework.component.ConnectionFactory;
import fr.urssaf.image.sae.anais.framework.component.DataSource;
import fr.urssaf.image.sae.anais.framework.component.ProfilAppliFactory;
import fr.urssaf.image.sae.anais.framework.component.ProfilServeurFactory;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAdresseServeur;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCodesEnvironnement;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilServeur;
import fr.urssaf.image.sae.anais.framework.service.dao.AuthentificationDAO;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.ValidationXmlParXsdException;
import fr.urssaf.image.sae.vi.exception.VIException;

/**
 * Classe principale de services sur le serveur ANAIS<br>
 * 
 * @see ConnectionFactory
 */
public class SaeAnaisService {

   /**
    * Création d’un jeton d’authentification à partir d’un couple login/mot de
    * passe <br>
    * <br>
    * A partir de <code>environnement</code> et <code>serveur</code> on
    * instancie {@link SaeAnaisProfilserveur}<br>
    * <br>
    * Si <code>serveur</code> n'est pas renseigné <code>environnement</code>
    * paramètre l'adressage du serveur ANAIS avec la classe
    * {@link ProfilServeurFactory}<br>
    * Sinon <code>serveur</code> paramètre l'adressage au serveur ANAIS<br>
    * <br>
    * A partir de <code>profilCptAppli</code> et <code>compteApplicatif</code>
    * on instancie {@link SaeAnaisProfilCompteApplicatif} avec la classe
    * {@link ProfilAppliFactory} <br>
    * <br>
    * L'appel de la méthode instancie dans l'ordre
    * <ol>
    * <li>{@link DataSource}</li>
    * <li>{@link ConnectionFactory}</li>
    * <li>{@link AuthentificationDAO}</li>
    * </ol>
    * Enfin elle appelle la méthode
    * {@link AuthentificationDAO#createXMLToken(String, String, String, String)}<br>
    * <br>
    * La méthode est soumise à une vérification par la méthode
    * {@link SaeAnaisServiceCheck#authentifierPourSaeParLoginPasswordCheck} par
    * une approche Aspect <br>
    * <br>
    * 
    * 
    * @param environnement
    *           L’environnement (Développement / Validation / Production)
    * @param serveur
    *           Les paramètres de connexion au serveur ANAIS
    * @param profilCptAppli
    *           Profil du compte applicatif(Sae/Autre)
    * @param compteApplicatif
    *           Les paramètres du compte applicatif
    * @param userLogin
    *           Le login de l’utilisateur
    * @param userPassword
    *           Le mot de passe de l’utilisateur
    * @param codeInterRegion
    *           Le code de l’inter-région où chercher les habilitations (peut
    *           être vide)
    * @param codeOrganisme
    *           Le code de l’organisme où chercher les habilitations (peut être
    *           vide)
    * @throws EnvironnementNonRenseigneException
    * @throws ProfilCompteApplicatifNonRenseigneException
    * @throws UserLoginNonRenseigneException
    * @throws UserPasswordNonRenseigneException
    * @throws HoteNonRenseigneException
    * @throws PortNonRenseigneException
    * @throws SaeAnaisApiException
    * @throws ParametresApplicatifsNonRenseigneException
    * @throws ValidationXmlParXsdException
    * @return Le jeton d’authentification sous la forme d’un flux XML
    * @throws AucunDroitException Le CTD n'a aucun droit
    */
   public final String authentifierPourSaeParLoginPassword(
         SaeAnaisEnumCodesEnvironnement environnement,
         SaeAnaisAdresseServeur serveur,
         SaeAnaisEnumCompteApplicatif profilCptAppli,
         SaeAnaisProfilCompteApplicatif compteApplicatif, String userLogin,
         String userPassword, String codeInterRegion, String codeOrganisme) throws AucunDroitException {

      ProfilAppliFactory appliFactory = new ProfilAppliFactory();
      SaeAnaisProfilCompteApplicatif profilAppli = appliFactory.createProfil(
            profilCptAppli, compteApplicatif);

      // initialisation du data source
      DataSource dataSource = new DataSource();
      dataSource.setAppdn(profilAppli.getDn());
      dataSource.setCodeapp(profilAppli.getCodeApplication());
      dataSource.setPasswd(profilAppli.getPassword());

      if (serveur == null) {

         ProfilServeurFactory serveurFactory = new ProfilServeurFactory();

         SaeAnaisProfilServeur serveurProfil = serveurFactory
               .createProfil(environnement);

         dataSource.setCodeenv(serveurProfil.getCodeEnvironnement().code());
         initAdresseServeur(dataSource, serveurProfil.getServeurs().get(0));

      }

      else {

         dataSource.setCodeenv(environnement.code());
         initAdresseServeur(dataSource, serveur);

      }

      // initialisation du connection factory
      ConnectionFactory connection = new ConnectionFactory(dataSource);
      // initialisation du dao authentification
      AuthentificationDAO authDAO = new AuthentificationDAO(connection);

      try {
         return authDAO.createXMLToken(userLogin, userPassword, codeInterRegion,
               codeOrganisme);
      } catch (VIException e) {
         throw new ValidationXmlParXsdException(e);
      }

   }

   private void initAdresseServeur(DataSource dataSource,
         SaeAnaisAdresseServeur serveur) {

      dataSource.setHostname(serveur.getHote());
      dataSource.setPort(serveur.getPort());
      dataSource.setTimeout(Integer.toString(serveur.getTimeout()));
      dataSource.setUsetls(serveur.isTls());
   }

}
