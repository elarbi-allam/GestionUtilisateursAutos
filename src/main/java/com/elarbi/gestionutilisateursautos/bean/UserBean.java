package com.elarbi.gestionutilisateursautos.bean;

import com.elarbi.gestionutilisateursautos.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import com.elarbi.gestionutilisateursautos.service.UserService;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserService userService = new UserService();
    private User user = new User();
    private List<User> userList;
    private User selectedUser;

    @PostConstruct
    public void init() {
        loadUsers();
    }

    // Load all users from database
    public void loadUsers() {
        userList = userService.getAllUsers();
    }

    // Save a user
    public String saveUser() {
        userService.saveUser(user);
        user = new User(); // Clear form
        loadUsers();
        return "index?faces-redirect=true";
    }

    // Delete a user
    public void deleteUser(int id) {
        System.out.println("Deleting user with ID: " + id);
        userService.deleteUser(id);

        // Force a refresh of the data
        loadUsers();

        // Add a sleep to ensure database operations complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Force JSF to refresh the view
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("usersTableForm");
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("autoTableForm");
    }

    // Select a user for editing
    public void selectUser(User user) {
        this.selectedUser = user;
    }

    // Update selected user
    public void updateSelectedUser() {
        if (selectedUser != null) {
            System.out.println("Updating user with ID: " + selectedUser.getId());
            userService.saveUser(selectedUser);

            // Force a refresh of the data from the database
            loadUsers();

            // Clear selection
            selectedUser = null;

            // Add a sleep to ensure database operations complete
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Force JSF to refresh the view
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("usersTableForm");
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("autoTableForm");
        }
    }

    // Cancel update
    public void cancelUpdate() {
        selectedUser = null;
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
} 