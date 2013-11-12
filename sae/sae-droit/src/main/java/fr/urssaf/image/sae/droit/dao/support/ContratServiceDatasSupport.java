/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.model.ServiceContractDatas;

/**
 * classe permettant de traiter les informations complètes d'un contrat de
 * service
 * 
 */
@Component
public class ContratServiceDatasSupport {

   private final ContratServiceSupport contratSupport;

   private final PagmSupport pagmSupport;

   private final PagmaSupport pagmaSupport;

   private final PagmpSupport pagmpSupport;

   private final ActionUnitaireSupport actionSupport;

   private final PrmdSupport prmdSupport;

   /**
    * Constructeur
    * @param contratSupport Support des contrats de Service
    * @param pagmSupport Support des PAGM
    * @param pagmaSupport Support des PAGMa
    * @param pagmpSupport Support des PAGMp
    * @param actionSupport Support des Actions unitaires
    * @param prmdSupport Support des PRMD
    */
   @Autowired
   public ContratServiceDatasSupport(ContratServiceSupport contratSupport,
         PagmSupport pagmSupport, PagmaSupport pagmaSupport,
         PagmpSupport pagmpSupport, ActionUnitaireSupport actionSupport,
         PrmdSupport prmdSupport) {
      this.contratSupport = contratSupport;
      this.pagmSupport = pagmSupport;
      this.pagmaSupport = pagmaSupport;
      this.pagmpSupport = pagmpSupport;
      this.actionSupport = actionSupport;
      this.prmdSupport = prmdSupport;

   }

   /**
    * Retourne la liste de tous les contrats de service et leur contenu complet
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à retourner
    * @return la liste des contrats de service
    */
   public final List<ServiceContractDatas> findAll(int maxKeysToRead) {

      List<ServiceContractDatas> list = null;
      List<ServiceContract> contracts = contratSupport.findAll(maxKeysToRead);

      if (CollectionUtils.isNotEmpty(contracts)) {
         list = getAllServiceContractDatas(contracts);
      }

      return list;

   }

   private List<ServiceContractDatas> getAllServiceContractDatas(
         List<ServiceContract> contracts) {

      List<ServiceContractDatas> datas = new ArrayList<ServiceContractDatas>(
            contracts.size());

      for (ServiceContract contract : contracts) {
         datas.add(getAllServiceContractDatas(contract));
      }

      return datas;
   }

   private List<Pagma> findPagmas(List<Pagm> pagms) {

      List<String> listeCodes = new ArrayList<String>();
      for (Pagm pagm : pagms) {
         if (!listeCodes.contains(pagm.getPagma())) {
            listeCodes.add(pagm.getPagma());
         }
      }

      List<Pagma> pagmas = new ArrayList<Pagma>(listeCodes.size());
      for (String code : listeCodes) {
         pagmas.add(pagmaSupport.find(code));
      }

      return pagmas;
   }

   private List<Pagmp> findPagmps(List<Pagm> pagms) {

      List<String> listeCodes = new ArrayList<String>();
      for (Pagm pagm : pagms) {
         if (!listeCodes.contains(pagm.getPagmp())) {
            listeCodes.add(pagm.getPagmp());
         }
      }

      List<Pagmp> pagmps = new ArrayList<Pagmp>(listeCodes.size());
      for (String code : listeCodes) {
         pagmps.add(pagmpSupport.find(code));
      }

      return pagmps;
   }

   private List<ActionUnitaire> findActionsUnitaires(List<Pagma> pagmas) {

      List<String> listeCodes = new ArrayList<String>();

      for (Pagma pagma : pagmas) {

         for (String code : pagma.getActionUnitaires()) {
            if (!listeCodes.contains(code)) {
               listeCodes.add(code);
            }
         }
      }

      List<ActionUnitaire> actions = new ArrayList<ActionUnitaire>(listeCodes
            .size());

      for (String code : listeCodes) {
         actions.add(actionSupport.find(code));
      }

      return actions;
   }

   private List<Prmd> findPrmd(List<Pagmp> pagmps) {
      List<String> listeCodes = new ArrayList<String>();

      for (Pagmp pagmp : pagmps) {
         if (!listeCodes.contains(pagmp.getPrmd())) {
            listeCodes.add(pagmp.getPrmd());
         }
      }

      List<Prmd> prmds = new ArrayList<Prmd>(listeCodes.size());
      for (String code : listeCodes) {
         prmds.add(prmdSupport.find(code));
      }

      return prmds;
   }

   /**
    * Renvoie le CS
    * @param ident identifiant du CS
    * @return le contrat de service
    */
   public final ServiceContractDatas getCs(String ident) {
      ServiceContract contract = contratSupport.find(ident);

      return getAllServiceContractDatas(contract);

   }

   private ServiceContractDatas getAllServiceContractDatas(
         ServiceContract contract) {

      ServiceContractDatas data = new ServiceContractDatas(contract);

      List<Pagm> pagms = pagmSupport.find(data.getCodeClient());
      data.setPagms(pagms);

      data.setPagmas(findPagmas(pagms));
      data.setPagmps(findPagmps(pagms));
      data.setActions(findActionsUnitaires(data.getPagmas()));
      data.setPrmds(findPrmd(data.getPagmps()));

      return data;
   }

}
