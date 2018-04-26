package recouv.cirti.anais.api.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AnaisInterRegion
{
	protected static Log log = LogFactory.getLog(AnaisInterRegion.class);
	
	private AnaisLdap LdapObj;
	private String dn;
	private String ou;
	
	public AnaisInterRegion(AnaisLdap paramAnaisLdap)
	{
		this.LdapObj = paramAnaisLdap;
	}
	
	public AnaisInterRegion getInterRegionByIrCode ( String ircode)
	{
	return getInterRegionByIrCodeInto(ircode,"");	
	}
	
	public AnaisInterRegion getInterRegionByIrCodeInto( String ircode, String branche)
	{
		if ( branche.trim().equals("") )branche = "Recouvrement";
		
		SearchControls scopeSec = new SearchControls();
		
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		String baseDN = "ou=Organismes,ou="+branche+",dc=recouv";
		String filter = "(&(objectClass=organizationalunit)(ou="+ircode+"))";
		AnaisInterRegion InterRegion = new AnaisInterRegion(this.LdapObj);
		try 
		{
			NamingEnumeration<?> _organisme = LdapObj.getAnais_ldap().search(baseDN, filter, scopeSec);
			
			while (_organisme != null && _organisme.hasMore()) 
			{
				SearchResult org = (SearchResult) _organisme.next();
				String DN = org.getName() + "," + baseDN;

				Attributes attrs = org.getAttributes();
				
				InterRegion.setInfos(attrs);
				InterRegion.setDn( DN );
			}
			
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
		}
		return InterRegion;
	}
	
	public List<AnaisInterRegion> ListAllInterRegions() throws AnaisExceptionNoObject
	{
	return ListAllInterRegionsInto("");
	}
	
	public List<AnaisInterRegion> ListAllInterRegionsInto( String branche ) throws AnaisExceptionNoObject
	{
		if ( branche.trim().equals("") )branche = "Recouvrement";
		
		SearchControls scopeSec = new SearchControls();
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		scopeSec.setReturningAttributes(new String[] {"ou","description"});
		
		String baseDN = "ou=Organismes,ou="+branche+",dc=recouv";
		String filter = "(&(objectClass=organizationalunit)(ou=*))";
		
		try 
		{
			List<AnaisInterRegion> ListInterRegions = new ArrayList<AnaisInterRegion>();
			
			NamingEnumeration<?> _interregion = LdapObj.getAnais_ldap().search(baseDN, filter, scopeSec);
			
			while (_interregion != null && _interregion.hasMore()) 
			{
				SearchResult ircode = (SearchResult) _interregion.next();
				
				
				if( ((String) ircode.getAttributes().get("ou").get(0)).equalsIgnoreCase("Organismes") )
				{
					
				}
				else
				{
					String DN = ircode.getName() + "," + baseDN;
					
					Attributes attrs = ircode.getAttributes();
					
					AnaisInterRegion InterRegion = new AnaisInterRegion(this.LdapObj);
					InterRegion.setInfos(attrs);
					InterRegion.setDn( DN );
					
					ListInterRegions.add(InterRegion);
					Collections.sort(ListInterRegions, InterRegion.IRCODE_COMPARATOR);
				}

			}
			
			return ListInterRegions;
		} 
		catch (NamingException e) 
		{
			throw new AnaisExceptionNoObject(e.toString());
		}
	}
	
	public void setInfos(Attributes attrs ) throws NamingException
	{
		if ( attrs.get("ou") != null )
			this.setOu( (String) attrs.get("ou").get());		
	}
	
	public String getDn() 
	{
		return dn;
	}

	public void setDn(String dn) 
	{
		this.dn = dn;
	}

	public String getOu() 
	{
		return ou;
	}

	public void setOu(String ou) 
	{
		this.ou = ou;
	}

	public final Comparator<AnaisInterRegion> IRCODE_COMPARATOR = new Comparator<AnaisInterRegion>() 
	{
		
		public int compare(AnaisInterRegion arg0, AnaisInterRegion arg1) 
		{
	
			AnaisInterRegion p=(AnaisInterRegion) arg0;
			AnaisInterRegion q=(AnaisInterRegion) arg1;
			if(p.getOu() == q.getOu())
			{    
				return p.ou.compareToIgnoreCase(q.ou);
			 }
			else
			{
				return p.ou.compareToIgnoreCase(q.ou);
			}				
		}
 
    };
}