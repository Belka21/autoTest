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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FirstTry {
    @Test
    public void firstTest() {
        System.setProperty("webdriver.chrome.driver","src/chromedriver/yandexdriver.exe");//путь к веб драйверу

        Config conf = new Config();//берем данные из конфига
        ChromeDriver driver = new ChromeDriver(); //создаем переменную driver
        driver.get("https://223.rts-tender.ru/supplier/auction/Trade/Search.aspx"); //открывам страницу по заданому url

        //ожидание до прогрузки (исчезновение таблички "загрузка")
        WebDriverWait wait = new WebDriverWait(driver,15);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));

        //проставляем галочки на атрибутах поиска
        driver.findElement(By.cssSelector("[for=\"BaseMainContent_MainContent_chkPurchaseType_0\"]")).click();//прожимаем ФКЗ
        driver.findElement(By.cssSelector("[for=\"BaseMainContent_MainContent_chkPurchaseType_1\"]")).click();//прожимаем комерческую закупку
        driver.findElement(By.id("BaseMainContent_MainContent_txtPublicationDate_txtDateFrom")).sendKeys(conf.getStartDate());//задаем начальную дату публикации
        driver.findElement(By.id("BaseMainContent_MainContent_txtPublicationDate_txtDateTo")).sendKeys(conf.getFinishDate());//задаем конечную дату публикации

        //выбираем отображение 100 элементов на странице
        Select select = new Select(driver.findElement(By.className("ui-pg-selbox")));
        select.selectByValue("100");

        //searchCust = driver.findElement(By.id("BaseMainContent_MainContent_btnSearch"));
        // searchCust.click();//прожимаем поиск

        //ждем прогрузку элементов (исчезновение таблички "загрузка")
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));

        //берем кол-во страниц
        int pageCount = Integer.parseInt(driver.findElement(By.id("sp_1_BaseMainContent_MainContent_jqgTrade_toppager")).getText());
        System.out.println("Page count = "+ pageCount);


        double summ = 0;//суммарная стоимость
        ArrayList purchase = new ArrayList(); //список закупок содержащих ЕИС

        //обработка заказов в таблице на всех страницах
        //добавление в список того, что содержит ЕИС
        for (int i = 1; i <= pageCount; i++){
            //ждем прогрузку элементов (исчезновение таблички "загрузка")
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));

            System.out.println("Текущая страница "+ i + " из "+pageCount);

            WebElement table = driver.findElement(By.id("BaseMainContent_MainContent_jqgTrade"));//получаем таблицу
            List <WebElement> allRows = table.findElements(By.tagName("tr"));//считываем все строки из таблицы

            for (WebElement row : allRows){//обработка строк
                List<WebElement> cells = row.findElements(By.tagName("td"));
                System.out.print("Номер закупки = "+ cells.get(4).getText());
                System.out.println(" ЕИС = " + cells.get(5).getText());
                //проверка на пустой ЕИС
                if ((cells.get(5).getText().length()!=0) && (cells.get(5).getText().length()!=1)){
                    purchase.add(cells.get(5).getText()); //добавили в список закупку
                    System.out.print("        Добавлен в список");
                    summ += StringTreatment.getMoney(cells.get(10).getAttribute("title"), conf);
                    System.out.println("        Увеличение суммы на = "+ StringTreatment.getMoney(cells.get(10).getAttribute("title"), conf));
                }//if

            }//for
            //переходим на следующую страницу если кнопка активна
            String enabled = driver.findElement(By.id("next_t_BaseMainContent_MainContent_jqgTrade_toppager")).getAttribute("class");
            if (enabled.indexOf("ui-state-disabled")==-1)//проверка на наличие отключающего класса в кнопке
                driver.findElement(By.id("next_t_BaseMainContent_MainContent_jqgTrade_toppager")).click();
        }//for

        //вывод промежуточных данных
        System.out.println();
        System.out.println("Суммарная стоимость(промеж.) = " + summ + " RUB");
        System.out.println(("Колличество закупок(промеж.) = "+ purchase.size()));
        System.out.println();

        //переходим в отмененые
        driver.findElement(By.xpath("//*[contains(@class, \"ui-tabs-anchor\") and @value='4']")).click();

        //ждем прогрузку элементов (исчезновение таблички "загрузка")
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));

        pageCount = Integer.parseInt(driver.findElement(By.id("sp_1_BaseMainContent_MainContent_jqgTrade_toppager")).getText());
        System.out.println("Page count = "+ pageCount);

        //обработка заказов в таблице на всех страницах
        //Удаление из списка закупок находящихся в отмененных
        for (int i = 1; i <= pageCount; i++){
            //ждем прогрузку элементов (исчезновение таблички "загрузка")
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));

            System.out.println("Текущая страница "+ i + " из "+pageCount);

            WebElement table = driver.findElement(By.id("BaseMainContent_MainContent_jqgTrade"));//получаем таблицу
            List <WebElement> allRows = table.findElements(By.tagName("tr"));//считываем все строки из таблицы

            for (WebElement row : allRows){//обработка строк
                List<WebElement> cells = row.findElements(By.tagName("td"));
                System.out.print("Номер закупки = "+ cells.get(4).getText());
                System.out.println(" ЕИС = " + cells.get(5).getText());
                if ((cells.get(5).getText().length()!=0) && (cells.get(5).getText().length()!=1))
                    for (int j = 0; j<purchase.size();j++) {
                       if (purchase.get(j).equals(cells.get(5).getText())) {
                            purchase.remove(j); //удалили из списка закупку
                            System.out.print("        Удален из списка");
                           summ -= StringTreatment.getMoney(cells.get(10).getAttribute("title"), conf);
                         System.out.println("    Уменьшение суммы на = " + StringTreatment.getMoney(cells.get(10).getAttribute("title"), conf));
                       }//if
                    }
            }//for
            //переходим на следующую страницу если кнопка активна
            String enabled = driver.findElement(By.id("next_t_BaseMainContent_MainContent_jqgTrade_toppager")).getAttribute("class");
            if (enabled.indexOf("ui-state-disabled")==-1)//проверка на наличие отключающего класса в кнопке
                driver.findElement(By.id("next_t_BaseMainContent_MainContent_jqgTrade_toppager")).click();
        }//for


       /* //след страница
        searchCust = driver.findElement(By.xpath("//*[contains(@class,\"ui-icon ui-icon-seek-next\")]"));
         searchCust.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("load_BaseMainContent_MainContent_jqgTrade")));
        String s = driver.findElement(By.className("ui-pg-input")).getAttribute("value");
        System.out.println("Now page "+ s);*/
        System.out.println();
        System.out.println("Суммарная стоимость = " + summ + " RUB");
        System.out.println(("Колличество закупок = "+ purchase.size()));
        driver.quit();
    }//firsTest
}
