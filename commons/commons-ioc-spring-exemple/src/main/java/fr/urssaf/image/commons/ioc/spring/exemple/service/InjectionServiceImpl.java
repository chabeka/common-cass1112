package fr.urssaf.image.commons.ioc.spring.exemple.service;

import org.apache.log4j.Logger;

public class InjectionServiceImpl implements InjectionService {

	protected static final Logger LOGGER = Logger
			.getLogger(InjectionServiceImpl.class);

	@Override
	public String methode(String libelle) {
	   LOGGER.debug(libelle);
		return libelle;

	}

}
