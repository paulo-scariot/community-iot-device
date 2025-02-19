package com.desafio.communityiotdevice.modules.device.model;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_device_user"))
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "devices_commands", joinColumns = {@JoinColumn(name = "device_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "command_id", referencedColumnName = "id")})
    private List<Command> commands = new ArrayList<>();

    @OneToMany(mappedBy = "device", targetEntity = Measurement.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Measurement> measurements = new ArrayList<>();

    public Device(String identifier, String description, String manufacturer, String url, Boolean status, User user, List<Command> commands, List<Measurement> measurements) {
        this.identifier = identifier;
        this.description = description;
        this.manufacturer = manufacturer;
        this.url = url;
        this.status = status;
        this.user = user;
        this.commands = commands;
        this.measurements = measurements;
    }

    public static Device of(DeviceRequest request,
                            User user){
        return Device
                .builder()
                .identifier(request.getIdentifier())
                .description(request.getDescription())
                .manufacturer(request.getManufacturer())
                .url(request.getUrl())
                .status(request.getStatus())
                .commands(request.getCommands().stream().map(Command::of).toList())
                .user(user)
                .build();
    }

}
