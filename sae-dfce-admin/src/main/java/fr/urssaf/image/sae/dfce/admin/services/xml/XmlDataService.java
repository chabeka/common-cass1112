package fr.urssaf.image.sae.dfce.admin.services.xml;

import java.io.File;
import java.io.FileNotFoundException;

import fr.urssaf.image.sae.dfce.admin.model.Applications;
import fr.urssaf.image.sae.dfce.admin.model.Codes;
import fr.urssaf.image.sae.dfce.admin.model.Contrats;
import fr.urssaf.image.sae.dfce.admin.model.DataBaseModel;
import fr.urssaf.image.sae.dfce.admin.model.LifeCycleRule;
import fr.urssaf.image.sae.dfce.admin.model.Objects;
import fr.urssaf.image.sae.dfce.admin.model.Organismes;
/**
 * Fournit un service de désérialisation de la base
 * @author akenore
 * 
 */
public interface XmlDataService {
	/**
	 * 
	 * @param xmlFile : Le fichier xml contenant les données
	 * @return Le model de base de donnée DFCE
	 * @throws FileNotFoundException Lorsque le fichier n'est pas présent
	 */
	DataBaseModel baseModelReader(final File xmlFile) throws FileNotFoundException;
	
	/**
	 * 
	 * @param xmlFile : Le fichier xml contenant les données
	 * @return Le Code Organisme
	 * @throws FileNotFoundException Lorsque le fichier n'est pas présent
	 */
	Organismes organismesReader(final File xmlFile) throws FileNotFoundException;
	
	/**
	 * 
	 * @param xmlFile : Le fichier xml contenant les données
	 * @return Le contrat de service
	 * @throws FileNotFoundException Lorsque le fichier n'est pas présent
	 */
	Contrats contratReader(final File xmlFile) throws FileNotFoundException;
	
	/**
	 * 
	 * @param xmlFile : Le fichier xml contenant les données
	 * @return Les applications sources
	 * @throws FileNotFoundException Lorsque le fichier n'est pas présent
	 */
	Applications applicationsReader(final File xmlFile) throws FileNotFoundException;
	
	/**
	 * 
	 * @param xmlFile : Le fichier xml contenant les données
	 * @return Les objets types
	 * @throws FileNotFoundException Lorsque le fichier n'est pas présent
	 */
	Objects objectTypeReader(final File xmlFile) throws FileNotFoundException;
	/**
	 * 
	 * @param xmlFile : Le fichier xml contenant les données
	 * @return Les objets types
	 * @throws FileNotFoundException Lorsque le fichier n'est pas présent
	 */
	Codes rndReader(final File xmlFile) throws FileNotFoundException;
	/**
	 * 
	 * @param xmlFile : Le fichier xml contenant les données
	 * @return Le cycle de vies des documents du sae.
	 * @throws FileNotFoundException Lorsque le fichier n'est pas présent
	 */
	LifeCycleRule lifeCycleRuleReader(final File xmlFile) throws FileNotFoundException;
}
