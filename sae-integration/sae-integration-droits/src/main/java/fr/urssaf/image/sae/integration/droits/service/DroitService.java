package fr.urssaf.image.sae.integration.droits.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.service.SaeActionUnitaireService;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.droit.service.SaePagmaService;
import fr.urssaf.image.sae.droit.service.SaePagmpService;
import fr.urssaf.image.sae.droit.service.SaePrmdService;
import fr.urssaf.image.sae.integration.droits.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.droits.factory.ObjectFactory;
import fr.urssaf.image.sae.integration.droits.modele.xml.CsType;
import fr.urssaf.image.sae.integration.droits.modele.xml.DroitType;
import fr.urssaf.image.sae.integration.droits.modele.xml.PagmType;
import fr.urssaf.image.sae.integration.droits.modele.xml.PrmdType;
import fr.urssaf.image.sae.integration.droits.utils.JAXBUtils;
import fr.urssaf.image.sae.integration.droits.utils.LogUtils;


/**
 * Service pour la création des jeux de test pour les droits
 */
@Service
public final class DroitService {

   private static final Logger LOG = LoggerFactory.getLogger(DroitService.class);
   
   @Autowired
   private SaePrmdService saePrmdService;
   
   @Autowired
   private SaeDroitService saeDroitService;
   
   @Autowired
   private SaePagmaService saePagmaService;
   
   @Autowired
   private SaePagmpService saePagmpService;
   
   @Autowired
   private SaeActionUnitaireService saeAuService;
   
   
   /**
    * Service de créations des droits dans Cassandra (PRMD et CS)
    * @param fichierDroitsXml le fichier contenant les droits à créer
    */
   public void creationDesDroits(File fichierDroitsXml) {
      
      // Init
      String cheminFichier = fichierDroitsXml.getAbsolutePath();
      
      // Traces
      LOG.info("Création des droits à partir du fichier {}", cheminFichier);
      
      // Chargement du fichier XML
      LOG.debug("Chargement du fichier XML");
      DroitType droitType = chargeFichierXml(cheminFichier);
      
      // Traitement des PRMD
      traitementPrmd(droitType);
      
      // Traitement des CS
      traitementCs(droitType);
      
      // Traces
      LOG.info("Création des droits terminée");
      
   }
   
   
   private DroitType chargeFichierXml(String cheminFichierDroitsXml) {

      try {
         
         return JAXBUtils.unmarshalAvecXsdDansRess(
               DroitType.class,
               cheminFichierDroitsXml,
               "/xsd/saedroits/saedroits.xsd");
         
      } catch (Exception ex) {
         throw new IntegrationRuntimeException(
               "Erreur lors du chargement du fichier XML contenant les droits à créer",ex);
      }
      
   }
   
   
   private void traitementPrmd(DroitType droitType) {
      
      int nbPrmd = droitType.getListePrmd().getPrmd().size();
      LOG.info("Nombre de PRMD contenus dans le fichier : {}", nbPrmd);
      if (nbPrmd>0) {

         // Trace
         LOG.info("Traitement des PRMD");
         
         // Boucle sur la liste des PRMD
         for(PrmdType prmdType: droitType.getListePrmd().getPrmd()) {
            
            // Init boucle
            String codePrmd = prmdType.getCode();
            
            // Trace
            LOG.info("Traitement du PRMD \"{}\"", codePrmd);
            
            // On regarde si le PRMD existe déjà
            // Si oui, on passe au PRMD suivant
            // Si non, on le créé
            boolean prmdExists = saePrmdService.prmdExists(codePrmd);
            if (prmdExists) {
               
               // Trace
               LOG.info("Le PRMD \"{}\" existe déjà dans la base. On passe au suivant.", codePrmd);
               
            } else {
               
               // Création d'un objet Prmd à passer ensuite au service de création
               // des PRMD de sae-droit
               Prmd prmd = ObjectFactory.createPrmd(prmdType);
               
               // Traces
               logPrmd(prmd);
                        
               // Appel du service de sae-droit pour créer les PRMD
               LOG.debug("Appel du service de sae-droit pour créer le PRMD");
               saePrmdService.createPrmd(prmd);
               
               // Trace
               LOG.debug("Traitement du PRMD \"{}\" terminé", codePrmd);
               
            }
            
         }
         
         // Trace
         LOG.info("Traitement des PRMD terminé");
      }
      
   }
   
   
   private void traitementCs(DroitType droitType) {
      
      int nbCs = droitType.getListeCs().getCs().size();
      LOG.info("Nombre de CS contenus dans le fichier : {}", nbCs);
      if (nbCs>0) {
      
         // Trace
         LOG.info("Traitement des CS");
         
         // Boucle sur la liste des CS
         for(CsType csType: droitType.getListeCs().getCs()) {
            
            // Init boucle
            String codeCs = csType.getIssuer();
            
            // Trace
            LOG.info("Traitement du CS \"{}\"", codeCs);
            
            // On regarde si le CS existe déjà
            // Si oui, on passe au CS suivant
            // Si non, on le créé
            boolean csExists = saeDroitService.contratServiceExists(codeCs);
            if (csExists) {
               
               // Trace
               LOG.info("Le CS \"{}\" existe déjà dans la base. On passe au suivant.", codeCs);
               
            } else {
               
               // Création de l'objet "Contrat de Service" requis pour le service de sae-droit
               ServiceContract serviceContract = ObjectFactory.createCs(csType);
               
               // Traces
               logCs(serviceContract);
               
               // Traitement des PAGM
               int nbPagm = csType.getPagms().getPagm().size();
               LOG.debug("Nombre de PAGM contenus dans les CS \"{}\" : {}", codeCs, nbPagm);
               LOG.debug("Traitement des PAGM");
               List<Pagm> pagms = new ArrayList<Pagm>();
               for (PagmType pagmType : csType.getPagms().getPagm()) {
                  
                  // Init boucle
                  String codePagm = pagmType.getCode();
                  
                  // Trace
                  LOG.debug("Traitement préliminaire du PAGM \"{}\"", codePagm);
                  
                  // Création de l'objet Pagm requis par le service de sae-droit
                  Pagm pagm = ObjectFactory.createPagm(pagmType);
                  pagms.add(pagm);
                  
                  // Création de l'objet Pagma requis par le service de sae-droit
                  Pagma pagma = ObjectFactory.createPagma(pagmType.getPagma());
                  
                  // Création de l'objet Pagmp requis par le service de sae-droit
                  Pagmp pagmp = ObjectFactory.createPagmp(pagmType.getPagmp());
                  
                  // Traces
                  logPagm(pagm, pagma, pagmp);
                  
                  // Appel du service de sae-droit pour créer le PAGMa
                  saePagmaService.createPagma(pagma);
                  
                  // Appel du service de sae-droit pour créer le PAGMp
                  saePagmpService.createPagmp(pagmp);
                  
                  // Trace
                  LOG.debug("Traitement préliminaire du PAGM \"{}\" terminé", codePagm);
                  
               }
               
               // Appel du service de sae-droit pour créer la base du contrat de service
               LOG.debug("Appel du service de création du CS \"{}\" et des PAGM en bdd", codeCs);
               saeDroitService.createContratService(serviceContract, pagms);
               LOG.debug("CS \"{}\" créé avec succès", codeCs);
               
               // Trace
               LOG.debug("Traitement du CS \"{}\" terminé", codeCs);
               
            }
            
            
            
            
            
         }
         
         // Trace
         LOG.info("Traitement des CS terminé");
         
      }
   }
   
   
   private void logPrmd(Prmd prmd) {
      
      LOG.debug("Information sur le PRMD \"{}\"", prmd.getCode());
      LOG.debug("Description : {}", prmd.getDescription());
      LOG.debug("Requête LUCENE : {}", prmd.getLucene());
      LOG.debug("Métadonnées : {}", LogUtils.mapToString(prmd.getMetadata()));
      LOG.debug("Bean : {}", prmd.getBean());
      
   }
   
   
   private void logCs(ServiceContract serviceContract) {
      
      LOG.debug("Informations sur le CS \"{}\"", serviceContract.getCodeClient());
      LOG.debug("Code intelligible : {}", serviceContract.getLibelle());
      LOG.debug("Description : {}", serviceContract.getDescription());
      LOG.debug("Durée de vie d'un VI (en secondes) : {}", serviceContract.getViDuree());
      
   }
   
