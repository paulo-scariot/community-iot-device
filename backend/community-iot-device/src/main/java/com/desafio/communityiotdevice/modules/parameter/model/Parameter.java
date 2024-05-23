package com.desafio.communityiotdevice.modules.parameter.model;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.parameter.dto.ParameterRequest;
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
@Table(name = "parameters")
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "command_id", foreignKey = @ForeignKey(name = "fk_parameter_command"))
    private Command command;

    public static Parameter of(ParameterRequest request,
                               Command command) {
        return Parameter
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .command(command)
                .build();
    }
}
