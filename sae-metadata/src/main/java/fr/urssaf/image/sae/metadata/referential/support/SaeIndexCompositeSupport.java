package fr.urssaf.image.sae.metadata.referential.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;

/**
 * Classe support de manipulation des indexes composites
 */
public class SaeIndexCompositeSupport {

   private final DFCEServices dfceServices;

   /**
    * Constructeur
    * @param serviceDfce service dfce
    */
   public SaeIndexCompositeSupport(final DFCEServices dfceServices) {
      this.dfceServices = dfceServices;
   }

   /**
    * Récupération de la liste des indexes compositees
    * @return List<SaeIndexComposite>
    */
   public final List<SaeIndexComposite> getListeCompositeIndex() {

      //-- Récupération la liste des index composites
      final List<SaeIndexComposite> listeIndexes = new ArrayList<SaeIndexComposite>();
      final Set<CompositeIndex> compositeIndexes = dfceServices.fetchAllCompositeIndex();
      final Iterator<CompositeIndex> iter = compositeIndexes.iterator();
      while(iter.hasNext()) {
         final CompositeIndex index = iter.next();
         final SaeIndexComposite saeIndexComposite = new SaeIndexComposite(index);
         listeIndexes.add(saeIndexComposite);
         //LOGGER.debug("{} : indexé {}", getCompositeIndexName(index), index.isComputed());
      }

      return listeIndexes;
   }
   /**
    * Création d'un indexe composite
    * @param codesCourts : composition de l'index à créer
    * @throws IndexCompositeException
    *
    * @return objet CompositeIndex
    */
   public final CompositeIndex creatIndexComposite(final List<String> codesCourts) throws IndexCompositeException {

      if (codesCourts == null || codesCourts.size() < 2) {
         throw new IndexCompositeException("La liste des métas de l'index ne peut être nulle ou < à 2.");
      }

      //-- Récupération des catégories de l'index composite
      final Category[] categories = new Category[codesCourts.size()];
      for (int i=0; i<codesCourts.size(); i++) {
         final String codeCourt = codesCourts.get(i);
         final Category category = dfceServices.getCategory(codeCourt);
         categories[i] = category;
         if (category == null) {
            //LOG.error("Impossible de récupérer la Category pour code {}", codeCourt);
            throw new IndexCompositeException("La category '" + codeCourt + "' n'a pas ete trouvee");
         }
      }

      //-- Création de l'index composite en base
      final CompositeIndex idx = dfceServices.findOrCreateCompositeIndex(categories);
      return idx;
   }
}
