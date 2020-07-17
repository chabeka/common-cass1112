package fr.urssaf.image.sae.lotinstallmaj.service.utils.cql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.droit.dao.cql.impl.ActionUnitaireCqlDaoImpl;
import fr.urssaf.image.sae.droit.dao.cql.impl.ContratServiceCqlDaoImpl;
import fr.urssaf.image.sae.droit.dao.cql.impl.PagmCqlDaoImpl;
import fr.urssaf.image.sae.droit.dao.cql.impl.PagmaCqlDaoImpl;
import fr.urssaf.image.sae.droit.dao.cql.impl.PagmpCqlDaoImpl;
import fr.urssaf.image.sae.droit.dao.cql.impl.PrmdCqlDaoImpl;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ContratServiceCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmaCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PagmpCqlSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;

@Component
public class DroitsServiceUtilsCQL {

   private static final Logger LOG = LoggerFactory.getLogger(DroitsServiceUtilsCQL.class);

   private final ActionUnitaireCqlSupport actionUnitaireCqlSupport;

   private final PrmdCqlSupport prmdCqlSupport;

   private final PagmaCqlSupport pagmaCqlSupport;

   private final PagmCqlSupport pagmCqlSupport;

   private final PagmpCqlSupport pagmpCqlSupport;

   private final ContratServiceCqlSupport contratServiceCqlSupport;

   @Autowired
   public DroitsServiceUtilsCQL(final CassandraCQLClientFactory ccf) {
      actionUnitaireCqlSupport = new ActionUnitaireCqlSupport(new ActionUnitaireCqlDaoImpl(ccf));
      prmdCqlSupport = new PrmdCqlSupport(new PrmdCqlDaoImpl(ccf));
      pagmaCqlSupport = new PagmaCqlSupport(new PagmaCqlDaoImpl(ccf));
      pagmCqlSupport = new PagmCqlSupport(new PagmCqlDaoImpl(ccf));
      pagmpCqlSupport = new PagmpCqlSupport(new PagmpCqlDaoImpl(ccf));
      contratServiceCqlSupport = new ContratServiceCqlSupport(new ContratServiceCqlDaoImpl(ccf));
   }

   /**
    * Conservation par defaut.
    */
   private static final int DEFAULT_CONSERVATION = 7200;

   private static final String DROIT_CONSULTATION = "consultation";

   private static final String DROIT_RECHERCHE = "recherche";

   private static final String DROIT_TRANSFERT = "transfert";

   private static final String DROIT_SUPPRESSION = "suppression";

   private static final String DROIT_ARCHIVAGE_MASSE = "archivage_masse";

   private static final String DROIT_ARCHIVAGE_UNITAIRE = "archivage_unitaire";

   private static final String DROIT_MODIFICATION = "modification";

   private static final String DROIT_AJOUT_NOTE = "ajoutNote";

   private static final String DROIT_RECHERCHE_PAR_ITERATEUR = "recherche_iterateur";

   private static final String DROIT_AJOUT_DOC_ATTACH = "ajout_doc_attache";

   private static final String DROIT_AJOUT_NOTE_2 = "ajout_note";

   private static final String DROIT_SUPPRESSION_MASSE = "suppression_masse";

   private static final String DROIT_RESTORE_MASSE = "restore_masse";

   private static final String DROIT_MODIFICATION_MASSE = "modification_masse";

   private static final String DROIT_TRANSFERT_MASSE = "transfert_masse";

   private static final String DROIT_DEBLOCAGE = "deblocage";

   private static final String DROIT_REPRISE_MASSE = "reprise_masse";

   private static final String DROIT_COPIE = "copie";

   /**
    * Insertion de données de droits.
    * 
    * @param keyspace
    *           Keyspace
    */
   public final void addDroits() {
      addActionsUnitaires();
      addPrmd();
      addPagma();
      addPagmp();
      addPagm();
      addContratService();
   }

   /**
    * Methode permettant de'ajouter des actions unitaires.
    * 
    * @param keyspace
    *           Keyspace
    */
   private void addActionsUnitaires() {
      addActionUnitaire(DROIT_CONSULTATION, DROIT_CONSULTATION);
      addActionUnitaire(DROIT_RECHERCHE, DROIT_RECHERCHE);
      addActionUnitaire(DROIT_ARCHIVAGE_MASSE, "archivage de masse");
      addActionUnitaire(DROIT_ARCHIVAGE_UNITAIRE, "archivage unitaire");
   }

   /**
    * Methode permettant d'ajouter des PRMD.
    */
   private void addPrmd() {
      final Prmd prmd = new Prmd();
      prmd.setCode("ACCES_FULL_PRMD");
      prmd.setDescription("acces total");
      prmd.setBean("permitAll");

      prmdCqlSupport.create(prmd);
   }

