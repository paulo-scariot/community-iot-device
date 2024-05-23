package com.desafio.communityiotdevice.modules.command.model;

import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import com.desafio.communityiotdevice.modules.commanddescription.model.CommandDescription;
import com.desafio.communityiotdevice.modules.parameter.model.Parameter;
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

    @Column(name = "command", nullable = false)
    private String command;

    @OneToMany(mappedBy = "command", targetEntity = CommandDescription.class, fetch = FetchType.LAZY)
    private List<CommandDescription> commandDescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "command", targetEntity = Parameter.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Parameter> parameters = new ArrayList<>();

    public static Command of(CommandRequest request) {
        Command command = new Command();
        BeanUtils.copyProperties(request, command);
        return command;
    }
}
