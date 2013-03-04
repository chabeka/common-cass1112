package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.TraceFormulaire;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;

/**
 * Classe gérant l'affichage de la page listeCsPrmd.do
 * 
 * 
 */

@Controller
public class TraceVisualisationController {

   private static final String NOM_VUE = "traceVisualisation";

   @Autowired
   private RegTechniqueService regtechniqueService;

   @Autowired
   private JournalEvtService journalEvtService;

   private TraceFormulaire formulaire = new TraceFormulaire();

   @RequestMapping(value = "traceVisualisation", method = RequestMethod.GET)
   public final String defaultView(HttpSession session, Model model) {
      formulaire.setAction("getTrace");
      formulaire.setUrl("traceVisualisation.do");
      formulaire.setTitre("Visualiser les traces");
      model.addAttribute("formulaire", formulaire);
      return NOM_VUE;
   }

   @RequestMapping(value = "journalVisualisation", method = RequestMethod.GET)
   public final String defaultViewJournal(HttpSession session, Model model) {
      // on a le même écran que pour les traces ..
      formulaire.setAction("getJournal");
      formulaire.setUrl("journalVisualisation.do");
      formulaire.setTitre("Visualiser les journaux");
      model.addAttribute("formulaire", formulaire);
      return NOM_VUE;
   }

   public final TraceFormulaire getFormulairePourGet() {

      TraceFormulaire formulaire = new TraceFormulaire();

      return formulaire;
   }

   /**
    * Retourne la liste des traces pour la periode donnée
    * 
    * @param model
    *           données spring
    * @param session
    *           session utilisateur
    * @return la liste traces
    */
   @SuppressWarnings("unchecked")
   @ResponseBody
   @RequestMapping(value = "traceVisualisation", method = RequestMethod.GET, params = "action=getTrace")
   public final Map<String, Object> lectureTrace(Model model,
         HttpSession session,
         @RequestParam(value = "dateDebut", required = true) Date dateDebut,
         @RequestParam(value = "dateFin", required = true) Date dateFin,
         @RequestParam(value = "inverse") boolean inverse,
         @RequestParam(value = "nbTrace") int nbTrace) {

      List listeDetail = new ArrayList();

      int compteur = 0;

      Date dateDebutOk = calculeDateDebutJournee(dateDebut);
      Date dateFinOk = calculeDateFinJournee(dateFin);

      List<TraceRegTechniqueIndex> traces = regtechniqueService.lecture(
            dateDebutOk, dateFinOk, nbTrace, inverse);

      if (CollectionUtils.isNotEmpty(traces)) {

         compteur = traces.size();

         for (TraceRegTechniqueIndex indexTrace : traces) {

            TraceRegTechnique trace = regtechniqueService.lecture(indexTrace
                  .getIdentifiant());

            Map<String, Object> traceInfos = traiteInfos(trace.getInfos());

            traceInfos.put("trace", trace);

            listeDetail.add(traceInfos);

         }
      }

      // Renvoie du résultat en json
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("traces", listeDetail);
      map.put("compteur", compteur);
      return map;

   }

   private Map<String, Object> traiteInfos(Map<String, Object> info) {

      List<String> tracContent = new ArrayList<String>();
      Map<String, Object> traceInfos = new HashMap<String, Object>();

      if (MapUtils.isNotEmpty(info)) {

         String key;
         Object valeur;
         for (Map.Entry<String, Object> entry : info.entrySet()) {
            key = entry.getKey();
            valeur = entry.getValue();
            if (valeur == null) {
               tracContent.add(key + ":" + "null");
            } else {
               if (valeur.toString().length() > 10) {
                  tracContent.add(key + ":"
                        + valeur.toString().substring(0, 10));
               } else {
                  tracContent.add(key + ":" + valeur.toString());
               }
            }
         }

         traceInfos.put("info", tracContent);

      } else {
         traceInfos.put("info", null);
      }

      return traceInfos;

   }

