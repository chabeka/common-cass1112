package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContractDatas;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceDatasSupport;
import fr.urssaf.image.sae.integration.ihmweb.comparator.PagmComparator;
import fr.urssaf.image.sae.integration.ihmweb.comparator.PrmdComparator;
import fr.urssaf.image.sae.integration.ihmweb.comparator.ServiceContractDatasComparator;

/**
 * Classe gérant l'affichage de la page listeCsPrmd.do
 * 
 * 
 */

@Controller
@RequestMapping(value = "listeCsPrmd")
public class ListeCsPrmdController {

   private static final String NOM_VUE = "listeCsPrmd";

   @Autowired
   private ContratServiceDatasSupport data;

   @RequestMapping(method = RequestMethod.GET)
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
   @RequestMapping(method = RequestMethod.GET, params = "action=getCsList")
   public final HashMap<String, Object> loadTable(Model model,
         HttpSession session) {

      // listes qui vont contenir les differents éléments d'un contrat service
      HashMap listAction = new HashMap<String, ActionUnitaire>();
      HashMap listPrmd = new HashMap<String, Prmd>();
      HashMap listPagma = new HashMap<String, Pagma>();
      HashMap listPagmp = new HashMap<String, Pagmp>();
      
      HashMap listCsRecompose = new HashMap<String, HashMap>();
      ArrayList listPrmdCs = new ArrayList();

      List<ServiceContractDatas> listCs = data.findAll(50);
      
      // Tri des CS par ordre d'identifiant croissant
      Collections.sort(listCs, new ServiceContractDatasComparator());
      
      // Et dans chaque CS, tri des PAGM par ordre croissant
      for (ServiceContractDatas cs : listCs) {
         Collections.sort(cs.getPagms(), new PagmComparator());
      }

      // parcour du résultat pour alimenter les feuilles
      for (ServiceContractDatas cs : listCs) {

         // alimentation de la liste des actions
         List<ActionUnitaire> actions = cs.getActions();
         for (ActionUnitaire action :actions ) {
            if (!listAction.containsKey(action.getCode())) {
               listAction.put(action.getCode(), action);
            }
         }
         // alimentation de la liste des prmd
         for (Prmd prmd : cs.getPrmds()) {
            if (!listPrmd.containsKey(prmd.getCode())) {
               listPrmd.put(prmd.getCode(), prmd);
            }
         }
      }

      // Parcour et alimentation du niveau 2
      for (ServiceContractDatas cs : listCs) {
         // alimentation de la liste des pagma
         for (Pagma pagma : cs.getPagmas()) {
            if (!listPagma.containsKey(pagma.getCode())) {
               ArrayList pagmaActionList = new ArrayList();
               for(String actionCode : pagma.getActionUnitaires()){
                  pagmaActionList.add(listAction.get(actionCode));
               }
               HashMap pagmaData = new HashMap();
               pagmaData.put("actions", pagmaActionList);
               pagmaData.put("code",pagma.getCode() );  
               listPagma.put(pagma.getCode(),pagmaData );
                            
            }
         }

         // alimentation de la liste des pagmp
         for (Pagmp pagmp : cs.getPagmps()) {
            if (!listPagmp.containsKey(pagmp.getCode())) {
               HashMap pagmpPrmd = new HashMap();
               pagmpPrmd.put("pagmp",pagmp);
               pagmpPrmd.put("prmd",listPrmd.get(pagmp.getPrmd()));
               ArrayList metadata = new ArrayList();
               HashMap<String, List<String>> meta = (HashMap<String, List<String>>) ((Prmd)listPrmd.get(pagmp.getPrmd())).getMetadata();
               if(meta!=null){
                  for(String key : meta.keySet()){
                     metadata.add(key +":"+ ((List)meta.get(key)).toString());
                  }
                  pagmpPrmd.put("metaString",metadata);   
               }else{
                  pagmpPrmd.put("metaString",metadata);
               }
               listPagmp.put(pagmp.getCode(),pagmpPrmd );
            }
      
         }
         

      }
      ArrayList listCsFinal = new ArrayList();
      // parcour des resultat pour reconstituer la structure des contrats
      // service
      for (ServiceContractDatas cs : listCs) {
         ArrayList pagmaList = new ArrayList();
         ArrayList pagmList = new ArrayList();
         
         // pour chaque pagm on complète les infos
         for (Pagm pagm : cs.getPagms()) {
            HashMap pagmRecompose = new HashMap();
            pagmRecompose.put("code",pagm.getCode());
            pagmRecompose.put("description",pagm.getDescription());
            pagmRecompose.put("pagmas",listPagma.get(pagm.getPagma()));
            pagmRecompose.put("pagmps",listPagmp.get(pagm.getPagmp()));
            ArrayList parameters = new ArrayList();
            if(pagm.getParametres()!=null){
               for(String key : pagm.getParametres().keySet()){
                  parameters.add(key +":"+pagm.getParametres().get(key));
               }
               pagmRecompose.put("parametres",parameters);   
            }else{
               pagmRecompose.put("parametres",parameters);
            }
            
            pagmList.add(pagmRecompose);
         }
         // on ajoute les composants du contrat service
         HashMap csPourJson = buildCsPourJson(cs, pagmList);

         // on ajout le contrat service à la liste des contrat service.
         listCsFinal.add(csPourJson);
         // on construit une deuxième liste qui sera stockée en session.
         listCsRecompose.put(cs.getCodeClient(), csPourJson);
         
      }
      
      // Alimentation de la liste des associations PRMD-CS

      Iterator codePrmd = listPrmd.keySet().iterator();
      while (codePrmd.hasNext()) {
         ArrayList prmdCs = new ArrayList();
         HashMap prmdCsMap = new HashMap();
         String code =codePrmd.next().toString(); 
         for (ServiceContractDatas cs : listCs) {
            for(Prmd p :cs.getPrmds()){
               if(p.getCode().equals(((Prmd)listPrmd.get(code)).getCode())){
                  prmdCs.add(cs);
               }
            }
         }
         
         ArrayList metadata = new ArrayList();
         HashMap<String, List<String>> meta = (HashMap<String, List<String>>) ((Prmd)listPrmd.get(code)).getMetadata();
         if(meta!=null){
            for(String key : meta.keySet()){
               metadata.add(key +":"+ ((List)meta.get(key)).toString());
            }
            prmdCsMap.put("metadata",metadata);   
         }else{
            prmdCsMap.put("metadata",metadata);
         }
         prmdCsMap.put("cs",prmdCs );
         prmdCsMap.put("prmd",listPrmd.get(code));
         listPrmdCs.add(prmdCsMap);
      }       
      
      // Tri les PRMD par ordre croissant de code
      Collections.sort(listPrmdCs, new PrmdComparator());

      // mise en session de la liste des Contrat service indexée
      session.setAttribute("listCs", listCsRecompose);
      
      // Renvoie du résultat en json
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("cs", listCsFinal);
      map.put("popup", false);
      map.put("listPrmd", listPrmdCs);
      return map;
      
   }
   
