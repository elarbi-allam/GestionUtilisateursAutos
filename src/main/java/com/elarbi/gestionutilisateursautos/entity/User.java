package com.elarbi.gestionutilisateursautos.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    private int age;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Auto> autos = new ArrayList<>();
    
    // Constructors
    public User() {
    }
    
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public List<Auto> getAutos() {
        return autos;
    }
    
    public void setAutos(List<Auto> autos) {
        this.autos = autos;
    }
    
    // Helper methods for bi-directional relationship
    public void addAuto(Auto auto) {
        autos.add(auto);
        auto.setUser(this);
    }
    
    public void removeAuto(Auto auto) {
        autos.remove(auto);
        auto.setUser(null);
    }
} 