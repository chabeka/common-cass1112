package fr.urssaf.image.sae.mapping.test.constants;

import java.io.File;

/**
 * Cette classe contient la liste des constantes utilisées dans l'application.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class Constants {
	// Fichier pour test la méthode les conversion
	public static final File MAPPING_FILE_1 = new File(
			"src/test/resources/Mapping/mapping_1.xml");
	// Fichier pour test la méthode les conversion avec un intru
	public static final File MAPPING_FILE_2 = new File(
			"src/test/resources/Mapping/mapping_2.xml");

	/** Cette classe n'est pas faite pour être instanciée. */
	private Constants() {
		assert false;
	}

}
