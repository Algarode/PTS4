package com.general;

import com.entities.Account;
import com.entities.*;
import java.util.ArrayList;
import java.util.List;
import com.entities.Collection;
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
            em.close();
            return account;
            
        }catch(Exception ex){
            Logger.getLogger(DBM.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // for photos
    public Collection verifyCode(String uid, String uniqueCode)
    {
        try{
            Photo photo = this.findById(Photo.class,uniqueCode);
            User user = this.getUser(Integer.parseInt(uid));
            if(photo != null && user != null){
                Collection collection = new Collection();
                collection.setPhotoID(photo);
                collection.setUserID(user);
                this.save(collection);
                return collection;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public User getUser(int accountID){
        Account account = this.findById(Account.class, accountID);
        User user = account.getUser();
        return user;
    }
    
    public List<Collection> verifyCodeAlbum(String uid, String uniqueCode)
    {
        try {
            Query q = em.createQuery("SELECT s FROM Share s WHERE s.id = :uniCode");
            Query inuse = em.createQuery("SELECT c FROM Collection c WHERE c.userID = :uid AND c.shareID = :uni");
            Query Photo = em.createQuery("SELECT p FROM PhotoShare p WHERE p.share_id = :share");
            q.setParameter("uniCode", uniqueCode);
            inuse.setParameter("uid", uid);
            inuse.setParameter("uni", uniqueCode);
            Photo.setParameter("share", uniqueCode);
            
            List<Album> sl = q.getResultList();
            List<Collection> cl = new ArrayList<Collection>();
            
            
            if ((Collection)inuse.getSingleResult() != null){
                for (Album s : sl){
                Collection c = new Collection();
                c.setUserID(GetUserById(uid));
                c.setAlbumID(s);
                PhotoAlbum ps = (PhotoAlbum)Photo.getSingleResult();
                c.setPhotoID(ps.getPhotoId());
                cl.add(c);
                save(c);}
            }
            return cl;
        } catch(Exception ex){
            Logger.getLogger(DBM.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    private User GetUserById(String uid){
        try {
            Query u = em.createQuery("SELECT u FROM User u WHERE u.id = :uid");
            u.setParameter("uid", uid);
            return (User)u.getSingleResult();
        }catch (Exception ex){
            Logger.getLogger(DBM.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    //Misschien later generieke functie maken om meerdere shizzle in een keer op te halen maar voor nu even dit. -hafid-
    public List<Collection> getCollectionByUserId(int userId)
    {
        User usr = getUser(userId);
 
        List<Collection> results = null;         
        results = em.createQuery("SELECT t FROM Collection t WHERE t.userID = :id")
                .setParameter("id", usr)
                .getResultList();
 
        return results;
    }
    
    public SizePrize getPriceBySizeId(int sizeId){
        Size1 size = findById(Size1.class, sizeId);
        
        SizePrize results = (SizePrize)em.createQuery("SELECT s FROM SizePrize s WHERE s.sizeID = :id")
                .setParameter("id", size)
                .getSingleResult();
        
        return results;
    }
 }

