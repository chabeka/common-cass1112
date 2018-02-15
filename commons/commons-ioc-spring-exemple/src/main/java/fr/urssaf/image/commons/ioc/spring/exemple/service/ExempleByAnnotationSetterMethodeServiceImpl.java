package fr.urssaf.image.commons.ioc.spring.exemple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("setter")
public class ExempleByAnnotationSetterMethodeServiceImpl implements MethodeService {
	
	private InjectionService service;
	
	@Autowired
	public void setInjectionService(InjectionService service){
		this.service = service;
	}

	@Override
	public String methode() {
		return service.methode(this.getClass().getSimpleName());
	}

}
