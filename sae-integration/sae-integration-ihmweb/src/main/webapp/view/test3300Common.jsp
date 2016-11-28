<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<sae:casTest />

<sae:urlServiceWeb />
	
<sae:captureUnitaire numeroEtape="capture_unitaire_copie" objetFormulaire="${formulaire.captureUnitaire}" pathFormulaire="captureUnitaire" />

<sae:copie numeroEtape="copie" objetFormulaire="${formulaire.copie}" pathFormulaire="copie" />

<sae:recherche numeroEtape="recherche_document_existant"  objetFormulaire="${formulaire.rechercheDocExistant}" pathFormulaire="rechercheDocExistant" />
	
<sae:recherche numeroEtape="recherche_document_copie"  objetFormulaire="${formulaire.rechercheDocCopie}" pathFormulaire="rechercheDocCopie" />

<sae:soapMessages objetFormulaire="${formulaire.soapFormulaire}" />
