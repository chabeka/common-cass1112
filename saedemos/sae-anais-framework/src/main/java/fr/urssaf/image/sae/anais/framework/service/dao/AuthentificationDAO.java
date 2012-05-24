package fr.urssaf.image.sae.anais.framework.service.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import recouv.cirti.anais.api.source.AnaisHabilitationInstance;
import recouv.cirti.anais.api.source.AnaisUser;

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

   private static final Logger LOG = LoggerFactory
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
    * Création d'un jeton d'authentification à partir d'un couple login/mot de
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
    *           Le login de l'utilisateur
    * @param userPassword
    *           Le mot de passe de l'utilisateur
    * @param codeInterRegion
    *           Le code de l'inter-région où chercher les habilitations
    * @param codeOrganisme
    *           Le code de l'organisme où chercher les habilitations
    * 
    * @return Le jeton d'authentification sous la forme d'un flux XML
    * @throws VIException
    *            exception lors de la création du jeton
    * @throws AucunDroitException le CTD ne possède aucun droit
    * @throws SaeAnaisApiException
    */
   public final String createXMLToken(String userLogin, String userPassword,
         String codeInterRegion, String codeOrganisme) throws VIException, AucunDroitException {

      try {
         AnaisUser userResult = this.checkUserCredential(userLogin,
               userPassword);

         AnaisUser user = this.getUserInfo(userResult.getDn());

         ArrayList<AnaisHabilitationInstance> hablist = this.getUserHabilitations(
               user.getDn(), codeInterRegion, codeOrganisme);

         String lastname = user.getSn();
         String firstname = user.getGivenname() ;

         LOG.debug(
               "Info connexion : Nom={}, Prenom={}",
               new Object[] {
                     lastname,
                     firstname
               });
         
         LOG.debug("Nombre d'habilitations : {}", hablist.size());

         List<DroitApplicatif> droits = new ArrayList<DroitApplicatif>();
         
         for (AnaisHabilitationInstance hab : hablist) {
            
            LOG.debug(
                  "Droit {} sur {}_{} déployé en {}",
                  new Object[] {
                        hab.getCn(),
                        hab.getCodeapp(),
                        hab.getCodeenv(),
                        hab.getOrgcode() 
                  });
            
            DroitApplicatif droit = ObjectFactory.createDroitAplicatif();

            droit.setCode(hab.getCn());
            droit.setPerimetreValue(hab.getOrgcode());
            
            //TODO: Gestion des différents périmètres de droit : Organisme, Inter-région, National
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
