package com.desafio.communityiotdevice.modules.commanddescription.model;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.commanddescription.dto.CommandDescriptionRequest;
import com.desafio.communityiotdevice.modules.device.model.Device;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "command_descriptions")
public class CommandDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "result")
    private String result;

    @Column(name = "format")
    private String format;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "command_id", foreignKey = @ForeignKey(name = "fk_commanddescription_command"))
    private Command command;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", foreignKey = @ForeignKey(name = "fk_commanddescription_device"))
    private Device device;

    public static CommandDescription of(CommandDescriptionRequest request,
                                        Command command) {
        return CommandDescription
                .builder()
                .operation(request.getOperation())
                .description(request.getDescription())
                .result(request.getResult())
                .format(request.getFormat())
                .command(command)
                .build();
    }

}



