package com.desafio.communityiotdevice.modules.command.repository;

import com.desafio.communityiotdevice.modules.command.model.Command;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<Command, Integer> {

    Page<Command> findByCommandContainingIgnoreCase(String filter, Pageable pageable);

}
