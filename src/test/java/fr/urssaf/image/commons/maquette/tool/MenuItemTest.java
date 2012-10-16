package fr.urssaf.image.commons.maquette.tool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.maquette.exception.ReferentialIntegrityException;


/**
 * Tests unitaires de la classe {@link MenuItem} 
 *
 */
@SuppressWarnings("PMD")
public class MenuItemTest {
   
   MenuItem aParent ;
   MenuItem firstChild ;
   MenuItem secondChild ;

   
   @Before
   public void setUp(){
      aParent = new MenuItem();
      aParent.setLink("google.com");
      aParent.setTitle("google");

      firstChild = new MenuItem();
      firstChild.setLink("microsoft.com");
      firstChild.setTitle("microsoft");

      secondChild = new MenuItem();
      secondChild.setLink("microsoft.com");
      secondChild.setTitle("microsoft");
   }

   
   @After
   public void tearDown(){
      aParent = null ;
      firstChild = null ;
   }

   
   /**
    * Test des getter/setter
    * 
    * @throws ReferentialIntegrityException 
    */
   @Test
   public void getterSetter() throws ReferentialIntegrityException {
      
      // Création d'un objet
      MenuItem item = new MenuItem();
      
      // Vérification du paramétrage initial
      assertTrue("Erreur dans l'initialisation de l'objet",item.getIdUnique()>=0);
      assertEquals("Erreur dans l'initialisation de l'objet","",item.getLink());
      assertEquals("Erreur dans l'initialisation de l'objet","",item.getTitle());
      assertEquals("Erreur dans l'initialisation de l'objet","",item.getDescription());
      assertNull("Erreur dans l'initialisation de l'objet",item.getParent());
      assertNotNull("Erreur dans l'initialisation de l'objet",item.getChildren());
      assertTrue("Erreur dans l'initialisation de l'objet",item.getChildren().size()==0);
      assertFalse("Erreur dans l'initialisation de l'objet",item.hasParent());
      assertFalse("Erreur dans l'initialisation de l'objet",item.hasParent());
      
      // Test des setters
      item.setLink("le_link");
      assertEquals("Erreur dans le getter/setter de link","le_link",item.getLink());
      item.setTitle("le_title");
      assertEquals("Erreur dans le getter/setter de title","le_title",item.getTitle());
      item.setDescription("la_description");
      assertEquals("Erreur dans le getter/setter de description","la_description",item.getDescription());
      MenuItem parent = new MenuItem();
      item.setParent(parent);
      assertSame("Erreur dans le getter/setter de parent", parent, item.getParent());
      
   }

   @Test
   public void simpleAddChild() throws ReferentialIntegrityException
   {
      aParent.addChild( firstChild ) ;
      assertEquals( firstChild, aParent.getChildren().get(0) ) ;
      assertEquals( aParent, firstChild.getParent() ) ;
   }


   
   @Test
   public void simpleSetParent() throws ReferentialIntegrityException
   {
      firstChild.setParent( aParent ) ;
      assertFalse(aParent.hasChildren()) ;
   }

   
   @Test
   public void twoAddChild() throws ReferentialIntegrityException
   {
      
      MenuItem aNewChild = new MenuItem();
      aNewChild.setLink("microsoft.com");
      aNewChild.setTitle("microsoft");

      aParent.addChild( firstChild ) ;
      aParent.addChild( aNewChild ) ;

      assertEquals( firstChild, aParent.getChildren().get(0) ) ;
      assertEquals( aParent, firstChild.getParent() ) ;

      assertEquals( aNewChild, aParent.getChildren().get(1) ) ;
      assertEquals( aParent, aNewChild.getParent() ) ;

   }

   
   @Test(expected=ReferentialIntegrityException.class)
   public void existingAddChild() throws ReferentialIntegrityException
   {
      aParent.addChild( firstChild ) ;
      aParent.addChild( firstChild ) ;
   }

   
   
