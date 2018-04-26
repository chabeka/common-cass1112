package fr.urssaf.image.commons.birt.exception;


/**
 * 
 * Si le serveur BIRT n'est pas démarré 
 *
 */
public class NoEngineBirtEngineException extends Exception {

   
   private static final long serialVersionUID = -2498087641703839399L;
   
   
   /**
    * Constructs a new exception with <code>null</code> as its detail message.
    * The cause is not initialized, and may subsequently be initialized by a
    * call to {@link initCause}.
    */
   public NoEngineBirtEngineException() {
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
   public NoEngineBirtEngineException(String message) {
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
   public NoEngineBirtEngineException(Throwable cause) {
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
   public NoEngineBirtEngineException(String message,Throwable cause) {
      super(message, cause);
   }
   
}
