package com.desafio.communityiotdevice.modules.parameter.repository;

import com.desafio.communityiotdevice.modules.parameter.model.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter, Integer> {

}
