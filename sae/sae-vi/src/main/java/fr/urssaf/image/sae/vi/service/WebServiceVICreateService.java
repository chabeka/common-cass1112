/**
 * 
 */
package fr.urssaf.image.sae.vi.service;

import java.security.KeyStore;
import java.util.List;

import org.w3c.dom.Element;

/**
 * 
 * 
 */
public interface WebServiceVICreateService {

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
}
