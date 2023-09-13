package com.stage.elearning.model.user;

import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true , nullable = false)
    private UUID id;

    @Column(name ="full_name", nullable = false)
    private String fullName;
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "email" , unique = true , nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true , nullable = false)
    private String phoneNumber;

    @Column(name = "password" , nullable = false)
    private String password;

    @Column(name = "creating_date" , nullable = false)
    private Date creatingDate;

    @Column( name = "is_enabled", nullable = false)
    private boolean isEnabled = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(mappedBy = "subscribers")
    private List<Article> subscribedArticles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }


}
