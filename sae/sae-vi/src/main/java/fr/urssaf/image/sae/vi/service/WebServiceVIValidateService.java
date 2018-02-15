/**
 * 
 */
package fr.urssaf.image.sae.vi.service;

import java.net.URI;
import java.util.Date;

import org.w3c.dom.Element;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.modele.SignatureVerificationResult;
import fr.urssaf.image.sae.vi.exception.VIAppliClientException;
import fr.urssaf.image.sae.vi.exception.VICertificatException;
import fr.urssaf.image.sae.vi.exception.VIFormatTechniqueException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VINivAuthException;
import fr.urssaf.image.sae.vi.exception.VIPagmIncorrectException;
import fr.urssaf.image.sae.vi.exception.VIServiceIncorrectException;
import fr.urssaf.image.sae.vi.exception.VISignatureException;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;

/**
 * 
 * 
 */
public interface WebServiceVIValidateService {

   /**
    * 
    * Validation de la structure XML de l'assertion et de sa signature
    * électronique du jeton SAML
    * 
    * @param identification
    *           jeton SAML
    * @param signVerifParams
    *           les informations permettant de vérifier la signature du VI
    * @return l'objet contenant les certificats clients et de la PKUI
    *         intervenants dans la vérification de la signature
    * @throws VIFormatTechniqueException
    *            Une erreur technique sur le format du VI a été détectée
    * @throws VISignatureException
    *            La signature électronique du VI est incorrecte
    */
   SignatureVerificationResult validate(Element identification,
         VISignVerifParams signVerifParams) throws VIFormatTechniqueException,
         VISignatureException;

   /**
    * Validation supplémentaires des informations extraites du jeton SAML
    * 
    * <ul>
    * <li>date systeme postérieure à notOnBefore</li>
    * <li>date systeme strictement antérieure à notOnOrAfte</li>
    * <li>serviceVise identique à audience</li>
    * <li>idAppliClient identique à issuer</li>
    * <li>methodAuthn2</li>
    * <li>PAGM</li>
    * </ul>
    * 
    * @param data
    *           information du jeton SAML à vérifier
    * @param serviceVise
    *           URI décrivant le service visé
    * @param idAppliClient
    *           Identifiant de l'application consommatrice du service
    * @param systemDate
    *           date du système
    * @throws VIInvalideException
    *            Le VI est invalide
    * @throws VIAppliClientException
    *            Le service visé ne correspond pas au service indiqué dans
    *            l'assertion
    * @throws VINivAuthException
    *            Le niveau d'authentification initial n'est pas conforme au
    *            contrat d'interopérabilité
    * @throws VIPagmIncorrectException
    *            Le ou les PAGM présents dans le VI sont invalides
    * @throws VIServiceIncorrectException
    *            Le service visé ne correspond pas au service indiqué dans
    *            l'assertion
    */
   void validate(SamlAssertionData data, URI serviceVise, String idAppliClient,
         Date systemDate) throws VIInvalideException, VIAppliClientException,
         VINivAuthException, VIPagmIncorrectException,
         VIServiceIncorrectException;

   /**
    * Vérification que les certificats client et de la PKI mis en jeu pour la
    * vérification de la signature sont ceux attendus
    * 
    * @param contract
    *           contrat de service défini
    * @param result
    *           certificats utilisés
    * @throws VICertificatException
    *            exception levée lorsque l'un des certificats utilisés (pki ou
    *            client) utilisé n'est pas celui attendu
    */
   void validateCertificates(ServiceContract contract, SignatureVerificationResult result)
         throws VICertificatException;

}