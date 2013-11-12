/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl.skip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.model.ServiceContractDatas;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
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
   public boolean contratServiceExists(String idClient) {
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void createContratService(ServiceContract serviceContract,
         List<Pagm> pagms) {
      /* Rien à faire */

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public SaeDroits loadSaeDroits(String idClient, List<String> pagms)
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
   public ServiceContract getServiceContract(String idClient) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addPagmContratService(String idContratService, Pagm pagm) {
      /* Rien à faire */
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<Pagm> getListePagm(String idContratService) {
      return null;
   }

   
   /**
    * {@inheritDoc}
    */
   @Override
   public List<ServiceContract> findAllContractService(int maxResult) {

      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ServiceContractDatas getFullContratService(String idClient) {
      return null;
   }

}
