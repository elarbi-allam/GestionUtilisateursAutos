package com.elarbi.gestionutilisateursautos.service;

import com.elarbi.gestionutilisateursautos.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.elarbi.gestionutilisateursautos.util.HibernateUtil;

import java.util.List;

public class UserService {

    // Create or update a user
    public void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Saving/updating user: " + user.getName() + ", ID: " + user.getId());

            if (user.getId() > 0) {
                // This is an update operation
                User existingUser = session.get(User.class, user.getId());
                if (existingUser != null) {
                    existingUser.setName(user.getName());
                    existingUser.setAge(user.getAge());
                    session.merge(existingUser);
                    System.out.println("Updated existing user: " + existingUser.getId());
                } else {
                    session.merge(user);
                }
            } else {
                // This is a create operation
                session.persist(user);
                System.out.println("Created new user with ID: " + user.getId());
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get user by ID
    public User getUserById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // First get all users
            List<User> users = session.createQuery("FROM User", User.class).list();
            System.out.println("Retrieved " + users.size() + " users from database");

            // Initialize collections for each user
            for (User user : users) {
                System.out.println("User: " + user.getName() + ", ID: " + user.getId());
                if (user.getAutos() != null) {
                    System.out.println("  Autos: " + user.getAutos().size());
                }
            }

            transaction.commit();
            return users;
        } catch (Exception e) {
            System.err.println("Error retrieving users: " + e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    // Delete a user
    public void deleteUser(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
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