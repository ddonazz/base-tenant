package it.andrea.start.service;

import it.andrea.start.exception.BusinessException;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.exception.MappingToEntityException;
import it.andrea.start.exception.user.UserNotFoundException;
import it.andrea.start.exception.user.UserRoleAlreadyExistsException;
import it.andrea.start.exception.user.UserRoleNotFoundException;

public interface InitializeService {

    public void executeStartOperation() throws BusinessException, UserNotFoundException, MappingToDtoException, MappingToEntityException, UserRoleNotFoundException, UserRoleAlreadyExistsException;

}
