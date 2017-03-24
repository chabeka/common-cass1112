package fr.urssaf.image.commons.ioc.spring.exemple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("constructeur")
public class ExempleByAnnotationConstructeurMethodeServiceImpl implements
		MethodeService {

	private final InjectionService service;

	@Autowired
	public ExempleByAnnotationConstructeurMethodeServiceImpl(
			InjectionService service) {
		this.service = service;
	}

	@Override
	public String methode() {
		return service.methode(this.getClass().getSimpleName());
	}

}
