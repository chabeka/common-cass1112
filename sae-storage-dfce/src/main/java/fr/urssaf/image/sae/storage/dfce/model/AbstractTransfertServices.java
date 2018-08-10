package fr.urssaf.image.sae.storage.dfce.model;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;

/**
 * Classe abstraite contenant l'attribut de connexion pour l'implémentation :
 * <ul>
 * <li>
 * {@link fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument.crud.TransfertServiceImpl}
 * : Implementation de l'interface InsertionService.</li>
 * <li>
 * </ul>
 * Elle contient également l'attribut :
 * <ul>
 * <li>
 * Attribut storageBase : Classe concrète contenant le nom de la base de
 * stockage</li>
 * </ul>
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractTransfertServices extends AbstractServices {

   /**
    * Parametre de connexion de DFCe
    */
   protected DFCEConnection cnxParameters;

   /**
    * {@inheritDoc}
    */
   @Override
   public DFCEConnection getCnxParameters() {
      return cnxParameters;
   }

}
