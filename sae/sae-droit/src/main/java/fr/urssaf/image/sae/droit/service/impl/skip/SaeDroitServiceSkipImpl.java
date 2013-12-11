/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl.skip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeContratService;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.SaeDroitService;

/**
 * 
 * 
 */
public class SaeDroitServiceSkipImpl implements SaeDroitService {

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean contratServiceExists(String idClient) {
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void createContratService(ServiceContract serviceContract,
         List<SaePagm> listeSaePagm) {
      /* Rien à faire */

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SaeDroits loadSaeDroits(String idClient, List<String> pagms)
         throws ContratServiceNotFoundException, PagmNotFoundException {
      SaeDroits saeDroits = new SaeDroits();

      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());

      Prmd prmd = new Prmd();
      prmd.setCode("prmd_default");
      prmd.setDescription("PRMD par défaut");
      prmd.setLucene(null);
      prmd.setMetadata(null);
      prmd.setBean("permitAll");
      saePrmd.setPrmd(prmd);

      List<SaePrmd> prmds = new ArrayList<SaePrmd>();
      prmds.add(saePrmd);

      saeDroits.put("consultation", prmds);
      saeDroits.put("recherche", prmds);
      saeDroits.put("archivage_unitaire", prmds);
      saeDroits.put("archivage_masse", prmds);

      return saeDroits;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ServiceContract getServiceContract(String idClient) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<ServiceContract> findAllContractService(int maxResult) {

      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SaeContratService getFullContratService(String idClient) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void ajouterPagmContratService(String idContratService, SaePagm pagm) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void modifierPagmContratService(String idContratService, SaePagm pagm) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void supprimerPagmContratService(String idContratService,
         String codePagm) {

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SaePagm> getListeSaePagm(String idContratService) {
      
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SaeContratService> findAllSaeContractService(int maxResult) {
   
      return null;
   }

}
