package fr.urssaf.image.sae.integration.droits.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.integration.droits.modele.xml.CsType;
import fr.urssaf.image.sae.integration.droits.modele.xml.MetadonneeType;
import fr.urssaf.image.sae.integration.droits.modele.xml.PagmType;
import fr.urssaf.image.sae.integration.droits.modele.xml.PagmaType;
import fr.urssaf.image.sae.integration.droits.modele.xml.PagmpType;
import fr.urssaf.image.sae.integration.droits.modele.xml.ParametrePagmType;
import fr.urssaf.image.sae.integration.droits.modele.xml.PrmdType;


/**
 * Factory d'objets
 */
public final class ObjectFactory {

   private ObjectFactory() {
      
   }
   
   
   /**
    * Création d'un objet de type Prmd à partir d'un objet de type PrmdType
    * @param prmdType l'objet de type PrmdType
    * @return l'objet de type Prmd
    */
   public static Prmd createPrmd(PrmdType prmdType) {
      
      Prmd prmd = new Prmd();
      
      prmd.setCode(prmdType.getCode());
      prmd.setDescription(prmdType.getDescription());
      prmd.setLucene(prmdType.getLucene());
      prmd.setBean(prmdType.getBean());
      
      prmd.setMetadata(createPrmdMetadatas(prmdType.getMetadonnees().getMetadonnee()));
      
      return prmd;
      
   }
   
   
   private static Map<String,List<String>> createPrmdMetadatas(List<MetadonneeType> listeMetaType) {
      
      Map<String,List<String>> metadonnees = new HashMap<String,List<String>>();
      
      String codeMeta;
      List<String> listeValeurs;
      for(MetadonneeType metadonnee: listeMetaType) {
         
         codeMeta = metadonnee.getCode();
         
         listeValeurs = new ArrayList<String>();
         for(String valeur: metadonnee.getValeurs().getValeur()) {
            listeValeurs.add(valeur);
         }
         
         metadonnees.put(codeMeta, listeValeurs);
         
      }
      
      return metadonnees;
      
   }
   
   
   /**
    * Création d'un objet de type ServiceContract à partir d'un objet de type CsType
    * @param csType l'objet de type CsType
    * @return l'objet de type ServiceContract
    */
   public static ServiceContract createCs(CsType csType) {
      
      ServiceContract serviceContract = new ServiceContract();
      
      serviceContract.setCodeClient(csType.getIssuer());
      serviceContract.setLibelle(csType.getCode());
      serviceContract.setDescription(csType.getDescription());
      serviceContract.setViDuree(csType.getViduree());
      serviceContract.setIdPki(csType.getCnPki());
      serviceContract.setVerifNommage(csType.isVerifCnCert());
      serviceContract.setIdCertifClient(csType.getCnCert());
      
      return serviceContract;
      
   }
   
   
   /**
    * Création d'un objet de type Pagm à partir d'un objet de type PagmType
    * @param pagmType l'objet de type PagmType
    * @return l'objet de type Pagm
    */
   public static Pagm createPagm(PagmType pagmType) {
      
      Map<String,String> parametres = createParametres(pagmType);
      
      Pagm pagm = new Pagm();
      
      pagm.setCode(pagmType.getCode());
      pagm.setDescription(pagmType.getDescription());
      pagm.setPagma(pagmType.getPagma().getCode());
      pagm.setPagmp(pagmType.getPagmp().getCode());
      pagm.setParametres(parametres);
      
      return pagm;
      
   }
   
   
   private static Map<String,String> createParametres(PagmType pagmType) {
      
      Map<String,String> parametres = new HashMap<String,String>();
      
      for (ParametrePagmType paramPagm : pagmType.getParametres().getParametre()) {

         parametres.put(paramPagm.getCode(), paramPagm.getValeur());
         
      }
         
      return parametres;
      
   }
   
   
   /**
    * Création d'un objet de type Pagma à partir d'un objet de type PagmaType
    * @param pagmaType l'objet de type PagmaType
    * @return l'objet de type Pagm
    */
   public static Pagma createPagma(PagmaType pagmaType) {
      
      Pagma pagma = new Pagma();
      
      pagma.setCode(pagmaType.getCode());
      
      
      List<String> actionUnitaires = new ArrayList<String>();
      
      pagma.setActionUnitaires(actionUnitaires);
      
      for (String action: pagmaType.getActions().getAction()) {
         actionUnitaires.add(action);
      }
      
      return pagma;
      
   }
   
   /**
    * Création d'un objet de type Pagmp à partir d'un objet de type PagmpType
    * @param pagmpType l'objet de type PagmpType
    * @return l'objet de type Pagmp
    */
   public static Pagmp createPagmp(PagmpType pagmpType) {
      
      Pagmp pagmp = new Pagmp();
      
      pagmp.setCode(pagmpType.getCode());
      
      pagmp.setDescription(pagmpType.getDescription());
      
      pagmp.setPrmd(pagmpType.getPrmd());
      
      return pagmp;
      
   }
   
}
