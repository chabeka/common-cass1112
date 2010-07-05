package fr.urssaf.image.commons.controller.spring.extjs.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.commons.controller.spring.extjs.formulaire.MyFormulaireExtJs;
import fr.urssaf.image.commons.controller.spring.formulaire.support.exception.FormulaireException;

import fr.urssaf.image.commons.controller.spring.servlet.AbstractMyController;
import fr.urssaf.image.commons.controller.spring.servlet.support.BeanException;

public abstract class AbstractMyControllerExtJs<F extends MyFormulaireExtJs> extends AbstractMyController<F>{

	@Autowired
	org.springframework.context.support.ResourceBundleMessageSource messageSource ;
	
	public AbstractMyControllerExtJs(Class<F> classe) {
		super(classe);
	}

	/**
	 * dans le cas où le controller a été appelé via XmlHttpRequest, on lance le post traitement des erreurs 
	 * au format ExtJS 
	 */
	protected void postView( 
			HttpServletRequest request,
			HttpServletResponse reponse )
	{
		if( request.getHeader("X-Requested-With") != null
			&& request.getHeader("X-Requested-With").equals( "XMLHttpRequest" ) )
		{
			
			MyFormulaireExtJs monFormulaire = this.getFormulaire(request, reponse);
			
			String jsonString = buildJsonForExtJS(request, reponse) ;
			
			updateBeanErrorField(monFormulaire, jsonString) ;
		
		}
	}
	
	/**
	 * ajoute la chaine json encodée en base 64 dans le champ d'erreur du formulaire
	 * @param formulaire
	 * @param jsonString
	 */
	protected void updateBeanErrorField( 
			MyFormulaireExtJs formulaire, 
			String jsonString )
	{
		//byte[] jsonObjectBytes = jsonString.getBytes() ;
		//String jsonProtectedString = Base64.encodeBytes( jsonObjectBytes ) ;
		String jsonProtectedString = jsonString ;

		formulaire.initFormErrorField( jsonProtectedString );
	}
	
	
	/**
	 * transforme les exceptions du formulaire en une chaine json au format ExtJs
	 * @param request
	 * @param reponse
	 * @return json ExtJs response
	 */
	@SuppressWarnings({ "unchecked" })
	protected String buildJsonForExtJS( 
			HttpServletRequest request,
			HttpServletResponse reponse )
	{
		Map<String, BeanException> errorList = (Map<String, BeanException>) request.getAttribute( AbstractMyController.exception ) ;
		MyFormulaireExtJs monFormulaire = this.getFormulaire(request, reponse);
		JSONObject jsonObject = new JSONObject();
		
		// echec
		if( errorList != null 
			&& errorList.size() > 0 )
		{
			jsonObject.accumulate("success", false) ;
			
			for( String fieldName : monFormulaire.getAllFieldName() )
			{
				if( errorList.containsKey(fieldName) )
				{
					List<String> fieldListError = new ArrayList<String>();
					for( FormulaireException exceptionList : errorList.get(fieldName).getFormulaireExceptions() )
					{
						String errMsgList = messageSource.getMessage(exceptionList.getCode(), null, null);
						if( errMsgList != null )
						{
							String protectedErrMsg = StringEscapeUtils.escapeHtml( errMsgList ) ;
							fieldListError.add( protectedErrMsg );
						}
					}

					Map<String, List<String>> errMsgConverted = new HashMap<String, List<String>>() ;
					errMsgConverted.put(fieldName, fieldListError);
					jsonObject.accumulate("errors", errMsgConverted ) ;
					
					errMsgConverted = null ;
					fieldListError = null ; 
				}
				
			}
			
		}
		// succes
		else
		{
			jsonObject.accumulate("success", true) ;
		}

		String jsonString = jsonObject.toString() ;
		
		return jsonString;
	}

}
