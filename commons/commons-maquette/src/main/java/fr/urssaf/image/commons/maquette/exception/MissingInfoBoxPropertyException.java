package fr.urssaf.image.commons.maquette.exception;

/**
 * S'il manque une propriété requise à une InfoBox
 * 
 */
public class MissingInfoBoxPropertyException extends Exception {


   private static final long serialVersionUID = -2652249357264566265L;


   /**
    * Propriété manquante
    */
   private String missingProperty;


   /**
    * Constructs a new exception with <code>null</code> as its detail message.
    * The cause is not initialized, and may subsequently be initialized by a
    * call to {@link java.lang.Exception#initCause(Throwable)}.
    */
   public MissingInfoBoxPropertyException() {
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
   public MissingInfoBoxPropertyException(String message) {  
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
   public MissingInfoBoxPropertyException(Throwable cause) {  
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
   public MissingInfoBoxPropertyException(String message, Throwable cause) {  
      super(message, cause); 
   }
   
   
   /**
    * Constructs a new exception with the specified detail message and
    * cause.  <p>Note that the detail message associated with
    * <code>cause</code> is <i>not</i> automatically incorporated in
    * this exception's detail message.
    *
    * @param  message the detail message (which is saved for later retrieval
    *         by the {@link #getMessage()} method).
    * @param  missingProperty le nom de la propriété manquante
    * 
    */
   public MissingInfoBoxPropertyException(String message, String missingProperty) {  
      super(message);
      this.missingProperty = missingProperty;
   }


   /**
    * Renvoie la propriété manquante
    * @return la propriété manquante
    */
   public final String getMissingProperty() {
      return missingProperty;
   }
   
   
   
   
}
