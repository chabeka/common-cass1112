package fr.urssaf.image.sae.anais.framework.service.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import anaisJavaApi.AnaisHabilitation;
import anaisJavaApi.AnaisHabilitationList;
import anaisJavaApi.AnaisUserInfo;
import anaisJavaApi.AnaisUserResult;
import fr.urssaf.image.sae.anais.framework.component.AnaisConnectionSupport;
import fr.urssaf.image.sae.anais.framework.component.ConnectionFactory;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.DroitApplicatif;
import fr.urssaf.image.sae.vi.modele.ObjectFactory;
import fr.urssaf.image.sae.vi.service.VIService;

/**
 * Classe de type DAO sur le serveur ANAIS<br>
 * Cette classe permet d'accéder aux différentes méthodes de ANAIS à travers la
 * classe {@link AnaisConnectionSupport}<br>
 * Les méthodes implémentées suivent le modèle: <code><br>
 * try {<br><br>
   &nbsp;&nbsp;//APPEL DES METHODES DE AnaisConnectionSupport<br><br>
   } finally {<br>
   &nbsp;&nbsp;this.close();<br>
   }<br>
 * </code>
 * 
 * 
 * @see ConnectionFactory
 */
public class AuthentificationDAO extends AnaisConnectionSupport {

   private static final Logger LOG = Logger
         .getLogger(AnaisConnectionSupport.class);

   private static final String TYPE_PERIMETRE = "URSSAF - Code organisme";

   private final VIService viService;

   /**
    * initialise la connection factory<br>
    * instanciation de {@link VIService}
    * 
    * @param connectionFactory
    *           connection factory pour le serveur ANAIS
    */
   public AuthentificationDAO(ConnectionFactory connectionFactory) {
      super(connectionFactory);
      this.viService = new VIService();
   }

   /**
    * Création d’un jeton d’authentification à partir d’un couple login/mot de
    * passe<br>
    * <br>
    * La création s'effectue en appelant la méthode
    * {@link VIService#createVI(String, String, List)}<br>
    * <br>
    * <code>lastname</code> et <code>firstname</code> sont récupérés à partir
    * {@link AnaisUserInfo#getInfo(String)}
    * <ul>
    * <li><code>lastname</code> : attribut:"sn"</li>
    * <li><code>firstname</code> : attribut:"givenName"</li>
    * </ul>
    * <code>droits</code> sont récupérés à partir de
    * {@link AnaisHabilitationList}<br>
    * <br>
    * <ul>
    * <li><code>code</code> : {@link AnaisHabilitation#getHabilitation()}</li>
    * <li><code>perimetreValue</code> : {@link AnaisHabilitation#getOrganisme()}
    * </li>
    * <li><code>perimetreType</code> : "URSSAF - Code organisme"</li>
    * </ul>
    * 
    * @param userLogin
    *           Le login de l’utilisateur
    * @param userPassword
    *           Le mot de passe de l’utilisateur
    * @param codeInterRegion
    *           Le code de l’inter-région où chercher les habilitations
    * @param codeOrganisme
    *           Le code de l’organisme où chercher les habilitations
    * 
    * @return Le jeton d’authentification sous la forme d’un flux XML
    * @throws VIException
    *            exception lors de la création du jeton
    * @throws AucunDroitException le CTD ne possède aucun droit
    * @throws SaeAnaisApiException
    */
   public final String createXMLToken(String userLogin, String userPassword,
         String codeInterRegion, String codeOrganisme) throws VIException, AucunDroitException {

      try {
         AnaisUserResult userResult = this.checkUserCredential(userLogin,
               userPassword);

         AnaisUserInfo userInfo = this.getUserInfo(userResult.getUserDn());

         AnaisHabilitationList hablist = this.getUserHabilitations(userResult
               .getUserDn(), codeInterRegion, codeOrganisme);

         String lastname = userInfo.getInfo("sn");
         String firstname = userInfo.getInfo("givenName");

         LOG.debug("INFO CONNECTION");
         LOG.debug(userInfo);
         LOG.debug("sn:" + lastname);
         LOG.debug("givenName:" + firstname);

         List<DroitApplicatif> droits = new ArrayList<DroitApplicatif>();
         for (AnaisHabilitation hab : hablist.getHabilitationsList()) {
            LOG.debug("habilitation:" + hab.getHabilitation());
            LOG.debug(hab.getOrganisme());

            DroitApplicatif droit = ObjectFactory.createDroitAplicatif();

            droit.setCode(hab.getHabilitation());
            droit.setPerimetreValue(hab.getOrganisme());
            droit.setPerimetreType(TYPE_PERIMETRE);

            droits.add(droit);

         }
         
         if(droits.isEmpty()){
            throw new AucunDroitException();
         }

         return viService.createVI(lastname, firstname, droits);

      } finally {
         this.close();
      }

   }

}
