package fr.urssaf.image.commons.maquette.exception;

import java.io.IOException;


/**
 * 
 * Exception à lever lorsque la ressource n'est pas spécifiée dans la 
 * {@link HttpServletRequest} de la servlet {@link MaquetteServlet} 
 * (paramètre <code>name</code> manquant ou vide)
 *
 */
public class RessourceNonSpecifieeException extends IOException {

   
   private static final long serialVersionUID = 1672964962963694487L;


   /**
    * Constructs a new exception with <code>null</code> as its detail message.
    * The cause is not initialized, and may subsequently be initialized by a
    * call to {@link java.lang.Exception#initCause(Throwable)}.
    */
   public RessourceNonSpecifieeException() {
      super();
   }
   
   
   /**
    * Constructs a new exception with the specified detail message.  The
    * cause is not initialized, and may subsequently be initialized by
    * a call to {@link #initCause}.
    *
    * @param   message   the detail message. The detail message is saved for 
    *          later retrieval by the {@link #getMessage()} method.
    */
   public RessourceNonSpecifieeException(String message) {  
      super(message); 
   }
   
   
   /**
    * Constructs a new exception with the specified cause and a detail
    * message of <tt>(cause==null ? null : cause.toString())</tt> (which
    * typically contains the class and detail message of <tt>cause</tt>).
    * This constructor is useful for exceptions that are little more than
    * wrappers for other throwables (for example, {@link
    * java.security.PrivilegedActionException}).
    *
    * @param  cause the cause (which is saved for later retrieval by the
    *         {@link #getCause()} method).  (A <tt>null</tt> value is
    *         permitted, and indicates that the cause is nonexistent or
    *         unknown.)
    */
   public RessourceNonSpecifieeException(Throwable cause) {  
      super(cause); 
   } 
    

   
   /**
    * Constructs a new exception with the specified detail message and
    * cause.  <p>Note that the detail message associated with
    * <code>cause</code> is <i>not</i> automatically incorporated in
    * this exception's detail message.
    *
    * @param  message the detail message (which is saved for later retrieval
    *         by the {@link #getMessage()} method).
    * @param  cause the cause (which is saved for later retrieval by the
    *         {@link #getCause()} method).  (A <tt>null</tt> value is
    *         permitted, and indicates that the cause is nonexistent or
    *         unknown.)
    */
   public RessourceNonSpecifieeException(String message, Throwable cause) {  
      super(message, cause); 
   }
   

}
