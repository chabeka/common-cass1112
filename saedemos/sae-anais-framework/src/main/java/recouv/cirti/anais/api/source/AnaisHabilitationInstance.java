package recouv.cirti.anais.api.source;

import java.util.ArrayList;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AnaisHabilitationInstance
{
	protected static Log log = LogFactory.getLog(AnaisHabilitationInstance.class);
	
	private AnaisLdap LdapObj;
	private String cn;
	private String name;
	private String codeapp;
	private String codeenv;
	private String ircode;
	private String orgcode;
	private String type;
	private String dn;
	private ArrayList<String> uniquemember;
	

	public AnaisHabilitationInstance(AnaisLdap paramAnaisLdap) 
	{
		 this.LdapObj = paramAnaisLdap;
	}
	
	public AnaisHabilitationInstance getAnaisHabilitationInstanceInfosByInstanceDn ( String habilitationDn) throws AnaisExceptionNoObject
	{
		
		if ( log.isDebugEnabled() )
			log.debug("AnaisHabilitationInstance-->getAnaisHabilitationInstanceInfosByInstanceDn: habilitationDn="+ habilitationDn);

		try 
		{
			Attributes attrs;
			attrs = LdapObj.getAnais_ldap().getAttributes(habilitationDn);
			this.setInfos(attrs);
			this.setDn( habilitationDn);
			return this;
		} 
		catch (NamingException e) 
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisHabilitationInstance-->getAnaisHabilitationInstanceInfosByInstanceDn - Erreur : impossible de recupérer les attributs de l'habilitation");
			throw new AnaisExceptionNoObject(e.toString());
		}
	}
	
	public void setInfos (Attributes attrs ) throws NamingException
	{
		if ( attrs.get("name") != null )
			this.setName( (String) attrs.get("name").get());
		if ( attrs.get("cn") != null )
			this.setCn( (String) attrs.get("cn").get());
		if ( attrs.get("anaisIRCode") != null )
			this.setIrcode( (String) attrs.get("anaisIRCode").get());
		if ( attrs.get("anaisOrgCode") != null )
			this.setOrgcode( (String) attrs.get("anaisOrgCode").get());
		if ( attrs.get("anaisType") != null )
			this.setType( (String) attrs.get("anaisType").get());
		if ( attrs.get("anaisApplicationCode") != null )
			this.setCodeapp( (String) attrs.get("anaisApplicationCode").get());
		if ( attrs.get("anaisEnvCode") != null )
			this.setCodeenv( (String) attrs.get("anaisEnvCode").get());
		if( attrs.get("uniqueMember") != null )
		{
			ArrayList<String> memberlist = new ArrayList<String>();
			for ( int i=0; i< attrs.get("uniqueMember").size(); i++ )
			{
				String member = (String)attrs.get("uniqueMember").get(i);
				memberlist.add(member);
			}
			this.setUniquemember(memberlist);
		}
	}	
	
	public String getCn() 
	{
		return cn;
	}
	public void setCn(String cn) 
	{
		this.cn = cn;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getCodeapp() 
	{
		return codeapp;
	}

	public void setCodeapp(String codeapp) 
	{
		this.codeapp = codeapp;
	}
	public String getCodeenv() 
	{
		return codeenv;
	}
	public void setCodeenv(String codeenv) 
	{
		this.codeenv = codeenv;
	}
	public String getIrcode() 
	{
		return ircode;
	}
	public void setIrcode(String ircode) 
	{
		this.ircode = ircode;
	}
	public String getOrgcode() 
	{
		return orgcode;
	}
	public void setOrgcode(String orgcode) 
	{
		this.orgcode = orgcode;
	}
	public String getType() 
	{
		return type;
	}
	public void setType(String type) 
	{
		this.type = type;
	}
	public String getDn() 
	{
		return dn;
	}
	public void setDn(String dn) 
	{
		this.dn = dn;
	}
	public ArrayList<String> getUniquemember() 
	{
		return uniquemember;
	}

	public void setUniquemember(ArrayList<String> uniquemember) 
	{
		this.uniquemember = uniquemember;
	}
}