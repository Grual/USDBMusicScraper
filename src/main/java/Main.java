import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    private static char[] CHARS = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static void main(String[] args) throws InterruptedException {
        if(args.length < 2) {
            System.err.println("Usage: USDBMusicScraper.jar FILENAME TIME_TO_WAIT_IN_SECS");
            return;
        }

        String filename = args[0];
        int sleepTime = Integer.parseInt(args[1]) * 1000;

        WebDriver driver = new ChromeDriver();
        int i = 0;

        driver.get("https://usdb.eu/music");
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("Title;Artist;Year");

            do {
                Thread.sleep(sleepTime);
                List<WebElement> tableRows = driver.findElement(By.id("tab_" + CHARS[i])).findElements(By.xpath("//tbody/tr"));
                System.out.println("Getting tab_" + CHARS[i]);
                for (WebElement elem : tableRows) {
                    List<WebElement> songTitleAndArtistInfo = elem.findElements(By.tagName("a"));
                    if(songTitleAndArtistInfo.size() < 4 || songTitleAndArtistInfo.get(0).getText().isEmpty()
                            || songTitleAndArtistInfo.get(2).getText().isEmpty()
                            || songTitleAndArtistInfo.get(3).getText().isEmpty()) continue;
                    bw.write(songTitleAndArtistInfo.get(0).getText() + ";");
                    bw.write(songTitleAndArtistInfo.get(2).getText() + ";");
                    bw.write(songTitleAndArtistInfo.get(3).getText() + ";\n");
                }

                System.out.println("done");
                List<WebElement> tabs = driver.findElement(By.className("tabs")).findElements(By.xpath("//ul/li/a"));
                if(tabs.size() == 0) continue;
                if(++i + 12 == 28) break;
                tabs.get(i + 12).click();
            } while (i < CHARS.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