   @ResponseBody
   @RequestMapping(value = "tracePopUp", method = RequestMethod.GET, params = "action=getTracePopUp")
   public final Map<String, Object> lectureTrace(Model model,
         HttpSession session,
         @RequestParam(value = "uuid", required = true) UUID uuid) {

      // List<TraceRegTechnique> listeDetail = new
      // ArrayList<TraceRegTechnique>();

      TraceRegTechnique trace = regtechniqueService.lecture(uuid);
      // listeDetail.add(trace);

      // Renvoie du résultat en json
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("trace", trace);
      return map;

   }

   @SuppressWarnings("unchecked")
   @ResponseBody
   @RequestMapping(value = "tracePopUpInfo", method = RequestMethod.GET, params = "action=getTracePopUpInfo")
   public final List<String> afficheInfo(Model model, HttpSession session,
         @RequestParam(value = "uuid", required = true) UUID uuid) {

      List<String> tracContent = new ArrayList<String>();
      TraceRegTechnique trace = regtechniqueService.lecture(uuid);
      Map<String, Object> info = trace.getInfos();
      Iterator iter = info.keySet().iterator();
      while (iter.hasNext()) {
         String key = iter.next().toString();
         Object valeur = info.get(key);
         if (valeur == null) {
            tracContent.add(key + ":null");
         } else {
            tracContent.add(key + ":"
                  + StringEscapeUtils.escapeHtml(valeur.toString()));
         }
      }
      return tracContent;
   }

   /**
    * Retourne la liste des Constrat service
    * 
    * @param model
    *           données spring
    * @param session
    *           session utilisateur
    * @return la liste des contrat service
    */
   @SuppressWarnings("unchecked")
   @ResponseBody
   @RequestMapping(value = "journalVisualisation", method = RequestMethod.GET, params = "action=getJournal")
   public final Map<String, Object> lectureJournal(Model model,
         HttpSession session,
         @RequestParam(value = "dateDebut", required = true) Date dateDebut,
         @RequestParam(value = "dateFin", required = true) Date dateFin,
         @RequestParam(value = "inverse") boolean inverse,
         @RequestParam(value = "nbTrace") int nbTrace) {

      List listeDetail = new ArrayList();

      int compteur = 0;

      Date dateDebutOk = calculeDateDebutJournee(dateDebut);
      Date dateFinOk = calculeDateFinJournee(dateFin);

      List<TraceJournalEvtIndex> journaux = journalEvtService.lecture(
            dateDebutOk, dateFinOk, nbTrace, inverse);

      if (CollectionUtils.isNotEmpty(journaux)) {

         compteur = journaux.size();

         for (TraceJournalEvtIndex indexJournal : journaux) {

            TraceJournalEvt journal = journalEvtService.lecture(indexJournal
                  .getIdentifiant());

            Map<String, Object> journalInfos = traiteInfos(journal.getInfos());

            journalInfos.put("trace", journal);

            listeDetail.add(journalInfos);

         }
      }

      // Renvoie du résultat en json
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("traces", listeDetail);
      map.put("compteur", compteur);
      return map;

   }

   private Date calculeDateDebutJournee(Date dateDebut) {

      Date dateDebutOk = dateDebut;
      dateDebutOk = DateUtils.setHours(dateDebutOk, 0);
      dateDebutOk = DateUtils.setMinutes(dateDebutOk, 0);
      dateDebutOk = DateUtils.setSeconds(dateDebutOk, 0);
      dateDebutOk = DateUtils.setMilliseconds(dateDebutOk, 0);

      return dateDebutOk;

   }

   private Date calculeDateFinJournee(Date dateFin) {

      Date dateFinOk = dateFin;
      dateFinOk = DateUtils.setHours(dateFinOk, 23);
      dateFinOk = DateUtils.setMinutes(dateFinOk, 59);
      dateFinOk = DateUtils.setSeconds(dateFinOk, 59);
      dateFinOk = DateUtils.setMilliseconds(dateFinOk, 999);

      return dateFinOk;

   }

}
