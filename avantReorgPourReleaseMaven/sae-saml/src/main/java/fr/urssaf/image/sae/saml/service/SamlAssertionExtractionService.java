package fr.urssaf.image.sae.saml.service;

import org.opensaml.saml2.core.Assertion;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.opensaml.SamlXML;
import fr.urssaf.image.sae.saml.opensaml.service.SamlAssertionService;

/**
 * Extraction des données d'une assertion SAML 2.0<br>
 * <br>
 * Le recours à cette classe nécessite une instanciation au préalable de
 * {@link fr.urssaf.image.sae.saml.opensaml.SamlConfiguration}
 * 
 */
public class SamlAssertionExtractionService {

   private final SamlAssertionService assertionService;

   /**
    * Configuration de la libraire OpenSAML {@link SAMLConfiguration#init()}<br>
    * initialisation de {@link SamlAssertionService}
    */
   public SamlAssertionExtractionService() {

      assertionService = new SamlAssertionService();

   }

   /**
    * Extraction des données du vecteur d'identification depuis une assertion
    * SAML 2.0
    * 
    * @param assertionSaml
    *           L'assertion SAML 2.0 à vérifier
    * 
    * @return Les données du vecteur d'identification présentes dans l'assertion
    *         SAML
    *         
    */
   public final SamlAssertionData extraitDonnees(Element assertionSaml) {

      Assertion assertion = (Assertion) SamlXML.unmarshaller(assertionSaml);

      return assertionService.load(assertion);

   }

}
