package spring.p2plending.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import spring.p2plending.enums.Role;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true, nullable = false, length = 20)
    private String nickname;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();
}

