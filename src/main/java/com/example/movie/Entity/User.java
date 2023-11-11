package com.example.movie.Entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "Email")
    private String Email;

    @Column(name = "Fullname")
    private String fullName;

    @Column(name = "Password")
    private String Password;

    @Column(name = "username")
    private String username;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL)
    Set<Reservation> reservations;

    public String getRoleNames() {
        return roles.stream()
                .map(Roles::getName)
                .collect(Collectors.joining(", "));
    }

}