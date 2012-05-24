//Date : 23 juillet 2010

package recouv.cirti.anais.api.source;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

public class AnaisUserWriter extends AnaisUser
{
	
	
	public AnaisUserWriter(AnaisLdap paramAnaisLdap) 
	{
		 super(paramAnaisLdap);
	}
	
	public void changeUserPassword(String newpasswd) {
		try { 	
			 
			  if ( newpasswd.isEmpty() ){
				  throw new AnaisExceptionAuthFailure();
				  
		 		}
			
			  if ( this.dn.isEmpty() ){
				  throw new AnaisExceptionAuthFailure();
			 	}
			  
		      Attributes attributes = new BasicAttributes(true); 
		      Attribute attribut = new BasicAttribute("userPassword"); 
		      attribut.add(newpasswd); 
		      attributes.put(attribut); 
		      //attributes = new BasicAttributes(false);
		    
      
		      LdapObj.getAnais_ldap().modifyAttributes( this.dn,    
		      DirContext.REPLACE_ATTRIBUTE,attributes); 
		      //LdapObj.getAnais_ldap().close(); 
		      
		    } catch (NamingException e) { 
			      e.printStackTrace(); 
		    } catch (AnaisExceptionAuthFailure e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    
	}
	
	    
   
}
