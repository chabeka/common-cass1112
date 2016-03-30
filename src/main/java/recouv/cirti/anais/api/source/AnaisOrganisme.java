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

public class AnaisOrganisme  
{
	protected static Log log = LogFactory.getLog(AnaisOrganisme.class);
	
	private AnaisLdap LdapObj;
	private String anaisOrgCodeCodique;
	private String anaisOrgCodeSnv2;
	private String anaisOrgCodeUcanss;
	private String anaisOrgType;
	private String cn;
	private String displayName;
	private String dn;
	private String ircode;



	public AnaisOrganisme(AnaisLdap paramAnaisLdap)
	{
		this.LdapObj = paramAnaisLdap;
	}

	
	public AnaisOrganisme getOrganismeByOrgCode ( String orgcode, String branche )
	{
		if ( branche.trim().equals("") )branche = "Recouvrement";
		
		SearchControls scopeSec = new SearchControls();
		
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		String baseDN = "ou=Organismes,ou="+branche+",dc=recouv";
		String filter = "(&(objectClass=anaispuborg)(cn="+orgcode+"))";
		
		AnaisOrganisme Organisme = new AnaisOrganisme(this.LdapObj);
		
		try 
		{
			NamingEnumeration<?> _organisme = LdapObj.getAnais_ldap().search(baseDN, filter, scopeSec);
			
			while (_organisme != null && _organisme.hasMore()) 
			{
				SearchResult org = (SearchResult) _organisme.next();
				String DN = org.getName() + "," + baseDN;
				
				Attributes attrs = org.getAttributes();
				
				Organisme.setInfos(attrs);
				Organisme.setDn( DN );
				
				String[] tokens = Organisme.getDn().split("," );
				String[] tokens2 = tokens[1].split("=" );
				String ircode = tokens2[1];
				
				Organisme.setIrcode(ircode);
			}
			
		} 
		catch (NamingException e) 
		{
			e.printStackTrace();
		}
		return Organisme;
	}
	
	public List<AnaisOrganisme> ListAllOrganismes() throws AnaisExceptionNoObject
	{
	return ListAllOrganismesInto("");
	}
	
	public List<AnaisOrganisme> ListAllOrganismesInto( String branche) throws AnaisExceptionNoObject
	{
		//par default branche recouvrement
		if ( branche.trim().equals("") )branche = "Recouvrement";
		
		SearchControls scopeSec = new SearchControls();
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String baseDN = "ou=Organismes,ou="+branche+",dc=recouv";
		String filter = "(&(objectClass=anaispuborg)(cn=*))";
		
		try 
		{
			List<AnaisOrganisme> ListOrganismes = new ArrayList<AnaisOrganisme>();
			
			NamingEnumeration<?> _organisme = LdapObj.getAnais_ldap().search(baseDN, filter, scopeSec);
			
			while (_organisme != null && _organisme.hasMore()) 
			{
				SearchResult org = (SearchResult) _organisme.next();
				String DN = org.getName() + "," + baseDN;

				Attributes attrs = org.getAttributes();
				
				AnaisOrganisme Organisme = new AnaisOrganisme(this.LdapObj);
				Organisme.setInfos(attrs);
				Organisme.setDn( DN );
				
				String[] tokens = Organisme.getDn().split("," );
				String[] tokens2 = tokens[1].split("=" );
				String ircode = tokens2[1];
				
				Organisme.setIrcode(ircode);
				ListOrganismes.add(Organisme);
				Collections.sort(ListOrganismes, Organisme.IRCODE_COMPARATOR);
			}
			
			return ListOrganismes;
		} 
		catch (NamingException e) 
		{
			throw new AnaisExceptionNoObject(e.toString());
		}
	}
	
	public List<AnaisOrganisme> ListAllOrganismesByInterRegion( String ircode ) throws AnaisExceptionNoObject
	{
	return 	ListAllOrganismesByInterRegionInto(ircode,"");
	}

