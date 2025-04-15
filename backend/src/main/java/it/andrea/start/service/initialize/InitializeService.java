package it.andrea.start.service.initialize;

import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.error.exception.mapping.MappingException;
import it.andrea.start.error.exception.user.UserException;

public interface InitializeService {

    public void executeStartOperation() throws BusinessException, UserException, MappingException;

}
