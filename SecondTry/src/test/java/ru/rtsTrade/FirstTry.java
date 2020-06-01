package ru.rtsTrade;

import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.stream.Stream;

public class FirstTry {
    @Test
    public void firstTest() {
        System.setProperty("webdriver.chrome.driver","src/chromedriver/yandexdriver.exe");//путь к веб драйверу

        Config date = new Config();
        ChromeDriver driver = new ChromeDriver(); //создаем переменную driver
        driver.get("https://223.rts-tender.ru/supplier/auction/Trade/Search.aspx"); //открывам страницу по заданому url

        //ожидание до прогрузки формы class="form_block filter"
        WebDriverWait wait = new WebDriverWait(driver,15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("form_block")));
        //проставляем галочки на атрибутах поиска
        WebElement searchCust = driver.findElement(By.cssSelector("[for=\"BaseMainContent_MainContent_chkPurchaseType_0\"]"));
         searchCust.click();//прожимаем ФКЗ
        searchCust = driver.findElement(By.cssSelector("[for=\"BaseMainContent_MainContent_chkPurchaseType_1\"]"));
         searchCust.click();//прожимаем комерческую закупку
        searchCust = driver.findElement(By.id("BaseMainContent_MainContent_txtPublicationDate_txtDateFrom"));
         searchCust.sendKeys(date.getStartDate());//задаем начальную дату публикации
        searchCust = driver.findElement(By.id("BaseMainContent_MainContent_txtPublicationDate_txtDateTo"));
         searchCust.sendKeys(date.getStartDate());//задаем конечную дату публикации
        searchCust = driver.findElement(By.id("BaseMainContent_MainContent_btnSearch"));
         searchCust.click();//прожимаем поиск


        driver.quit();
    }
}
