//Date : 23 juillet 2010

package recouv.cirti.anais.api.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AnaisUser 
{
	protected static Log log = LogFactory.getLog(AnaisUser.class);
	
	public AnaisLdap LdapObj;
	public String uid;
	public String cn;
	public String sn;
	public String mail;
	public String telephonenumber;
	public String anaisdateentreerh;
	public String anaisdatesortierh;
	public String anaisperscodeemploiucanss;
	public String anaispersdonatirmail;
	public String anaisperslibelleemploi;
	public String anaisperssercode;
	public String anaisperstypecontrat;
	public String anaisperstelinterne;
	public String anaisperstelrecouv;
	public String anaispersidsnv2;
	public String anaisoriginesource;
	public String facsimiletelephonenumber;
	public String givenname;
	public String dn;
	public String orgcode;
	public String ircode;
	public String irdn;
	public String ou;
	public String l;
	public String title;
	public String businesscategory;
	public String manager;
	public String secretary;
	public String departmentnumber;
	public String donatirmail;
	public String mobile;
	public String nsaccountlock;
	public String activcardsn;
	public String activcardframedaddress;
	public String activcardgroupname;
	public String anaisPersNomVille;
	public String anaisPersCodeDept;


	public AnaisUser(AnaisLdap paramAnaisLdap) 
	{
		 this.LdapObj = paramAnaisLdap;
	}

	public String GetDNFromLogin ( String userLogin ) throws AnaisExceptionNoObject, AnaisExceptionAuthMultiUid
	{
		SearchControls scopeSec = new SearchControls();
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String baseDN = "ou=recouvrement,dc=recouv";
		String filter = new String("(&(objectClass=anaisUser)(uid="+userLogin+"))");
		int count = 0;
		String DN = null;
		
		if ( log.isDebugEnabled() )
			log.debug("AnaisUser-->GetDNFromLogin: userLogin="+ userLogin);	
		
		try 
		{
			
			NamingEnumeration<?> users = LdapObj.getAnais_ldap().search(baseDN, filter,	scopeSec);
			
			while (users != null && users.hasMore()) 
			{
				count++;
				SearchResult user = (SearchResult) users.next();

				DN = new String(user.getName() + "," + baseDN);
			}		
			
			if (count > 1) 
			{
				throw new AnaisExceptionAuthMultiUid();
			}
			
			if (count == 0) 
			{
				throw new AnaisExceptionNoObject("Aucun utilisateur dont l'attribut uid="+userLogin+"est trouvé");
			}			
			return DN;
		} 
		catch (NamingException e) 
		{
			if ( log.isErrorEnabled() ) {
				log.error("AnaisUser-->GetDNFromLogin - Erreur1 : impossible de recuperer le dn de l'utilisateur");
				log.error("AnaisUser-->GetDNFromLogin - Erreur2 : userLogin="+userLogin);
				}
				
			throw new AnaisExceptionNoObject(e.toString());
		}	
	}
	
	public AnaisUser GetUserInfoFromUserDN(String userdn)throws AnaisExceptionNoObject, AnaisExceptionFailure 
	{
	
		if ( log.isDebugEnabled() )
			log.debug("AnaisUser-->GetUserInfoFromUserDN: userdn="+ userdn);

		try 
		{
		
		    Attributes attrs = LdapObj.getAnais_ldap().getAttributes(userdn);
						
			this.setInfos(attrs);
			this.setDn( userdn);
			int i = userdn.toLowerCase().indexOf("personnes");
			
			if (i == -1)
				throw new AnaisExceptionFailure();
				
			
			int j = userdn.indexOf(",");
			String codeorg = userdn.substring(j + 4, i - 4);
			
			int k = userdn.indexOf(",", i + 9 + 1);
			String codeir = userdn.substring(i + 9 + 4, k);

			this.setOrgcode(codeorg);
			this.setIrcode(codeir);
			
			return this;
		} 
		catch (NamingException e) 
		{
			if ( log.isErrorEnabled() ) {
				log.error("AnaisUser-->GetUserInfoFromUserDN - Erreur1 : impossible de recuperer les attributs de l'utilisateur");
				log.error("AnaisUser-->GetUserInfoFromUserDN - Erreur2 : userdn="+ userdn);
			}
				
			throw new AnaisExceptionNoObject(e.toString());
		}

	}
	
	public AnaisUser GetUserInfoFromUserLogin(String userLogin)throws AnaisExceptionNoObject, AnaisExceptionAuthMultiUid, AnaisExceptionFailure 
	{
		SearchControls scopeSec = new SearchControls();
		
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		//scopeSec.setReturningAttributes(new String[] {"cn"});
						
		String baseDN = "ou=recouvrement,dc=recouv";
		String filter = new String("(&(objectClass=anaisUser)(uid=" + userLogin+"))");
		int count = 0;
		String DN = null;
		
		if ( log.isDebugEnabled() )
			log.debug("AnaisUser-->GetUserInfoFromUserLogin: userLogin="+ userLogin);

		try 
		{
		
			NamingEnumeration<?> users = LdapObj.getAnais_ldap().search(baseDN, filter,	scopeSec);
			
			SearchResult user = null;
			while (users != null && users.hasMore()) 
			{
				count++;
				user = (SearchResult) users.next();

				DN = new String(user.getName() + "," + baseDN);
			}		
			
			if (count > 1) 
			{
				throw new AnaisExceptionAuthMultiUid();
			}
			
			if (count == 0) 
			{
				throw new AnaisExceptionNoObject("Aucun utilisateur dont l'attribut uid="+userLogin+"est trouvé :1");
			}
			
			if (user == null) 
			{
				throw new AnaisExceptionNoObject("Aucun utilisateur dont l'attribut uid="+userLogin+"est trouvé :2");
			}

	     	Attributes attrs = user.getAttributes();
			
			//AnaisUser user = new AnaisUser();
			this.setInfos(attrs);
			this.setDn( DN );
			int i = DN.toLowerCase().indexOf("personnes");
			
			if (i == -1)
				throw new AnaisExceptionFailure();
				
			
			int j = DN.indexOf(",");
			String codeorg = DN.substring(j + 4, i - 4);
			
			int k = DN.indexOf(",", i + 9 + 1);
			String codeir = DN.substring(i + 9 + 4, k);

			this.setOrgcode(codeorg);
			this.setIrcode(codeir);			
			return this;
		} 
		catch (NamingException e) 
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisUser-->GetUserInfoFromUserLogin - Erreur : impossible de recupérer les attributs de l'utilisateur");
			throw new AnaisExceptionNoObject(e.toString());
		}		
	}
	
	public AnaisUser checkUserCredential(String login, String passwd)
	throws AnaisExceptionServerCommunication, AnaisExceptionAuthFailure,
	AnaisExceptionAuthAccountLocked, AnaisExceptionAuthMultiUid,
	AnaisExceptionFailure, AnaisExceptionNoObject

	{
		if ( log.isDebugEnabled() )
			log.debug("AnaisUser-->checkUserCredential: login(uid)="+ login);
		
		if (login==null) {
			AnaisExceptionAuthFailure e2 = new AnaisExceptionAuthFailure();
			throw e2;	
			}
		
		if (login.length()==0) {
			AnaisExceptionAuthFailure e2 = new AnaisExceptionAuthFailure();
			throw e2;
			}
		
		String userdn = null;
		try 
		{
			userdn = this.GetDNFromLogin(login);
		} 
		catch (AnaisExceptionAuthMultiUid e) 
		{
			throw e;
		} 
		catch (AnaisExceptionNoObject e) 
		{
			AnaisExceptionAuthFailure e2 = new AnaisExceptionAuthFailure();
			throw e2;
		}
	

		try 
		{
			
			if (passwd==null) {
				AnaisExceptionAuthFailure e2 = new AnaisExceptionAuthFailure();
				throw e2;
				}
			
			if (userdn==null) {
				AnaisExceptionAuthFailure e2 = new AnaisExceptionAuthFailure();
				throw e2;
				}
			
			if (passwd.length()==0) {
				AnaisExceptionAuthFailure e2 = new AnaisExceptionAuthFailure();
				throw e2;
				}
			
			if (userdn.length()==0) {
				AnaisExceptionAuthFailure e2 = new AnaisExceptionAuthFailure();
				throw e2;
				}
			
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, LdapObj.getUrl());
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, userdn);
			env.put(Context.SECURITY_CREDENTIALS, passwd);
				
			// authentification
			InitialLdapContext anais_ldap_tmp = new InitialLdapContext(env, null);
						
			//anais_ldap_tmp.reconnect(null);
			anais_ldap_tmp.close();
			anais_ldap_tmp=null;
		
		} 
		catch (CommunicationException e) 
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisConnection_Application-->CheckUserCredential :: ERREUR :: Connexion impossible a l'url : "
						+ LdapObj.getUrl());
			AnaisExceptionServerCommunication servDownEx = new AnaisExceptionServerCommunication(e.toString());
			throw servDownEx;
		} 
		catch (AuthenticationException e) 
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisConnection_Application-->CheckUserCredential :: ERREUR :: Authentification Erronée : "
						+ LdapObj.getUrl());
			AnaisExceptionAuthFailure authEx = new AnaisExceptionAuthFailure(e.toString());
		
			throw authEx;
		} 
		catch (AuthenticationNotSupportedException e) 
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisConnection_Application-->CheckUserCredential :: ERREUR :: Mot de passe non inialise : "
						+ LdapObj.getUrl());
			AnaisExceptionAuthFailure authEx = new AnaisExceptionAuthFailure(e.toString());
			
			throw authEx;
		} 
		catch (NamingException e) 
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisConnection_Application-->CheckUserCredential:: ERREUR :: Connexion impossible a l'url : "
						+ LdapObj.getUrl());
			AnaisExceptionFailure anaisEx = new AnaisExceptionFailure(e.toString());
			throw anaisEx;
		}
	
		// recuperation de l'IR et l'org de l'utilisateur
		int i = userdn.toLowerCase().indexOf("personnes");
		
		if (i == -1)
			throw new AnaisExceptionFailure();
			
		
		int j = userdn.indexOf(",");
		String codeorg = userdn.substring(j + 4, i - 4);
		
		int k = userdn.indexOf(",", i + 9 + 1);
		String codeir = userdn.substring(i + 9 + 4, k);
		
		AnaisUser user = this.GetUserInfoFromUserDN(userdn);

		user.setOrgcode(codeorg);
		user.setIrcode(codeir);

		return user;
	}
	
	public List<AnaisUser> SearchUsersWithNoDateSortieRh 
	( String firstname, String lastname, String login,  String codeir, String codeorg ) throws AnaisExceptionNoObject
	{
		
		String basedn = "ou=Recouvrement,dc=recouv";

		if ( firstname.trim().equals("") )firstname = "*";
		if ( lastname.trim().equals("") )lastname = "*";
		if ( login.trim().equals("") )login = "*";

		String filter = "(&(objectClass=anaisUser)(uid="+login+")(sn="+lastname+")(givenname="+firstname+"))";
		

		if(!codeorg.trim().equals(""))
		{
			try
			{
				basedn = this.GetOrgPeopleDNFromOrgCode(codeorg, "recouvrement");
			}
			catch(NamingException e)
			{
				throw new AnaisExceptionNoObject(e.toString());
			}
		}
		else if(!codeir.trim().equals(""))
		{
			AnaisInterRegion InterRegion = new AnaisInterRegion(this.LdapObj);
			InterRegion = InterRegion.getInterRegionByIrCode(codeir);
			basedn = InterRegion.getDn();
		}

		SearchControls scopeSec = new SearchControls();
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);

		
		
		try 
		{
			List<AnaisUser> userlist = new ArrayList<AnaisUser>();
		
			NamingEnumeration<?> users = LdapObj.getAnais_ldap().search(basedn, filter, scopeSec);
			
			while (users != null && users.hasMore()) 
			{
				
				SearchResult user = (SearchResult) users.next();
				String DN = user.getName() + "," + basedn;
				
				Attributes attrs = user.getAttributes();
				
				AnaisUser userobject = new AnaisUser(this.LdapObj);
				userobject.setInfos(attrs);
				userobject.setDn( DN );
				
				userlist.add(userobject);
				Collections.sort(userlist, userobject.SN_COMPARATOR);
				
			}	
			return  userlist;
		} 
		catch (NamingException e)
		{
			throw new AnaisExceptionNoObject(e.toString());
		}
	}
	
	public ArrayList<AnaisSecurityGroup> ListAllSecurityGroupsByUserDn ( String userdn ) throws AnaisExceptionNoObject
	{
		SearchControls scopeSec = new SearchControls();
		
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		scopeSec.setReturningAttributes(new String[] {"cn","anaisIRCode","anaisOrgCode","anaisSecurityGroupAdminGroups","anaisSecurityGroupModelObject","anaisType","anaisSecurityGroupListCodeOrg"});
		
		String baseDN = "ou=recouvrement,dc=recouv";
		String filter=new String("(&(objectClass=anaisSecurityGroup)(anaisType=2)(uniqueMember=" + userdn + "))" );
		
		try 
		{
			ArrayList<AnaisSecurityGroup> securitygrouplist = new ArrayList<AnaisSecurityGroup>();
			
			NamingEnumeration<?> securityGroups = LdapObj.getAnais_ldap().search(baseDN, filter, scopeSec);
			
			while (securityGroups != null && securityGroups.hasMore()) 
			{
				
				SearchResult securitygroup = (SearchResult) securityGroups.next();
				String DN = securitygroup.getName() + "," + baseDN;
				
				Attributes attrs = securitygroup.getAttributes();
				
				AnaisSecurityGroup secgroup = new AnaisSecurityGroup(this.LdapObj);
				secgroup.setInfos(attrs);
				secgroup.setDn( DN );
				
				
				securitygrouplist.add(secgroup);
				
			}	
			return  securitygrouplist;
		} 
		catch (NamingException e)
		{
			e.printStackTrace();
			throw new AnaisExceptionNoObject(e.toString());
		}
	}
	
	public List<Object> getListUsersByHabilitationAndApplication ( String codeorg, String codehab) throws AnaisExceptionFailure
	{
		String filter;
		String basedn = "ou=Recouvrement,dc=recouv";
		
		SearchControls scopeSec = new SearchControls();
		
		scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
		scopeSec.setReturningAttributes(new String[] {"cn","anaisApplicationCode","anaisEnvCode","anaisHabilitationModelObject","anaisIRCode","anaisOrgCode","anaisType","uniqueMember"});
				
		List<Object> allusers = new ArrayList<Object>();
		
		if ( !codeorg.trim().equals(""))
		{
			if( !codehab.trim().equals(""))
			{
				filter = "(&(objectClass=anaisHabilitation) (anaisOrgCode="+codeorg+")" +
						"(cn="+codehab+")(anaisEnvCode="+LdapObj.m_codeenv+")" +
						"(anaisApplicationCode="+LdapObj.m_codeapp+"))";
			}
			else
			{
				filter = "(&(objectClass=anaisHabilitation) (anaisOrgCode="+codeorg+")" +
						"(anaisEnvCode="+LdapObj.m_codeenv+")" +
						"(anaisApplicationCode="+LdapObj.m_codeapp+"))";
			}
		}
		else
		{
			if( !codehab.trim().equals(""))
			{
				filter = "(&(objectClass=anaisHabilitation)(cn="+codehab+")" +
						"(anaisEnvCode="+LdapObj.m_codeenv+")" +
						"(anaisApplicationCode="+LdapObj.m_codeapp+"))";
			}
			else
			{
				filter = "(&(objectClass=anaisHabilitation)" +
						"(anaisEnvCode="+LdapObj.m_codeenv+")" +
						"(anaisApplicationCode="+LdapObj.m_codeapp+"))";
			}			
		}
		
		
		try 
		{
		
			NamingEnumeration<?> habilitationsInstances = LdapObj.getAnais_ldap().search(basedn, filter, scopeSec);
			
			while (habilitationsInstances != null && habilitationsInstances.hasMore()) 
			{
				
				SearchResult habilitation = (SearchResult) habilitationsInstances.next();
					
				String DN = habilitation.getName() + "," + basedn;
			
				Attributes attrs = habilitation.getAttributes();
							
				AnaisHabilitationInstance habobject = new AnaisHabilitationInstance(this.LdapObj);
				habobject.setInfos(attrs);
				habobject.setDn( DN );

				if (habobject.getUniquemember()!= null)
				{
					for (int i =0; i< habobject.getUniquemember().size(); i++)
					{	
						String uniquemember = habobject.getUniquemember().get(i);
						
						if ( uniquemember.contains("Modeles") )
						{
							String InstanceMetier = uniquemember.replace("Modeles", "Instances");
							try 
							{
								AnaisSecurityGroup  secgroup = new AnaisSecurityGroup(this.LdapObj);
								ArrayList<AnaisUser> userslist = secgroup.ListAllUsersIntoSecurityGroupInstance(InstanceMetier);
								if ( userslist != null)
								{
									for ( int h=0; h<userslist.size(); h++)
									{
										AnaisUser user = userslist.get(h);
										HashMap<String, Object> listobj = new HashMap<String, Object>() ;
										listobj.put("user",user);
										listobj.put("hab",habobject);
										allusers.add(listobj);
									}
								}
							} 
							catch (AnaisExceptionNoObject e) 
							{
								e.printStackTrace();
							}

						}
						else
						{
							
							try 
							{
								AnaisUser user = new AnaisUser(this.LdapObj);
								user = user.GetUserInfoFromUserDN(uniquemember);
								HashMap<String, Object> listobj = new HashMap<String, Object>() ;
								listobj.put("user",user);
								listobj.put("hab",habobject);
								allusers.add(listobj);	
							} 
							catch (AnaisExceptionNoObject e) 
							{
								e.printStackTrace();
							}
						}
					}					
				}
			}
		} 
		catch (NamingException e)
		{
			e.printStackTrace();
		}
		return  allusers;
	}
	
	public String GetOrgPeopleDNFromOrgCode( String orgcode, String branche) throws AnaisExceptionNoObject, NamingException
	{
		if ( branche.trim().equals("") )branche = "Recouvrement";
		
		AnaisOrganisme  Organisme = new AnaisOrganisme(this.LdapObj);
		Organisme = Organisme.getOrganismeByOrgCode(orgcode, branche);
			
		String[] tokens = Organisme.getDn().split("," );
		String[] tokens2 = tokens[1].split("=" );
		String ircode = tokens2[1];
		String orgdn =  "ou="+orgcode+",ou=Personnes,ou="+ircode+",OU="+branche+",dc=recouv";
		return orgdn;

	}
	
	public ArrayList<AnaisHabilitationInstance> GetAllUserHabilitations 
	( String userdn, String codeir, String codeorg ) throws AnaisExceptionNoObject
	{
		
	   // ----------------------------------------------------------------
	   // Cette méthode est une adaptation Java du code PHP de l'API PHP
	   // ANAIS 1.5.5 (class.AnaisUser.php)
	   // ----------------------------------------------------------------
	   
	   
	   // Log de début
	   log.debug("GetAllUserHabilitations() - début");
	   log.debug(
	         "GetAllUserHabilitations() - userdn=\"" + userdn + 
	         "\", codeir=\"" + codeir + "\", codeorg=\"" + codeorg + "\"");
	   
	   // Constantes
	   String backend_recouv = "ou=recouvrement,dc=recouv";
	   
	   // on cherche d'abord les groupes de sécurité auxquels appartient l'utilisateur
	   ArrayList<AnaisSecurityGroup> securitygrouplist = 
	      this.ListAllSecurityGroupsByUserDn(userdn);
	   
	   // pour chaque groupe de sécurité, et aussi pour l'utilisateur lui-même (habilitation directe), 
      // on cherche les instances d'habilitations le référant dans l'organisme ou l'inter région spécifiée

      String filter = "(&(objectClass=anaisHabilitation)(anaisEnvCode=" + LdapObj.m_codeenv + ") ";
      String basedn = "" ;

      if (StringUtils.isBlank(codeir))
      {
         basedn = "DC=Recouv";
      }
      else
      {
         filter += "(anaisIRCode=" + codeir + ")";
         if (StringUtils.isNotBlank(codeorg))
         {
            basedn = 
               "ou=" + codeorg + ",ou=" + LdapObj.m_codeapp + 
               "_" + LdapObj.m_codeenv + ",OU=Applications,OU=" + 
               codeir + "," + backend_recouv;
            filter += "(anaisOrgCode=" + codeorg + ")";
         }
         else
         {
            basedn = 
               "ou=" + LdapObj.m_codeapp + "_" + LdapObj.m_codeenv + 
               ",OU=Applications,OU=" + codeir + "," + backend_recouv;
         }
      }
	   
      if (CollectionUtils.isEmpty(securitygrouplist))
      {
         filter += "(uniqueMember=" + userdn + "))";
      }
      else
      {
         filter += "(|(uniqueMember=$userdn)";
         for (AnaisSecurityGroup secgroup: securitygrouplist)
         {
            
            filter += "(uniqueMember=" + secgroup.getModeldn() + ")";
         }
         filter += "))";
      }
	   
      // Logs
	   log.debug("Resolution des habilitations. Filtre : " + filter);
	   log.debug("Resolution des habilitations. Base de recherche : " + basedn);
	   
	   // Réalise la recherche dans le LDAP
	   ArrayList<AnaisHabilitationInstance> habilitationList = new ArrayList<AnaisHabilitationInstance>();
	   SearchControls scopeSec = new SearchControls();
	   scopeSec.setSearchScope(SearchControls.SUBTREE_SCOPE);
	   scopeSec.setReturningAttributes(new String[] {"cn", "anaisApplicationCode", "anaisEnvCode", "anaisIRCode", "anaisOrgCode"});
	   try {
	      
	      // Effectue la recherche
	      NamingEnumeration<SearchResult> searchResult = 
            LdapObj.getAnais_ldap().search(
                  basedn, 
                  filter,
                  scopeSec);
      
         // Conversion des résultats de la recherche dans le LDAP en objets du modèle
         while (searchResult != null && searchResult.hasMore()) {
   
            SearchResult hab = (SearchResult) searchResult.next();
            String DN = hab.getName() + "," + basedn;
   
            Attributes attrs = hab.getAttributes();
   
            AnaisHabilitationInstance habInstance = new AnaisHabilitationInstance(
                  this.LdapObj);
            habInstance.setInfos(attrs);
            habInstance.setDn(DN);
            habilitationList.add(habInstance);
   
         }
      
	   } catch (NamingException e) {
         throw new AnaisExceptionNoObject(e.toString());
      }
	   
	   // Renvoie de la valeur de retour
	   log.debug("GetAllUserHabilitations() - fin");
	   return habilitationList;
	   
	}
	
	
	public void setInfos (Attributes attrs ) throws NamingException
	{
		if ( attrs.get("activcardsn") != null )
			this.setActivcardsn( (String) attrs.get("activcardsn").get());
		if ( attrs.get("activcardgroupname") != null )
			this.setActivcardgroupname( (String) attrs.get("activcardgroupname").get());
		if ( attrs.get("activcardframedaddress") != null )
			this.setActivcardframedaddress( (String) attrs.get("activcardframedaddress").get());
		if ( attrs.get("anaisdateentreerh") != null )
			this.setAnaisdateentreerh( (String) attrs.get("anaisdateentreerh").get());
		if ( attrs.get("anaisdatesortierh") != null )
			this.setAnaisdatesortierh( (String) attrs.get("anaisdatesortierh").get());
		if ( attrs.get("anaisoriginesource") != null )
			this.setAnaisoriginesource( (String) attrs.get("anaisoriginesource").get());
		if ( attrs.get("anaisperscodeemploiucanss") != null )
			this.setAnaisperscodeemploiucanss( (String) attrs.get("anaisperscodeemploiucanss").get());
		if ( attrs.get("anaispersdonatirmail") != null )
			this.setAnaispersdonatirmail( (String) attrs.get("anaispersdonatirmail").get());
		if ( attrs.get("anaisperslibelleemploi") != null )
			this.setAnaisperslibelleemploi( (String) attrs.get("anaisperslibelleemploi").get());
		if ( attrs.get("anaisperssercode") != null )
			this.setAnaisperssercode( (String) attrs.get("anaisperssercode").get());
		if ( attrs.get("anaisperstelinterne") != null )
			this.setAnaisperstelinterne( (String) attrs.get("anaisperstelinterne").get());
		if ( attrs.get("anaisperstelrecouv") != null )
			this.setAnaisperstelrecouv( (String) attrs.get("anaisperstelrecouv").get());
		if ( attrs.get("anaispersidsnv2") != null )
			this.setAnaispersidsnv2( (String) attrs.get("anaispersidsnv2").get());
		if ( attrs.get("anaisperstypecontrat") != null )
			this.setAnaisperstypecontrat( (String) attrs.get("anaisperstypecontrat").get());
		if ( attrs.get("title") != null )
			this.setTitle( (String) attrs.get("title").get());
		if ( attrs.get("businesscategory") != null )
			this.setBusinesscategory( (String) attrs.get("businesscategory").get());
		if ( attrs.get("departmentnumber") != null )
			this.setDepartmentnumber( (String) attrs.get("departmentnumber").get());
		if ( attrs.get("donatirmail") != null )
			this.setDonatirmail( (String) attrs.get("donatirmail").get());
		if ( attrs.get("facsimiletelephonenumber") != null )
			this.setFacsimiletelephonenumber( (String) attrs.get("facsimiletelephonenumber").get());
		if ( attrs.get("givenname") != null )
			this.setGivenname( (String) attrs.get("givenname").get());
		if ( attrs.get("mail") != null )
			this.setMail( (String) attrs.get("mail").get());
		if ( attrs.get("manager") != null )
			this.setManager( (String) attrs.get("manager").get());
		if ( attrs.get("mobile") != null )
			this.setMobile( (String) attrs.get("mobile").get());
		if ( attrs.get("nsaccountlock") != null )
			this.setNsaccountlock( (String) attrs.get("nsaccountlock").get());
		if ( attrs.get("ou") != null )
			this.setOu( (String) attrs.get("ou").get());
		if ( attrs.get("secretary") != null )
			this.setSecretary( (String) attrs.get("secretary").get());
		if ( attrs.get("sn") != null )
			this.setSn( (String) attrs.get("sn").get());
		if ( attrs.get("telephonenumber") != null )
			this.setTelephonenumber( (String) attrs.get("telephonenumber").get());
		if ( attrs.get("cn") != null )
			this.setCn( (String) attrs.get("cn").get());
		if ( attrs.get("uid") != null )
			this.setUid( (String) attrs.get("uid").get());
		if ( attrs.get("dn") != null )
			this.setDn( (String) attrs.get("dn").get());
		if ( attrs.get("l") != null )
			this.setL( (String) attrs.get("l").get());
		if ( attrs.get("anaisPersNomVille") != null )
			this.setAnaisPersNomVille( (String) attrs.get("anaisPersNomVille").get());		
		if ( attrs.get("anaisPersCodeDept") != null )
			this.setAnaisPersCodeDept( (String) attrs.get("anaisPersCodeDept").get());		
	}
	
	public String getUid() 
	{
		return uid;
	}


	public void setUid(String uid) 
	{
		this.uid = uid;
	}


	public String getCn() 
	{
		return cn;
	}


	public void setCn(String cn)
	{
		this.cn = cn;
	}


	public String getSn() 
	{
		return sn;
	}


	public void setSn(String sn) 
	{
		this.sn = sn;
	}


	public String getMail() 
	{
		return mail;
	}


	public void setMail(String mail) 
	{
		this.mail = mail;
	}


	public String getTelephonenumber() 
	{
		return telephonenumber;
	}


	public void setTelephonenumber(String telephonenumber) 
	{
		this.telephonenumber = telephonenumber;
	}


	public String getAnaisdateentreerh() 
	{
		return anaisdateentreerh;
	}


	public void setAnaisdateentreerh(String anaisdateentreerh)
	{
		this.anaisdateentreerh = anaisdateentreerh;
	}


	public String getAnaisdatesortierh() 
	{
		return anaisdatesortierh;
	}


	public void setAnaisdatesortierh(String anaisdatesortierh) 
	{
		this.anaisdatesortierh = anaisdatesortierh;
	}


	public String getAnaisperscodeemploiucanss()
	{
		return anaisperscodeemploiucanss;
	}


	public void setAnaisperscodeemploiucanss(String anaisperscodeemploiucanss) 
	{
		this.anaisperscodeemploiucanss = anaisperscodeemploiucanss;
	}


	public String getAnaispersdonatirmail() 
	{
		return anaispersdonatirmail;
	}


	public void setAnaispersdonatirmail(String anaispersdonatirmail) 
	{
		this.anaispersdonatirmail = anaispersdonatirmail;
	}


	public String getAnaisperslibelleemploi() 
	{
		return anaisperslibelleemploi;
	}


	public void setAnaisperslibelleemploi(String anaisperslibelleemploi) 
	{
		this.anaisperslibelleemploi = anaisperslibelleemploi;
	}


	public String getAnaisperssercode() 
	{
		return anaisperssercode;
	}


	public void setAnaisperssercode(String anaisperssercode) 
	{
		this.anaisperssercode = anaisperssercode;
	}


	public String getAnaisperstypecontrat() 
	{
		return anaisperstypecontrat;
	}


	public void setAnaisperstypecontrat(String anaisperstypecontrat) 
	{
		this.anaisperstypecontrat = anaisperstypecontrat;
	}


	public String getAnaisperstelinterne() 
	{
		return anaisperstelinterne;
	}


	public void setAnaisperstelinterne(String anaisperstelinterne) 
	{
		this.anaisperstelinterne = anaisperstelinterne;
	}


	public String getAnaisperstelrecouv() 
	{
		return anaisperstelrecouv;
	}


	public void setAnaisperstelrecouv(String anaisperstelrecouv)
	{
		this.anaisperstelrecouv = anaisperstelrecouv;
	}
	
	public String getAnaispersidsnv2() 
	{
		return anaispersidsnv2;
	}


	public void setAnaispersidsnv2(String anaispersidsnv2)
	{
		this.anaispersidsnv2 = anaispersidsnv2;
	}


	public String getAnaisoriginesource() 
	{
		return anaisoriginesource;
	}


	public void setAnaisoriginesource(String anaisoriginesource) 
	{
		this.anaisoriginesource = anaisoriginesource;
	}


	public String getFacsimiletelephonenumber() 
	{
		return facsimiletelephonenumber;
	}


	public void setFacsimiletelephonenumber(String facsimiletelephonenumber) 
	{
		this.facsimiletelephonenumber = facsimiletelephonenumber;
	}


	public String getGivenname() 
	{
		return givenname;
	}


	public void setGivenname(String givenname) 
	{
		this.givenname = givenname;
	}


	public String getDn() 
	{
		return dn;
	}


	public void setDn(String dn)
	{
		this.dn = dn;
	}


	public String getOrgcode() 
	{
		return orgcode;
	}


	public void setOrgcode(String orgcode) 
	{
		this.orgcode = orgcode;
	}


	public String getIrcode() 
	{
		return ircode;
	}


	public void setIrcode(String ircode) 
	{
		this.ircode = ircode;
	}


	public String getIrdn()
	{
		return irdn;
	}


	public void setIrdn(String irdn) 
	{
		this.irdn = irdn;
	}


	public String getOu() 
	{
		return ou;
	}


	public void setOu(String ou) 
	{
		this.ou = ou;
	}


	public String getL() 
	{
		return l;
	}


	public void setL(String l) 
	{
		this.l = l;
	}


	public String getBusinesscategory() 
	{
		return businesscategory;
	}


	public void setBusinesscategory(String businesscategory) 
	{
		this.businesscategory = businesscategory;
	}
	
	public String getTitle() 
	{
		return title;
	}


	public void setTitle(String title) 
	{
		this.title = title;
	}


	public String getManager() 
	{
		return manager;
	}


	public void setManager(String manager) 
	{
		this.manager = manager;
	}


	public String getSecretary() 
	{
		return secretary;
	}


	public void setSecretary(String secretary) 
	{
		this.secretary = secretary;
	}


	public String getDepartmentnumber() 
	{
		return departmentnumber;
	}


	public void setDepartmentnumber(String departmentnumber) 
	{
		this.departmentnumber = departmentnumber;
	}


	public String getDonatirmail() 
	{
		return donatirmail;
	}


	public void setDonatirmail(String donatirmail) 
	{
		this.donatirmail = donatirmail;
	}


	public String getMobile() 
	{
		return mobile;
	}


	public void setMobile(String mobile) 
	{
		this.mobile = mobile;
	}


	public String getNsaccountlock() 
	{
		return nsaccountlock;
	}


	public void setNsaccountlock(String nsaccountlock) 
	{
		this.nsaccountlock = nsaccountlock;
	}


	public String getActivcardsn() 
	{
		return activcardsn;
	}


	public void setActivcardsn(String activcardsn) 
	{
		this.activcardsn = activcardsn;
	}


	public String getActivcardframedaddress()
	{
		return activcardframedaddress;
	}


	public void setActivcardframedaddress(String activcardframedaddress) 
	{
		this.activcardframedaddress = activcardframedaddress;
	}


	public String getActivcardgroupname() 
	{
		return activcardgroupname;
	}


	public void setActivcardgroupname(String activcardgroupname) 
	{
		this.activcardgroupname = activcardgroupname;
	}
	public final Comparator<AnaisUser> SN_COMPARATOR = new Comparator<AnaisUser>() 
	{
		
		public int compare(AnaisUser arg0, AnaisUser arg1) 
		{
			AnaisUser p=(AnaisUser) arg0;
			AnaisUser q=(AnaisUser) arg1;
			if(p.getSn() == q.getSn())
			{    
				return p.cn.compareToIgnoreCase(q.cn);
			 }
			else
			{
				return p.cn.compareToIgnoreCase(q.cn);
			}				
		}
 
    };


	/**
	 * @return the anaisPersNomVille
	 */
	public String getAnaisPersNomVille() {
		return anaisPersNomVille;
	}

	/**
	 * @param anaisPersNomVille the anaisPersNomVille to set
	 */
	public void setAnaisPersNomVille(String anaisPersNomVille) {
		this.anaisPersNomVille = anaisPersNomVille;
	}

	/**
	 * @return the anaisPersCodeDept
	 */
	public String getAnaisPersCodeDept() {
		return anaisPersCodeDept;
	}

	/**
	 * @param anaisPersCodeDept the anaisPersCodeDept to set
	 */
	public void setAnaisPersCodeDept(String anaisPersCodeDept) {
		this.anaisPersCodeDept = anaisPersCodeDept;
	}
    
   
}
