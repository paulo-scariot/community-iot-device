package com.desafio.communityiotdevice.modules.measurement.model;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.device.model.Device;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "result", nullable = false)
    private Double result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", foreignKey = @ForeignKey(name = "fk_commandsresults_device"))
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "command_id", foreignKey = @ForeignKey(name = "fk_commandsresults_command"))
    private Command command;

    public Measurement(LocalDateTime createdAt, Double result, Device device, Command command) {
        this.createdAt = createdAt;
        this.result = result;
        this.device = device;
        this.command = command;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
