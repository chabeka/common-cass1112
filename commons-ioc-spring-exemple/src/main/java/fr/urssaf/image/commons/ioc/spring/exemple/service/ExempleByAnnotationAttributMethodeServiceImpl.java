package fr.urssaf.image.commons.ioc.spring.exemple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("attribut")
public class ExempleByAnnotationAttributMethodeServiceImpl implements MethodeService {

	@Autowired
	private InjectionService service;

	@Override
	public String methode() {
		return service.methode(this.getClass().getSimpleName());
	}

}
