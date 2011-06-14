/*
 * Created on 9 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.docubase.toolkit.base;

import java.util.List;

import net.docubase.toolkit.exception.ged.CustomTagControlException;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.ICustomTagControl;
import net.docubase.toolkit.service.ServiceProvider;

/**
 * 
 * Classe chargée d'effectuer le contrôle de validité du TAG du document DOC1
 */
public class MyTagControl implements ICustomTagControl {

    public final static int NO_ERROR = 0;
    public final static int CATEGORY_MAX_ERROR = 1;
    public final static int CATEGORY_MIN_ERROR = 2;
    public final static int CATEGORY_BAD_ERROR = 3;
    public final static int CREATION_DATE_ERROR = 4;
    public final static int CATEGORY_NO_KEYWORD = 5;

    private String _msgError = "Pas d'erreur";
    private int _errorCode = NO_ERROR;
    private final String[] categoriesNames;

    public MyTagControl(String[] categoriesNames) {
	this.categoriesNames = categoriesNames;
    }

    public void logError() {
	System.err.println(this._msgError);
    }

    public String getErrorMessage() {
	return _msgError;
    }

    public int getErrorCode() {
	return _errorCode;
    }

    public void control(Document document, Base base)
	    throws CustomTagControlException {
	BaseCategory baseCategory0 = base.getBaseCategory(categoriesNames[0]);

	List<Criterion> criteria = document.getCriteria(baseCategory0);
	for (Criterion criterion : criteria) {
	    Object word = criterion.getWord();
	    boolean valueExists = ServiceProvider.getStoreService()
		    .valueExists(baseCategory0, word.toString());
	    if (valueExists) {
		throw new CustomTagControlException(word + " already exists");
	    }
	}
    }
}
