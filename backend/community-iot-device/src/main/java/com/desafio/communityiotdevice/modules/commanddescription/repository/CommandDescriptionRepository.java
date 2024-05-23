package com.desafio.communityiotdevice.modules.commanddescription.repository;

import com.desafio.communityiotdevice.modules.commanddescription.model.CommandDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandDescriptionRepository extends JpaRepository<CommandDescription, Integer> {

    Page<CommandDescription> findByOperationContainingIgnoreCase(String filter, Pageable pageable);

    Boolean existsByCommandId(Integer id);


}
