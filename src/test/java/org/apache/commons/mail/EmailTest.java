package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.annotation.Mock;

public class EmailTest {
	
	private static final String[] TEST_EMAILS = {"ab@bc.com", "a.b@bc.org", "abcdefgfdhg@gdfghgfgd.com.bd"};

	@Mock
    MimeMessage mimeMessage;
	
	
	/* Concrete email Class for testing */
	private EmailConcrete email;
	
	@Before
	public void setUpemailTest() throws Exception{
		
		 email = new EmailConcrete();
	}
	
	
	@After
	public void tearDownEmailTest() throws Exception{
		
		
		
	}
//	/****************************************** Test addBcc (String... email) function  100% done*/

	@Test
	public void testAddBcc() throws Exception{
		
		email.addBcc(TEST_EMAILS);
		
		assertEquals(3, email.getBccAddresses().size());
	}
	
	@Test
	public void testAddBcc_shouldThrowException_whenEmailsIsNull() {
		String[] strings = {};
		try {
			this.email.addBcc(strings);
		} catch (Exception e) {
			assertNotNull(e);
			assertEquals(e.getMessage(), "Address List provided was invalid");
		}
	}
		
	
//	/*******************************************Test addCc (String email) function 100% done */ 
	@Test
	public void testAddCc() throws Exception{
		for(final String emailAddress : TEST_EMAILS) {
			email.addCc(emailAddress);
			
		}
		
		assertEquals(3, email.getCcAddresses().size());
	}
	
	//	/*******************************************Test addHeader (String name, String value) function 100% done*/
	
	@Test(expected = IllegalArgumentException.class)
     public void testAddHeaderEmptyName() throws Exception
	     {
	        email.addHeader("", "abc");
     }
	@Test(expected = IllegalArgumentException.class)
    public void testAddHeaderEmptyValue() throws Exception
	     {
	        email.addHeader("Jinal", "");
    }
//	/*******************************************Test addReplyTo (String email, String name) function done 100% */
	
	@Test
	public void testAddReplyTo() throws Exception{
		for(final String address : TEST_EMAILS) {
			email.addReplyTo(address);
		}
		assertEquals(TEST_EMAILS.length, email.getReplyToAddresses().size());
		
		
	}
//	/*******************************************Test buildMimeMessage () function done 73.3% done*/
	@Test
	public void testBuildMimeMessage()throws EmailException{
		email.setHostName("Localhost");
		email.setSmtpPort(1234);
		email.setFrom("Jinal@gmail.com");
		email.addTo("sutariya@gmail.com");
		email.setSubject("Test mail");
		email.setCharset("ISO-8859-1");
		email.setContent("test content", "text/plain");
		email.addCc("jinal@gmail.com");
		email.addBcc("jinal@gmail.com");
		email.addHeader("jinal", "Sutariya");
		email.addReplyTo("jinal@gmail.com", "Jinal sutariya");
		email.buildMimeMessage();
		
	}
	
	@Test
	public void testBuildMimeMessage_shouldThrowIllegalStateException_whenMessageIsNotNull() throws EmailException {
		this.email.setHostName("localhost");
		this.email.setSmtpPort(587);
		this.email.setFrom("Jinal@gmail.com");
		this.email.addTo("sutariya@gmail.com");
		this.email.setSubject("test mail"); 
		this.email.setCharset("ISO-8859-1");
		this.email.setContent("test content", "text/plain");
		final MimeMessage msg = email.getMimeMessage();
		this.email.message = msg;
		
		try {
			this.email.buildMimeMessage();
		} catch (Exception e) {
			// TODO: handle exception
			assertNotNull(e);
			assertEquals(e.getMessage(), "The MimeMessage is already built.");
		}
		
	}
	
	@Test
	public void testBuildMimeMessage_shouldCallSetSubject_whenCharsetisEmpty() throws EmailException {
		this.email.setHostName("localhost");
		this.email.setSmtpPort(587);
		this.email.setFrom("a@b.com");
		this.email.addTo("c@d.com");
		this.email.setSubject("test mail"); 
		this.email.setContent("test content", "text/plain");
		this.email.message = null;
		
		this.email.buildMimeMessage();
		
		assertEquals(this.email.subject, "test mail");
	}
//	/*******************************************Test getHostName () function 100% done*/
	@Test
	public void testGetHostName() {
		
		email.setHostName("Localhost");
		String hostName = email.getHostName();
		assertEquals("Localhost", hostName);
		
	}
	@Test
	public void testGetSetHostNull() {
		email.setHostName(null);
		assertEquals(null, email.getHostName());
		
	}
		
	@Test
	public void testGetSetHostNameWithSession() {
		Properties properties = new Properties();
		Session session = Session.getDefaultInstance(properties,null);
		properties.put(EmailConstants.MAIL_HOST,"Localhost");
		email.setHostName("Localhost");
		assertEquals("Localhost", email.getHostName());
		
		
	}
	
	@Test
	public void testGetHostName_shouldSetMailHost_whenSessionIsNotNull() {
		final Properties properties = new Properties(System.getProperties());
		properties.setProperty(EmailConstants.MAIL_TRANSPORT_PROTOCOL, EmailConstants.SMTP);
		properties.setProperty(
		EmailConstants.MAIL_PORT, String.valueOf(587));
		properties.setProperty(EmailConstants.MAIL_HOST, "localhost");
		properties.setProperty(EmailConstants.MAIL_DEBUG, String.valueOf(false));
		  
		final Session mySession = Session.getInstance(properties, null);
		this.email.setMailSession(mySession);
		
		String hostName = this.email.getHostName();
		
		assertEquals(hostName, "localhost");
	}
//	/*******************************************Test getMailSession() function done 100% */
	@Test
	public void testGetMailSession_shouldThrowException_whenhostNameIsEmptry() throws EmailException {
		this.email.setSmtpPort(587);
		this.email.setFrom("a@b.com");
		this.email.addTo("c@d.com");
		this.email.setSubject("test mail"); 
		this.email.setCharset("ISO-8859-1");
		this.email.setContent("test content", "text/plain");
		final MimeMessage msg = email.getMimeMessage();
		this.email.message = msg;
		
		try {
			this.email.getMailSession();
		} catch (Exception e) {
			assertNotNull(e);
		}
	}
	//	/*******************************************Test getSentDate() function done 100% */
	@Test
	public void testGetSetSentDate() {
		 Date date = Calendar.getInstance().getTime();
		 email.setSentDate(date);
         assertEquals(date, email.getSentDate());
	}

//	/*******************************************Test getSocketConnection() function done 100% */
	
	@Test
	public void testGestScketConnectionTimeout() {
		int socketConnection = 0;
		email.setSocketConnectionTimeout(socketConnection);
		assertEquals(socketConnection, email.getSocketConnectionTimeout());
		
	}
//	/*******************************************Test setFrom(String) function done 100% */
	
	@Test
	public void testSetFrom() throws Exception{
		email.setFrom("Jinal@gmail.com", "");
		
	}

	
}
 