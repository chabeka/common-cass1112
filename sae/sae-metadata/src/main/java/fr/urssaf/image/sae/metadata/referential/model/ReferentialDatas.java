package fr.urssaf.image.sae.metadata.referential.model;

/**
 * Classe provisoire contenant le référentiel des métadonnées.
 * 
 * @author akenore
 * 
 */
public final class ReferentialDatas {

	/**
	 * permettant de construie le flux xml pour les servies de d'insertion
	 * 
	 * @return Le flux xml pour les servies d'insertion
	 */
	@SuppressWarnings({ "PMD.NcssMethodCount", "PMD.ExcessiveMethodLength",
			"PMD.AvoidDuplicateLiterals" })
	public static String buildXmlfile() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		stringBuilder.append("<referentiel>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NbPages</longCode>");
		stringBuilder.append("<shortCode>NBP</shortCode>");
		stringBuilder.append("<label>Nombre de pages</label>");
		stringBuilder
				.append("<description>Nombre de pages du document</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>INTEGER</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>-1</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>CodeRND</longCode>");
		stringBuilder.append("<shortCode>RND</shortCode>");
		stringBuilder
				.append("<label>Code référentiel nationale de document	</label>");
		stringBuilder
				.append("<description>Code issu d'un référentiel permettant de typer nationalement un document</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>11</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>VersionRND</longCode>");
		stringBuilder.append("<shortCode>VRN</shortCode>");
		stringBuilder.append("<label>Version de Code RND</label>");
		stringBuilder
				.append("<description>Numéro permettant d'identifier la version de provenance	du code RND renseigné précédemment</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>CodeFonction</longCode>");
		stringBuilder.append("<shortCode>DOM</shortCode>");
		stringBuilder.append("<label>Code fonction RFP</label>");
		stringBuilder
				.append("<description>Code de la fonction/domaine RFP</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>2</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>CodeActivite</longCode>");
		stringBuilder.append("<shortCode>ACT</shortCode>");
		stringBuilder.append("<label>Code Activite RFP</label>");
		stringBuilder
				.append("<description>Code de l'acitivté RFP</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>2</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>DureeConservation</longCode>");
		stringBuilder.append("<shortCode>DCO</shortCode>");
		stringBuilder.append("<label>Duree de conservation</label>");
		stringBuilder
				.append("<description>Duree de conservation de l'archive au terme duquel l'archive est détruite</description>");
		stringBuilder.append("<archivable>False</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>INTEGER</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>-1</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>DateDebutConservation</longCode>");
		stringBuilder.append("<shortCode>DDC</shortCode>");
		stringBuilder.append("<label>Date debut conservation</label>");
		stringBuilder
				.append("<description>Date de début de la durée de conservation. (Si elle n'est pas renseignée, la date de début de conservation est considérée comme	étant égale à la date d'archivage)</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>DATE</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>DateFinConservation</longCode>");
		stringBuilder.append("<shortCode>DFC</shortCode>");
		stringBuilder.append("<label>Date fin conservation</label>");
		stringBuilder
				.append("<description>Date de fin de la durée de conservation</description>");
		stringBuilder.append("<archivable>False</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>DATE</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>JetonDePreuve</longCode>");
		stringBuilder.append("<shortCode>JDP</shortCode>");
		stringBuilder.append("<label>Jeton de preuve</label>");
		stringBuilder
				.append("<description>Possibilité de stocker le jeton de preuve à des fins de	conservation long terme</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>-1</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>TracabilitrePreArchivage</longCode>");
		stringBuilder.append("<shortCode>TPA</shortCode>");
		stringBuilder.append("<label>Tracabilite pre archivage</label>");
		stringBuilder
				.append("<description>Tracabilité utilisé surtout dans le cas de la chaine	d'acquisition</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal></internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>200</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>Hash</longCode>");
		stringBuilder.append("<shortCode>version.1.digest</shortCode>");
		stringBuilder.append("<label>Hash du fichier</label>");
		stringBuilder.append("<description>Hash du fichier	</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>15</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>TypeHash</longCode>");
		stringBuilder
				.append("<shortCode>version.1.digest.algorithm</shortCode>");
		stringBuilder.append("<label>type de hash utilisé</label>");
		stringBuilder.append("<description>type de hash utilisé</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>15</length>");
		stringBuilder.append("<required></required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>DateArchivage</longCode>");
		stringBuilder.append("<shortCode>_archivageDate</shortCode>");
		stringBuilder.append("<label>Date archivage	</label>");
		stringBuilder
				.append("<description>Date à laquelle le document a été archivé dans le SAE</description>");
		stringBuilder.append("<archivable>False</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>DATE</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>DateCreation</longCode>");
		stringBuilder.append("<shortCode>_creationDate</shortCode>");
		stringBuilder.append("<label>Date creation</label>");
		stringBuilder
				.append("<description>Date de création du document au niveau de l'application	émettrice</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>DATE</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>DateModification</longCode>");
		stringBuilder.append("<shortCode>_modificationDate</shortCode>");
		stringBuilder.append("<label>Date modification (DFCE)</label>");
		stringBuilder
				.append("<description>Date modification (DFCE)</description>");
		stringBuilder.append("<archivable>False</archivable>");
		stringBuilder.append("<consultable>False</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type></type>");
		stringBuilder.append("<pattern>DATE</pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>DateReception</longCode>");
		stringBuilder.append("<shortCode>DRE</shortCode>");
		stringBuilder.append("<label>Date reception</label>");
		stringBuilder
				.append("<description>Date à laquelle le document a été reçu pour un courrier par exemple</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>DATE</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>ContratDeService</longCode>");
		stringBuilder.append("<shortCode>CSE</shortCode>");
		stringBuilder.append("<label>Contrat de Service</label>");
		stringBuilder
				.append("<description>Numero permettant d'identifier sur quel contrat de	service le document a pu être archivé</description>");
		stringBuilder.append("<archivable>False</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>15</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>ApplicationProductrice</longCode>");
		stringBuilder.append("<shortCode>APR</shortCode>");
		stringBuilder
				.append("<label>Application Produtrice du document</label>");
		stringBuilder
				.append("<description>Code de l'application qui a produit le document (pour un	document WATT issu du scan, ce sera la chaine d'acquisiton)	</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>15</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>ApplicationTraitement</longCode>");
		stringBuilder.append("<shortCode>ATR</shortCode>");
		stringBuilder.append("<label>Application traitant le document</label>");
		stringBuilder
				.append("<description>Code de l'application qui a fait des traitements sur le	document avant archivage</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>String</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>15</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>SiteAcquisition</longCode>");
		stringBuilder.append("<shortCode>SAC</shortCode>");
		stringBuilder.append("<label>Site acquisition</label>");
		stringBuilder
				.append("<description>Code site du lieu d'acquisition dans le cas d'une numérisation</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>String</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>5</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>FormatFichier</longCode>");
		stringBuilder.append("<shortCode>FFI</shortCode>");
		stringBuilder.append("<label>Format de fichier</label>");
		stringBuilder
				.append("<description>identifiant pronom (PRONOM Unique ID )</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>15</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>TailleFichier</longCode>");
		stringBuilder.append("<shortCode>TFI</shortCode>");
		stringBuilder.append("<label>Taille du fichier</label>");
		stringBuilder
				.append("<description>taille du fichier en ko</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>LONG</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>-1</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>CodeOrganismeProprietaire</longCode>");
		stringBuilder.append("<shortCode>COP</shortCode>");
		stringBuilder.append("<label>Code organisme proprietaire</label>");
		stringBuilder
				.append("<description>code organisme propriétaire du document</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>5</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>CodeOrganismeGestionnaire</longCode>");
		stringBuilder.append("<shortCode>COG</shortCode>");
		stringBuilder.append("<label>Code organisme gestionnaire</label>");
		stringBuilder
				.append("<description>code organisme gestionnaire de la pièce </description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>String</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>5</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>Periode</longCode>");
		stringBuilder.append("<shortCode>PER</shortCode>");
		stringBuilder.append("<label>Periode</label>");
		stringBuilder
				.append("<description>Correspond à la période V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>4</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>Siren</longCode>");
		stringBuilder.append("<shortCode>SRN</shortCode>");
		stringBuilder.append("<label>Siren</label>");
		stringBuilder.append("<description>Numéro de SIREN V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NniEmployeur</longCode>");
		stringBuilder.append("<shortCode>NNE</shortCode>");
		stringBuilder.append("<label>NNI employeur</label>");
		stringBuilder
				.append("<description>Numéro National d'Identification ou Numéro de sécurité sociale de l'Employeur</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>15</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NumeroPersonne</longCode>");
		stringBuilder.append("<shortCode>NPE</shortCode>");
		stringBuilder.append("<label>Numero de personne</label>");
		stringBuilder
				.append("<description>Numero de personne (V2)</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>20</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>Denomination</longCode>");
		stringBuilder.append("<shortCode>DEN</shortCode>");
		stringBuilder.append("<label>Denomination du compte</label>");
		stringBuilder
				.append("<description>Dénomination du compte (provient de la raison sociale V2)</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>100</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>CodeCategorieV2</longCode>");
		stringBuilder.append("<shortCode>CV2</shortCode>");
		stringBuilder.append("<label>Code categorie V2</label>");
		stringBuilder.append("<description>Code categorie V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>1</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>CodeSousCategorieV2</longCode>");
		stringBuilder.append("<shortCode>SCV</shortCode>");
		stringBuilder.append("<label>Code sous categorie V2</label>");
		stringBuilder
				.append("<description>Code sous categorie V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>2</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NumeroCompteInterne</longCode>");
		stringBuilder.append("<shortCode>NCI</shortCode>");
		stringBuilder.append("<label>Numero de compte interne</label>");
		stringBuilder
				.append("<description>Numero de compte interne V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>18</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NumeroCompteExterne</longCode>");
		stringBuilder.append("<shortCode>NCE</shortCode>");
		stringBuilder.append("<label>Numero de compte interne</label>");
		stringBuilder
				.append("<description>Numero de compte externe V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>18</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>Siret</longCode>");
		stringBuilder.append("<shortCode>SRT</shortCode>");
		stringBuilder.append("<label>Numero de siret</label>");
		stringBuilder.append("<description>Numéro de Siret V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>14</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>PseudoSiret</longCode>");
		stringBuilder.append("<shortCode>PSI</shortCode>");
		stringBuilder.append("<label>Numero de pseudosiret</label>");
		stringBuilder
				.append("<description>Numéro de Pseudo Siret V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>10</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NumeroStructure</longCode>");
		stringBuilder.append("<shortCode>NST</shortCode>");
		stringBuilder.append("<label>Numero de structure</label>");
		stringBuilder
				.append("<description>Numéro de structure V2</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>20</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NumeroRecours</longCode>");
		stringBuilder.append("<shortCode>NRE </shortCode>");
		stringBuilder.append("<label>Numero de recours</label>");
		stringBuilder.append("<description>Numero de recours</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>20</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>NumeroIntControle</longCode>");
		stringBuilder.append("<shortCode>NIC</shortCode>");
		stringBuilder.append("<label>Numero interne contrôle</label>");
		stringBuilder
				.append("<description>Numero interne contrôle</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>20</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>Titre</longCode>");
		stringBuilder.append("<shortCode>_title</shortCode>");
		stringBuilder.append("<label>Titre du document (DFCE)</label>");
		stringBuilder
				.append("<description>Titre du document (DFCE)</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>100</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>type</longCode>");
		stringBuilder.append("<shortCode>_type</shortCode>");
		stringBuilder.append("<label>type (DFCE)</label>");
		stringBuilder.append("<description>type</description>");
		stringBuilder.append("<archivable>False</archivable>");
		stringBuilder.append("<consultable>False</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>20</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>versionNumber</longCode>");
		stringBuilder.append("<shortCode>_versionNb</shortCode>");
		stringBuilder.append("<label>versionNumber (DFCE)</label>");
		stringBuilder.append("<description>versionNumber</description>");
		stringBuilder.append("<archivable>False</archivable>");
		stringBuilder.append("<consultable>False</consultable>");
		stringBuilder.append("<searchable>False</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>INTEGER</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>20</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>referenceUUID</longCode>");
		stringBuilder.append("<shortCode>_referenceUUID</shortCode>");
		stringBuilder.append("<label>referenceUUID (DFCE)</label>");
		stringBuilder
				.append("<description>Pour un document virtuel, il s'agit de l'uuid du document container</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>False</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>30</length>");
		stringBuilder.append("<required>True</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("<metaDataReference>");
		stringBuilder.append("<longCode>IdTraitementMasse</longCode>");
		stringBuilder.append("<shortCode>ITM</shortCode>");
		stringBuilder.append("<label>IdTraitementMasse</label>");
		stringBuilder
				.append("<description>Identifiant permettant de gérer le rollback sur les traitements de masse</description>");
		stringBuilder.append("<archivable>True</archivable>");
		stringBuilder.append("<consultable>True</consultable>");
		stringBuilder.append("<searchable>True</searchable>");
		stringBuilder.append("<internal>True</internal>");
		stringBuilder.append("<type>STRING</type>");
		stringBuilder.append("<pattern></pattern>");
		stringBuilder.append("<length>20</length>");
		stringBuilder.append("<required>False</required>");
		stringBuilder.append("</metaDataReference>");
		stringBuilder.append("</referentiel>");
		return stringBuilder.toString();
	}

	/**
	 * Cette classe n'est pas faite pour être instantiée.
	 */
	private ReferentialDatas() {
		assert false;
	}
}
