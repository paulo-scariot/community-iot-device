package com.desafio.communityiotdevice.modules.device.model;

import com.desafio.communityiotdevice.modules.commanddescription.model.CommandDescription;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "status", nullable = false)
    private Boolean status = Boolean.TRUE;

    @OneToMany(mappedBy = "device", targetEntity = CommandDescription.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommandDescription> commandDescriptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_device_user"))
    private User user;

    public static Device of(DeviceRequest request,
                            User user){
        return Device
                .builder()
                .identifier(request.getIdentifier())
                .description(request.getDescription())
                .manufacturer(request.getManufacturer())
                .url(request.getUrl())
                .user(user)
                .build();
    }

}
