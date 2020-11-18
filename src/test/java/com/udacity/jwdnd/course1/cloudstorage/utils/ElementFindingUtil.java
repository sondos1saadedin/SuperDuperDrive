package com.udacity.jwdnd.course1.cloudstorage.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementFindingUtil {

    public static void waitForVisibility(WebDriver driver, WebElement element) throws Error {
        new WebDriverWait(driver, 40).until(ExpectedConditions.visibilityOf(element));
    }

    public static void performClickJSECommand(WebDriver driver, WebElement ele) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ele);
    }
}
