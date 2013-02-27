package fr.urssaf.image.sae.services.capturemasse.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.util.HostnameUtil;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;

/**
 * Classe utilitaires pour vérifier la traçabilité dans les TU
 */
public final class TraceAssertUtils {

   private static final int REG_TECH_NB_INFOS = 7;
   
   private static final int DELTA_ANNEE = 100;
   
   private TraceAssertUtils() {
      // Constructeur privé
   }
   
   /**
    * Vérifie qu'il y a une et une seule trace dans le registre de surveillance
    * technique
    * 
    * @param regTechniqueService
    *           le service permettant d'accéder au registre de surveillance
    *           technique
    * @param debutStackTrace
    *           le début de la stacktrace que l'on doit trouver dans la trace
    * @param idTdm
    *           l'identifiant de la capture de masse, que l'on doit trouver dans
    *           les infos
    * @param urlSommaire
    *           l'URL ECDE du sommaire.xml, que l'on doit trouver dans les infos
    */
   public static void verifieTraceCaptureMasseDansRegTechnique(
         RegTechniqueService regTechniqueService, String debutStackTrace,
         String idTdm, String urlSommaire) {

      int nbTraces = 1;

      // Vérification sur le nombre de trace trouvées via l'index
      List<TraceRegTechniqueIndex> tracesTechIndex = verifieNombreTracesDansTraceRegTechnique(
            regTechniqueService, nbTraces);

      // Vérifications sur l'index

      TraceRegTechniqueIndex traceTechIndex = tracesTechIndex.get(0);

      // Le code événement
      Assert.assertEquals("Le code éventement est incorrect",
            Constantes.TRACE_CODE_EVT_ECHEC_CM, traceTechIndex.getCodeEvt());

      // Le contexte
      Assert.assertEquals("Le code éventement est incorrect", "captureMasse",
            traceTechIndex.getContexte());

      // Le CS
      Assert.assertEquals("Le contrat de service est incorrect",
            "TESTS_UNITAIRES", traceTechIndex.getContrat());

      // Les PAGM
      Assert.assertEquals("Les PAGM sont incorrects", Arrays.asList("TU_PAGM1",
            "TU_PAGM2"), traceTechIndex.getPagms());

      // Le login
      Assert.assertEquals("Le login est incorrect", "UTILISATEUR TEST",
            traceTechIndex.getLogin());

      // Vérifications sur la trace en elle-même

      TraceRegTechnique trace = regTechniqueService.lecture(traceTechIndex
            .getIdentifiant());

      // Le code événement
      Assert.assertEquals("Le code éventement est incorrect",
            Constantes.TRACE_CODE_EVT_ECHEC_CM, trace.getCodeEvt());

      // Le contexte
      Assert.assertEquals("Le code éventement est incorrect", "captureMasse",
            trace.getContexte());

      // Le CS
      Assert.assertEquals("Le contrat de service est incorrect",
            "TESTS_UNITAIRES", trace.getContrat());

      // Les PAGM
      Assert.assertEquals("Les PAGM sont incorrects", Arrays.asList("TU_PAGM1",
            "TU_PAGM2"), trace.getPagms());

      // Le login
      Assert.assertEquals("Le login est incorrect", "UTILISATEUR TEST", trace
            .getLogin());

      // La stacktrace
      Assert.assertTrue("La stacktrace ne devrait pas être vide", StringUtils
            .isNotEmpty(trace.getStacktrace()));
      Assert.assertTrue("La stacktrace ne contient pas la bonne exception",
            StringUtils.startsWith(trace.getStacktrace(), debutStackTrace));

      // Les infos supplémentaires
      // jobParams.capture.masse.idtraitement=b7d58210-34b3-4cc5-936d-e4c9a0902089
      // jobParams.capture.masse.sommaire=ecde://ecde.testunit.recouv/1/20110101/4875110237439144125/sommaire.xml
      // jobParams.hash=null
      // jobParams.typeHash=null
      // batchStatus=COMPLETED
      // saeServeurHostname=CER69-TEC16803
      // saeServeurIP=10.3.104.81
      Assert.assertNotNull("Les informations ne devraient pas être vide", trace
            .getInfos());
      Assert.assertTrue("Les informations ne devraient pas être vide", MapUtils
            .isNotEmpty(trace.getInfos()));
      Assert.assertEquals("Le nombre d'informations est incorrect", REG_TECH_NB_INFOS, trace
            .getInfos().size());
      verifieInfo(trace.getInfos(), "jobParams.capture.masse.idtraitement",
            idTdm);
      verifieInfo(trace.getInfos(), "jobParams.capture.masse.sommaire",
            urlSommaire);
      verifieInfo(trace.getInfos(), "jobParams.hash", null);
      verifieInfo(trace.getInfos(), "jobParams.typeHash", null);
      verifieInfo(trace.getInfos(), "batchStatus", "COMPLETED");
      verifieInfo(trace.getInfos(), "saeServeurHostname", HostnameUtil
            .getHostname());
      verifieInfo(trace.getInfos(), "saeServeurIP", HostnameUtil.getIP());

   }

   private static List<TraceRegTechniqueIndex> verifieNombreTracesDansTraceRegTechnique(
         RegTechniqueService regTechniqueService, int expected) {

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

   private static void verifieInfo(Map<String, Object> infos, String nomInfo,
         Object valeurAttendue) {

      Assert.assertTrue("L'information " + nomInfo + " est absente", infos
            .containsKey(nomInfo));

      Assert.assertEquals("L'information " + nomInfo + " est incorrect",
            valeurAttendue, infos.get(nomInfo));

   }

}
