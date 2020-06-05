package ru.rtsTrade;

import java.io.*;
import  java.util.Properties;

//класс считывания даты из конфига
public class Config {

    private String startDate,finishDate;
    private double USD, EUR;

    public  Config() {

        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/config.ini");
            property.load(fis);

             startDate = property.getProperty("start_date");
             finishDate = property.getProperty("finish_date");

             USD = Double.parseDouble(property.getProperty("USD"));
             EUR = Double.parseDouble(property.getProperty("EUR"));

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

    }

    public String getStartDate() {
        return startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public double getEUR() {
        return EUR;
    }

    public double getUSD() {
        return USD;
    }

    public void update()    {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);

            String startDate = property.getProperty("start_date");
            String finishDate = property.getProperty("finish_date");
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }
}
