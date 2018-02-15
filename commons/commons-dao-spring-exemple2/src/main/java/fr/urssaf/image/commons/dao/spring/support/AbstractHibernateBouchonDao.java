package fr.urssaf.image.commons.dao.spring.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.service.EntityCountDao;
import fr.urssaf.image.commons.dao.spring.service.EntityFindDao;
import fr.urssaf.image.commons.dao.spring.service.EntityIdDao;
import fr.urssaf.image.commons.dao.spring.service.EntityModifyDao;

/**
 * Classe bouchon d'implémentation de DAO
 *
 * @param <P> classe de l'entité persistée
 * @param <I> type Java de l'identifiant de la classe de l'entité persistée
 * 
 */
@Transactional(readOnly = true)
public abstract class AbstractHibernateBouchonDao<P, I extends Serializable>
   implements
      EntityModifyDao<P>,
      EntityIdDao<P, I>,
      EntityFindDao<P>,
		EntityCountDao {

	private final Map<I, P> data = new HashMap<I, P>();
	
	
	/**
	 * Renvoie l'identifiant unique de l'entité passée en paramètre
	 * 
	 * @param obj l'entité dont il faut renvoyer l'identifiant unique
	 * @return l'identifiant unique de l'entité
	 */
	protected abstract I getId(P obj);
	
	
	/**
	 * Renvoie le moteur de comparaison entre deux entités
	 * 
	 * @param order nom de la propriété sur laquelle il faut comparer
	 * @param inverse true s'il faut inverser le résultat de la comparaison
	 * (pour un tri décroissant)
	 * @return le moteur de comparaison
	 */
	protected abstract Comparator<P> getComparator(String order, Boolean inverse);
	
	
	@Override
   public final int count() {
      return data.size();
   }
		
	@Override
   public final void save(P obj) {
      data.put(getId(obj), obj);
   }

   @Override
   public final void update(P obj) {
      data.put(getId(obj), obj);
   }
   
   @Override
   public final void delete(P obj) {
      data.remove(getId(obj));
   }
	
	@Override
   public final P get(I identifiant) {
      return data.get(identifiant);
   }
	
	
	@SuppressWarnings("unchecked")
   @Override
   public final List<P> find() {
      return (List<P>) Arrays.asList(data.values().toArray());
   }
	
	@Override
	public final P find(I identifiant) {
		return data.get(identifiant);
	}
	
	
	@SuppressWarnings("unchecked")
   private P[] getTableauTrie(String order, Boolean inverse) {
	   
	   P[] resultat = (P[]) data.values().toArray();
      
      Comparator<P> comparator = getComparator(order,inverse);
      
      Arrays.sort(resultat, comparator);
      
      return resultat;
	   
	}
	

	@Override
   public final List<P> find(String order) {
      
	   P[] resultat = getTableauTrie(order,false);
	   
	   return Arrays.asList(resultat);
	   
	}

   @Override
   public final List<P> find(String order, boolean inverse) {
      
      P[] resultat = getTableauTrie(order,inverse);
      
      return Arrays.asList(resultat);
      
   }
	
	@SuppressWarnings("PMD.UseArraysAsList")
   @Override
	public final List<P> find(
	      int firstResult, 
	      int maxResult, 
	      String order,
			boolean inverse) {
		
	   P[] resultat1 = getTableauTrie(order,inverse);
	   
	   List<P> resultat = new ArrayList<P>() ;
	   
	   for(int i=firstResult;(i<(firstResult+maxResult) && (i<resultat1.length));i++) {
	      resultat.add(resultat1[i]);
	   }
	   
	   return resultat;
	   
	}

}
