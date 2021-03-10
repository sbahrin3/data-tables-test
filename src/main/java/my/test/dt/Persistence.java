package my.test.dt;


import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

/**
 * 
 * @author Shamsul Bahrin
 *
 */
public class Persistence {
	
	private static Persistence instance = null;
	private static Configuration cfg = null;
	private static SessionFactory factory = null;
	private Transaction transaction;
	//private Session session;
	
	private boolean add = true;
	
	private Persistence() {
		createSessionFactory();
	}
	
	public static Persistence db() {
		if ( instance == null )
			instance = new Persistence();
		return instance;
	}
	
	private void createSessionFactory() {
		cfg = new Configuration();
		cfg.configure();
		factory = cfg.buildSessionFactory();
	}
	
	private void createSessionFactory2() {
		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build(); 
		Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
		factory = meta.getSessionFactoryBuilder().build();  
	}
	
	public SessionFactory factory() {
		return factory;
	}
	
	public void close() {
		factory.close();
	}
	
	public void save(Object object) {
		Session session = factory.openSession();
		transaction = session.beginTransaction();
		session.save(object);
		transaction.commit();
		session.close();
	}
	
	public void save(Object[] objects) {
		Session session = factory.openSession();
		transaction = session.beginTransaction();
		Arrays.asList(objects).stream().forEach(object -> session.save(object));
		transaction.commit();
		session.close();
	}
	
	public void update(Object object) {
		Session session = factory.openSession();
		transaction = session.beginTransaction();
		session.update(object);
		transaction.commit();
		session.close();
	}
	
	public void delete(Object object) {
		Session session = factory.openSession();
		transaction = session.beginTransaction();
		session.delete(object);
		transaction.commit();
		session.close();
	}
	
	public <T> T find(Class<T> klass, Object id) {
		Session session = factory.openSession();
		T t = session.find(klass, id);
		session.close();
		return t;
	}
	
	public <T> List<T> list(String hql) {
		Session session = factory.openSession();
		Query q = session.createQuery(hql);
		List<T> list = q.getResultList();
		session.close();
		return list;
	}
	
	public <T> T get(String hql) {
		Session session = factory.openSession();
		Query q = session.createQuery(hql);
		List<T> list = q.getResultList();
		session.close();
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public <T> List<T> list(String hql, Hashtable<String, Object> h) {
		Session session = factory.openSession();
		Query q = session.createQuery(hql);
		for ( Enumeration<String> e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			q.setParameter(key, value);
		}
		List<T> list = q.getResultList();
		session.close();
		return list;
	}
	
	public <T> List<T> list(String hql, Hashtable<String, Object> h, int size) {
		Session session = factory.openSession();
		Query q = session.createQuery(hql);
		for ( Enumeration<String> e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			q.setParameter(key, value);
		}
		List<T> list = q.getResultList();
		session.close();
		return list;
	} 
	
	public <T> List<T> list(String q, int start, int chunkSize, Hashtable<String, Object>  h) {
		Session session = factory.openSession();
        Query query = session.createQuery(q);
		for ( Enumeration e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			query.setParameter(key, value);
		}        
        query = query.setFirstResult(start);
        query = query.setMaxResults(chunkSize);
        
		List<T> list = query.getResultList();
		session.close();
		
        return list;
    }	
	
	
	public <T> List<T> list(String q, int start, int chunkSize) {
		
		Session session = factory.openSession();
        Query query = session.createQuery(q);
        query = query.setFirstResult(start);
        query = query.setMaxResults(chunkSize);
        
		List<T> list = query.getResultList();
		
		session.close();
		
        return list;
    }
	
	
	public int execute(String q) throws ConstraintViolationException {
		Session session = factory.openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery(q);
		int n = query.executeUpdate();
		transaction.commit();
		session.close();
		return n;
	}
	
	public Persistence ifAdd(boolean b) {
		add = b;
		return this;
	}
	
	public void saveOrUpdate(Object object) {
		if ( add )
			save(object);
		else
			update(object);
	}
	
}
