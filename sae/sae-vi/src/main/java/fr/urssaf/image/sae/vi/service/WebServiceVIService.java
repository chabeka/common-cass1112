/**
 * 
 */
package fr.urssaf.image.sae.vi.service;

import java.net.URI;
import java.security.KeyStore;
import java.util.List;

import org.w3c.dom.Element;

import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;

/**
 * 
 * 
 */
public interface WebServiceVIService {

   /**
    * Génération d'un Vecteur d'Identification (VI) pour s'authentifier auprès
    * d'un service web du SAE. <br>
    * Le VI prend la forme d'une assertion SAML 2.0 signée électroniquement. <br>
    * La récupération des droits applicatifs doit être faite en amont.<br>
    * <br>
    * Paramètres obligatoires
    * <ul>
    * <li>pagm (au moins un droit)</li>
    * <li>issuer</li>
    * <li>keystore</li>
    * <li>alias</li>
    * <li>password</li>
    * </ul>
    * 
    * @param pagm
    *           Liste des droits applicatifs
    * @param issuer
    *           L'identifiant de l'application cliente
    * @param idUtilisateur
    *           L'identifiant de l'utilisateur
    * @param keystore
    *           Le certificat applicatif de l'application cliente, sa clé
    *           privée, et la chaîne de certification associée, pour la
    *           signature électronique du VI
    * @param alias
    *           L'alias de la clé privée du KeyStore
    * @param password
    *           mot du de la clé privée
    * @return Le Vecteur d'Identification
    */
   Element creerVIpourServiceWeb(List<String> pagm,
         String issuer, String idUtilisateur, KeyStore keystore, String alias,
         String password);

   /**
    * Vérification d'un Vecteur d'Identification (VI) généré pour s'authentifier
    * auprès d'un service web du SAE<br>
    * 
    * @param identification
    *           Le Vecteur d'Identification à vérifier
    * @param serviceVise
    *           URI décrivant le service visé
    * @param signVerifParams
    *           Les éléments permettant de vérifier la signature électronique du
    *           VI
    * @return Des valeurs extraits du VI qui peuvent être exploités pour mettre
    *         en place un contexte de sécurité basé sur l’authentification,
    *         et/ou pour de la traçabilité
    * @throws VIVerificationException
    *            Les informations extraites du VI sont invalides
    */
   VIContenuExtrait verifierVIdeServiceWeb(
         Element identification, URI serviceVise,
         VISignVerifParams signVerifParams) throws VIVerificationException;

}