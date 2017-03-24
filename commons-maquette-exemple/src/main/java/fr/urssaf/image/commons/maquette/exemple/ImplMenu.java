package fr.urssaf.image.commons.maquette.exemple;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.urssaf.image.commons.maquette.definition.IMenu;
import fr.urssaf.image.commons.maquette.exception.MenuException;
import fr.urssaf.image.commons.maquette.tool.MenuItem;


/**
 * Pour la maquette : menu principal
 *
 */
public final class ImplMenu implements IMenu {


   @Override
	public List<MenuItem> getMenu(HttpServletRequest request) throws MenuException {
		
	   List<MenuItem> listMenuItem = new ArrayList<MenuItem>() ;
	   
	   try {
	   
	      // accueil
	      MenuItem miAccueil = new MenuItem();
	      miAccueil.setTitle("Accueil");
	      miAccueil.setLink("index.jsp");
         listMenuItem.add(miAccueil);
	      
	      // les cas de tests
	      
	      MenuItem miTests = new MenuItem();
	      miTests.setTitle("Cas de tests");
	      listMenuItem.add(miTests);
	      
	      MenuItem miTestXX;
	      
	      miTestXX = new MenuItem();
	      miTestXX.setLink("00_pagecomplete.html");
	      miTestXX.setTitle("Page complète");
	      miTests.addChild(miTestXX);
	      
	      miTestXX = new MenuItem();
         miTestXX.setLink("01_pagesanshtmlniheadnibody.jsp");
         miTestXX.setTitle("Sans html, head et body");
         miTests.addChild(miTestXX);
         
         miTestXX = new MenuItem();
         miTestXX.setLink("03_erreurHead.jsp");
         miTestXX.setTitle("Avec une erreur dans le head");
         miTests.addChild(miTestXX);
         
         miTestXX = new MenuItem();
         miTestXX.setLink("04_afficherImage.html");
         miTestXX.setTitle("Avec une image");
         miTests.addChild(miTestXX);
         
         miTestXX = new MenuItem();
         miTestXX.setLink("05_surchargeCSS.html");
         miTestXX.setTitle("Avec surcharge CSS");
         miTests.addChild(miTestXX);

         miTestXX = new MenuItem();
         miTestXX.setLink("06_injectionLeftCol.jsp");
         miTestXX.setTitle("Avec une boîte de gauche spécifique #1");
         miTests.addChild(miTestXX);
         
         miTestXX = new MenuItem();
         miTestXX.setLink("07_injectionLeftColAutre.jsp");
         miTestXX.setTitle("Avec une boîte de gauche spécifique #2");
         miTests.addChild(miTestXX);
         
         miTestXX = new MenuItem();
         miTestXX.setLink("08_texte.txt");
         miTestXX.setTitle("Fichier texte");
         miTests.addChild(miTestXX);
         
         miTestXX = new MenuItem();
         miTestXX.setLink("09_exclude_1.html");
         miTestXX.setTitle("Exclude 1");
         miTests.addChild(miTestXX);
         
         miTestXX = new MenuItem();
         miTestXX.setLink("10_exclude_2.html");
         miTestXX.setTitle("Exclude 2");
         miTests.addChild(miTestXX);
	      
	   } catch (Exception ex) {
	      throw new MenuException(ex);
	   }
		
		return listMenuItem;
	}

	@Override
	public String getBreadcrumb(HttpServletRequest request) {
		return null ;
	}

}
