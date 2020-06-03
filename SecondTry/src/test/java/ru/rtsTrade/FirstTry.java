package ru.rtsTrade;

import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
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
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));
        //проставляем галочки на атрибутах поиска
        WebElement searchCust = driver.findElement(By.cssSelector("[for=\"BaseMainContent_MainContent_chkPurchaseType_0\"]"));
         searchCust.click();//прожимаем ФКЗ
        searchCust = driver.findElement(By.cssSelector("[for=\"BaseMainContent_MainContent_chkPurchaseType_1\"]"));
         searchCust.click();//прожимаем комерческую закупку
        searchCust = driver.findElement(By.id("BaseMainContent_MainContent_txtPublicationDate_txtDateFrom"));
         searchCust.sendKeys(date.getStartDate());//задаем начальную дату публикации
        searchCust = driver.findElement(By.id("BaseMainContent_MainContent_txtPublicationDate_txtDateTo"));
         searchCust.sendKeys(date.getFinishDate());//задаем конечную дату публикации
        //выбираем отображение 100 элементов на странице
        searchCust = driver.findElement(By.className("ui-pg-selbox"));
        Select select = new Select(searchCust);
        select.selectByValue("100");

        //searchCust = driver.findElement(By.id("BaseMainContent_MainContent_btnSearch"));
        // searchCust.click();//прожимаем поиск

        //ждем прогрузку элементов
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));
        //берем кол-во страниц
        String pageCount = driver.findElement(By.id("sp_1_BaseMainContent_MainContent_jqgTrade_toppager")).getText();
        System.out.println("Page count = "+ pageCount);
        //берём кол-во элементов на странице
        int elCount = driver.findElements(By.xpath("//*[contains(@class,\"ui-widget-content jqgrow ui-row-ltr\")]")).size();
        System.out.println("Element count = "+ elCount);


        WebElement table = driver.findElement(By.id("BaseMainContent_MainContent_jqgTrade"));
        List <WebElement> allRows = table.findElements(By.tagName("tr"));
        for (WebElement row : allRows){
            List<WebElement> cells = row.findElements(By.tagName("td"));
            System.out.println("Столбец = "+ cells.get(5).getText());
       }



       /* //след страница
        searchCust = driver.findElement(By.xpath("//*[contains(@class,\"ui-icon ui-icon-seek-next\")]"));
         searchCust.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));
        String s = driver.findElement(By.className("ui-pg-input")).getAttribute("value");
        System.out.println("Now page "+ s);*/
        driver.quit();
    }
}
