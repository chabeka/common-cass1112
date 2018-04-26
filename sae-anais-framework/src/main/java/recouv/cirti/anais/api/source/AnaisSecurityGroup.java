package recouv.cirti.anais.api.source;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class AnaisSecurityGroup
{
	protected static Log log = LogFactory.getLog(AnaisSecurityGroup.class);
	
	private AnaisLdap LdapObj;
	private String cn;
	private String displayname;
	private String ircode;
	private String orgcode;
	private String dn;
	private String modeldn;
	private String level;
	private List<String> anaisSecurityGroupListCodeOrg = new ArrayList<String>();
	private ArrayList<String> uniquemember;


	public AnaisSecurityGroup(AnaisLdap paramAnaisLdap) 
	{
		 this.LdapObj = paramAnaisLdap;
	}
	
	public AnaisSecurityGroup getSecurityGroupByDn (String dn) throws AnaisExceptionNoObject
	{
		if ( log.isDebugEnabled() )
			log.debug("AnaisSecurityGroup-->getSecurityGroupByDn: dn="+ dn);

		try 
		{
			Attributes attrs = LdapObj.getAnais_ldap().getAttributes(dn);
			
			this.setInfos(attrs);
			this.setDn(dn);
									
			return this;
		} 
		catch (NamingException e) 
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisSecurityGroup-->getSecurityGroupByDn - Erreur : impossible de recupérer les attributs du metier");
			throw new AnaisExceptionNoObject(e.toString());
		}
	}
	
	public ArrayList<AnaisUser>ListAllUsersIntoSecurityGroupInstance(String InstanceDn) throws AnaisExceptionNoObject, AnaisExceptionFailure
	{
		try 
		{
			Attributes attrs = LdapObj.getAnais_ldap().getAttributes(InstanceDn);
			
			AnaisSecurityGroup securitygroupobject = new AnaisSecurityGroup(this.LdapObj);
			
			securitygroupobject.setInfos(attrs);
			securitygroupobject.setDn(InstanceDn);
			
			ArrayList<AnaisUser> userslist = new ArrayList<AnaisUser>();
			
			if (securitygroupobject.getUniquemember()!= null)
			{
				for (int i =0; i< securitygroupobject.getUniquemember().size(); i++)
				{	
					String uniquemember = securitygroupobject.getUniquemember().get(i);
					AnaisUser user = new AnaisUser(this.LdapObj);
					user.GetUserInfoFromUserDN(uniquemember);
					userslist.add(user);
				}
			}
			return userslist;
		} 
		catch (NamingException e) 
		{
			throw new AnaisExceptionNoObject(e.toString());
		}
	}
	

	public void setInfos (Attributes attrs ) throws NamingException
	{
		if ( attrs.get("displayname") != null )
			this.setDisplayname( (String) attrs.get("displayname").get());
		if ( attrs.get("cn") != null )
			this.setCn( (String) attrs.get("cn").get());
		if ( attrs.get("anaisIRCode") != null )
			this.setIrcode( (String) attrs.get("anaisIRCode").get());
		if ( attrs.get("anaisOrgCode") != null )
			this.setOrgcode( (String) attrs.get("anaisOrgCode").get());
		if ( attrs.get("anaisSecurityGroupModelObject") != null )
			this.setModeldn( (String) attrs.get("anaisSecurityGroupModelObject").get());
		if ( attrs.get("anaisSecurityGroupListCodeOrg") != null ) {
			List<String> listCodeOrg = new ArrayList<String>();
			for ( int i=0; i< attrs.get("anaisSecurityGroupListCodeOrg").size(); i++ ) {
				String codeOrg = (String)attrs.get("anaisSecurityGroupListCodeOrg").get(i);
				listCodeOrg.add(codeOrg);
			}
			this.setAnaisSecurityGroupListCodeOrg(listCodeOrg);
		}
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

	public String getDisplayname() 
	{
		return displayname;
	}

	public void setDisplayname(String displayname) 
	{
		this.displayname = displayname;
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

	public String getDn() 
	{
		return dn;
	}

	public void setDn(String dn) 
	{
		this.dn = dn;
	}

	public String getModeldn() 
	{
		return modeldn;
	}

	public void setModeldn(String modeldn) 
	{
		this.modeldn = modeldn;
	}

	public String getLevel() 
	{
		return level;
	}

	public void setLevel(String level) 
	{
		this.level = level;
	}
	
	public ArrayList<String> getUniquemember() 
	{
		return uniquemember;
	}

	public void setUniquemember(ArrayList<String> uniquemember) 
	{
		this.uniquemember = uniquemember;
	}

	/**
	 * @return the anaisSecurityGroupListCodeOrg
	 */
	public List<String> getAnaisSecurityGroupListCodeOrg() {
		return anaisSecurityGroupListCodeOrg;
	}

	/**
	 * @param anaisSecurityGroupListCodeOrg the anaisSecurityGroupListCodeOrg to set
	 */
	public void setAnaisSecurityGroupListCodeOrg(
			List<String> anaisSecurityGroupListCodeOrg) {
		this.anaisSecurityGroupListCodeOrg = anaisSecurityGroupListCodeOrg;
	}

}