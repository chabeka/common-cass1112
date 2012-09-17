package fr.urssaf.image.sae.saml.service;

import org.opensaml.saml2.core.Assertion;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.exception.SamlFormatException;
import fr.urssaf.image.sae.saml.exception.signature.SamlSignatureException;
import fr.urssaf.image.sae.saml.modele.SignatureVerificationResult;
import fr.urssaf.image.sae.saml.opensaml.SamlXML;
import fr.urssaf.image.sae.saml.opensaml.service.SamlAssertionService;
import fr.urssaf.image.sae.saml.opensaml.signature.SamlSignatureValidateService;
import fr.urssaf.image.sae.saml.params.SamlSignatureVerifParams;

/**
 * Vérification technique d'une assertion SAML 2.0 signée électroniquement<br>
 * <br>
 * Le recours à cette classe nécessite une instanciation au préalable de
 * {@link fr.urssaf.image.sae.saml.opensaml.SamlConfiguration}
 * 
 */
public class SamlAssertionVerificationService {

   private final SamlAssertionService assertionService;
   private final SamlSignatureValidateService signValService;

   /**
    * instanciation de {@link SamlAssertionService} pour la validation de la
    * signature
    */
   public SamlAssertionVerificationService() {

      assertionService = new SamlAssertionService();
      signValService = new SamlSignatureValidateService();

   }

   /**
    * Méthode de vérification du corps et de la signature d'un jeton SAML
    * 
    * @param assertionSaml
    *           L'assertion SAML à vérifier
    * @param signVerifParams
    *           Les éléments nécessaires à la vérification de la signature de
    *           l'assertion
    * @return les certificats de la PKI et le certificat client intervenants
    *         dans la vérification de la signature
    * @throws SamlFormatException
    *            Lorsque le format de l’assertion est incorrecte par rapport
    *            au(x) schéma(s) XSD
    * @throws SamlSignatureException
    *            Lorsque la signature électronique de l'assertion n'est pas
    *            valide
    */
   public final SignatureVerificationResult verifierAssertion(
         Element assertionSaml, SamlSignatureVerifParams signVerifParams)
         throws SamlFormatException, SamlSignatureException {

      // Création de l'objet Assertion du framework opensaml à partir
      // de l'objet Element
      Assertion assertion = (Assertion) SamlXML.unmarshaller(assertionSaml);

      // Vérification du format de l'assertion par rapport à ses schémas XSD
      // (valeurs obligatoires, ...)
      assertionService.validate(assertion);

      // Vérification de la signature
      return signValService.verifierSignature(assertion, signVerifParams);

   }

}
