package fr.urssaf.image.commons.maquette.tool;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


/**
 * 
 * Classe permettant de récupérer le contenu d'une HttpServletResponse après
 * l'appel des filtres, et de modifier le contenu de la réponse.<br>
 * <br>
 * Parfaitement adaptée pour le pattern "Décorateur".<br>
 * <br>
 * Fortement inspiré de http://java.sun.com/products/servlet/Filters.html<br>
 * <br>
 * Utilisé dans le filtre de décoration {@link fr.urssaf.image.commons.maquette.MaquetteFilter}<br>
 * <br>
 * Le principe d'utilisation est le suivant, au sein de la méthode doFilter
 * d'un filtre ({@link javax.servlet.Filter}) décorateur  :
 * <pre>
 *   
 *   // Création du Wrapper
 *   CharResponseWrapper wrapper = new CharResponseWrapper(response);
 *   
 *   // On se réserve un PrintWriter sur la réponse
 *   PrintWriter printWriter = response.getWriter();
 *   
 *   // Appel des filtres suivants
 *   chain.doFilter(request, wrapper);
 *   
 *   // On peut décorer le résultat
 *   String resultat = wrapper.toString() ;
 *   // => décorer resultat
 *   
 * </pre>
 */
public final class CharResponseWrapper extends HttpServletResponseWrapper 
{
	
   /**
    * Status initial de la réponse HTTP<br>
    * <br>
    * Le statut est mis à jour uniquement en cas d'erreur<br>
    * <br>
    * Pour tester si le traitement des filtres s'est bien passé,
    * il faut regarder si : <br>
    * &nbsp;&nbsp;&nbsp;<code>monWrapper.getStatus()==CharResponseWrapper.DEFAULT_STATUS</code>
    */
   public static final int DEFAULT_STATUS = 0;
   
   
	private final CharArrayWriter output;
	
	
	private int status = DEFAULT_STATUS;
	

	/**
	 * Renvoie le contenu de la réponse
	 * 
	 * @return le contenu de la réponse
	 */
	@Override
	public String toString() {
		return output.toString();
	}

	
	/**
	 * Constructeur
	 * @param response l'objet HttpServletResponse dont on veut se réserver 
	 * la possibilité de la modifier après le traitement des filtres
	 */
	public CharResponseWrapper(HttpServletResponse response) {
		super(response);
		output = new CharArrayWriter();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrintWriter getWriter() {
		return new PrintWriter(output);
	}

	
	/**
    * {@inheritDoc}
    */
	@Override
	public void setStatus(int status){
      super.setStatus(status);
      this.status = status;
   }
	
	
	/**
    * {@inheritDoc}
    */
	@Override
	public void setStatus(int status, String msg){
      super.setStatus(status, msg);
      this.status = status;
   }
	
	
	/**
    * {@inheritDoc}
    */
	@Override
	public void sendError(int errorStatusCode) throws IOException {
      super.sendError(errorStatusCode);
      status = errorStatusCode;
   }
	
	
	/**
    * {@inheritDoc}
    */
	@Override
   public void sendError(int errorStatusCode, String msg) throws IOException {
      super.sendError(errorStatusCode, msg);
      status = errorStatusCode;
   }
	
	
	/**
	 * Renvoie le statut de la réponse
	 * @return le statut de la réponse
	 * 
	 * @see #DEFAULT_STATUS
	 */
   public int getStatus(){
      return status;
   }
	
}
