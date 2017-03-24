package fr.urssaf.image.commons.dao.spring.support;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.service.EntityCountDao;
import fr.urssaf.image.commons.dao.spring.service.EntityFindDao;
import fr.urssaf.image.commons.dao.spring.service.EntityIdDao;
import fr.urssaf.image.commons.dao.spring.service.EntityModifyDao;
import fr.urssaf.image.commons.dao.spring.service.impl.EntityCountDaoImpl;
import fr.urssaf.image.commons.dao.spring.service.impl.EntityFindDaoImpl;
import fr.urssaf.image.commons.dao.spring.service.impl.EntityIdDaoImpl;
import fr.urssaf.image.commons.dao.spring.service.impl.EntityModifyDaoImpl;


/**
 * Support pour les fonctions typiques d'une classe persistante.
 * 
 * 
 * @param <P> classe de l'entité persistée
 * @param <I> type Java de l'identifiant de la classe de l'entité persistée
 */
@SuppressWarnings("PMD.TooManyMethods")
@Transactional(propagation = Propagation.SUPPORTS)
public class MyHibernateDaoSupport<P, I extends Serializable>
   extends
      HibernateDaoSupport
   implements
      EntityCountDao,
      EntityModifyDao<P>,
		EntityFindDao<P>,
		EntityIdDao<P, I>
{

	private final Class<P> table;

	private final EntityModifyDaoImpl<P> modifyDaoImpl;

	private final EntityIdDaoImpl<P, I> idDaoImpl;

	private final EntityFindDaoImpl<P> findDaoImpl;

	private final EntityCountDaoImpl<P> countDaoImpl;

	
	/**
	 * Constructeur
	 * 
	 * @param sessionFactory session factory d'Hibernate
	 * @param table classe de l'entité persistée
	 * @param identifiant nom de l'attribut de la clé primaire
	 */
	public MyHibernateDaoSupport(
	      SessionFactory sessionFactory, 
	      Class<P> table,
			String identifiant) {
		super();
      setSessionFactory(sessionFactory);
      this.table = table;
      this.modifyDaoImpl = new EntityModifyDaoImpl<P>(sessionFactory);
      this.idDaoImpl = new EntityIdDaoImpl<P, I>(sessionFactory, this.table,identifiant);
      this.countDaoImpl = new EntityCountDaoImpl<P>(sessionFactory,this.table);
      this.findDaoImpl = new EntityFindDaoImpl<P>(sessionFactory, this.table);
	}
	

	/**
	 * Renvoie le nom de la classe persistante
	 * 
	 * @return nom de la classe persistante
	 */
	protected final String getTable() {
		return this.table.getCanonicalName();
	}

	/**
	 * Renvoie un objet criteria de la classe persistante
	 * 
	 * @return objet criteria de la classe persistante
	 */
	protected final Criteria getCriteria() {
		return this.getSession().createCriteria(table);
	}

	@Override
	public void save(P obj) {
		this.modifyDaoImpl.save(obj);
	}

	@Override
	public void delete(P obj) {
		this.modifyDaoImpl.delete(obj);
	}

	@Override
	public void update(P obj) {
		this.modifyDaoImpl.update(obj);
	}

	@Override
	public P get(I identifiant) {
		return idDaoImpl.get(identifiant);
	}

	@Override
	public P find(I identifiant) {
		return idDaoImpl.find(identifiant);
	}

	@Override
	public List<P> find(
	      int firstResult, 
	      int maxResult, 
	      String order,
			boolean inverse) {
		return this.findDaoImpl.find(firstResult, maxResult, order, inverse);
	}

	@Override
	public List<P> find(String order, boolean inverse) {
		return this.findDaoImpl.find(order, inverse);
	}

	@Override
	public List<P> find(String order) {
		return this.findDaoImpl.find(order);

	}

	@Override
	public List<P> find() {
		return this.findDaoImpl.find();
	}

	
	protected List<P> find(
	      Criteria criteria, 
	      int firstResult, 
	      int maxResult,
			String order, 
			boolean inverse) {
		return this.findDaoImpl.find(
		      criteria, 
		      firstResult, 
		      maxResult, 
		      order,
				inverse);
	}

	protected List<P> find(Criteria criteria, String order, boolean inverse) {
		return this.findDaoImpl.find(criteria, order, inverse);

	}

	protected List<P> find(Criteria criteria, int firstResult, int maxResult) {
		return this.findDaoImpl.find(criteria, firstResult, maxResult);
	}

	protected List<P> find(Criteria criteria) {
		return this.findDaoImpl.find(criteria);
	}

	/**
	 * Renvoie tous les objets persistants filtrés par un objet exemple
	 * 
	 * @param obj objet exemple
	 * @return liste des objets persistants
	 */
	@SuppressWarnings("unchecked")
	protected List<P> findByExample(P obj) {
		return this.getCriteria().add(Example.create(obj)).list();
	}

	@Override
	public int count() {
		return countDaoImpl.count();
	}

	protected int count(Criteria criteria) {
		return countDaoImpl.count(criteria);
	}

}
