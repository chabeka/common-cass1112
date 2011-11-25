package fr.urssaf.image.sae.anais.framework.service.exception;

import java.util.Arrays;

import org.apache.commons.lang.SystemUtils;

import fr.urssaf.image.sae.vi.exception.VIException;

/**
 * Classe d'exception hérite de {@link RuntimeException}<br>
 * Le document XML n'est pas conforme au schéma XSD<br>
 * <br>
 * Cette exception peut être levée par l'appel de la méthode
 * <code>authentifierPourSaeParLoginPassword<code>
 * 
 * @see SaeAnaisService
 */
public class ValidationXmlParXsdException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private final String nomDuDocumentXml;

   private final String nomDuSchemaXsd;

   private final String[] erreurs;

   /**
    * initialisation des attributs
    * <ul>
    * <li><code>nomDuDocumentXml</code> : <code>VECTEUR IDENTIFICATION</code></li>
    * <li><code>nomDuSchemaXsd</code> : {@link VIException#getXsd()}</li>
    * <li><code>erreurs</code> : {@link VIException#getErrors()}</li>
    * </ul>
    * 
    * @param exception
    *           exception levée lors de l'écriture du VI
    */
   public ValidationXmlParXsdException(VIException exception) {
      super(exception);
      this.nomDuSchemaXsd = exception.getXsd();
      this.nomDuDocumentXml = "VECTEUR IDENTIFICATION";
      this.erreurs = exception.getErrors();

   }

   /**
    * initialisation du message :<br>
    * <br>
    * <code>Le document XML {0} n'est pas conforme au schéma XSD {1}.<br>
    * Les erreurs suivantes sont survenues :<br>
    * &nbsp;&nbsp;- {2}<br>
    * &nbsp;&nbsp;- {n}<br></code>
    * <br>
    * <ul>
    * <li><code>{0}</code> : la valeur de <code>nomDuDocumentXml</code></li>
    * <li><code>{1}</code> : la valeur de <code>nomDuSchemaXsd</code></li>
    * <li><code>{2}</code> : la 1ère <code>erreurs</code></li>
    * <li><code>{n}</code> : les autres <code>erreurs</code></li>
    * </ul>
    * 
    * 
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.InsufficientStringBufferDeclaration")
   public final String getMessage() {

      StringBuffer msg = new StringBuffer();
      msg.append("Le document XML " + this.nomDuDocumentXml
            + " n'est pas conforme au schéma XSD " + this.nomDuDocumentXml);
      msg.append(SystemUtils.LINE_SEPARATOR);
      for (String erreur : erreurs) {
         msg.append("\t- ");
         msg.append(erreur);
         msg.append(SystemUtils.LINE_SEPARATOR);
      }
      
      return msg.toString();

   }

   /**
    * @return Nom du fichier (sans le path) ou nom logique du document XML dont
    *         la validation à échouer
    */
   public final String getNomDuDocumentXml() {
      return nomDuDocumentXml;
   }

   /**
    * @return Nom du fichier (sans le path) du schéma XSD utilisé par la
    *         validation
    */
   public final String getNomDuSchemaXsd() {
      return nomDuSchemaXsd;
   }

   /**
    * @return Liste des erreurs survenues pendant la validation
    */
   public final String[] getErreurs() {
      return Arrays.copyOf(erreurs, erreurs.length);
   }

}
