package fr.urssaf.image.commons.maquette.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Assert;


/**
 * Fonctions utilitaires pour les tests unitaires
 *
 */
public final class TestsUtils {

   
   /**
    * Constructeur privé, pour empêcher l'instantiation de la classe
    */
   private TestsUtils() {
      
   }
   
   
   /**
    * Renvoie un nom de fichier unique <br>
    * La construction est la suivante : date/heure + nombre aléatoire<br>
    * <br>
    * Il existe également une fonction de l'API Java qui permet de <b><u>CREER</u></b>
    * un fichier temporaire : {@link java.io.File#createTempFile(String, String)}
    * 
    * @return le nom de fichier unique
    * @see TestsUtils#getTemporaryFileName(String)
    * @see TestsUtils#getTemporaryFileName(String,String)
    */
   public static String getTemporaryFileName()
   {
      return getTemporaryFileName(null,null);
   }
   
   
   /**
    * Renvoie un nom de fichier unique <br>
    * La construction est la suivante : prefixe + date/heure + nombre aléatoire<br>
    * <br>
    * Il existe également une fonction de l'API Java qui permet de <b><u>CREER</u></b>
    * un fichier temporaire : {@link java.io.File#createTempFile(String, String)}
    * 
    * @param prefixe un préfixe éventuel (ou null)
    * @return le nom de fichier unique
    * @see TestsUtils#getTemporaryFileName
    * @see TestsUtils#getTemporaryFileName(String,String)
    */
   public static String getTemporaryFileName(String prefixe)
   {
      return getTemporaryFileName(prefixe,null);
   }
   
   
   /**
    * Renvoie un nom de fichier unique <br>
    * La construction est la suivante : prefixe + date/heure + nombre aléatoire + suffixe<br>
    * <br>
    * Il existe également une fonction de l'API Java qui permet de <b><u>CREER</u></b>
    * un fichier temporaire : {@link java.io.File#createTempFile(String, String)}
    * 
    * @param prefixe un préfixe éventuel (ou null)
    * @param suffixe un suffixe éventuel (ou null)
    * @return le nom de fichier unique
    * @see TestsUtils#getTemporaryFileName
    * @see TestsUtils#getTemporaryFileName(String)
    */
   public static String getTemporaryFileName(
         String prefixe,
         String suffixe)
   {
      // La méthode getTemporaryFileName() est déplacée dans la classe TestsUtils
      return TempFileUtils.getTemporaryFileName(prefixe, suffixe);
   }
   
   
   /**
    * Teste le constructeur privé sans arguments de la classe passée en paramètre, 
    * afin de dépolluer le code coverage des constructeurs privés non testés<br>
    * <br>
    * Exemple d'utilisation :<br>
    * <pre>
    *    Boolean result = TestsUtils.testConstructeurPriveSansArgument(LaClasseATester.class);
    *    assertTrue("Le constructeur privé n'a pas été trouvé",result);
    * </pre>
    * 
    * @param classe la classe dont on veut tester le constructeur privé sans argument
    * @return true si un constructeur privé sans argument a été trouvé et testé, 
    *         false si aucun constructeur privé sans argument n'a été trouvé 
    * @throws TestConstructeurPriveException en cas de problème lors du test 
    */
   @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
   public static Boolean testConstructeurPriveSansArgument(final Class<?> classe)
   throws TestConstructeurPriveException { 
      Boolean result = false;
      final Constructor<?>[] constructeurs = classe.getDeclaredConstructors();

      for(final Constructor<?> constructeur : constructeurs) {
         if ((
               Modifier.isPrivate(constructeur.getModifiers())) && // constructeur privé 
               (constructeur.getParameterTypes().length==0))       // constructeur sans argument
         {
            constructeur.setAccessible(true); 
            Object objet;
            try {

               objet = constructeur.newInstance((Object[])null);
               Assert.assertNotNull(objet);
               result = true;
               break;

            } catch (IllegalArgumentException e) {
               throw new TestConstructeurPriveException(e);
            } catch (InstantiationException e) {
               throw new TestConstructeurPriveException(e);
            } catch (IllegalAccessException e) {
               throw new TestConstructeurPriveException(e);
            } catch (InvocationTargetException e) {
               throw new TestConstructeurPriveException(e);
            }

         }
      }
      return result;
   }
   
}
