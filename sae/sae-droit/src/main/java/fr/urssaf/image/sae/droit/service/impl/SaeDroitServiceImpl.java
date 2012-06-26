/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.exception.ContractNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.service.SaeDroitService;

/**
 * Classe d'implémentation du service {@link SaeDroitService}.<br>
 * Cette classe est un singleton et peut être accessible par le mécanisme
 * d'injection IOC avec l'annotation @Autowired
 * 
 */
@Component
public class SaeDroitServiceImpl implements SaeDroitService {

   private LoadingCache<String, ServiceContract> contratsCache;
   private LoadingCache<String, List<Pagm>> pagmsCache;

   /**
    * Constructeur
    * 
    * @param contratSupport
    *           support pour les contratsCache de service
    * @param pagmSupport
    *           support pour les pagm
    */
   @Autowired
   public SaeDroitServiceImpl(final ContratServiceSupport contratSupport,
         final PagmSupport pagmSupport) {
      contratsCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, ServiceContract>() {

               @Override
               public ServiceContract load(String id) throws Exception {
                  return contratSupport.find(id);
               }

            });

      pagmsCache = CacheBuilder.newBuilder().build(
            new CacheLoader<String, List<Pagm>>() {

               @Override
               public List<Pagm> load(String id) throws Exception {
                  return pagmSupport.find(id);
               }

            });

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SaeDroits loadSaeDroits(String idClient, List<String> pagms)
         throws ContractNotFoundException {

      ServiceContract contract = contratsCache.getUnchecked(idClient);

      if (contract == null) {
         throw new ContractNotFoundException(
               "Aucun contrat de service n'a été trouvé "
                     + " pour l'identifiant " + idClient);
      }
      
      List<Pagm> listPagm = pagmsCache.getUnchecked(idClient);
      

      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void createContratService(ServiceContract serviceContract,
         List<Pagm> pagms) {
      // TODO Auto-generated method stub

   }

}
