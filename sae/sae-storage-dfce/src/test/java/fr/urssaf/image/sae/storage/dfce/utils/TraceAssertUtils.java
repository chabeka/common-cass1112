package fr.urssaf.image.sae.storage.dfce.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegExploitationIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.RegExploitationService;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;

/**
 * Classe utilitaires pour vérifier la traçabilité dans les TU
 */
public final class TraceAssertUtils {

   private static final int JOURNAL_SAE_NB_INFOS = 6;

   private static final int DELTA_ANNEE = 100;

   @Autowired
   private RegTechniqueService regTechniqueService;

   @Autowired
   private RegExploitationService regExploitationService;

   @Autowired
   private RegSecuriteService regSecuriteService;

   @Autowired
   private JournalEvtService journalEvtService;

   /**
    * Vérifie qu'il y a une et une seule trace de dépôt de document dans DFCE
    * dans le journal des événements SAE
    * 
    * @param idDoc
    *           l'identifiant unique du document dans DFCE
    * @param hash
    *           le hash du document
    * @param typeHash
    *           l'algo de hash
    */
   public void verifieTraceDepotDfceDansJournalSae(UUID idDoc, String hash,
         String typeHash) {

      // Vérification sur le nombre de trace trouvées via l'index
      List<TraceJournalEvtIndex> tracesJrnIndex = verifieNombreTracesDansJournalSae(1);

      // Vérifications sur l'index
      TraceJournalEvtIndex traceJrnIndex = tracesJrnIndex.get(0);

      // Le code événement
      Assert.assertEquals("Le code événement est incorrect",
            Constants.TRACE_CODE_EVT_DEPOT_DOC_DFCE, traceJrnIndex
                  .getCodeEvt());

      // Le contexte
      Assert.assertEquals("Le contexte est incorrect", "DepotDocumentDansDFCE",
            traceJrnIndex.getContexte());

      // Le CS
      Assert.assertEquals("Le contrat de service est incorrect",
            "TESTS_UNITAIRES", traceJrnIndex.getContratService());

      // Les PAGM
      Assert.assertEquals("Les PAGM sont incorrects", Arrays.asList("TU_PAGM1",
            "TU_PAGM2"), traceJrnIndex.getPagms());

      // Le login
      Assert.assertEquals("Le login est incorrect", "UTILISATEUR TEST",
            traceJrnIndex.getLogin());

      // Vérifications sur la trace en elle-même
      TraceJournalEvt trace = journalEvtService.lecture(traceJrnIndex
            .getIdentifiant());

      // Le code événement
      Assert.assertEquals("Le code événement est incorrect",
            Constants.TRACE_CODE_EVT_DEPOT_DOC_DFCE, trace.getCodeEvt());

      // Le contexte
      Assert.assertEquals("Le contexte est incorrect", "DepotDocumentDansDFCE",
            trace.getContexte());

      // Le CS
      Assert.assertEquals("Le contrat de service est incorrect",
            "TESTS_UNITAIRES", trace.getContratService());

      // Les PAGM
      Assert.assertEquals("Les PAGM sont incorrects", Arrays.asList("TU_PAGM1",
            "TU_PAGM2"), trace.getPagms());

      // Le login
      Assert.assertEquals("Le login est incorrect", "UTILISATEUR TEST", trace
            .getLogin());

      // Les infos supplémentaires
      Assert.assertNotNull("Les informations ne devraient pas être vide", trace
            .getInfos());
      Assert.assertTrue("Les informations ne devraient pas être vide", MapUtils
            .isNotEmpty(trace.getInfos()));
      Assert.assertEquals("Le nombre d'informations est incorrect",
            JOURNAL_SAE_NB_INFOS, trace.getInfos().size());
      verifieInfo(trace.getInfos(), "saeServeurHostname", HostnameUtil
            .getHostname());
      verifieInfo(trace.getInfos(), "saeServeurIP", HostnameUtil.getIP());
      verifieInfo(trace.getInfos(), "idDoc", idDoc.toString());
      verifieInfo(trace.getInfos(), "hash", hash);
      verifieInfo(trace.getInfos(), "typeHash", typeHash);

      // Pour l'instant, pas de vérification sur la date d'archivage dans DFCE
      // verifieInfo(trace.getInfos(), "dateArchivageDfce", dateArchivageDfce);

   }

