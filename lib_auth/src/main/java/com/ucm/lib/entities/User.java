package com.ucm.lib.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * User Entity class, ported & modified
 *
 * @author Charvin Patel
 * @author Joshua Podhola
 */


@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 31)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max = 127)
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Size(min = 12, max = 31)
    @Column(name = "phone")
    private String phNum;

    @NotBlank
    @Size(max = 63)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 63)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name="is_active")
    private Boolean isActive;


    public User() {
    }

    public User(String username, String email, String password, String phNum, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phNum = phNum;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(phNum, user.phNum) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(isActive, user.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, phNum, firstName, lastName, isActive);
    }
}
