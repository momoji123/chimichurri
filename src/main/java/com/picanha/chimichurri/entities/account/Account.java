package com.picanha.chimichurri.entities.account;

import com.picanha.chimichurri.entities.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ACCOUNT", schema = "chimichurri")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_SEQ")
    @SequenceGenerator(schema="chimichurri", name="ACCOUNT_SEQ", sequenceName="ACCOUNT_SEQ", initialValue=1, allocationSize=1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TYPE", nullable = false, length = 150)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "USERID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_ACCOUNT_USER")
    )
    private User user;

    public Account() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
