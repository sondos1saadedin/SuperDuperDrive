package com.udacity.jwdnd.course1.cloudstorage.testPage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Driver;

import static com.udacity.jwdnd.course1.cloudstorage.utils.ElementFindingUtil.performJSECommand;
import static com.udacity.jwdnd.course1.cloudstorage.utils.ElementFindingUtil.waitForVisibility;

public class HomePage {

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes")
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

    private final WebDriver driver;

    public HomePage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.logoutButton.submit();
    }

    public void goToNotesNav() {
        WebElement notes = driver.findElement(By.xpath("//a[@href='#nav-notes']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", notes);
        waitForVisibility(this. driver, this.addNoteButton);
    }

    public void createNewNote(String noteTitle, String noteDescription) {
        System.out.println("Creating new Note");
        goToNotesNav();
        performJSECommand(this.driver, this.addNoteButton);
        waitForVisibility(this.driver, this.noteTitleInput);

        this.noteTitleInput.sendKeys(noteTitle);
        this.noteDescriptionInput.sendKeys(noteDescription);

        this.noteSubmit.submit();
    }

    public void editNote(String noteTitle, String noteDescription) {
        System.out.println("Editing a Note");
        goToNotesNav();
        performJSECommand(this.driver, this.editNoteButton);
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


}
