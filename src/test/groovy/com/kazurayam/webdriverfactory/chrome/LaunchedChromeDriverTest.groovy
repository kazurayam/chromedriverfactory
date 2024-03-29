package com.kazurayam.webdriverfactory.chrome

import com.kazurayam.webdriverfactory.CacheDirectoryName
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.junit.Assert.assertNotNull

class LaunchedChromeDriverTest {

    static Path outputFolder
    ChromeDriver driver

    @BeforeClass
    static void beforeClass() {
        WebDriverManager.chromedriver().clearDriverCache().setup()
        outputFolder = Paths.get(".").resolve("build/tmp/testOutput")
                .resolve(LaunchedChromeDriverTest.class.getSimpleName())
        Files.createDirectories(outputFolder)
    }

    @After
    void tearDown() {
        if (driver != null) {
            driver.quit()
            driver = null
        }
    }

    @Test
    void test_smoke() {
        ChromeOptions opts = new ChromeOptions()
        opts.addArguments("--headless")
        driver = new ChromeDriver(opts)
        LaunchedChromeDriver launched =
                new LaunchedChromeDriver(driver)
        assertNotNull(launched)
        ChromeUserProfile chromeUserProfile =
                ChromeProfileUtils.findChromeUserProfileByCacheDirectoryName(
                        new CacheDirectoryName("Default")
                )
        launched.setChromeUserProfile(chromeUserProfile)
                .setInstruction(ChromeDriverFactory.UserDataAccess.TO_GO)
        //
        launched.getChromeUserProfile().ifPresent(
                { ChromeUserProfile cup ->
                    println "ChromeUserProfile => " + cup.toString()
                })
        launched.getInstruction().ifPresent(
                { ChromeDriverFactory.UserDataAccess instruction ->
                    println "UserDataAccess => " + instruction.toString()
                })
        launched.getEmployedOptions().ifPresent(
                { ChromeOptions options ->
                    println "options => " + options.toString()
                })
    }

    @Test
    void test_getEmployedOptions() {
        ChromeDriverFactory factory = ChromeDriverFactory.newHeadlessChromeDriverFactory()
        factory.addChromeOptionsModifier(ChromeOptionsModifiers.incognito())
        LaunchedChromeDriver launched = factory.newChromeDriver()
        launched.getEmployedOptions().ifPresent { ChromeOptions options ->
            println options
        }
        launched.getEmployedOptionsAsJSON().ifPresent { String json ->
            println json
            assert json.contains("incognito")
        }
        launched.getDriver().quit()
    }
}
