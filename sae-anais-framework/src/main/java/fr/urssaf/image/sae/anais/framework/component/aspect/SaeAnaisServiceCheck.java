package fr.urssaf.image.sae.anais.framework.component.aspect;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAdresseServeur;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCodesEnvironnement;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.service.exception.EnvironnementNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.HoteNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.ParametresApplicatifsNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.PortNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.ProfilCompteApplicatifNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.UserLoginNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.UserPasswordNonRenseigneException;

/**
 * La classe valide les arguments en entrée des méthode de la classe
 * {@link fr.urssaf.image.sae.anais.framework.service.SaeAnaisService}<br>
 * C'est une classe Aspect qui neccessite une compilation particulière<br>
 * <br>
 * Avec Eclipse il suffit de convertir le projet en projet AspectJ Project<br>
 * Configure --> Convert to AspectJ Project <br>
 * <br>
 * Avec Maven il suffit de configurer le plugin <a
 * href='http://mojo.codehaus.org/aspectj-maven-plugin/'>
 * <code>aspectj-maven-plugin</code></a> <br>
 * Si une des règles n'est pas respectée elle lève une exception spécifique<br>
 * Les règles sont ordonnées:
 * <ol>
 * <li>environnement vide : {@link EnvironnementNonRenseigneException}</li>
 * <li>serveur non vide et hote vide : {@link HoteNonRenseigneException}</li>
 * <li>serveur non vide et port vide : {@link PortNonRenseigneException}</li>
 * <li>compteApplicatif vide :
 * {@link ProfilCompteApplicatifNonRenseigneException}</li>
 * <li>compteApplicatif à « AUTRE » et compteApplicatifParametres vide :
 * {@link ParametresApplicatifsNonRenseigneException}</li>
 * <li>userLogin vide : {@link UserLoginNonRenseigneException}</li>
 * <li>userPassword vide : {@link UserPasswordNonRenseigneException}</li>
 * </ol>
 * <br>
 * l'ordre des arguments
 * {@link SaeAnaisService#authentifierPourSaeParLoginPassword} est importante<br>
 * <ul>
 * <li><code>joinPoint.getArgs()[0]</code> : <code>environnement</code></li>
 * <li><code>joinPoint.getArgs()[1]</code> : <code>serveur</code></li>
 * <li><code>joinPoint.getArgs()[2]</code> : <code>profilCompteApplicatif</code>
 * </li>
 * <li><code>joinPoint.getArgs()[3]</code> : <code>compteApplicatif</code></li>
 * <li><code>joinPoint.getArgs()[4]</code> : <code>userLogin</code></li>
 * <li><code>joinPoint.getArgs()[5]</code> : <code>userPassword</code></li>
 * </ul>
 * <br>
 * la classe utilise {@link StringUtils#isNotBlank(String)} pour tester les
 * chaines de caractères non renseignées
 */
@Aspect
public class SaeAnaisServiceCheck {

   private static final String METHODE = "execution(public * fr.urssaf.image.sae.anais.framework.service.SaeAnaisService.authentifierPourSaeParLoginPassword(..))";

   // ordre des arguments
   private static final int ENVIRONNEMENT = 0;
   private static final int SERVEUR = 1;
   private static final int PROFIL_CPT_APPLI = 2;
   private static final int COMPTE_APPLICATIF = 3;
   private static final int USER_LOGIN = 4;
   private static final int USER_PASSWORD = 5;

   /**
    * Validation de <code>environnement</code><br>
    * Règle : le paramètre <code>environnement</code> doit être renseigné<br>
    * <br>
    * Pour rappel <code>joinPoint.getArgs()[0]</code> :
    * <code>environnement</code>
    * 
    * @param joinPoint
    *           joinpoint de la méthode authentifierPourSaeParLoginPassword
    * @throws EnvironnementNonRenseigneException
    */
   @Before(METHODE)
   public final void environnementCheck(JoinPoint joinPoint) {

      SaeAnaisEnumCodesEnvironnement environnement = (SaeAnaisEnumCodesEnvironnement) joinPoint
            .getArgs()[ENVIRONNEMENT];
      if (environnement == null) {
         throw new EnvironnementNonRenseigneException();
      }
   }

