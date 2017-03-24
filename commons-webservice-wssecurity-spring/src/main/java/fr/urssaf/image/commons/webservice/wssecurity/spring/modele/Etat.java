package fr.urssaf.image.commons.webservice.wssecurity.spring.modele;

public enum Etat {

	init("init"),close("close"),open("open");
	
	private String libelle;
	
	Etat(String libelle){
		this.libelle = libelle;
	}
	
	public String libelle(){
		return this.libelle;
	}
	
	public String getName(){
		return this.name();
	}
}
