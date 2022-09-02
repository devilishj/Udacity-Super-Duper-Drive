package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.io.File;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private  static WebDriver driver;
	private static String baseUrl;
	private final String  username="UdacityStudent";
	private final String  password="JavaProgramming";

	private final String  username2="JavaStudent";
	private final String  password2="StudentProgrammer";

	private final String  badUsername="RandomUsername";
	private final String  badPassword="RandomPassword";

	//Note
	private final String testNoteTitle="Hello Udacity";
	private final String testNoteTitleAfterEdit="Hello World";
	private final String testNoteDescription ="This is a note";
	private final String testNoteDescriptionAfterEdit="This is an edited note";

	//Credentials
	private final String[] credentialUrls= new String[]{"udacity.com", "java.com"};
	private final String[] credentialUsernames = new String[]{"student","javaman"};
	private final String[] credentialPasswords = new String[]{"password1","password2"};
	private final String[] credentialUrlsAfterEdit= new String[]{"udacityclassroom.com", "javaprogramming.com"};
	private final String[] credentialUsernamesAfterEdit = new String[]{"starstudent","javastar"};
	private final String[] credentialPasswordsAfterEdit = new String[]{"edited1","edited2"};
	private static final String fileName = "upload5m.zip";
	private final int total = 2;


	@Autowired
	private EncryptionService encryptionService;
	@Autowired
	private CredentialService credentialService;

	private Logger logger= LoggerFactory.getLogger(CloudStorageApplicationTests.class);

	SignupPageTesting signupPageTesting = new SignupPageTesting(driver);
	LoginPageTesting loginPageTesting= new LoginPageTesting(driver);
	HomePageTesting homePageTesting= new HomePageTesting(driver);
	NotePageTesting notePageTesting = new NotePageTesting(driver);
	CredentialController credentialController = new CredentialController(driver);
	WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterAll
	 public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeEach
	public void beforeEach() throws InterruptedException {
		baseUrl="http://localhost:" + this.port;
		sleep(1000);
	}

	@AfterEach
	public void takeABreak() throws InterruptedException {
		sleep(2000);
		//driver.quit();
	}

	public void awaitPageLoaded(String id){
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
	}

	@Test
	@Order(1)
	// Write a test that verifies that the home page is not accessible without logging in
	// also tests redirection to login page
	public void testGetPages() {

		logger.info("Beginning Test 1");
		logger.info("Home Inaccessible Without Login and Redirect to Login");

		driver.get(baseUrl+"/home");
        Assertions.assertNotEquals("Home",driver.getTitle());
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
    // Write a test that signs up a new user, verifies redirection to login page,
	// logs that user in, verifies that they can access the home page,
	// then logs out and verifies that the home page is no longer accessible
    public void testSignupProcess() throws InterruptedException {

		logger.info("Beginning Test 2");
		logger.info("Signup Process and Confirm Creation with Login");

		driver.get(baseUrl + "/signup");
		String lastName = "Renek";
		String firstName = "Jason";
		signupPageTesting.signUpUser(firstName, lastName, username, password);
		sleep(1000);

		assertEquals("Login", driver.getTitle());
		sleep(1000);

        driver.get(baseUrl + "/login");
        loginPageTesting.LoginUser(username,password);
		sleep(1000);

		Assertions.assertEquals("Home",driver.getTitle());

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		Assertions.assertNotEquals("Home",driver.getTitle());

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username,password);
		sleep(1000);

		Assertions.assertEquals("Home",driver.getTitle());
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(3)
	// Write a test that attempts signs up a user with existing credentials
	public void testDuplicateSignupAttempt() throws Exception {

		logger.info("Beginning Test 3");
		logger.info("Attempt to sign up with existing credentials");

		driver.get(baseUrl + "/signup");
		String lastName = "Renek";
		String firstName = "Jason";
		signupPageTesting.signUpUser(firstName, lastName, username, password);
		sleep(1000);

		Assertions.assertTrue(driver.getPageSource().contains("User name already exists"));
	}

	@Test
	@Order(4)
	// Write a test attempts to sign up a user with incomplete signup form
	public void testIncompleteSignupForm() throws Exception {

		logger.info("Beginning Test 4");
		logger.info("Attempt to sign up with incomplete form");

		driver.get(baseUrl + "/signup");
		String lastName = "";
		String firstName = "";
		signupPageTesting.signUpUser(firstName, lastName, username, password);
		sleep(1000);

		Assertions.assertEquals("Sign Up",driver.getTitle());
	}

	@Test
	@Order(5)
	// Write a test that attempts signs in with invalid credentials
	public void testLoginWithInvalidCredentials() throws Exception {

		logger.info("Beginning Test 5");
		logger.info("Attempt to log in with invalid credentials");

		driver.get(baseUrl + "/login");

		loginPageTesting.LoginUser(badUsername, badPassword);
		sleep(1000);

		Assertions.assertTrue(driver.getPageSource().contains("Invalid username or password"));
	}

	@Test
	@Order(6)
	// Test a bad url
	public void testBadUrl() throws Exception{

		logger.info("Beginning Test 6");
		logger.info("Login and Navigate to Bad URL");

		driver.get(baseUrl + "/login");

		loginPageTesting.LoginUser(username, password);
		sleep(1000);

		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		driver.get(baseUrl + "/bad-url");
		Assertions.assertTrue(driver.getPageSource().contains("Page Not Found"));
		sleep(1000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(7)
	// Write a test that logs in an existing user,
	// attempts to submit a blank note, add note tab should remain open to continue
	// creates a note and verifies that the note details are visible in the note list
	public void testCreateNotes() throws Exception {

		logger.info("Beginning Test 7");
		logger.info("Login and Create Notes");

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username,password);
		sleep(1000);

		Assertions.assertEquals("Home",driver.getTitle());

		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		awaitPageLoaded(notePageTesting.getAddNoteBtnId());
		notePageTesting.clickAddNoteBtn();

		notePageTesting.inputNoteTitle("");
		notePageTesting.inputNoteDescription("");
		sleep(1000);

		notePageTesting.submitNote();
		sleep(2000);

		notePageTesting.inputNoteTitle(testNoteTitle);
		notePageTesting.inputNoteDescription("");
		sleep(1000);

		notePageTesting.submitNote();
		sleep(2000);

		notePageTesting.inputNoteTitle(testNoteTitle);
		notePageTesting.inputNoteDescription(testNoteDescription);
		sleep(1000);

		notePageTesting.submitNote();
		sleep(2000);

		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		Assertions.assertEquals(notePageTesting.getNoteTitleDisplay(), testNoteTitle);
		Assertions.assertEquals(notePageTesting.getNoteDesDisplay(), testNoteDescription);
		sleep(1000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl+"/home");
		Assertions.assertNotEquals("Home",driver.getTitle());
	}

	@Test
	@Order(8)
	// Write a test that logs in an existing user with existing notes,
	// clicks the edit note button on an existing note, changes the note data,
	// saves the changes, and verifies that the changes appear in the note list.
	public void testLoginAndEditNotes() throws Exception {

		logger.info("Beginning Test 8");
		logger.info("Login and Edit Notes");

		driver.get(baseUrl + "/login");

		loginPageTesting.LoginUser(username, password);
		sleep(1000);

		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		Assertions.assertEquals(notePageTesting.getNoteTitleDisplay(), testNoteTitle);
		Assertions.assertEquals(notePageTesting.getNoteDesDisplay(), testNoteDescription);
		notePageTesting.clickNoteEditBtn();
		sleep(2000);

		notePageTesting.inputNoteTitle(testNoteTitleAfterEdit);
		notePageTesting.inputNoteDescription(testNoteDescriptionAfterEdit);
		sleep(1000);

		notePageTesting.submitNote();
		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		sleep(2000);

		Assertions.assertEquals(notePageTesting.getNoteTitleDisplay(), testNoteTitleAfterEdit);
		Assertions.assertEquals(notePageTesting.getNoteDesDisplay(), testNoteDescriptionAfterEdit);
		sleep(1000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}
	@Test
	@Order(9)
	// Write a Selenium test that logs in an existing user with existing notes,
	// clicks the delete note button on an existing note, and verifies that the note no longer appears in the note list.
	public void testLoginAndDeleteNotes() throws Exception{

		logger.info("Beginning Test 9");
		logger.info("Login and Delete Notes");

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username, password);
		sleep(1000);

		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		sleep(2000);

		Assertions.assertEquals(notePageTesting.getNoteTitleDisplay(), testNoteTitleAfterEdit);
		Assertions.assertEquals(notePageTesting.getNoteDesDisplay(), testNoteDescriptionAfterEdit);
		sleep(1000);

		notePageTesting.clickNoteDeleteBtn();
		sleep(2000);

		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		sleep(2000);

		Assertions.assertEquals(0,notePageTesting.getNoteEditBtns().size());
		sleep(1000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());

	}

	@Test
	@Order(10)
	// Write a Selenium test that logs in an existing user,
	// creates a credential and verifies that the credential details are visible in the credential list.
	public void testLoginAndCreateCredential() throws Exception {

		logger.info("Beginning Test 11");
		logger.info("Login and Create Credentials");

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username, password);
		sleep(1000);

		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		awaitPageLoaded(credentialController.getCredTabId());
		credentialController.clickCredTab();
		sleep(1000);

		awaitPageLoaded(credentialController.getAddCredBtnId());
		credentialController.clickAddCredBtn();

		credentialController.inputUrl("");
		credentialController.inputUserName("");
		credentialController.inputPasswd("");
		sleep(2000);

		credentialController.clickCredSubmitBtn();
		sleep(3000);

		credentialController.inputUrl(credentialUrls[0]);
		credentialController.inputUserName("");
		credentialController.inputPasswd("");
		sleep(2000);

		credentialController.clickCredSubmitBtn();
		sleep(3000);

		credentialController.inputUrl(credentialUrls[0]);
		credentialController.inputUserName(credentialUsernames[0]);
		credentialController.inputPasswd("");
		sleep(2000);

		credentialController.clickCredSubmitBtn();
		sleep(3000);

		credentialController.inputUrl(credentialUrls[0]);
		credentialController.inputUserName(credentialUsernames[0]);
		credentialController.inputPasswd(credentialPasswords[0]);
		sleep(2000);

		credentialController.clickCredSubmitBtn();
		sleep(3000);


		awaitPageLoaded(credentialController.getCredTabId());
		credentialController.clickCredTab();
		sleep(1000);

		awaitPageLoaded(credentialController.getAddCredBtnId());
		credentialController.clickAddCredBtn();

		credentialController.inputUrl(credentialUrls[1]);
		credentialController.inputUserName(credentialUsernames[1]);
		credentialController.inputPasswd(credentialPasswords[1]);
		sleep(2000);

		credentialController.clickCredSubmitBtn();
		sleep(3000);

		awaitPageLoaded(credentialController.getCredTabId());
		credentialController.clickCredTab();
		sleep(2000);

		for (int pos = 0; pos < total; pos++) {
			String displayedUrl = credentialController.getUrl(pos);
			String displayedUname = credentialController.getUname(pos);
			String displayedPwd = credentialController.getPw(pos);
			String key = credentialService.getKeyById(pos + 1);
			displayedPwd = encryptionService.decryptValue(displayedPwd, key);

			Assertions.assertEquals(displayedUrl, credentialUrls[pos]);
			Assertions.assertEquals(displayedUname, credentialUsernames[pos]);
			Assertions.assertEquals(displayedPwd, credentialPasswords[pos]);
		}
		sleep(1000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(11)
	// Write a Selenium test that logs in an existing user with existing credentials,
	// clicks the edit credential button on an existing credential,
	// changes the credential data, saves the changes, and verifies that the changes appear in the credential list.
	public void testLoginAndEditCredential() throws Exception {

		logger.info("Beginning Test 11");
		logger.info("Login and Edit Credentials");

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username, password);
		sleep(1000);

		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		for(int pos=0;pos<total;pos++) {
			//wait for Credential page is visible
			awaitPageLoaded(credentialController.getCredTabId());
			credentialController.clickCredTab();
			sleep(2000);

			credentialController.clickEditCredBtn(pos);
			sleep(2000);

			credentialController.inputUrl(credentialUrlsAfterEdit[pos]);
			credentialController.inputUserName(credentialUsernamesAfterEdit[pos]);

			Assertions.assertEquals(credentialController.getPasswdInModal(),credentialPasswords[pos]);
			credentialController.inputPasswd(credentialPasswordsAfterEdit[pos]);
			sleep(2000);

			credentialController.clickCredSubmitBtn();
			sleep(3000);
		}

		awaitPageLoaded(credentialController.getCredTabId());
		credentialController.clickCredTab();
		sleep(2000);

		for(int pos=0;pos<total;pos++){
			String displayedUrl=credentialController.getUrl(pos);
			String displayedUname=credentialController.getUname(pos);
			String displayedPwd = credentialController.getPw(pos);
			String key=credentialService.getKeyById(pos+1);
			displayedPwd= encryptionService.decryptValue(displayedPwd,key);

			Assertions.assertEquals(displayedUrl,credentialUrlsAfterEdit[pos]);
			Assertions.assertEquals(displayedUname,credentialUsernamesAfterEdit[pos]);
			Assertions.assertEquals(displayedPwd,credentialPasswordsAfterEdit[pos]);
		}
		sleep(3000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(12)
	// Write a Selenium test that logs in an existing user with existing credentials,
	// clicks the delete credential button on an existing credential,
	// and verifies that the credential no longer appears in the credential list.
	public void testLoginAndDeleteCredential() throws Exception {

		logger.info("Beginning Test 12");
		logger.info("Login and Delete Credentials");

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username, password);
		sleep(1000);

		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		for(int pos=0;pos<total;pos++){
			awaitPageLoaded(credentialController.getCredTabId());
			credentialController.clickCredTab();
			sleep(1000);

			credentialController.clickDeleteCredBtn(0);
			sleep(3000);
		}

		awaitPageLoaded(credentialController.getCredTabId());
		credentialController.clickCredTab();
		Assertions.assertEquals(0,credentialController.getEditBtns().size());
		sleep(2000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(14)
	// Write a test that verifies an error when no file is selected
	public void testNoFileSelected() throws Exception{

		logger.info("Beginning Test 14");
		logger.info("No File Selected");

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username, password);
		sleep(1000);

		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uploadButton")));
		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		Assertions.assertTrue(driver.getPageSource().contains("No file selected"));

	}

	@Test
	@Order(15)
	// Write a test that verifies an error when trying to upload a file size that exceeds limit
	public void testTooLargeUpload() throws Exception{

		logger.info("Beginning Test 15");
		logger.info("File Upload Exceeds Limit");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		String fileName2 = "13mbtoolarge.zip";
		fileSelectButton.sendKeys(new File(fileName2).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("errorMsg")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertTrue(driver.getPageSource().contains("File size exceeds the limit"));

	}

	@Test
	@Order(16)
	// Write a test that verifies large file upload success
	public void testLargeUpload() throws Exception {

		logger.info("Beginning Test 16");
		logger.info("Large File Upload");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("successMsg")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertTrue(driver.getPageSource().contains("File successfully uploaded"));
	}

	@Test
	@Order(17)
	// write a test verifies an error when a file with a duplicate name is uploaded
	public void testDuplicateFileUpload() {

		logger.info("Beginning Test 17");
		logger.info("Duplicate File Upload");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("errorMsg")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertTrue(driver.getPageSource().contains("A file with the same name already exists"));
	}

	@Test
	@Order(18)
	// Write a test that downloads a file, verifies it was successfully downloaded,
	// and deletes the downloaded file from the download directory
	void downloadFileTest() throws InterruptedException {
		logger.info("Beginning Test 18");
		logger.info("Test Download File");

		boolean isDownloaded = false;
		boolean isDeleted = false;

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("downloadButton")));
		WebElement downloadButton = driver.findElement(By.id("downloadButton"));
		downloadButton.click();
		sleep(6000);

		isDownloaded = DownloadTesting.isFileDownloaded();
		sleep(2000);

		Assertions.assertTrue(isDownloaded);
		sleep(2000);

		isDeleted = DownloadTesting.isFileDeleted();
		sleep(2000);

		Assertions.assertTrue(isDeleted);
	}

	@Test
	@Order(19)
	// write a test that deletes the file from the files tab and verifies it was deleted
	void deleteFileTest() throws InterruptedException {
		logger.info("Beginning Test 19");
		logger.info("Test Delete File");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteButton")));
		WebElement deleteButton = driver.findElement(By.id("deleteButton"));
		deleteButton.click();
		sleep(2000);

		Assertions.assertTrue(driver.getPageSource().contains("File successfully deleted"));
		sleep(2000);

		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());

	}

	@Test
	@Order(20)
	// write a test that verifies a user cannot view another users info
	void visibilityTest() throws InterruptedException {
		logger.info("Beginning Test 19");
		logger.info("Test Visibility between users");

		driver.get(baseUrl + "/login");
		loginPageTesting.LoginUser(username,password);
		sleep(1000);

		Assertions.assertEquals("Home",driver.getTitle());

		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		awaitPageLoaded(notePageTesting.getAddNoteBtnId());
		notePageTesting.clickAddNoteBtn();
		sleep(1000);

		notePageTesting.inputNoteTitle(testNoteTitle);
		notePageTesting.inputNoteDescription(testNoteDescription);
		sleep(1000);

		notePageTesting.submitNote();
		sleep(2000);

		driver.get(baseUrl + "/home");
		sleep(1000);
		Assertions.assertEquals("Home", driver.getTitle());
		sleep(1000);

		awaitPageLoaded(credentialController.getCredTabId());
		credentialController.clickCredTab();
		sleep(1000);

		awaitPageLoaded(credentialController.getAddCredBtnId());
		credentialController.clickAddCredBtn();
		sleep(1000);

		credentialController.inputUrl(credentialUrls[0]);
		credentialController.inputUserName(credentialUsernames[0]);
		credentialController.inputPasswd(credentialPasswords[0]);
		sleep(2000);

		credentialController.clickCredSubmitBtn();
		sleep(3000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/signup");
		String lastName = "LastName";
		String firstName = "FirstName";
		signupPageTesting.signUpUser(firstName, lastName, username2, password2);
		sleep(1000);

		assertEquals("Login", driver.getTitle());
		sleep(1000);

		loginPageTesting.LoginUser(username2,password2);
		sleep(1000);

		Assertions.assertEquals("Home",driver.getTitle());
		awaitPageLoaded(notePageTesting.getNoteTabId());
		notePageTesting.clickNoteTab();
		sleep(2000);

		Assertions.assertEquals(0,notePageTesting.getNoteEditBtns().size());
		sleep(1000);

		awaitPageLoaded(credentialController.getCredTabId());
		credentialController.clickCredTab();
		sleep(1000);

		Assertions.assertEquals(0,credentialController.getEditBtns().size());
		sleep(2000);

		driver.get(baseUrl + "/home");
		homePageTesting.clickLogoutBtn();
		sleep(1000);

		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}



	public static class DownloadTesting {
		// MUST change filepath to your default Chrome download directory
		static String filePath = "D:\\Downloads";
		static File directory = new File(filePath);
		static File[] filesList = directory.listFiles();

		public static boolean isFileDownloaded() {
			for (File file : filesList) {
				if (file.getName().equals(fileName)) {
					return true;
				}
			}
			return false;
		}

		public static boolean isFileDeleted() {

			for (int i = 0; i < filesList.length; i++) {
				if (filesList[i].getName().equals(fileName)) {
					filesList[i].delete();
				}
			}
			for (File file : filesList) {
				if (file.getName().equals(fileName)) {
					return true;
				}
			}
			return false;
		}
	}

	public static class LoginPageTesting {

		@FindBy(id="inputUsername")
		private WebElement username;

		@FindBy(id="inputPassword")
		private WebElement password;

		@FindBy(id="loginButton")
		private WebElement loginButton;

		public LoginPageTesting(WebDriver webDriver) {
			PageFactory.initElements(webDriver,this);}

		public void LoginUser(String user,String pass){
			username.clear();
			username.sendKeys(user);
			password.clear();
			password.sendKeys(pass);
			loginButton.click();
		}
	}

	public static class SignupPageTesting {
		@FindBy(id="inputFirstName")
		WebElement firstName;

		@FindBy(id="inputLastName")
		WebElement lastName;

		@FindBy(id="inputUsername")
		WebElement userName;

		@FindBy(id="inputPassword")
		WebElement passWord;

		@FindBy(id="signupButton")
		WebElement signupButton;
		public SignupPageTesting(WebDriver webDriver) {
			PageFactory.initElements(webDriver,this);
		}

		public void signUpUser(String fname, String lname, String uname, String pword){
			firstName.clear(); firstName.sendKeys(fname);
			lastName.clear(); lastName.sendKeys(lname);
			userName.clear(); userName.sendKeys(uname);
			passWord.clear(); passWord.sendKeys(pword);
			signupButton.click();
		}
	}



	public static class CredentialController {
		private final WebDriver webDriver;

		private final String credTabId="nav-credentials-tab";
		private final String addCredBtnId="addCredBtnId";
		private final String credUrlId="credential-url";
		private final String credUserId="credential-username";
		private final String credPasswdId="credential-password";
		private final String credSubmitBtnId="credSubmitBtn";
		private final String credUrlTextId="credUrlText";
		private final String credUsernameTextId="credUsernameText";
		private final String credPasswordTextId="credPasswordText";
		private final String editCredBtnId="editCredBtn";
		private final String deleteCredBtnId="deleteCredBtn";

		public CredentialController(WebDriver webDriver) {
			this.webDriver = webDriver;
			PageFactory.initElements(webDriver,this);
		}

		@FindBy(id=credTabId)
		WebElement credTab;
		@FindBy(id=addCredBtnId)
		WebElement addCredBtn;
		@FindBy(id=credUrlId)
		WebElement credUrl;
		@FindBy(id=credUserId)
		WebElement credUser;
		@FindBy(id=credPasswdId)
		WebElement credPasswd;
		@FindBy(id=credSubmitBtnId)
		WebElement credSubmitBtn;

		public String getCredTabId() { return credTabId; }
		public String getAddCredBtnId() { return addCredBtnId;}

		public void clickCredTab(){credTab.click();}
		public void clickAddCredBtn(){addCredBtn.click();}
		public void inputUrl(String url){
			((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+url+"';",this.credUrl);
		}
		public void inputUserName(String uname){
			((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+uname+"';",this.credUser);
		}
		public void inputPasswd(String pw){
			((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+pw+"';",this.credPasswd);
		}
		public String getPasswdInModal(){
			return credPasswd.getAttribute("value");
		}

		public void clickCredSubmitBtn(){
			((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",this.credSubmitBtn);
		}

		public String getUrl(int pos){
			List<WebElement> urls=webDriver.findElements(By.id(credUrlTextId));
			return urls.get(pos).getAttribute("innerHTML");
		}
		public String getUname(int pos){
			List<WebElement> unames=webDriver.findElements(By.id(credUsernameTextId));
			return unames.get(pos).getAttribute("innerHTML");
		}
		public String getPw(int pos){
			List<WebElement> pwds=webDriver.findElements(By.id(credPasswordTextId));
			return pwds.get(pos).getAttribute("innerHTML");
		}

		public void clickEditCredBtn(int pos){
			List<WebElement> edbtns=webDriver.findElements(By.id(editCredBtnId));
			((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",edbtns.get(pos));
		}

		public void clickDeleteCredBtn(int pos){
			List<WebElement> delbtns=webDriver.findElements(By.id(deleteCredBtnId));
			((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",delbtns.get(pos));
		}

		public List<WebElement> getEditBtns(){ return webDriver.findElements(By.id(editCredBtnId));}

	}

	public static class HomePageTesting {

		private final WebDriver webDriver;
		public HomePageTesting(WebDriver webDriver) {
			this.webDriver=webDriver;
			PageFactory.initElements(webDriver, this);
		}

		@FindBy(id="logout")
		WebElement logoutBtn;

		public void clickLogoutBtn(){
			((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",logoutBtn);
		}
	}

	public static class NotePageTesting {
		private WebDriver webDriver;

		private final String noteTabId="nav-notes-tab";
		private final String addNoteBtnId="addNoteBtn";
		private final String noteTitleId="note-title";
		private final String noteDescriptionId="note-description";
		private final String noteSubmitBtnId="noteSubmit";
		private final String noteTitleDisplayId="note-title-display";
		private final String noteDesDisplayId="note_description-display";
		private final String noteEditBtnId="noteEditBtn";
		private final String noteDeleteBtnId="noteDeleteBtn";

		public String getNoteTabId() {
			return noteTabId;
		}
		public String getAddNoteBtnId() {
			return addNoteBtnId;
		}

		@FindBy(id=noteTabId)
		private WebElement noteTab;

		@FindBy(id=addNoteBtnId)
		private WebElement addNoteBtn;

		@FindBy(id=noteTitleId)
		WebElement noteTitle;

		@FindBy(id=noteDescriptionId)
		WebElement noteDescription;

		@FindBy(id=noteSubmitBtnId)
		WebElement noteSubmitBtn;

		@FindBy(id=noteTitleDisplayId)
		WebElement noteTitleDisplay;

		@FindBy(id=noteDesDisplayId)
		WebElement noteDesDisplay;

		@FindBy(id=noteEditBtnId)
		WebElement noteEditBtn;
		@FindBy(id=noteDeleteBtnId)
		WebElement noteDeleteBtn;

		public NotePageTesting(WebDriver webDriver) {
			this.webDriver=webDriver;
			PageFactory.initElements(webDriver,this);
		}

		public void clickNoteTab(){
			noteTab.click();
		}

		public void clickAddNoteBtn(){ addNoteBtn.click(); }

		public void inputNoteTitle(String title){
			((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + title + "';", this.noteTitle);
		}
		public void inputNoteDescription(String des) {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + des + "';", this.noteDescription);
		}
		public void submitNote() {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteSubmitBtn);
		}

		public String getNoteTitleDisplay(){
			return noteTitleDisplay.getAttribute("innerHTML");
		}

		public String getNoteDesDisplay(){
			return noteDesDisplay.getAttribute("innerHTML");
		}

		public void clickNoteEditBtn() {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteEditBtn);
		}
		public void clickNoteDeleteBtn(){
			((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteDeleteBtn);
		}

		public List<WebElement> getNoteEditBtns(){return webDriver.findElements(By.id(noteEditBtnId));}

	}
}
