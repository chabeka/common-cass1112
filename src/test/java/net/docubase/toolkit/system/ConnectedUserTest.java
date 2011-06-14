package net.docubase.toolkit.system;

import net.docubase.toolkit.Authentication;

import org.junit.Test;

public class ConnectedUserTest {
    private static final String ADM_LOGIN = "_ADMIN";
    private static final String ADM_PASSWORD = "DOCUBASE";
    private static final String HOSTNAME = "cer69-ds4int";
    private static final Integer PORT = Integer.valueOf(4020);
    private static final Integer DOMAIN_ID = Integer.valueOf(1);

    @Test(expected = RuntimeException.class)
    public void testBadCredential() throws Exception {
	Authentication.openSession("BadLogin", "BadPassword", HOSTNAME, PORT,
		DOMAIN_ID);
    }

    @Test
    public void testGetConnectedUserList() throws Exception {
	//
	// ConnectedUser[] userList = ConnectedUserFactory
	// .getConnectedUserList(getSession(ADM_LOGIN, ADM_PASSWORD));
	// assertEquals(1, userList.length);
    }
}
