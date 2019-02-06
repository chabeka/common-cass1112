package fr.urssaf.image.sae.metadata.referential.model;

import java.util.List;

import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;

/**
 * Cette classe représente un élément du référentiel des index composites. <BR />
 * Elle contient les attributs :
 * <ul>
 * <li><b>name</b> : Le nom de l'index composite, qui correspond à la concaténation des codes courts des métadonnées.
 * ex: cpt&sco&SM_DOCUMENT_TYPE&</li>
 * <li><b>categories</b> : Les catégories qui composent l'index</li>
 * <li><b>isComputed</b> : Détermine si l'indexe composite est indexé</li>
 * </ul>
 */
public class SaeIndexComposite {

   /**
    * Nom de l'index
    */
   private String name;

   /**
    * Catégories qui composent l'index
    */
   private List<Category> categories;

   /**
    * Flag permettant de savoir si l'index composite est indexé
    */
   private boolean isComputed;

   /**
    * Construit le SaeIndexComposite à partir d'un {@link CompositeIndex} dfce
    * 
    * @param indexComposite
    *           : index composite dfce
    */
   public SaeIndexComposite(final CompositeIndex indexComposite) {
      categories = indexComposite.getCategories();
      name = getCompositeIndexName(indexComposite);
      isComputed = indexComposite.isComputed();
   }

   /**
    * Récupère la liste des catégories dfce composant l'index
    * 
    * @return : la liste des catégories de l'indexe
    */
   public final List<Category> getCategories() {
      return categories;
   }

   /**
    * @param categories
    *           : catégories
    */
   public final void setCategories(final List<Category> categories) {
      this.categories = categories;
   }

   /**
    * @return Le nom
    */
   public final String getName() {
      return name;
   }

   /**
    * @param name
    *           : Le nom
    */
   public final void setName(final String name) {
      this.name = name;
   }

   /**
    * Check si l'index composite est indexé
    * 
    * @return : Le flag isComputed
    */
   public final boolean isComputed() {
      return isComputed;
   }

   /**
    * @param isComputed
    *           : flag isComputed
    */
   public final void setIsComputed(final boolean isComputed) {
      this.isComputed = isComputed;
   }

   /**
    * Méthode récupération du nom
    * 
    * @param index
    *           : Un index composite dfce
    * @return
    */
   private String getCompositeIndexName(final CompositeIndex index) {
      final StringBuilder buffer = new StringBuilder();
      for (final Category category : index.getCategories()) {
         buffer.append(category.getName());
         buffer.append('&');
      }
      return buffer.toString();
   }
}
