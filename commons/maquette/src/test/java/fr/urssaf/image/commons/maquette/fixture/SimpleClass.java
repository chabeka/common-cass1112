package fr.urssaf.image.commons.maquette.fixture;

public class SimpleClass implements SimpleInterface{

	public SimpleClass() {
	}

	public String sayHello()
	{
		return "Hello" ;
	}

	@Override
	public Boolean isFunnyDay() {
		return true;
	}
}
