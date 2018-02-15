package fr.urssaf.image.commons.webservice.generate;

import java.util.List;

import org.apache.axis.utils.CLArgsParser;
import org.apache.axis.utils.CLOption;
import org.apache.axis.utils.Messages;
import org.apache.axis.wsdl.WSDL2Java;
import org.apache.log4j.Logger;

/**
 * Classe de génération de code d'un webservice.<br>
 * Basée sur le Framework <u>Apache Axis</u><br>
 * elle instancie un objet WSDLToJava {@link org.apache.axis.wsdl.WSDL2Java}.
 * <br><br>
 * Utilisé pour les webservices de type RPC/Literal & RPC/Encoded
 */
public class GenerateSourceAxis {

	protected static final Logger LOGGER = Logger
			.getLogger(GenerateSourceAxis.class);

	private final String packagePath;

	private final String wsdl;

	private final MyWSDL2Java myWSDL2Java;
	
	private String path = "src/main/java";

	/**
    * Paramétrage du fichier wsdl et du nom du package des classes générées<br>
    * L'emplacement du package généré est src/main/java
    * 
    * @param packagePath nom du package des classes générées
    * @param wsdl chemin du fichier wsdl
    */
	public GenerateSourceAxis(String packagePath, String wsdl) {

		myWSDL2Java = new MyWSDL2Java();

		this.wsdl = wsdl;
		this.packagePath = packagePath;

	}
	
	/**
    *  Paramétrage du fichier wsdl, du nom du package des classes générées et du chemin du package généré<br>
    * 
    * @param packagePath nom du package des classes générées
    * @param wsdl chemin du fichier wsdl
    * @param path emplacement du package généré
    */
	public GenerateSourceAxis(String packagePath, String wsdl,String path) {

      this(packagePath,wsdl);
      this.path = path;

   }

	/**
    * Génére le code source
    */
	public final void generate() {

		String[] args = new String[] { "-p" + this.packagePath,
				"-o"+path, this.wsdl };

		myWSDL2Java.run(args);

	}

	private static class MyWSDL2Java extends WSDL2Java {

		@SuppressWarnings("unchecked")
		@Override
		protected void run(String[] args) {

			// Parse the arguments
			CLArgsParser argsParser = new CLArgsParser(args, options);

			// Print parser errors, if any
			if (null != argsParser.getErrorString()) {

				LOGGER.error(Messages.getMessage("error01", argsParser
						.getErrorString()));
				printUsage();
			}

			// Get a list of parsed options
			List clOptions = argsParser.getArguments();
			int size = clOptions.size();

			try {

				// Parse the options and configure the emitter as appropriate.
				for (int i = 0; i < size; i++) {
					parseOption((CLOption) clOptions.get(i));
				}

				// validate argument combinations
				// 
				validateOptions();
				parser.run(wsdlURI);

			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
