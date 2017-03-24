package fr.urssaf.image.commons.webservice.generate;

import org.apache.cxf.tools.common.ToolContext;
import org.apache.cxf.tools.wsdlto.WSDLToJava;

/**
 * Classe de génération de code d'un webservice.<br>
 * Basée sur le Framework <u>Apache Cxf</u><br>
 * elle instancie un objet WSDLToJava {@link org.apache.cxf.tools.wsdlto.WSDLToJava}.
 * <br><br>
 * Utilisé pour les webservices de type Document/Literal
 */
public class GenerateSourceCxf {

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
	public GenerateSourceCxf(String packagePath, String wsdl) {

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
	public GenerateSourceCxf(String packagePath, String wsdl,String path) {
      this(packagePath,wsdl);
      this.path = path;

   }

	/**
	 * Génére le code source
	 */
	public final void generate() {

		ToolContext context = new ToolContext();
		context.setPackageName(this.packagePath);

		myWSDL2Java.setArguments(new String[] { "-d", path,"-verbose",
				this.wsdl });

		try {
         myWSDL2Java.run(context);
      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }

	}
	
	@SuppressWarnings("PMD.AtLeastOneConstructor")
	private static class MyWSDL2Java extends WSDLToJava {
	   
	}
}
