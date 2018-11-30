package fr.urssaf.image.sae.integration.ihmweb.modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Liste de PAGM<br>
 * <br>
 * Un transtypage pour les classes de formulaire est réalisé dans
 * la classe PagmListEditor
 */
@SuppressWarnings("PMD.TooManyMethods")
public class PagmList implements List<String> {

   private final List<String> liste = new ArrayList<String>();
   
      
   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean add(String element) {
      return liste.add(element);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void add(int index, String element) {
      liste.add(index,element);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean addAll(Collection<? extends String> collection) {
      return liste.addAll(collection);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean addAll(int index, Collection<? extends String> collection) {
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
   public final String get(int index) {
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
   public final Iterator<String> iterator() {
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
   public final ListIterator<String> listIterator() {
      return liste.listIterator();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ListIterator<String> listIterator(int index) {
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
   public final String remove(int index) {
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
   public final String set(int index, String element) {
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
   public final List<String> subList(int fromIndex, int toIndex) {
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

   
}