   @Test(expected=ReferentialIntegrityException.class)
   public void addChildWhereasIsParent() throws ReferentialIntegrityException
   {
      aParent.addChild( firstChild ) ;
      firstChild.addChild( aParent ) ;
   }

   
   @Test
   public void addSubChild() throws ReferentialIntegrityException
   {
      
      aParent.addChild(firstChild) ;
      firstChild.addChild( secondChild );

      assertEquals( aParent, firstChild.getParent() ) ;
      assertEquals( firstChild, secondChild.getParent() ) ;

      assertEquals( firstChild, aParent.getChildren().get(0) ) ;
      assertEquals( secondChild, firstChild.getChildren().get(0) ) ;
      
   }

   
   @Test
   public void anotherTest() throws ReferentialIntegrityException
   {
      List<MenuItem> listMenuItem = new ArrayList<MenuItem>();

      MenuItem miGoogle = new MenuItem();
      miGoogle.setLink("google.com");
      miGoogle.setTitle("google");

      MenuItem miGooglePortal = new MenuItem();
      miGooglePortal.setLink("igoogle.com");
      miGooglePortal.setTitle("igoogle");
      miGoogle.addChild( miGooglePortal ) ;
      
      MenuItem miGoogleDocs = new MenuItem();
      miGoogleDocs.setLink("google.com");
      miGoogleDocs.setTitle("google docs");
      miGoogle.addChild( miGoogleDocs ) ;
      
      MenuItem miGooglePicasa = new MenuItem();
      miGooglePicasa.setLink("picasa.google.com");
      miGooglePicasa.setTitle("picasa");
      miGoogle.addChild( miGooglePicasa ) ;
      
      MenuItem miGoogleReader = new MenuItem();
      miGoogleReader.setLink("reader.google.com");
      miGoogleReader.setTitle("google reader");
      miGoogle.addChild( miGoogleReader ) ;
      
      listMenuItem.add(miGoogle);

      MenuItem miKrosoft = new MenuItem();
      miKrosoft.setLink("microsoft.com");
      miKrosoft.setTitle("Microsoft");

      MenuItem miKrosoftOS = new MenuItem();
      miKrosoftOS.setLink("windows.com");
      miKrosoftOS.setTitle("Système d'exploitation");
      miKrosoft.addChild( miKrosoftOS ) ;
      
      MenuItem miWin95 = new MenuItem();
      miWin95.setLink("w95.com");
      miWin95.setTitle("windows 95");
      miKrosoftOS.addChild( miWin95 ) ;
      
      MenuItem miWin98 = new MenuItem();
      miWin98.setLink("w98.com");
      miWin98.setTitle("windows 98");
      miKrosoftOS.addChild( miWin98) ;
      
      MenuItem miWinMe = new MenuItem();
      miWinMe.setLink("wME.com");
      miWinMe.setTitle("windows Millenium");
      miKrosoftOS.addChild( miWinMe ) ;
      
      MenuItem miWinXP = new MenuItem();
      miWinXP.setLink("wXP.com");
      miWinXP.setTitle("windows XP");
      miKrosoftOS.addChild( miWinXP ) ;
      
      MenuItem miWin7 = new MenuItem();
      miWin7.setLink("w7.com");
      miWin7.setTitle("windows 7");
      miKrosoftOS.addChild( miWin7 ) ;
      
      MenuItem miKrosoftMsn = new MenuItem();
      miKrosoftMsn.setLink("msn.com");
      miKrosoftMsn.setTitle("MSN");
      miKrosoft.addChild( miKrosoftMsn ) ;
      
      MenuItem miKrosoftHardware = new MenuItem();
      miKrosoftHardware.setLink("krosoftHardware.com");
      miKrosoftHardware.setTitle("microsoft hardware");
      miKrosoft.addChild( miKrosoftHardware ) ;
      
      MenuItem miKrosoftXBox = new MenuItem();
      miKrosoftXBox.setLink("xbox.com");
      miKrosoftXBox.setTitle("Xbox");
      miKrosoftHardware.addChild( miKrosoftXBox ) ;

      MenuItem miKrosoftXBox360 = new MenuItem();
      miKrosoftXBox360.setLink("xbox360.com");
      miKrosoftXBox360.setTitle("Xbox 360");
      miKrosoftHardware.addChild( miKrosoftXBox360 ) ;
      
      listMenuItem.add(miKrosoft);

      MenuItem miDivers = new MenuItem();
      miDivers.setLink("euh.com");
      miDivers.setTitle("rien à voir");

      listMenuItem.add(miDivers);
   }
   

   
   /**
    * Test de la méthode {@link MenuItem#setParent(MenuItem)}<br>
    * <br>
    * Cas de test : on essaye de définir comme parent l'item lui-même<br>
    * <br>
    * Résultat attendu : levée d'une exception
    * 
    * @throws ReferentialIntegrityException 
    *
    */
   @Test(expected=ReferentialIntegrityException.class)
   public void setParent_ParentDeLuiMeme() throws ReferentialIntegrityException {
      MenuItem item = new MenuItem();
      item.setParent(item);
   }
   
   
   /**
    * Test de la méthode {@link MenuItem#setParent(MenuItem)}<br>
    * <br>
    * Cas de test : on essaye de définir comme parent un des enfants de l'item<br>
    * <br>
    * Résultat attendu : levée d'une exception
    * 
    * @throws ReferentialIntegrityException 
    */
   @Test(expected=ReferentialIntegrityException.class)
   public void setParent_ParentMaisAussiEnfant() throws ReferentialIntegrityException {
      
      MenuItem item1 = new MenuItem();
      MenuItem item2 = new MenuItem();
      
      item1.addChild(item2);
      item1.setParent(item2);
   
   }
   
   
   /**
    * Test de la méthode {@link MenuItem#setParent(MenuItem)}<br>
    * <br>
    * Cas de test : cas standard<br>
    * <br>
    * Résultat attendu : le parent est bien défini
    * 
    * @throws ReferentialIntegrityException
    */
   @Test
   public void setParent_CasNormal() throws ReferentialIntegrityException {
      
      MenuItem item1 = new MenuItem();
      MenuItem item2 = new MenuItem();
      
      item1.setParent(item2);
      
      assertEquals("Echec de la définition du parent",true,item1.hasParent());
      assertSame("Echec de la définition du parent",item2,item1.getParent());
   
   }

   
   /**
    * Tests de la méthode {@link MenuItem#isAChild}
    * 
    * @throws ReferentialIntegrityException 
    */
   @Test
   public void isAChild() throws ReferentialIntegrityException {
      
      MenuItem item1;
      MenuItem item2;
      MenuItem item3;
      
      // Test à blanc
      item1 = new MenuItem();
      item2 = new MenuItem();
      assertFalse("Echec de isAChild",MenuItem.isAChild(item1, item2));
      
      // Test simple
      item1 = new MenuItem();
      item2 = new MenuItem();
      item1.addChild(item2);
      assertTrue("Echec de isAChild",MenuItem.isAChild(item1, item2));
      
      // Test sur 3 générations
      item1 = new MenuItem();
      item2 = new MenuItem();
      item1.addChild(item2);
      item3 = new MenuItem();
      item2.addChild(item3);
      assertTrue("Echec de isAChild",MenuItem.isAChild(item1, item3));
      
      
   }
   
}
