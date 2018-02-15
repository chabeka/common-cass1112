package fr.urssaf.image.commons.ioc.spring.exemple.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("injection")
public class InjectionAnnotationServiceImpl implements InjectionService {

	protected static final Logger LOGGER = Logger
			.getLogger(InjectionAnnotationServiceImpl.class);

	@Override
	public String methode(String libelle) {
		LOGGER.debug(libelle);
		return libelle;

	}

}
