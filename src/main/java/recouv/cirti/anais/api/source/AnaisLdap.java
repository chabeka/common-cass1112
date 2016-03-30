//Date : 23 juillet 2010

package recouv.cirti.anais.api.source;

import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AnaisLdap
{
	protected static Log log = LogFactory.getLog(AnaisLdap.class);
	
	public Integer m_usetls = 0;
	public String m_suffixdn = "dc=recouv";
	public Boolean m_debug = false;
	public String m_codeapp = "";
	public String m_codeenv = "";
	public String m_host ="";
	public String m_comptePortail ="";
	public Integer m_port = 389;
	public Boolean activerDroitsDirect = Boolean.FALSE;
	private InitialLdapContext anais_ldap;
	private String url;
	private final String VERSION_API = "1.5";
	
	public InitialLdapContext getAnais_ldap() 
	{
		return anais_ldap;
	}

	public void setAnais_ldap(InitialLdapContext anais_ldap) 
	{
		this.anais_ldap = anais_ldap;
	}

	public String getUrl() 
	{
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = url;
	}
	
	/**
	 * Initialisation du context LDAP permettant d'activer les droits directs,
	 * En production, il n'y a pas de droit direct donc à désactiver.
	 * 
	 * @param hostname
	 * @param port
	 * @param usetls
	 * @param appdn
	 * @param passwd
	 * @param codeapp
	 * @param codeenv
	 * @param timeout
	 * @param comptePortail
	 * @param activerDroitsDirect Si true, les droits directs sont activés
	 * @throws AnaisExceptionServerAuthentication
	 * @throws AnaisExceptionFailure
	 * @throws AnaisExceptionServerCommunication
	 * @since 1.5.4
	 */
	public void init(String hostname, Integer port, boolean usetls,
			String appdn, String passwd, String codeapp, String codeenv,
			String timeout, String comptePortail,Boolean activerDroitsDirect)
			throws AnaisExceptionServerAuthentication, AnaisExceptionFailure,
			AnaisExceptionServerCommunication {
		
		this.activerDroitsDirect = activerDroitsDirect;
		this.init(hostname, port, usetls, appdn, passwd, codeapp, codeenv, timeout, comptePortail);
	}

	/**
	 * Initialisation du context LDAP.
	 * Les droits directs sont par défaut désactivé.
	 * @param hostname
	 * @param port
	 * @param usetls
	 * @param appdn
	 * @param passwd
	 * @param codeapp
	 * @param codeenv
	 * @param timeout
	 * @param comptePortail
	 * @throws AnaisExceptionServerAuthentication
	 * @throws AnaisExceptionFailure
	 * @throws AnaisExceptionServerCommunication
	 */		
	public void init(String hostname, Integer port, boolean usetls,
			String appdn, String passwd, String codeapp, String codeenv, String timeout , String comptePortail) 
			throws AnaisExceptionServerAuthentication, AnaisExceptionFailure, AnaisExceptionServerCommunication
	{
		m_codeapp = codeapp;
		m_codeenv = codeenv;
		m_host = hostname;
		m_port = port;
		
		m_comptePortail = comptePortail;
				
		boolean ok = false;
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		
		StringTokenizer st = new StringTokenizer(hostname,",");
		
		while((!ok) && (st.hasMoreTokens()))
		{
			String host_tmp = st.nextToken();
			if(usetls)
				url = new String("ldaps://" + host_tmp + ":" + port.toString());
			else
				url = new String("ldap://" + host_tmp + ":" + port.toString());
			
			
			if ( log.isDebugEnabled() )
				log.debug("AnaisLdap-->Init: url=" + url);
			
			try 
			{
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: debut env");
				
				env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
				env.put("java.naming.ldap.version", "3");
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: 2 env");
				
				env.put(Context.PROVIDER_URL, url);
				
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: 3 env");
				
				env.put(Context.SECURITY_AUTHENTICATION, "simple");
				
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: 4 env appdn:" +appdn);
								
				env.put(Context.SECURITY_PRINCIPAL, appdn);
				
				if ( log.isDebugEnabled() )
					log.debug("AnaisConnection_Application-->Init: 5 env");
				
				env.put(Context.SECURITY_CREDENTIALS, passwd);
				
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: 6 env");
				
				/* Prise en compte du parametre timeout */
				env.put("com.sun.jndi.ldap.connect.timeout", timeout);
				/* Activation du pool */
				//env.put("com.sun.jndi.ldap.connect.pool", "true");
			
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: 7 env");
				
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: fin env");
				
				anais_ldap = new InitialLdapContext(env, null);
				
				if ( log.isInfoEnabled() )
					log.info("Connexion : Succes");
				
				//anais_ldap.reconnect(null);
				
				if ( log.isDebugEnabled() )
					log.debug("AnaisLdap-->Init: url=" + url+ " : connexion succesfull.");
				
				ok = true;
			}
			catch (CommunicationException comEx) 
			{
				if ( log.isErrorEnabled() )
					log.error("AnaisLdap-->init :: ERREUR :: Connexion impossible a l'url : "+ url);
			}
			catch (AuthenticationException authEx) 
			{
				if ( log.isErrorEnabled() ) {
					log.error("AnaisLdap-->init :: ERREUR :: Impossible de se connecter au serveur : "+ url);
					log.error("appdn : "+appdn);
					log.error("paswd : "+passwd);
					log.error("codeapp : "+codeapp);
					log.error("codeenv : "+codeenv);
				}
				
				AnaisExceptionServerAuthentication servAuthEx = new AnaisExceptionServerAuthentication(authEx.toString());
				throw servAuthEx;
			} 
			catch (NamingException e) 
			{
				if ( log.isErrorEnabled() )
					log.error("AnaisLdap-->init :: ERREUR :: Connexion impossible a l'url : "+ url);
				AnaisExceptionFailure anaisEx = new AnaisExceptionFailure(e.toString());
				throw anaisEx;
			}
		}
		if(!ok)
		{
			if ( log.isErrorEnabled() )
				log.error("AnaisLdap-->init :: ERREUR :: Impossible de se connecter aux serveurs.");
			
			AnaisExceptionServerCommunication servDownEx = new AnaisExceptionServerCommunication(
							"Connexion impossible aux serveurs Ldap");
			throw servDownEx;
		}
	}
	
	public void close() throws AnaisExceptionServerCommunication 
	{
		try 
		{
			if (anais_ldap==null) {
				AnaisExceptionServerCommunication servDownEx = new AnaisExceptionServerCommunication("Erreur lors de la fermeture des connections au serveur.");
				
				if ( log.isErrorEnabled() )
					log.error("AnaisLdap-->close :: ERREUR :: Erreur lors de la fermeture des connections au serveur.");
				
				throw servDownEx;
			}
			else {
				anais_ldap.close();
			}
		} 
		catch (NamingException e) 
		{
			AnaisExceptionServerCommunication servDownEx = new AnaisExceptionServerCommunication(e.toString());
			throw servDownEx;
		}
	}
	
	public String getVERSION_API() 
	{
		return VERSION_API;
	}

}