   /**
    * Validation de <code>serveur</code><br>
    * Règle : si le paramètre <code>serveur</code> est renseigné alors ses
    * attributs hote et port doivent l'être aussi<br>
    * <br>
    * Pour rappel <code>joinPoint.getArgs()[1]</code> : <code>serveur</code>
    * 
    * @param joinPoint
    *           joinpoint de la méthode authentifierPourSaeParLoginPassword
    * @throws HoteNonRenseigneException
    * @throws PortNonRenseigneException
    */
   @Before(METHODE)
   public final void serveurCheck(JoinPoint joinPoint) {

      SaeAnaisAdresseServeur serveur = (SaeAnaisAdresseServeur) joinPoint
            .getArgs()[SERVEUR];

      if (serveur != null) {

         if (isEmpty(serveur.getHote())) {
            throw new HoteNonRenseigneException();
         }

         if (serveur.getPort() == null) {
            throw new PortNonRenseigneException();
         }

      }
   }

   /**
    * Validation de <code>profilCptAppli</code> et <code>compteApplicatif</code><br>
    * Règle 1 : le paramètre <code>compteApplicatif</code> doit être renseigné<br>
    * Règle 2 : si le paramètre <code>compteApplicatif</code> est à « AUTRE »
    * alors <code>compteApplicatif<Code> doit être renseigné<br>
    * <br>
    * Pour rappel
    * <ul>
    * <li><code>joinPoint.getArgs()[2]</code> : <code>profilCptAppli</code></li>
    * <li><code>joinPoint.getArgs()[3]</code> : <code>compteApplicatif</code></li>
    * </ul>
    * 
    * @param joinPoint
    *           joinpoint de la méthode authentifierPourSaeParLoginPassword
    * @throws ParametresApplicatifsNonRenseigneException
    */
   @Before(METHODE)
   public final void compteApplicatifCheck(JoinPoint joinPoint) {

      SaeAnaisEnumCompteApplicatif profilCptAppli = (SaeAnaisEnumCompteApplicatif) joinPoint
            .getArgs()[PROFIL_CPT_APPLI];
      SaeAnaisProfilCompteApplicatif compteApplicatif = (SaeAnaisProfilCompteApplicatif) joinPoint
            .getArgs()[COMPTE_APPLICATIF];

      if (profilCptAppli == null) {
         throw new ProfilCompteApplicatifNonRenseigneException();
      }

      if (profilCptAppli.equals(SaeAnaisEnumCompteApplicatif.Autre)
            && compteApplicatif == null) {
         throw new ParametresApplicatifsNonRenseigneException();
      }

   }

   /**
    * Validation de <code>userLogin</code><br>
    * Règle : le paramètre <code>userLogin</code> doit être renseigné <br>
    * 
    * <br>
    * Pour rappel <code>joinPoint.getArgs()[4]</code> : <code>userLogin</code>
    * 
    * @param joinPoint
    *           joinpoint de la méthode authentifierPourSaeParLoginPassword
    * @throws UserLoginNonRenseigneException
    */
   @Before(METHODE)
   public final void loginCheck(JoinPoint joinPoint) {

      String userLogin = (String) joinPoint.getArgs()[USER_LOGIN];
      if (isEmpty(userLogin)) {
         throw new UserLoginNonRenseigneException();
      }
   }

   /**
    * Validation de <code>userPassword</code><br>
    * Règle : le paramètre <code>userPassword</code> doit être renseigné<br>
    * <br>
    * Pour rappel <code>joinPoint.getArgs()[5]</code> :
    * <code>userPassword</code>
    * 
    * @param joinPoint
    *           joinpoint de la méthode authentifierPourSaeParLoginPassword
    * @throws UserPasswordNonRenseigneException
    */
   @Before(METHODE)
   public final void passwordCheck(JoinPoint joinPoint) {

      String userPassword = (String) joinPoint.getArgs()[USER_PASSWORD];
      if (isEmpty(userPassword)) {
         throw new UserPasswordNonRenseigneException();
      }
   }

   private static boolean isEmpty(String str) {
      return !StringUtils.isNotBlank(str);
   }
}
