package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.udacity.jwdnd.course1.cloudstorage.consts.CloudStorageApplicationTestConsts.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;

	public String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterAll
	public static void afterAll() {
		driver.quit();
		driver = null;
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}


	@Test
	public void testHomePageFunctionalities() {
		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Sondos", "Saad", TEST_USERNAME, TEST_PASSWORD);

		driver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(TEST_USERNAME, TEST_PASSWORD);
		assertEquals(driver.getCurrentUrl(), baseURL + "/home");

		HomePage homePage = new HomePage(driver);
		homePage.logout();

		assertEquals(driver.getCurrentUrl(), baseURL + "/login");

		testNoteCreationEditionDeletion();
		testCredentialCreationEditionDeletion();

	}


	private void testNoteCreationEditionDeletion() {
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(TEST_USERNAME, TEST_PASSWORD);
		assertEquals(driver.getCurrentUrl(), baseURL + "/home");

		HomePage homePage = new HomePage(driver);
		homePage.createNewNote(TEST_NOTE_TITLE, TEST_NOTE_DESCRIPTION);
		Note firstNote = homePage.getFirstNote();

		assertEquals(TEST_NOTE_TITLE, firstNote.getNoteTitle());
		assertEquals(TEST_NOTE_DESCRIPTION, firstNote.getNoteDescription());

		homePage.editNote(TEST_NOTE_TITLE_V2, TEST_NOTE_DESCRIPTION_V2);

		Note firstNoteV2 = homePage.getFirstNote();
		assertEquals(TEST_NOTE_TITLE_V2, firstNoteV2.getNoteTitle());
		assertEquals(TEST_NOTE_DESCRIPTION_V2, firstNoteV2.getNoteDescription());

		homePage.deleteNote();
		homePage.goToNotesNav();
		assertThrows(NoSuchElementException.class, () -> {driver.findElement(By.className("noteTitle"));});
	}


	private void testCredentialCreationEditionDeletion() {

		HomePage homePage = new HomePage(driver);
		homePage.createNewCredential(TEST_CREDENTIAL_URL, TEST_CREDENTIAL_USERNAME, TEST_CREDENTIAL_PASSWORD);
		Credential firstCredential = homePage.getFirstCredential();

		assertEquals(TEST_CREDENTIAL_URL, firstCredential.getUrl());
		assertEquals(TEST_CREDENTIAL_USERNAME, firstCredential.getUsername());
		assertNotEquals(TEST_CREDENTIAL_PASSWORD, firstCredential.getPassword());

		homePage.editCredential(TEST_CREDENTIAL_URL_V2, TEST_CREDENTIAL_USERNAME_V2, TEST_CREDENTIAL_PASSWORD_V2);

		Credential firstCredentialV2 = homePage.getFirstCredential();
		assertEquals(TEST_CREDENTIAL_URL_V2, firstCredentialV2.getUrl());
		assertEquals(TEST_CREDENTIAL_USERNAME_V2, firstCredentialV2.getUsername());
		assertNotEquals(TEST_CREDENTIAL_PASSWORD_V2, firstCredentialV2.getPassword());

		homePage.deleteCredential();
		homePage.goToCredentialNav();
		assertThrows(NoSuchElementException.class, () -> {driver.findElement(By.className("credentialUrl"));});
	}

}
