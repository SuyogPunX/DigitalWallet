package com.suyog.Wallet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Hibernate {

    private SessionFactory sessionFactory;

    // Constructor to initialize Hibernate and Configuration
    public Hibernate() {
        Configuration config = new Configuration().configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(PersonalUser.class)
                .addAnnotatedClass(BusinessUser.class)
                .addAnnotatedClass(Wallet.class)
                .addAnnotatedClass(Transection.class)
                .addAnnotatedClass(Bank.class)
                .addAnnotatedClass(BankTransection.class)
                .addAnnotatedClass(Utility.class)
                .addAnnotatedClass(UtilityPayment.class);
        this.sessionFactory = config.buildSessionFactory();
    }

    // Method to open a new session
    public Session openSession() {
        return sessionFactory.openSession();
    }

    // Method to begin a transaction
    public Transaction beginTransaction(Session session) {
        return session.beginTransaction();
    }

    // Method to commit a transaction
    public void commitTransaction(Transaction transaction) {
        if (transaction != null) {
            transaction.commit();
        }
    }

    // Method to close the session
    public void closeSession(Session session) {
        if (session != null) {
            session.close();
        }
    }

    // Method to save an entity
    public void saveEntity(Session session, Object entity) {
        if (entity != null) {
            session.save(entity);
        }
    }

//    // Method to retrieve a single entity based on HQL query
// 
//	public <T> T getEntity(Session session, String hql, String param, Object value) {
//        return (T) session.createQuery(hql)
//                          .setParameter(param, value)
//                          .uniqueResult();
//    }

    // Method to update an entity
    public void updateEntity(Session session, Object entity) {
        if (entity != null) {
            session.update(entity);
        }
    }

    // Method to delete an entity
    public void deleteEntity(Session session, Object entity) {
        if (entity != null) {
            session.delete(entity);
        }
    }

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
    
    
    
}

