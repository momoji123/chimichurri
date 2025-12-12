package com.picanha.chimichurri.entities.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.picanha.chimichurri.entities.account.Account;
import com.picanha.chimichurri.entities.auth.Auth;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS", schema = "chimichurri")
public class User {
	
	@Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
	@SequenceGenerator(schema="chimichurri", name="USER_SEQ", sequenceName="USER_SEQ", initialValue=1, allocationSize=1)
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Auth auth;

    public User() {
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}
    
    
}
