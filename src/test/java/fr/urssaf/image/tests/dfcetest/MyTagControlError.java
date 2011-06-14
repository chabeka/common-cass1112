/*
 * Created on 9 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fr.urssaf.image.tests.dfcetest;


import net.docubase.rheatoolkit.base.BaseIndex;
import net.docubase.rheatoolkit.base.CategoryDescription;
import net.docubase.rheatoolkit.session.base.DocumentTagControlError;

/**
 * 
 * Classe charg�e d'effectuer le contr�le de validit� du TAG du document DOC1
 */
public class MyTagControlError extends DocumentTagControlError {
	
	public static final int NO_ERROR = 0;
	public static final int CATEGORY_MAX_ERROR = 1;
	public static final int CATEGORY_MIN_ERROR = 2;
	public static final int CATEGORY_BAD_ERROR = 3;
	public static final int CREATION_DATE_ERROR = 4;
	public static final int CATEGORY_NO_KEYWORD = 5;
	
	private String _msgError = "Pas d'erreur";
	private int _errorCode = NO_ERROR;
	private CategoryDescription categories[];
	
	
	
	public MyTagControlError(BaseIndex basedef) {
		super(true, true, true);
		categories = basedef.getCategoryList();
	}
	
	String getLibelle(int catNum){
		try{
			return 	"\"" + categories[catNum].getName() + "\"";
		}
		catch(IndexOutOfBoundsException ex){
			return "" + catNum;
		}
	}
	
	public boolean categoryMaximum(int catNum, int maximum, int nbrKeyword){
		_msgError = "Le nombre de valeurs (" + nbrKeyword + ") affectées à la catégorie:" + getLibelle(catNum) + " dépasse le nombre maximum autorisé:" + maximum;
		_errorCode = CATEGORY_MAX_ERROR;
		return false;
	}		
	
	public boolean categoryBad(int catNum, int nbrCategory){
		_msgError = "La cat�gorie:" + getLibelle(catNum) + " n'existe pas dans la base. La plus grande catégorie autorisée est:" + (nbrCategory-1);
		_errorCode = CATEGORY_BAD_ERROR;
		return false;	
	}
	
	 public boolean creationDateMandatory(){
	 	_msgError = "La date de création doit être renseignée.";
	 	_errorCode = CREATION_DATE_ERROR;
	 	return false;
	 }
	 
	 public boolean categoryMinimum(int catNum, int minimum, int nbrKeyword) 
	 {
	 	_msgError = "La catégorie:" + getLibelle(catNum) + " doit posséder au moins:" + minimum + " valeurs.";
	 	_errorCode = CATEGORY_MIN_ERROR;
	 	
	 	return false;
	 	
	 	
	 }
	 
	 public boolean noKeyword(){
	 	_msgError = "Pas de catégorie renseignée pour le document.";
	 	_errorCode = CATEGORY_NO_KEYWORD;
	 	
	 	return false;
	 }
	 
	 public void logError(){
	 	System.err.println(this._msgError);
	 }
	 
	 

	public boolean keywordExist(int catNum, String keyword) {			
		if (catNum == 0 || catNum == 1 || catNum == 2 || catNum == 3) {
			if (keyword == null || keyword.length() == 0) {
				return false;
			}
		}
		return true;
	}
	
	public String getErrorMessage(){
		return _msgError;
	}
	
	public int getErrorCode(){
		return _errorCode;
	}
}
