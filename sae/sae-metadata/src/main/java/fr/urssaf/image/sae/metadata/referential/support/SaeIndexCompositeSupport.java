package fr.urssaf.image.sae.metadata.referential.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.administration.StorageAdministrationService;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;

/**
 * Classe support de manipulation des indexes composites
 */
public class SaeIndexCompositeSupport {
   
   private DFCEConnectionService serviceDfce;
   
   /**
    * Constructeur
    * @param serviceDfce service dfce
    */
   public SaeIndexCompositeSupport(DFCEConnectionService serviceDfce) {
      this.serviceDfce = serviceDfce;
   }
   
   /**
    * Récupération de la liste des indexes compositees
    * @return List<SaeIndexComposite>
    */
   public final List<SaeIndexComposite> getListeCompositeIndex() {
      
      //-- Ouverture de la connexion à DFCE;
      ServiceProvider serviceProvider = serviceDfce.openConnection();
      
      //-- Recuperation la liste des index composites
      List<SaeIndexComposite> listeIndexes = new ArrayList<SaeIndexComposite>();
      Set<CompositeIndex> compositeIndexes = serviceProvider.getStorageAdministrationService().fetchAllCompositeIndex();
      Iterator<CompositeIndex> iter = compositeIndexes.iterator();
      while(iter.hasNext()) {
         CompositeIndex index = iter.next();
         SaeIndexComposite saeIndexComposite = new SaeIndexComposite(index);
         listeIndexes.add(saeIndexComposite);
         //LOGGER.debug("{} : indexé {}", getCompositeIndexName(index), index.isComputed());
      }
      
      //LOGGER.debug("Fermeture de la connexion à DFCE");
      serviceProvider.disconnect();
      
      return listeIndexes;
   }
   /**
    * Création d'un indexe composite
    * @param codesCourts : composition de l'index à créer
    * @throws IndexCompositeException 
    * 
    * @return objet CompositeIndex
    */
   public final CompositeIndex creatIndexComposite(List<String> codesCourts) throws IndexCompositeException {
      
      if (codesCourts == null || codesCourts.size() < 2) {
         throw new IndexCompositeException("La liste des métas de l'index ne peut être nulle ou < à 2.");
      }
      
      //-- Ouverture de la connexion à DFCE;
      ServiceProvider serviceProvider = serviceDfce.openConnection();
      
      //-- Récupération des catégories de l'index composite
      StorageAdministrationService store = serviceProvider.getStorageAdministrationService();
      Category[] categories = new Category[codesCourts.size()];
      for (int i=0; i<codesCourts.size(); i++) {
         String codeCourt = codesCourts.get(i);
         Category category = store.getCategory(codeCourt);  
         categories[i] = category;
         if (category == null) {
            //LOG.error("Impossible de récupérer la Category pour code {}", codeCourt);
            throw new IndexCompositeException("La category '" + codeCourt + "' n'a pas ete trouvee");
         }
      }
      
      //-- Création de l'index composite en base
      CompositeIndex idx = store.findOrCreateCompositeIndex(categories);

      serviceProvider.disconnect();
      
      return idx;
   }
}
