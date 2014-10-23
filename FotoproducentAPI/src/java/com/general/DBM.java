package com.general;

import com.entities.Account;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DBM {
    
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("FotoproducentAPIPU");
    private final EntityManager em = emf.createEntityManager();
    private final EntityTransaction et = em.getTransaction();
    
    public void delete(Object entity){
        et.begin();
        em.remove(entity);
        et.commit();
    }
    
    public <T> T findById(Class<T> entityClass, Object primaryKey){
        return em.find(entityClass, primaryKey);
    }
    
    public void save(Object entity){
        et.begin();
        em.persist(entity);
        et.commit();
    }
    
    public void update(Object entity){
        save(entity);
    }
    
    public <T> boolean deleteById(Class<T> entityClass, Object primaryKey){
        T enitity = findById(entityClass, primaryKey);
        if(enitity == null){return false;}
        delete(enitity);
        return true;
    }
    
    public Account authenicateUser(String username, String password)
    {
        try{
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.userName = :USERNAME AND a.password = :PASSWORD");
            q.setParameter("USERNAME", username);
            q.setParameter("PASSWORD", password);

            Account account = (Account)q.getSingleResult();
            return account;
            
        }catch(Exception ex){
            Logger.getLogger(DBM.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}