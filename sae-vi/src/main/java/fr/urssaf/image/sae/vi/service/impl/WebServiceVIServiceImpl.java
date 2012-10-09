package fr.urssaf.image.sae.vi.service.impl;

import java.net.URI;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.saml.data.SamlAssertionData;
import fr.urssaf.image.sae.saml.exception.SamlExtractionException;
import fr.urssaf.image.sae.saml.modele.SignatureVerificationResult;
import fr.urssaf.image.sae.saml.service.SamlAssertionCreationService;
import fr.urssaf.image.sae.saml.service.SamlAssertionExtractionService;
import fr.urssaf.image.sae.vi.exception.VIAppliClientException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VIPagmIncorrectException;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.vi.service.WebServiceVIValidateService;

/**
 * Classe d'implémentation du service {@link WebServiceVIService}
 * 
 */
@Component
public class WebServiceVIServiceImpl implements WebServiceVIService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(WebServiceVIServiceImpl.class);

   private static final String OLD_CS = "CS_ANCIEN_SYSTEME";

   private static final String OLD_PAGM = "ROLE_TOUS;FULL";

   private static final String TRC_CHECK = "verifierVIdeServiceWeb";

   private final SamlAssertionExtractionService extractService;

   private final WebServiceVIValidateService validateService;

   private final SaeDroitService droitService;

   /**
    * instanciation de {@link SamlAssertionCreationService}<br>
    * instanciation de {@link SamlAssertionExtractionService}<br>
    * instanciation de {@link WebServiceVIValidateServiceImpl}<br>
    * 
    * @param droitService
    *           service permettant de réaliser les opérations sur les droits
    * 
    */
   @Autowired
   public WebServiceVIServiceImpl(SaeDroitService droitService,
         WebServiceVIValidateService validateService,
         ContratServiceSupport support) {
      extractService = new SamlAssertionExtractionService();

      this.validateService = validateService;

      this.droitService = droitService;
   }

   /**
    * {@inheritDoc}
    */
   public final VIContenuExtrait verifierVIdeServiceWeb(Element identification,
         URI serviceVise, VISignVerifParams signVerifParams, boolean acceptOldWs)
         throws VIVerificationException {

      // vérification du jeton SAML
      SignatureVerificationResult result = validateService.validate(
            identification, signVerifParams);

      // extraction du jeton SAML
      SamlAssertionData data;
      try {
         data = extractService.extraitDonnees(identification);
      } catch (SamlExtractionException exception) {
         throw new VIInvalideException(exception.getMessage(), exception);
      }

      String issuer = data.getAssertionParams().getCommonsParams().getIssuer();

      boolean isOldRole = data.getAssertionParams().getCommonsParams()
            .getPagm().size() == 1
            && data.getAssertionParams().getCommonsParams().getPagm().contains(
                  OLD_PAGM);

      LOGGER.debug("{} - Appel du service avec l'ancien role : "
            + (isOldRole ? "oui" : "non"), TRC_CHECK);

      LOGGER.debug("{} - Connexion avec ancien mode d'appel accepté "
            + (acceptOldWs ? "oui" : "non"), TRC_CHECK);

      if (isOldRole && !acceptOldWs) {
         throw new VIInvalideException(
               "le webservice n'accepte pas le mode d'appel sans autorisations");
      }

      if (isOldRole) {
         LOGGER.info("{} - Utilisation du contrat de service " + OLD_CS,
               TRC_CHECK);
         data.getAssertionParams().getCommonsParams().setPagm(
               Arrays.asList(new String[] { "ACCES_FULL_PAGM" }));
         issuer = OLD_CS;
      }

      // Extraction de l'identifiant de l'application cliente depuis le
      // certificat
      // de la clé publique de signature du VI
      // l'extraction du CN vient du code
      // http://stackoverflow.com/questions/2914521/how-to-extract-cn-from-x509certificate-in-java
      String idAppliCliente;
      try {
         X509Principal principal = PrincipalUtil.getSubjectX509Principal(data
               .getClePublique());
         idAppliCliente = (String) principal.getValues(X509Name.CN).get(0);
      } catch (CertificateEncodingException e) {
         throw new IllegalStateException(e);
      }

      // vérification supplémentaires sur le jeton SAML
      validateService.validate(data, serviceVise, idAppliCliente, new Date());

      List<String> pagms = data.getAssertionParams().getCommonsParams()
            .getPagm();

      // vérification que les certificats qui entrent en jeu sont ceux attendus
      ServiceContract contract = this.droitService.getServiceContract(issuer);
      validateService.validateCertificates(contract, result);

      // Extraction des PAGM du VI
      SaeDroits saeDroits;
      try {
         saeDroits = this.droitService.loadSaeDroits(issuer, pagms);

      } catch (ContratServiceNotFoundException exception) {
         throw new VIAppliClientException(exception.getMessage());

      } catch (PagmNotFoundException exception) {
         throw new VIPagmIncorrectException(exception.getMessage());

      } catch (RuntimeException exception) {
         throw new VIInvalideException(exception.getMessage(), exception);
      }

      // instanciation de la valeur retour
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setSaeDroits(saeDroits);
      extrait.setIdUtilisateur(data.getAssertionParams().getSubjectId2());
      extrait.setCodeAppli(issuer);

      // Renvoie du résultat
      return extrait;

   }
}
