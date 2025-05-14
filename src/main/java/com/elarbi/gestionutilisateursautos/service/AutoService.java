package com.elarbi.gestionutilisateursautos.service;

import com.elarbi.gestionutilisateursautos.entity.Auto;
import com.elarbi.gestionutilisateursautos.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.elarbi.gestionutilisateursautos.util.HibernateUtil;

import java.util.List;

public class AutoService {
    
    // Create or update an auto
    public void saveAuto(Auto auto) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(auto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    // Get auto by ID
    public Auto getAutoById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Auto.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Get all autos
    public List<Auto> getAllAutos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Auto", Auto.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Get autos by user
    public List<Auto> getAutosByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Auto WHERE user.id = :userId", Auto.class)
                    .setParameter("userId", user.getId())
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Delete an auto
    public void deleteAuto(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Auto auto = session.get(Auto.class, id);
            if (auto != null) {
                session.remove(auto);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
} 