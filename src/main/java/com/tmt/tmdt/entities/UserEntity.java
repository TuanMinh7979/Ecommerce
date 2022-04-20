package com.tmt.tmdt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tmt.tmdt.constant.UserStatus;
import com.tmt.tmdt.entities.customType.ListToStringConverter;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6, max = 64)
    private String password;

    @Transient
    @Size(min = 6, max = 64)
    private String confirmPassword;

    @Size(max = 128)
    private String email;
    private String imageLink = defaultImage();


    @JsonIgnore
    @OneToOne(
            mappedBy = "userEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Image image;

    @Enumerated(EnumType.STRING)

    private UserStatus status;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @Column(name = "rolenames")
    @Convert(converter = ListToStringConverter.class)
    private List<String> roleNameList = new ArrayList<>();


    public UserEntity(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = UserStatus.ENABLE;

    }

    @Transient
    public String defaultImage() {
        return "/resource/img/defaultAvatar.jpg";
    }


}