package com.pizzastudio.centerpoint.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzastudio.centerpoint.model.Place;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import retrofit2.http.Query;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SeleniumCrawler {

    FirefoxOptions options = new FirefoxOptions();
    WebDriver driver;

    @PostConstruct
    public void init(){
        createDriver();
    }

    private void createDriver(){
        if(driver == null){
            options.setHeadless(true);
            driver = new FirefoxDriver(options);
        }
    }
    // https://nominatim.openstreetmap.org/search?q=%EA%B2%BD%ED%9D%AC%EB%8C%80%ED%95%99%EA%B5%90&format=json&addressdetails=1
    public List<Place> getPlace(String q, String format, String addressdetails, String countrycodes){
        createDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10L);
        String url =
                String.format("https://nominatim.openstreetmap.org/search?q=%s&format=%s&addressdetails=%s&countrycode=%s",
                        q,format,addressdetails,countrycodes);
        List<Place>  placeList = null;
        try {
            driver.get(url);
            String jsonString = driver.findElement(By.xpath("//div[@id='json']")).getText();
            System.out.println(jsonString);
            placeList = Arrays.asList(new ObjectMapper().readValue(jsonString, Place[].class));
        } catch(Exception e){
            System.err.println(String.format("getPlace error : %s", e.getMessage()));
        } finally {

        }
        return placeList;
    }

    // https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=37.5281149&lon=126.8753029
    public List<Place> reverseGeocoding(String format, String latitude, String longitude){
        createDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10L);
        String url =
                String.format("https://nominatim.openstreetmap.org/reverse?format=%s&lat=%s&lon=%s",
                        format, latitude, longitude);
        List<Place>  placeList = null;

        try {
            driver.get(url);
            String jsonString = driver.findElement(By.xpath("//div[@id='json']")).getText();
            if(!jsonString.startsWith("[")){
                placeList = new ArrayList<>();
                Place p = new ObjectMapper().readValue(jsonString, Place.class);
                placeList.add(p);
            }
            placeList = Arrays.asList(new ObjectMapper().readValue(jsonString, Place[].class));
        } catch(Exception e){
            System.err.println(String.format("getPlace error : %s", e.getMessage()));
        } finally {

        }
        return placeList;
    }

}