   /**
    * Methode permettant d'ajouter des PAGMA.
    */
   private void addPagma() {
      final Pagma pagma = new Pagma();
      pagma.setCode("ACCES_FULL_PAGMA");
      final List<String> actionUnitaires = Arrays.asList(DROIT_CONSULTATION, DROIT_RECHERCHE, DROIT_ARCHIVAGE_MASSE, DROIT_ARCHIVAGE_UNITAIRE);
      pagma.setActionUnitaires(actionUnitaires);

      pagmaCqlSupport.create(pagma);
   }

   /**
    * Methode permettant d'ajouter des PAGMP.
    */
   private void addPagmp() {

      final Pagmp pagmp = new Pagmp();
      pagmp.setCode("ACCES_FULL_PAGMP");
      pagmp.setDescription("acces pagmp full");
      pagmp.setPrmd("ACCES_FULL_PRMD");

      pagmpCqlSupport.create(pagmp);
   }

   /**
    * Methode permettant d'ajouter des PAGM.
    * 
    * @param keyspace
    *           Keyspace
    */
   private void addPagm() {

      final PagmCql pagmCql = new PagmCql();
      pagmCql.setIdClient("CS_ANCIEN_SYSTEME");
      pagmCql.setCode("ACCES_FULL_PAGM");
      pagmCql.setDescription("Pagm accès total");
      pagmCql.setPagma("ACCES_FULL_PAGMA");
      pagmCql.setPagmp("ACCES_FULL_PAGMP");

      pagmCqlSupport.create(pagmCql);
   }

   /**
    * Methode permettant d'ajouter des contrats de service.
    * 
    */
   private void addContratService() {

      final ServiceContract serviceContract = new ServiceContract();
      serviceContract.setCodeClient("CS_ANCIEN_SYSTEME");
      serviceContract.setLibelle("accès ancien contrat de service");
      serviceContract.setDescription("accès ancien contrat de service");
      serviceContract.setViDuree(Long.valueOf(DEFAULT_CONSERVATION));
      final List<String> listPki = Arrays.asList("CN=IGC/A");
      serviceContract.setListPki(listPki);

      contratServiceCqlSupport.create(serviceContract);

   }

   /**
    * Methode permettant d'ajouter des actions unitaires.
    * 
    * @param identifiant
    *           Identifiant de l'action
    * @param description
    *           Description de l'action
    */
   private void addActionUnitaire(final String identifiant, final String description) {
      final ActionUnitaire actionUnitaire = new ActionUnitaire();
      actionUnitaire.setCode(identifiant);
      actionUnitaire.setDescription(description);
      actionUnitaireCqlSupport.create(actionUnitaire);
   }

   /**
    * Ajout des droits spécifiques GED :
    * <ul>
    * <li>modification</li>
    * <li>suppression</li>
    * <li>transfert</li>
    * </ul>
    * 
    */
   public void addDroitsGed() {
      final List<String> actions = Arrays.asList(DROIT_MODIFICATION, DROIT_SUPPRESSION, DROIT_TRANSFERT);
      for (final String action : actions) {
         addActionUnitaire(action, action);
      }
   }

   /**
    * Ajout de l'action unitaire ajoutNote
    * 
    */
   public void addActionUnitaireNote() {
      addActionUnitaire(DROIT_AJOUT_NOTE, DROIT_AJOUT_NOTE);
   }

   /**
    * Ajout de l'action unitaire recherche_iterateur
    */
   public void addActionUnitaireRechercheParIterateur() {
      addActionUnitaire(DROIT_RECHERCHE_PAR_ITERATEUR, "Recherche par iterateur");
   }

   /**
    * Ajout de l'action unitaire ajout_doc_attache
    */
   public void addActionUnitaireAjoutDocAttache() {
      addActionUnitaire(DROIT_AJOUT_DOC_ATTACH,
            "Ajout de document attache");
   }

   /**
    * Ajout de l'action unitaire ajout_note car oublé dans
    * modifyActionUnitaireAjoutNote
    */
   public void addActionUnitaireNote2() {
      addActionUnitaire(DROIT_AJOUT_NOTE_2, "Ajout de notes");
   }

   /**
    * Ajout de l'action unitaire suppression_masse et restore_masse
    */
   public void addActionUnitaireTraitementMasse() {
      addActionUnitaire(DROIT_SUPPRESSION_MASSE, "Suppression de masse");
      addActionUnitaire(DROIT_RESTORE_MASSE, "Restore de masse");
   }

   /**
    * Ajout de l'action unitaire modification_masse, transfert_masse et
    * deblocage
    */
   public void addActionUnitaireTraitementMasseBis() {
      addActionUnitaire(DROIT_MODIFICATION_MASSE, "modification en masse");
      addActionUnitaire(DROIT_TRANSFERT_MASSE, "transfert de masse");
      addActionUnitaire(DROIT_DEBLOCAGE, "deblocage de traitement de masse");
   }

   /**
    * Ajout de l'action unitaire reprise_masse
    */
   public void addActionUnitaireRepriseMasse() {
      addActionUnitaire(DROIT_REPRISE_MASSE, "reprise de traitement de masse");

   }

