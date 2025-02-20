package com.desafio.communityiotdevice.modules.user.model;

import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.user.dto.UserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;

    @OneToMany(mappedBy = "user", targetEntity = Device.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList<>();

    public User(String username, String password, RoleEnum role, List<Device> devices) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.devices = devices;
    }

    public static User of(UserRequest request){
        User user = new User();
        BeanUtils.copyProperties(request, user);
        return user;
    }
}
