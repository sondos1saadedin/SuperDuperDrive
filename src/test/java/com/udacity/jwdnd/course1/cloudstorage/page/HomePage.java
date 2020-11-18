package com.udacity.jwdnd.course1.cloudstorage.page;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.udacity.jwdnd.course1.cloudstorage.utils.ElementFindingUtil.performClickJSECommand;
import static com.udacity.jwdnd.course1.cloudstorage.utils.ElementFindingUtil.waitForVisibility;

public class HomePage {

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;

    //Note Part
    @FindBy(id = "nav-notes-tab")
    private WebElement navNotes;

    @FindBy(id = "addNoteButton")
    private WebElement addNoteButton;

    @FindBy(className = "editNoteButton")
    private WebElement editNoteButton;

    @FindBy(className = "deleteNoteButton")
    private WebElement deleteNoteButton;

    @FindBy(className = "noteTitle")
    private WebElement noteTitle;

    @FindBy(className = "noteDescription")
    private WebElement noteDescription;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmit;

    //Credential part
    @FindBy(className = "credentialTab")
    private WebElement navCredentials;

    @FindBy(id = "addCredentialButton")
    private WebElement addCredentialButton;

    @FindBy(className = "editCredentialButton")
    private WebElement editCredentialButton;

    @FindBy(className = "deleteCredentialButton")
    private WebElement deleteCredentialButton;

    @FindBy(className = "credentialUrl")
    private WebElement credentialUrl;

    @FindBy(className = "credentialId")
    private WebElement credentialId;

    @FindBy(className = "credentialUsername")
    private WebElement credentialUsername;

    @FindBy(className = "credentialPassword")
    private WebElement credentialPassword;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlInput;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(id = "credentialSubmit")
    private WebElement credentialSubmit;

    private final WebDriver driver;

    public HomePage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.logoutButton.submit();
    }

    //Note part
    public void goToNotesNav() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", navNotes);
        waitForVisibility(this. driver, this.addNoteButton);
    }

    public void createNewNote(String noteTitle, String noteDescription) {
        System.out.println("Creating new Note");
        goToNotesNav();
        performClickJSECommand(this.driver, this.addNoteButton);
        waitForVisibility(this.driver, this.noteTitleInput);

        this.noteTitleInput.sendKeys(noteTitle);
        this.noteDescriptionInput.sendKeys(noteDescription);

        this.noteSubmit.submit();
    }

    public void editNote(String noteTitle, String noteDescription) {
        System.out.println("Editing a Note");
        goToNotesNav();
        performClickJSECommand(this.driver, this.editNoteButton);
        waitForVisibility(this.driver, this.noteTitleInput);

        this.noteTitleInput.clear();
        this.noteTitleInput.sendKeys(noteTitle);

        this.noteDescriptionInput.clear();
        this.noteDescriptionInput.sendKeys(noteDescription);

        this.noteSubmit.submit();
    }

    public Note getFirstNote() {
        System.out.println("Getting first Note");
        goToNotesNav();
        Note note = new Note();
        note.setNoteTitle(noteTitle.getText());
        note.setNoteDescription(noteDescription.getText());
        return note;
    }

    public void deleteNote() {
        System.out.println("Deleting a Note");
        goToNotesNav();
        deleteNoteButton.click();
    }


    //Credential part
    public void goToCredentialNav() {
        performClickJSECommand(this.driver, navCredentials);
        waitForVisibility(this.driver, this.addCredentialButton);
    }

    public void createNewCredential(String url, String username, String password) {
        System.out.println("Creating new Credential");
        goToCredentialNav();
        performClickJSECommand(this.driver, this.addCredentialButton);
        waitForVisibility(this.driver, this.credentialUrlInput);

        this.credentialUrlInput.sendKeys(url);
        this.credentialUsernameInput.sendKeys(username);
        this.credentialPasswordInput.sendKeys(password);

        this.credentialSubmit.submit();
    }


    public void editCredential(String url, String username, String password) {
        System.out.println("Editing a Credential");
        goToCredentialNav();
        performClickJSECommand(this.driver, this.editCredentialButton);
        waitForVisibility(this.driver, this.credentialUrlInput);

        this.credentialUrlInput.clear();
        this.credentialUrlInput.sendKeys(url);

        this.credentialUsernameInput.clear();
        this.credentialUsernameInput.sendKeys(username);

        this.credentialPasswordInput.clear();
        this.credentialPasswordInput.sendKeys(password);

        this.credentialSubmit.submit();
    }

    public Credential getCredentialInfoInEditDialog() {
        System.out.println("Getting Credential info from the the edit dialog");

        goToCredentialNav();
        performClickJSECommand(this.driver, this.editCredentialButton);
        waitForVisibility(this.driver, this.credentialUrlInput);

        Credential credential = new Credential();
        credential.setCredentialId(Integer.parseInt(credentialId.getAttribute("textContent")));
        credential.setUrl( this.credentialUrlInput.getAttribute("value"));
        credential.setUsername(this.credentialUsernameInput.getAttribute("value"));
        credential.setPassword(this.credentialPasswordInput.getAttribute("value"));

        this.credentialSubmit.submit();
        return credential;
    }


    public Credential getFirstCredential() {
        System.out.println("Getting first Credential");
        goToCredentialNav();
        Credential credential = new Credential();
        credential.setCredentialId(Integer.parseInt(credentialId.getAttribute("textContent")));
        credential.setUrl(credentialUrl.getText());
        credential.setUsername(credentialUsername.getText());
        credential.setPassword(credentialPassword.getText());

        return credential;
    }

    public void deleteCredential() {
        System.out.println("Deleting a Credential");
        goToCredentialNav();
        deleteCredentialButton.click();
    }
}
