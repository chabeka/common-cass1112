package fr.urssaf.image.sae.webservices.security;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.modele.WebServiceConfiguration;
import fr.urssaf.image.sae.webservices.security.igc.IgcService;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;
import fr.urssaf.image.sae.webservices.security.igc.modele.CertifsAndCrl;

/**
 * Service de sécurisation du service web par authentification
 * 
 * 
 */
@Service
public class SecurityService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SecurityService.class);

   private final WebServiceVIService service;

   private final URI serviceVise;

   private final IgcService igcService;

   private final boolean acceptOldWs;

   // private final VISignVerifParams signVerifParams;

   /**
    * Instanciation de {@link WebServiceVIService}
    * 
    * @param igcService
    *           instance IgcService
    * @param service
    *           service du VI
    * @param configuration
    *           configuration du webservice
    */
   @Autowired
   public SecurityService(IgcService igcService, WebServiceVIService service,
         WebServiceConfiguration configuration) {

      Assert.notNull(igcService);

      this.service = service;
      this.igcService = igcService;

      try {
         this.serviceVise = new URI("http://sae.urssaf.fr");
      } catch (URISyntaxException e) {
         throw new IllegalStateException(e);
      }

      this.acceptOldWs = configuration.isAncienWsActif();

   }

   /**
    * Création d'un contexte de sécurité par partir du Vecteur d'indentifcation<br>
    * <br>
    * Paramètres du {@link org.springframework.security.core.Authentication} de
    * type Anonymous
    * <ul>
    * <li>Credentials : empty</li>
    * <li>Principal : {@link VIContenuExtrait#getIdUtilisateur()}</li>
    * <li>Authorities : {@link VIContenuExtrait#getPagm()}</li>
    * </ul>
    * 
    * @param identification
    *           Vecteur d'identification
    * @throws VIVerificationException
    *            exception levée par le VI
    * @throws LoadCertifsAndCrlException
    *            exception levée lors du chargement des crls
    */
   public void authentification(Element identification)
         throws VIVerificationException, LoadCertifsAndCrlException {

      Assert
            .notNull(
                  identification,
                  "Le paramètre 'identification' n'est pas renseigné alors qu'il est obligatoire ");

      VIContenuExtrait viExtrait;

      VISignVerifParams signVerifParams = new VISignVerifParams();
      signVerifParams.setPatternsIssuer(this.igcService.getPatternIssuers());

      CertifsAndCrl certifsAndCrl = this.igcService.getInstanceCertifsAndCrl();
      signVerifParams.setCertifsACRacine(certifsAndCrl.getCertsAcRacine());
      signVerifParams.setCrls(certifsAndCrl.getCrl());

      viExtrait = this.service.verifierVIdeServiceWeb(identification,
            serviceVise, signVerifParams, acceptOldWs);

      logVI(viExtrait);

      Set<String> rolesSet = viExtrait.getSaeDroits().keySet();
      String[] roles = new String[rolesSet.size()];
      rolesSet.toArray(roles);

      AuthenticationToken authentication = AuthenticationFactory
            .createAuthentication(viExtrait.getIdUtilisateur(), viExtrait,
                  roles);

      AuthenticationContext.setAuthenticationToken(authentication);
   }

   private void logVI(VIContenuExtrait viExtrait) {

      String prefixeLog = "Informations extraites du VI : ";

      // LOG des PAGM
      if ((viExtrait != null) && (viExtrait.getSaeDroits() != null)) {
         StringBuffer sBufferMsgLog = new StringBuffer();
         sBufferMsgLog.append(prefixeLog);
         sBufferMsgLog.append("PAGM(s) : ");
         for (String key : viExtrait.getSaeDroits().keySet()) {
            for (SaePrmd prmd : viExtrait.getSaeDroits().get(key)) {
               sBufferMsgLog.append(key);
               sBufferMsgLog.append(';');
               sBufferMsgLog.append(prmd.getPrmd().getCode());
               sBufferMsgLog.append(' ');
            }
         }
         LOG.info(sBufferMsgLog.toString());

         // LOG du code application
         LOG
               .info(prefixeLog + "Code application : "
                     + viExtrait.getCodeAppli());

         // LOG de l'identifiant utilisateur
         LOG.info(prefixeLog + "Identifiant utilisateur : "
               + viExtrait.getIdUtilisateur());
      }

   }

}