	public List<AnaisOrganisme> ListAllOrganismesByInterRegionInto( String ircode, String branche ) throws AnaisExceptionNoObject
	{
		if ( branche.trim().equals("") )branche = "Recouvrement";
		
		SearchControls scopeSec = new SearchControls();
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String baseDN = "ou="+ircode+",ou=Organismes,ou="+branche+",dc=recouv";
		String filter = "(&(objectClass=anaispuborg)(cn=*))";
		
		try 
		{
			List<AnaisOrganisme> ListOrganismes = new ArrayList<AnaisOrganisme>();
		
			NamingEnumeration<?> _organisme = LdapObj.getAnais_ldap().search(baseDN, filter, scopeSec);
			
			while (_organisme != null && _organisme.hasMore()) 
			{
				SearchResult org = (SearchResult) _organisme.next();
				String DN = org.getName() + "," + baseDN;

				Attributes attrs =  org.getAttributes();
				
				AnaisOrganisme Organisme = new AnaisOrganisme(this.LdapObj);
				Organisme.setInfos(attrs);
				Organisme.setDn( DN );
				
				Organisme.setIrcode(ircode);
				ListOrganismes.add(Organisme);
				Collections.sort(ListOrganismes, Organisme.ORGCODE_COMPARATOR);
			}
			
			return ListOrganismes;
		} 
		catch (NamingException e) 
		{
			throw new AnaisExceptionNoObject(e.toString());
		}
	}	   
	public void setInfos (Attributes attrs ) throws NamingException
	{
		if ( attrs.get("anaisOrgCodeCodique") != null )
			this.setAnaisOrgCodeCodique( (String) attrs.get("anaisOrgCodeCodique").get());
		if ( attrs.get("anaisOrgCodeSnv2") != null )
			this.setAnaisOrgCodeSnv2( (String) attrs.get("anaisOrgCodeSnv2").get());
		if ( attrs.get("anaisOrgCodeUcanss") != null )
			this.setAnaisOrgCodeUcanss( (String) attrs.get("anaisOrgCodeUcanss").get());
		if ( attrs.get("anaisOrgType") != null )
			this.setAnaisOrgType( (String) attrs.get("anaisOrgType").get());
		if ( attrs.get("cn") != null )
			this.setCn( (String) attrs.get("cn").get());
		if ( attrs.get("displayName") != null )
			this.setDisplayName( (String) attrs.get("displayName").get());
	}

	public String getDn() 
	{
		return dn;
	}


	public void setDn(String dn) 
	{
		this.dn = dn;
	}

	public String getAnaisOrgCodeCodique() 
	{
		return anaisOrgCodeCodique;
	}
	
	public void setAnaisOrgCodeCodique(String anaisOrgCodeCodique) 
	{
		this.anaisOrgCodeCodique = anaisOrgCodeCodique;
	}


	public String getAnaisOrgCodeSnv2() 
	{
		return anaisOrgCodeSnv2;
	}


	public void setAnaisOrgCodeSnv2(String anaisOrgCodeSnv2) 
	{
		this.anaisOrgCodeSnv2 = anaisOrgCodeSnv2;
	}


	public String getAnaisOrgCodeUcanss() 
	{
		return anaisOrgCodeUcanss;
	}


	public void setAnaisOrgCodeUcanss(String anaisOrgCodeUcanss) 
	{
		this.anaisOrgCodeUcanss = anaisOrgCodeUcanss;
	}


	public String getAnaisOrgType() 
	{
		return anaisOrgType;
	}


	public void setAnaisOrgType(String anaisOrgType) 
	{
		this.anaisOrgType = anaisOrgType;
	}


	public String getCn() 
	{
		return cn;
	}


	public void setCn(String cn) 
	{
		this.cn = cn;
	}


	public String getDisplayName() 
	{
		return displayName;
	}


	public void setDisplayName(String displayName) 
	{
		this.displayName = displayName;
	}

	public String getIrcode() 
	{
		return ircode;
	}


	public void setIrcode(String ircode)
	{
		this.ircode = ircode;
	}

	public final Comparator<AnaisOrganisme> IRCODE_COMPARATOR = new Comparator<AnaisOrganisme>() 
	{
		
		public int compare(AnaisOrganisme arg0, AnaisOrganisme arg1) 
		{
	
			AnaisOrganisme p=(AnaisOrganisme) arg0;
			AnaisOrganisme q=(AnaisOrganisme) arg1;
			if(p.getIrcode() == q.getIrcode())
			{    
				return p.ircode.compareToIgnoreCase(q.ircode);
			 }
			else
			{
				return p.ircode.compareToIgnoreCase(q.ircode);
			}				
		}
 
    };
 
	public final Comparator<AnaisOrganisme> ORGCODE_COMPARATOR = new Comparator<AnaisOrganisme>() 
	{
		
		public int compare(AnaisOrganisme arg0, AnaisOrganisme arg1) 
		{
			AnaisOrganisme p=(AnaisOrganisme) arg0;
			AnaisOrganisme q=(AnaisOrganisme) arg1;
			if(p.getCn() == q.getCn())
			{    
				return p.cn.compareToIgnoreCase(q.cn);
			 }
			else
			{
				return p.cn.compareToIgnoreCase(q.cn);
			}				
		}
 
    };
 
}
