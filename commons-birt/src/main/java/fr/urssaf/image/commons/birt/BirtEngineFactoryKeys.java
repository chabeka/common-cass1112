package fr.urssaf.image.commons.birt;


/**
 * 
 * Clés à utiliser dans la Map de paramètres qu'il faut passer au {@link BirtEngineFactory}
 * pour instancier le {@link BirtEngine}<br>
 * <br>
 * <b><u>Pour l'utilisation des classes de BIRT, se référer à la fiche de développement F025</u></b>
 * 
 * @see BirtEngineFactory#getBirtEngineInstance
 *  
 */
public final class BirtEngineFactoryKeys {
   
   private BirtEngineFactoryKeys()
   { }

   /**
    * La clé pour le chemin complet du BIRT Report Engine
    */
   public static final String REPORT_ENGINE_HOME = "reportEngineHome" ; //NOPMD
   
   
   /**
    * La clé pour l'objet ServletContext associé à la servlet qui veut utiliser BIRT
    */
   public static final String SERVLET_CONTEXT = "servletContext" ;
   
   
   /**
    * La clé pour le chemin complet des fichiers de logs de BIRT
    */
   public static final String LOG_PATH = "logPath" ;
   
}
