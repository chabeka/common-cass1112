package fr.urssaf.image.commons.birt.exception;

/**
 * 
 * Si l'objet {@link fr.urssaf.image.commons.birt.BirtEngine} n'a pas  
 * été créé au préalable à l'aide de la factory
 * {@link fr.urssaf.image.commons.birt.BirtEngineFactory}
 *
 */
public class NoInstanceBirtEngineException extends Exception {

   
   private static final long serialVersionUID = -5126953736178670443L;

   
   /**
    * Constructs a new exception with <code>null</code> as its detail message.
    * The cause is not initialized, and may subsequently be initialized by a
    * call to {@link initCause}.
    */
   public NoInstanceBirtEngineException() {
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
   public NoInstanceBirtEngineException(String message) {
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
   public NoInstanceBirtEngineException(Throwable cause) {
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
   public NoInstanceBirtEngineException(String message,Throwable cause) {
      super(message, cause);
   }
   
   
}