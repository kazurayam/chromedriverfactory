package examples


import com.kazurayam.webdriverfactory.chrome.ChromeDriverFactory
import com.kazurayam.webdriverfactory.chrome.ChromeOptionsModifiers
import com.kazurayam.webdriverfactory.chrome.LaunchedChromeDriver
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openqa.selenium.chrome.ChromeOptions

class PrintEmployedOptions {

    LaunchedChromeDriver launched

    @Test
    void test_getEmployedOptions() {
        ChromeDriverFactory cdf = ChromeDriverFactory.newHeadlessChromeDriverFactory()
        cdf.addChromeOptionsModifier(ChromeOptionsModifiers.headless())
        cdf.addChromeOptionsModifier(ChromeOptionsModifiers.incognito())
        launched = cdf.newChromeDriver()
        launched.getEmployedOptions().ifPresent { ChromeOptions options ->
            println options
        }
        launched.getEmployedOptionsAsJSON().ifPresent { String json ->
            println json
            assert json.contains("incognito")
        }
    }

    @BeforeClass
    static void beforeClass() {
        // setup the ChromeDriver binary
        WebDriverManager.chromedriver().setup()
    }

    @Before
    void setUp() {
        launched = null
    }

    @After
    void tearDown() {
        if (launched != null) {
            launched.getDriver().quit()
            launched = null
        }
    }

}
