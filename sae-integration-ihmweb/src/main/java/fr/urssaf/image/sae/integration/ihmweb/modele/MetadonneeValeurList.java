package fr.urssaf.image.sae.integration.ihmweb.modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;


/**
 * Liste de métadonnées<br>
 * <br>
 * Un transtypage pour les classes de formulaire est réalisé dans
 * la classe MetadonneeListEditor
 */
@SuppressWarnings("PMD.TooManyMethods")
public class MetadonneeValeurList implements List<MetadonneeValeur> {

   private final List<MetadonneeValeur> liste = new ArrayList<MetadonneeValeur>();
   
      
   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean add(MetadonneeValeur element) {
      return liste.add(element);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void add(int index, MetadonneeValeur element) {
      liste.add(index,element);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean addAll(Collection<? extends MetadonneeValeur> collection) {
      return liste.addAll(collection);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean addAll(int index, Collection<? extends MetadonneeValeur> collection) {
      return liste.addAll(index,collection);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void clear() {
      liste.clear();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean contains(Object object) {
      return liste.contains(object);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean containsAll(Collection<?> collection) {
      return liste.containsAll(collection);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final MetadonneeValeur get(int index) {
      return liste.get(index);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int indexOf(Object object) {
      return liste.indexOf(object);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isEmpty() {
      return liste.isEmpty();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Iterator<MetadonneeValeur> iterator() {
      return liste.iterator();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int lastIndexOf(Object object) {
      return liste.lastIndexOf(object);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ListIterator<MetadonneeValeur> listIterator() {
      return liste.listIterator();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ListIterator<MetadonneeValeur> listIterator(int index) {
      return liste.listIterator(index);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean remove(Object object) {
      return liste.remove(object);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final MetadonneeValeur remove(int index) {
      return liste.remove(index);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean removeAll(Collection<?> collection) {
      return liste.removeAll(collection);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean retainAll(Collection<?> collection) {
      return liste.retainAll(collection);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final MetadonneeValeur set(int index, MetadonneeValeur element) {
      return liste.set(index,element);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int size() {
      return liste.size();
   }

   @Override
   public final List<MetadonneeValeur> subList(int fromIndex, int toIndex) {
      return liste.subList(fromIndex,toIndex);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Object[] toArray() {
      return liste.toArray();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final <T> T[] toArray(T[] array) {
      return liste.toArray(array);
   }
   
   
   
   /**
    * Recherche une métadonnée dans la liste à partir du code long de la métadonnée
    * 
    * @param codeLong le code long de la métadonnée à rechercher
    * @return l'indice de la métadonnée dans la liste si cette métadonnée est présente, ou
    *         -1 dans le cas contraire
    */
   public final int indexOf(String codeLong) {
      int result = -1;
      
      if (CollectionUtils.isNotEmpty(liste)) {
         for(int i=0;i<liste.size();i++) {
            if (liste.get(i).getCode().equals(codeLong)) {
               result = i;
               break;
            }
         }
      }
      
      return result;
      
   }
   

   /**
    * Modifie la valeur d'une métadonnée de la liste
    * 
    * @param codeLong le code long de la métadonnée dont il faut modifier la valeur
    * @param nouvelleValeur la nouvelle valeur qu'il faut affecter à la métadonnée
    */
   public final void modifieValeurMeta(
         String codeLong, 
         String nouvelleValeur) {
      
      int idx = indexOf(codeLong);
      if (idx!=-1) {
         liste.get(idx).setValeur(nouvelleValeur);
      }
      
   }
   
   
   /**
    * Ajoute un élément dans la liste
    * 
    * @param code le code de la métadonnée
    * @param valeur la valeur de la métadonnée
    * @return true
    */
   public final boolean add(String code, String valeur) {
      return liste.add(new MetadonneeValeur(code,valeur));
   }

   /**
    * Affichage des métas
    */
   public final String toString() {
      String affichage = "";
      for (MetadonneeValeur meta  : liste) {
         affichage = affichage.concat(meta.toString() + ", ");
      }
      
      return affichage;
   }
}
