package es.kazzpa.selattserver.models;

// A (temporary) class represent the user saved in the database.

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class AppUser extends EntityDatabase {


    private String username;
    private String password;
    private String email;
    private String role;

    public AppUser() {
    }

    public AppUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @PrePersist
    public void setRoleOnSave(){
        if(this.role == null || this.role.trim().isEmpty()){
            this.role = "USER";
        }

    }

}