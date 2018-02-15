package fr.urssaf.image.commons.maquette.exemple;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.urssaf.image.commons.maquette.definition.ILeftCol;
import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;

/**
 * Pour la maquette : boîtes de gauche 
 *
 */
public final class ImplLeftCol implements ILeftCol {

   @Override
	public String getNomApplication(HttpServletRequest request) {
		return "Test webapp";
	}

	@Override
	public String getVersionApplication(HttpServletRequest request) {
		return "v 0.xxx";
	}

	@Override
	public String getNomUtilisateur(HttpServletRequest request) {
		return "Henry T";
	}

	@Override
	public String getRoleUtilisateur(HttpServletRequest request) {
		return "admin";
	}

	@Override
	public String getLienDeconnexion(HttpServletRequest request) {
		return "javascript:alert('logout');";
	}

	@Override
	public List<InfoBoxItem> getInfoBox(HttpServletRequest request) {
		
	   ArrayList<InfoBoxItem> infoBoxList = new ArrayList<InfoBoxItem>();
	   
	   if (request.getRequestURI().endsWith("06_injectionLeftCol.jsp")) {
	      InfoBoxItem otherInfo = new InfoBoxItem( "other", "Divers", "divers...." ) ;
         otherInfo.addSpan( "test", "ma description", "Contenu du 1er span" ) ;
         otherInfo.addSpan( "light", "une autre description", "Contenu du 2&egrave;me span" ) ;
         infoBoxList.add( otherInfo ) ;
	   }
	   else if (request.getRequestURI().endsWith("07_injectionLeftColAutre.jsp")) {
	      InfoBoxItem otherInfo = new InfoBoxItem( "other", "Autres", "autres...." ) ;
			otherInfo.addSpan( "test", "ma description", "Contenu du 1er span" ) ;
         otherInfo.addSpan( "light", "une autre description", "Contenu du 2&egrave;me span" ) ;
			infoBoxList.add( otherInfo ) ;
		}
		
		return infoBoxList;
	}

}