   @ResponseBody
   @RequestMapping(method = RequestMethod.GET, params = "action=getCs")
   public final HashMap<String, Object> getCs(@RequestParam String csCode, Model model,
         HttpSession session) {
         HashMap<String, Object> map = new HashMap<String, Object>();
      
            HashMap csList = (HashMap) session.getAttribute("listCs");
            
            map.put("cs", csList.get(csCode));
            map.put("popup", true);
      
            return map;
   }
   
   
   private HashMap buildCsPourJson(
         ServiceContractDatas cs,
         ArrayList pagmList) {

      HashMap csJson = new HashMap();
      
      csJson.put("codeClient",cs.getCodeClient());
      csJson.put("libelle",cs.getLibelle());
      csJson.put("description",cs.getDescription());
      csJson.put("pagms", pagmList);
      
      csJson.put("pki", buildListePki(cs));
      csJson.put("certifClient", buildListeCertifClients(cs));
      if (cs.isVerifNommage()) {
         csJson.put("verifNommageCertifClient", "Oui");
      } else {
         csJson.put("verifNommageCertifClient", "Non");
      }
      
      return csJson;
      
   }
   
   
   private String buildListePki(ServiceContractDatas cs) {
      
      String result = StringUtils.EMPTY;
      
      if (!CollectionUtils.isEmpty(cs.getListPki())) {
         
         for(String pki: cs.getListPki()) {
            result += "\"" + pki + "\" ";
         }
         
      } else {
         result = "\"" + cs.getIdPki() + "\"" ;
      }
      
      return result;
      
   }
   
   
   private String buildListeCertifClients(ServiceContractDatas cs) {
      
      String result = StringUtils.EMPTY;
      
      if (!CollectionUtils.isEmpty(cs.getListCertifsClient() )) {
         
         for(String certifClient: cs.getListCertifsClient()) {
            result += "\"" + certifClient + "\" ";
         }
         
      } else {
         
         if (StringUtils.isNotBlank(cs.getIdCertifClient())) {
            result = "\"" + cs.getIdCertifClient() + "\" ";
         }
         
      }
      
      if (StringUtils.isBlank(result)) {
         result = "aucun";
      }
      
      return result;
      
   }
   
   
}
