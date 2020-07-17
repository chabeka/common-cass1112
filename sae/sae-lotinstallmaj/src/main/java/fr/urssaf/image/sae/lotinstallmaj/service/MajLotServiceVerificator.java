package fr.urssaf.image.sae.lotinstallmaj.service;

public interface MajLotServiceVerificator {

   /**
    * Vérification qu'une mise à jour a bien été installée
    * 
    * @param version
    *           numéro de la version à vérifier
    * @return
    */
   boolean verify(int version);

}
