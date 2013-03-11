/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

import java.util.List;

/**
 * Classe représentant un contrat de service et contenant l'ensemble des objets
 * le composant. Tous les objets sont situés à la racine, sans prendre en compte
 * l'arborescence véritable. Celle-ci est la suivante :
 * <ul>
 * <li>contrat de service</li>
 * <ul>
 * <li>pagm (*)</li>
 * <ul>
 * <li>pagma</li>
 * <ul>
 * <li>action unitaire (*)</li>
 * </ul>
 * <li>pagmp</li> </ul> </ul> </ul>
 * 
 */
public class ServiceContractDatas extends ServiceContract {

   private List<Pagm> pagms;

   private List<Pagma> pagmas;

   private List<Pagmp> pagmps;

   private List<Prmd> prmds;

   private List<ActionUnitaire> actions;

   /**
    * Constructeur par défaut
    */
   public ServiceContractDatas() {
   }

   /**
    * Constructeur
    * 
    * @param serviceContract
    *           le service de contrat
    */
   public ServiceContractDatas(ServiceContract serviceContract) {
      this.setCodeClient(serviceContract.getCodeClient());
      this.setDescription(serviceContract.getDescription());
      this.setLibelle(serviceContract.getLibelle());
      this.setViDuree(serviceContract.getViDuree());
      this.setVerifNommage(serviceContract.isVerifNommage());
      this.setIdCertifClient(serviceContract.getIdCertifClient());
      this.setListCertifsClient(serviceContract.getListCertifsClient());
      this.setIdPki(serviceContract.getIdPki());
      this.setListPki(serviceContract.getListPki());
   }

   /**
    * @return la liste des pagms
    */
   public final List<Pagm> getPagms() {
      return pagms;
   }

   /**
    * @param pagms
    *           la liste des pagms
    */
   public final void setPagms(List<Pagm> pagms) {
      this.pagms = pagms;
   }

   /**
    * @return la liste des pagmas
    */
   public final List<Pagma> getPagmas() {
      return pagmas;
   }

   /**
    * @param pagmas
    *           la liste des pagmas
    */
   public final void setPagmas(List<Pagma> pagmas) {
      this.pagmas = pagmas;
   }

   /**
    * @return la liste des pagmps
    */
   public final List<Pagmp> getPagmps() {
      return pagmps;
   }

   /**
    * @param pagmps
    *           la liste des pagmps
    */
   public final void setPagmps(List<Pagmp> pagmps) {
      this.pagmps = pagmps;
   }

   /**
    * @return la liste des prmds
    */
   public final List<Prmd> getPrmds() {
      return prmds;
   }

   /**
    * @param prmds
    *           la liste des prmds
    */
   public final void setPrmds(List<Prmd> prmds) {
      this.prmds = prmds;
   }

   /**
    * @return la liste des actions unitaires
    */
   public final List<ActionUnitaire> getActions() {
      return actions;
   }

   /**
    * @param actions
    *           la liste des actions unitaires
    */
   public final void setActions(List<ActionUnitaire> actions) {
      this.actions = actions;
   }

}
