package fr.urssaf.image.sae.integration.ihmweb.modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Liste de liens HTTP<br>
 * <br>
 * Un transtypage pour les classes de formulaire est réalisé dans
 * la classe LienHttpListEditor
 */
@SuppressWarnings("PMD.TooManyMethods")
public class LienHttpList implements List<LienHttp> {

   private final List<LienHttp> liste = new ArrayList<LienHttp>();
   
      
   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean add(LienHttp element) {
      return liste.add(element);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void add(int index, LienHttp element) {
      liste.add(index,element);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean addAll(Collection<? extends LienHttp> collection) {
      return liste.addAll(collection);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean addAll(int index, Collection<? extends LienHttp> collection) {
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
   public final LienHttp get(int index) {
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
   public final Iterator<LienHttp> iterator() {
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
   public final ListIterator<LienHttp> listIterator() {
      return liste.listIterator();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ListIterator<LienHttp> listIterator(int index) {
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
   public final LienHttp remove(int index) {
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
   public final LienHttp set(int index, LienHttp element) {
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
   public final List<LienHttp> subList(int fromIndex, int toIndex) {
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
    * Ajoute un lien à la liste
    * 
    * @param texte le texte du lien
    * @param url l'URL du lien
    * 
    * @return l'indice de l'élément ajouté
    */
   public final int ajouteLien(String texte, String url) {
      
      int idProchainLien = this.liste.size() + 1;
      
      String numeroFormate = String.format("%03d", idProchainLien);
      
      String texteLien = "[" + numeroFormate + "] - " + texte;
      
      this.liste.add(new LienHttp(texteLien,url));
      
      return this.liste.size();
   }
   
}
