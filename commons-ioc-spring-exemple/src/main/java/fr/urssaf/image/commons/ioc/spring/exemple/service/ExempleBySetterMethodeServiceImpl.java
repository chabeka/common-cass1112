package fr.urssaf.image.commons.ioc.spring.exemple.service;

public class ExempleBySetterMethodeServiceImpl implements MethodeService {

	private InjectionService service;
	
	public void setInjectionService(InjectionService service){
		this.service = service;
	}
	
	
	@Override
	public String methode() {
		
		return service.methode(this.getClass().getSimpleName());
	}

}