   /**
    * Ajout de l'action unitaire Copie Ajout de l'action unitaire
    */
   public  void addActionUnitaireCopie() {
      addActionUnitaire(DROIT_COPIE, "copie d'un document");
   }

   /**
    * Ajout de l'action unitaire suppression et modification Ajout de l'action
    * unitaire
    */
   public void addActionUnitaireSuppressionModification() {
      addActionUnitaire(DROIT_SUPPRESSION, "Suppression unitaire");
      addActionUnitaire(DROIT_MODIFICATION, "Modification unitaire");
   }

   /**
    * On remplace l'action unitaire ajoutNote par ajout_note afin d'être
    * homogène !!! L'AJOUT a été oublié, fait dans la méthode
    * addActionUnitaireNote2 Ajout de l'action unitaire
    */
   public void modifyActionUnitaireAjoutNote() {
      deleteActionUnitaire(DROIT_AJOUT_NOTE);
   }

   /**
    * Methode permettant de supprimer des actions unitaires.
    * 
    * @param identifiant
    *           identifiant
    */
   private void deleteActionUnitaire(final String identifiant) {
      actionUnitaireCqlSupport.delete(identifiant);
   }

   /**
    * Lot 160600 : Mise en place des expression régulières dans les PRMD Il faut
    * donc échapper tous les . déjà présents dans les valeur de métadonnée comme
    * pour les RND
    * Méthode à ne par réutiliser telle qu'elle dans un prochain lot
    * 
    */
   public void majPrmdExpReguliere160600() {

      LOG.info("Mise à jour des contrats de service pour gérer les expressions régulières CQL");


      final List<Prmd> listePrmd = prmdCqlSupport.findAll(500);

      final Prmd prmdEchappe = new Prmd();
      for (final Prmd prmd : listePrmd) {
         LOG.info("Mise à jour du PRMD {}", prmd.getCode());
         prmdEchappe.setBean(prmd.getBean());
         prmdEchappe.setCode(prmd.getCode());
         prmdEchappe.setDescription(prmd.getDescription());
         prmdEchappe.setLucene(prmd.getLucene());

         final Map<String, List<String>> listeMeta = prmd.getMetadata();
         final Map<String, List<String>> listeMetaEchappe = new HashMap<>();

         if (MapUtils.isNotEmpty(listeMeta)) {

            final Iterator<Entry<String, List<String>>> iterator = listeMeta
                  .entrySet()
                  .iterator();
            String key;
            while (iterator.hasNext()) {

               key = iterator.next().getKey();
               final List<String> listeValeur = listeMeta.get(key);
               final List<String> listeValeurEchappe = new ArrayList<>();
               for (String valeur : listeValeur) {
                  // Au cas ou on souhaite rejouer le script, on remplace
                  // d'abord les \. par des .
                  // Si c'est la 1ère execution, ca ne fera rien
                  valeur = valeur.replace("\\.", ".");

                  // Ensuite on remplace les . par des \. afin que le . ne soit
                  // pas pris comme un caractère spécial d'une expression
                  // régulière
                  valeur = valeur.replace(".", "\\.");

                  listeValeurEchappe.add(valeur);

               }
               listeMetaEchappe.put(key, listeValeurEchappe);
            }
         }

         prmdEchappe.setMetadata(listeMetaEchappe);
         prmdCqlSupport.create(prmdEchappe);

      }

      LOG.info("Fin mise à jour des contrats de service CQL");

   }

   public void majPagmCsV2AjoutActionReprise170900() {

      LOG.info("Mise à jour du CS_V2 (ajout de la reprise) CQL");

      final List<Pagm> listePagmV2 = pagmCqlSupport.findByIdClient("CS_V2");

      if (listePagmV2 != null) {

         for (final Pagm pagm : listePagmV2) {
            final Pagma pagma = pagmaCqlSupport.find(pagm.getPagma());
            final List<String> listeActionsU = pagma.getActionUnitaires();
            listeActionsU.add(DROIT_REPRISE_MASSE);
            pagma.setActionUnitaires(listeActionsU);
            pagmaCqlSupport.create(pagma);
         }
         LOG.info("Fin mise à jour du CS_V2");
      } else {
         LOG.info("CS_V2 inexistant, aucune modification");
      }

   }

   public void majPagmCsPourPKINationale180300() {

      LOG.info("Mise à jour des CS (gestion pki nationale CQL)");

      final List<ServiceContract> listeCS = contratServiceCqlSupport.findAll(1000);

      for (final ServiceContract serviceContract : listeCS) {

         final List<String> listePKI = serviceContract.getListPki();
         if (listePKI != null) {

            if (!listePKI.contains("CN=ACOSS_Reseau_des_URSSAF")) {
               listePKI.add("CN=ACOSS_Reseau_des_URSSAF");
            }
            serviceContract.setListPki(listePKI);
            contratServiceCqlSupport.create(serviceContract);
         }
      }

      LOG.info("Fin mise à jour des CS");
   }

}
