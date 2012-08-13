
/**
 * VersionExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fr.urssaf.image.commons.webservice.axis.client.modele.version;

// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class VersionExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1344607617061L;
    
    private fr.urssaf.image.commons.webservice.axis.client.modele.version.VersionStub.VersionException faultMessage;

    
        public VersionExceptionException() {
            super("VersionExceptionException");
        }

        public VersionExceptionException(java.lang.String s) {
           super(s);
        }

        public VersionExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public VersionExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fr.urssaf.image.commons.webservice.axis.client.modele.version.VersionStub.VersionException msg){
       faultMessage = msg;
    }
    
    public fr.urssaf.image.commons.webservice.axis.client.modele.version.VersionStub.VersionException getFaultMessage(){
       return faultMessage;
    }
}
    