   private List<TraceRegTechniqueIndex> verifieNombreTracesDansTraceRegTechnique(
         int expected) {

      List<TraceRegTechniqueIndex> tracesIndex = regTechniqueService.lecture(
            DateUtils.addYears(new Date(), -1), DateUtils.addYears(new Date(),
                  1), DELTA_ANNEE, true);

      if (expected <= 0) {
         assertTrue(
               "La liste des traces du registre de surveillance technique devrait être vide",
               CollectionUtils.isEmpty(tracesIndex));
      } else {
         assertFalse(
               "La liste des traces du registre de surveillance technique ne devrait pas être vide",
               CollectionUtils.isEmpty(tracesIndex));
         assertEquals(
               "Le nombre de traces dans le registre de surveillance technique est incorrect",
               expected, tracesIndex.size());
      }

      return tracesIndex;

   }

   private List<TraceRegExploitationIndex> verifieNombreTracesDansTraceRegExploitation(
         int expected) {

      List<TraceRegExploitationIndex> tracesIndex = regExploitationService
            .lecture(DateUtils.addYears(new Date(), -1), DateUtils.addYears(
                  new Date(), 1), 100, true);

      if (expected <= 0) {
         assertTrue(
               "La liste des traces du registre d'exploitation technique devrait être vide",
               CollectionUtils.isEmpty(tracesIndex));
      } else {
         assertFalse(
               "La liste des traces du registre d'exploitation technique ne devrait pas être vide",
               CollectionUtils.isEmpty(tracesIndex));
         assertEquals(
               "Le nombre de traces dans le registre d'exploitation est incorrect",
               expected, tracesIndex.size());
      }

      return tracesIndex;

   }

   private List<TraceRegSecuriteIndex> verifieNombreTracesDansTraceRegSecurite(
         int expected) {

      List<TraceRegSecuriteIndex> tracesIndex = regSecuriteService.lecture(
            DateUtils.addYears(new Date(), -1), DateUtils.addYears(new Date(),
                  1), 100, true);

      if (expected <= 0) {
         assertTrue(
               "La liste des traces du registre de sécurité devrait être vide",
               CollectionUtils.isEmpty(tracesIndex));
      } else {
         assertFalse(
               "La liste des traces du registre de sécurité ne devrait pas être vide",
               CollectionUtils.isEmpty(tracesIndex));
         assertEquals(
               "Le nombre de traces dans le registre de sécurité est incorrect",
               expected, tracesIndex.size());
      }

      return tracesIndex;

   }

   private List<TraceJournalEvtIndex> verifieNombreTracesDansJournalSae(
         int expected) {

      List<TraceJournalEvtIndex> tracesIndex = journalEvtService.lecture(
            DateUtils.addYears(new Date(), -1), DateUtils.addYears(new Date(),
                  1), 100, true);

      if (expected <= 0) {
         assertTrue("La liste des traces du journal SAE devrait être vide",
               CollectionUtils.isEmpty(tracesIndex));
      } else {
         assertFalse(
               "La liste des traces du journal SAE ne devrait pas être vide",
               CollectionUtils.isEmpty(tracesIndex));
         assertEquals("Le nombre de traces dans le journal SAE est incorrect",
               expected, tracesIndex.size());
      }

      return tracesIndex;

   }

   private void verifieInfo(Map<String, Object> infos, String nomInfo,
         Object valeurAttendue) {

      Assert.assertTrue("L'information " + nomInfo + " est absente", infos
            .containsKey(nomInfo));

      Assert.assertEquals("L'information " + nomInfo + " est incorrect",
            valeurAttendue, infos.get(nomInfo));

   }

   /**
    * Vérifie qu'aucune trace ne soit présente dans aucun des 3 registres
    */
   public void verifieAucuneTraceDansRegistres() {

      verifieNombreTracesDansTraceRegTechnique(0);
      verifieNombreTracesDansTraceRegExploitation(0);
      verifieNombreTracesDansTraceRegSecurite(0);

   }

}