   private void logPagm(Pagm pagm, Pagma pagma, Pagmp pagmp) {
      
      LOG.debug("Informations sur le PAGM \"{}\"", pagm.getCode());
      
      LOG.debug("Description : {}", pagm.getDescription());
      
      LOG.debug("PAGMa - Code: {}", pagm.getPagma());
      LOG.debug("PAGMa - AU: {}", LogUtils.listeToString(pagma.getActionUnitaires()));
      
      LOG.debug("PAGMp - Code : {}", pagm.getPagmp());
      LOG.debug("PAGMp - Description : {}", pagmp.getDescription());
      LOG.debug("PAGMp - PRMD : {}", pagmp.getPrmd());
      
      LOG.debug("Paramètres dynamiques : {}", LogUtils.mapToString(pagm.getParametres()));
      
   }
   
   
   /**
    * Temporaire : doit être remplacé par une évol de sae-lotinstallmaj
    */
   public void creationDesAu() {
      
      ActionUnitaire actionUnitaire = new ActionUnitaire();
      
      actionUnitaire.setCode("consultation");
      actionUnitaire.setDescription("Consultation");
      saeAuService.createActionUnitaire(actionUnitaire);
      
      actionUnitaire.setCode("archivage_unitaire");
      actionUnitaire.setDescription("Archivage unitaire");
      saeAuService.createActionUnitaire(actionUnitaire);
      
      actionUnitaire.setCode("archivage_masse");
      actionUnitaire.setDescription("Archivage de masse");
      saeAuService.createActionUnitaire(actionUnitaire);
      
      actionUnitaire.setCode("recherche");
      actionUnitaire.setDescription("Recherche");
      saeAuService.createActionUnitaire(actionUnitaire);
      
      
   }
   

}
