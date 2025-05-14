package com.elarbi.gestionutilisateursautos.bean;

import com.elarbi.gestionutilisateursautos.entity.Auto;
import com.elarbi.gestionutilisateursautos.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import com.elarbi.gestionutilisateursautos.service.AutoService;
import com.elarbi.gestionutilisateursautos.service.UserService;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class AutoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private AutoService autoService = new AutoService();
    private UserService userService = new UserService();
    private Auto auto = new Auto();
    private List<Auto> autoList;
    private Auto selectedAuto;
    private int selectedUserId;

    @PostConstruct
    public void init() {
        loadAutos();
    }

    // Load all autos from database
    public void loadAutos() {
        autoList = autoService.getAllAutos();
    }

    // Save an auto
    public String saveAuto() {
        User user = userService.getUserById(selectedUserId);
        auto.setUser(user);
        autoService.saveAuto(auto);
        auto = new Auto(); // Clear form
        selectedUserId = 0;
        loadAutos();
        return "index?faces-redirect=true";
    }

    // Delete an auto
    public void deleteAuto(int id) {
        System.out.println("Deleting auto with ID: " + id);
        autoService.deleteAuto(id);

        // Force a refresh of the data
        loadAutos();

        // Add a sleep to ensure database operations complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Force JSF to refresh the view
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("autoTableForm");
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("usersTableForm");
    }

    // Select an auto for editing
    public void selectAuto(Auto auto) {
        this.selectedAuto = auto;
        if (auto.getUser() != null) {
            this.selectedUserId = auto.getUser().getId();
        }
    }

    // Update selected auto
    public void updateSelectedAuto() {
        if (selectedAuto != null) {
            System.out.println("Updating auto with ID: " + selectedAuto.getId());
            User user = userService.getUserById(selectedUserId);
            selectedAuto.setUser(user);
            autoService.saveAuto(selectedAuto);

            // Force a refresh of the data from the database
            loadAutos();

            // Clear selection
            selectedAuto = null;
            selectedUserId = 0;

            // Add a sleep to ensure database operations complete
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Force JSF to refresh the view
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("autoTableForm");
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("usersTableForm");
        }
    }

    // Cancel update
    public void cancelUpdate() {
        selectedAuto = null;
        selectedUserId = 0;
    }

    // Get all users for dropdown
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Getters and Setters
    public Auto getAuto() {
        return auto;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    public List<Auto> getAutoList() {
        return autoList;
    }

    public void setAutoList(List<Auto> autoList) {
        this.autoList = autoList;
    }

    public Auto getSelectedAuto() {
        return selectedAuto;
    }

    public void setSelectedAuto(Auto selectedAuto) {
        this.selectedAuto = selectedAuto;
    }

    public int getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(int selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
} 