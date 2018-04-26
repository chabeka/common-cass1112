package fr.urssaf.image.commons.ioc.spring.exemple.service;

public class ExempleByConstructeurMethodeServiceImpl implements MethodeService {

	private final InjectionService service;
	
	public ExempleByConstructeurMethodeServiceImpl(InjectionService service){
		this.service = service;
	}
	
	
	@Override
	public String methode() {
		return service.methode(this.getClass().getSimpleName());
	}

}
