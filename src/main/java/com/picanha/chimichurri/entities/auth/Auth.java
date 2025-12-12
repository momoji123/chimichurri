package com.picanha.chimichurri.entities.auth;

import com.picanha.chimichurri.entities.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "AUTH", schema = "chimichurri")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="AUTH_SEQ")
    @SequenceGenerator(schema="chimichurri", name="AUTH_SEQ", sequenceName="AUTH_SEQ", initialValue=1, allocationSize=1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "USERNAME", nullable = false, length = 100)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 500)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "USERID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_AUTH_USER")
    )
    private User user;

    // Constructors
    public Auth() {}

    public Auth(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    // Getters and setters
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
