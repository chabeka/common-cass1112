/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.serializer.exception.ActionUnitaireReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmaReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PagmpReferenceException;
import fr.urssaf.image.sae.droit.dao.serializer.exception.PrmdReferenceException;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmaSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmpSupport;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.exception.ContractNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;

/**
 * Classe d'implémentation du service {@link SaeDroitService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaeDroitServiceImpl implements SaeDroitService {

   private final LoadingCache<String, ServiceContract> contratsCache;
   private final LoadingCache<String, List<Pagm>> pagmsCache;
   private final LoadingCache<String, Pagma> pagmasCache;
   private final LoadingCache<String, Pagmp> pagmpsCache;
   private final LoadingCache<String, Prmd> prmdsCache;
   private final LoadingCache<String, ActionUnitaire> actionsCache;

   /**
    * Constructeur
    * 
    * @param contratSupport
    *           support pour les contratsCache de service
    * @param pagmSupport
    *           support pour les pagm
    * @param pagmaSupport
    *           support pour les pagma
    * @param pagmpSupport
    *           support pour les pagmp
    * @param actionSupport
    *           support pour les actions unitaires
    * @param prmdSupport
    *           support pour les prmd
    */
   @Autowired
   public SaeDroitServiceImpl(final ContratServiceSupport contratSupport,
         final PagmSupport pagmSupport, final PagmaSupport pagmaSupport,
         final PagmpSupport pagmpSupport,
         final ActionUnitaireSupport actionSupport,
         final PrmdSupport prmdSupport) {
      contratsCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, ServiceContract>() {

               @Override
               public ServiceContract load(String identifiant) {
                  return contratSupport.find(identifiant);
               }

            });

      pagmsCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, List<Pagm>>() {

               @Override
               public List<Pagm> load(String identifiant) {
                  return pagmSupport.find(identifiant);
               }

            });

      pagmasCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, Pagma>() {

               @Override
               public Pagma load(String identifiant) {
                  return pagmaSupport.find(identifiant);
               }

            });

      pagmpsCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, Pagmp>() {

               @Override
               public Pagmp load(String identifiant) {
                  return pagmpSupport.find(identifiant);
               }

            });

      actionsCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, ActionUnitaire>() {

               @Override
               public ActionUnitaire load(String identifiant) {
                  return actionSupport.find(identifiant);
               }

            });

      prmdsCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, Prmd>() {

               @Override
               public Prmd load(String identifiant) {
                  return prmdSupport.find(identifiant);
               }

            });

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SaeDroits loadSaeDroits(String idClient, List<String> pagms)
         throws ContractNotFoundException, PagmNotFoundException {

      try {
         contratsCache.getUnchecked(idClient);
      } catch (InvalidCacheLoadException e) {
         throw new ContractNotFoundException(
               "Aucun contrat de service n'a été trouvé "
                     + " pour l'identifiant " + idClient);
      }

      List<Pagm> listPagm;
      try {
         listPagm = pagmsCache.getUnchecked(idClient);
      } catch (InvalidCacheLoadException e) {
         // initialisation de la liste à vide, afin d'avoir une liste de
         // référence
         listPagm = new ArrayList<Pagm>();
      }

      SaeDroits saeDroits = new SaeDroits();

      for (String codePagm : pagms) {
         Pagm pagm = checkPagmExists(codePagm, listPagm, idClient);

         Pagma pagma;
         try {
            pagma = pagmasCache.getUnchecked(pagm.getPagma());
         } catch (InvalidCacheLoadException e) {
            throw new PagmaReferenceException("Le PAGMa " + pagm.getPagma()
                  + " n'a pas été trouvé dans la famille de colonne DroitPagma");
         }

         Prmd prmd = getPrmd(pagm.getPagmp());

         for (String codeAction : pagma.getActionUnitaires()) {
            try {
               actionsCache.getUnchecked(codeAction);
            } catch (InvalidCacheLoadException e) {
               throw new ActionUnitaireReferenceException("L'action unitaire "
                     + codeAction + " n'a pas été trouvée "
                     + "dans la famille de colonne DroitActionUnitaire");
            }

            gererPrmd(saeDroits, codeAction, prmd, pagm);

         }

      }

      return saeDroits;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createContratService(ServiceContract serviceContract,
         List<Pagm> pagms) {
      // TODO Auto-generated method stub

   }

   private Pagm checkPagmExists(String codePagm, List<Pagm> listPagm,
         String idClient) throws PagmNotFoundException {

      Pagm pagm = null;
      boolean found = false;
      int index = 0;
      while (index < listPagm.size() && !found) {
         if (listPagm.get(index).getCode().equals(codePagm)) {
            found = true;
            pagm = listPagm.get(index);
         }
         index++;
      }

      if (!found) {
         throw new PagmNotFoundException("Aucun PAGM '" + codePagm
               + "' n'a été trouvé pour le contrat de service " + idClient);
      }

      return pagm;

   }

   private Prmd getPrmd(String codePagmp) {

      Pagmp pagmp;
      try {
         pagmp = pagmpsCache.getUnchecked(codePagmp);
      } catch (InvalidCacheLoadException e) {
         throw new PagmpReferenceException("Le PAGMp " + codePagmp
               + " n'a pas été trouvé dans la famille de colonne DroitPagma");
      }

      Prmd prmd;
      try {
         prmd = prmdsCache.getUnchecked(pagmp.getPrmd());
      } catch (InvalidCacheLoadException e) {
         throw new PrmdReferenceException("Le PRMD " + pagmp.getPrmd()
               + " n'a pas été trouvé dans la famille de colonne DroitPagma");
      }

      return prmd;
   }

   private void gererPrmd(SaeDroits saeDroits, String actionUnitaire,
         Prmd prmd, Pagm pagm) {

      if (saeDroits.get(actionUnitaire) == null) {
         saeDroits.put(actionUnitaire, new ArrayList<SaePrmd>());
      }

      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setPrmd(prmd);
      saePrmd.setValues(pagm.getParametres());

      saeDroits.get(actionUnitaire).add(saePrmd);

   }

}
