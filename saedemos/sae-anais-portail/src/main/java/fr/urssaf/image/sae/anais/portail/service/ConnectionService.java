package fr.urssaf.image.sae.anais.portail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.anais.framework.config.SaeAnaisConfig;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAuth;
import fr.urssaf.image.sae.anais.framework.service.SaeAnaisService;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.portail.configuration.AppliSaeConfig;
import fr.urssaf.image.sae.anais.portail.exception.PortailRuntimeException;
import fr.urssaf.image.sae.anais.portail.exception.VIBuildException;

/**
 * Classe de service pour la connexion à ANAIS
 */
@Service
public class ConnectionService {

   private final SaeAnaisService service;

   @Autowired
   private AppliSaeConfig appliSaeConfig;

   @Autowired
   @Qualifier("SAE_IHM_EXPLOIT")
   private VIService viServicePourIhmExploit;

   /**
    * Initialisation de la configuration à ANAIS
    * 
    * @param configuration
    *           configuration à ANAIS
    */
   @Autowired
   public ConnectionService(
         @Qualifier("saeAnaisConfig") SaeAnaisConfig anaisConfig) {

      this.service = new SaeAnaisService(anaisConfig);

   }

   /**
    * Récupération des habilitations de l'agent
    * 
    * @param userLogin
    *           login de l'agent
    * @param userPassword
    *           mot de passe de l'agent
    * @return Vecteur d'identification au format XML
    * @throws AucunDroitException
    *            l'agent ne possède aucun droit
    * @throws VIBuildException
    *            si un problème se produit pendant la création du VI
    */
   public final String connect(String userLogin, String userPassword)
         throws AucunDroitException, VIBuildException {

      // Récupération des habilitations de l'agent depuis ANAIS
      SaeAnaisAuth auth = service.habilitationsAnais(userLogin, userPassword,
            null, null);

      // Récupération du bean de génération du VI
      // Ce bean est fonction de l'application sur laquelle est branchée le
      // portail
      // Cette application est spécifiée sous la forme d'un code dans le fichier
      // properties
      // du portail, dans la clé codeAppliRedirection
      String codeAppli = appliSaeConfig.getCodeAppli();
      VIService viService;
      if ("SAE_IHM_EXPLOIT".equals(codeAppli)) {
         viService = viServicePourIhmExploit;
      } else {
         throw new PortailRuntimeException("Le code application " + codeAppli
               + " est inconnu du portail");
      }

      // Génération du VI
      String vi = viService.buildVI(auth);

      // Renvoie le VI
      return vi;

   }

}
