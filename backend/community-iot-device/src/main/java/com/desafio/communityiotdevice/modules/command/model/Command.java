package com.desafio.communityiotdevice.modules.command.model;

import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
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
@Table(name = "commands")
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "command", nullable = false)
    private String command;

    @ManyToMany(mappedBy = "commands", fetch = FetchType.LAZY)
    private List<Device> devices = new ArrayList<>();

    @OneToMany(mappedBy = "command", targetEntity = Measurement.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Measurement> measurements = new ArrayList<>();

    public Command(String description, String command, List<Device> devices, List<Measurement> measurements) {
        this.description = description;
        this.command = command;
        this.devices = devices;
        this.measurements = measurements;
    }

    public static Command of(CommandRequest request) {
        Command command = new Command();
        BeanUtils.copyProperties(request, command);
        return command;
    }
}
