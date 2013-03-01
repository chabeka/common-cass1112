package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javassist.expr.Instanceof;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
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

   @RequestMapping(value="traceVisualisation", method = RequestMethod.GET)
   public final String defaultView(HttpSession session) {
      return NOM_VUE;
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
   @RequestMapping(value="traceVisualisation", method = RequestMethod.GET, params = "action=getTrace")
   public final Map<String, Object> lectureTrace(Model model,
         HttpSession session,
         @RequestParam(value = "dateDebut", required = true) Date dateDebut,
         @RequestParam(value = "dateFin", required = true) Date dateFin,
         @RequestParam(value = "inverse") boolean inverse,
         @RequestParam(value = "nbTrace") int nbTrace) {

      List listeDetail = new ArrayList();
      
      List<TraceRegTechniqueIndex> traces = regtechniqueService.lecture(dateDebut, dateFin,nbTrace , inverse);
      List<String> tracContent = new ArrayList<String>();
      for(TraceRegTechniqueIndex i : traces){
         TraceRegTechnique trace = regtechniqueService.lecture(i.getIdentifiant());
         Map<String, Object>info = trace.getInfos();
         if(info!=null){
         Iterator iter =info.keySet().iterator(); 
         while(iter.hasNext()){
            String key = iter.next().toString();
             Object valeur = info.get(key);
             if(valeur.toString().length()>10){
                tracContent.add(key+":"+valeur.toString().substring(0, 10));
             }else{
                tracContent.add(key+":"+valeur.toString());
             }
         }
         }
         Map<String, Object> traceInfos = new HashMap<String, Object>();
         traceInfos.put("trace", trace);
         traceInfos.put("info", tracContent);
         listeDetail.add(traceInfos);
      }
      
      // Renvoie du résultat en json
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("traces", listeDetail);
      return map;
      
   }
    
   
   @SuppressWarnings("unchecked")
   @ResponseBody
   @RequestMapping(value="tracePopUp", method = RequestMethod.GET, params = "action=getTracePopUp")
   public final Map<String, Object> lectureTrace(Model model,
         HttpSession session,
         @RequestParam(value = "uuid", required = true) UUID uuid) {

      //List<TraceRegTechnique> listeDetail = new ArrayList<TraceRegTechnique>();
      

         TraceRegTechnique trace = regtechniqueService.lecture(uuid);
      //   listeDetail.add(trace);
      
      // Renvoie du résultat en json
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("trace", trace);
      return map;
      
   }
   
   @SuppressWarnings("unchecked")
   @ResponseBody
   @RequestMapping(value="tracePopUpInfo", method = RequestMethod.GET, params = "action=getTracePopUpInfo")
   public final List<String> afficheInfo(Model model,
         HttpSession session,
         @RequestParam(value = "uuid", required = true) UUID uuid) {
      
      List<String> tracContent = new ArrayList<String>();
      TraceRegTechnique trace = regtechniqueService.lecture(uuid);
      Map<String, Object>info = trace.getInfos();
      Iterator iter =info.keySet().iterator(); 
      while(iter.hasNext()){
         String key = iter.next().toString();
          Object valeur = info.get(key);
          tracContent.add(key+":"+StringEscapeUtils.escapeHtml(valeur.toString()));             
      }
      return tracContent;      
   }
   
}
