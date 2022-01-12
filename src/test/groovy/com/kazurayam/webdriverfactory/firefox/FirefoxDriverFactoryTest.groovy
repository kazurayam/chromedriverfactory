package com.kazurayam.webdriverfactory.firefox

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import com.kazurayam.subprocessj.Subprocess
import com.kazurayam.subprocessj.Subprocess.CompletedProcess

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Test
import org.openqa.selenium.WebDriver

import io.github.bonigarcia.wdm.WebDriverManager

class FirefoxDriverFactoryTest {

	LaunchedFirefoxDriver launched

	@BeforeClass
	static void beforeClass() {
		// GitHub Personal Access Token
		Subprocess subprocess = new Subprocess()
		CompletedProcess cp = subprocess.run(Arrays.asList(
				"security", "find-internet-password",
				"-s", "github.com", "-a", "kazurayam",
				"-w"))
		assert cp.returncode() == 0
		String GHT = cp.stdout().get(0).trim()
		WebDriverManager.firefoxdriver().gitHubToken(GHT).setup()
	}

	@Before
	void setup() {
		launched = null
	}

	@After
	void teardown() {
		if (launched != null) {
			if (launched.getDriver() != null) {
				launched.getDriver().quit()
			}
			launched = null
		}
	}

	@Test
	void test_newFirefoxDriver_noArg() {
		FirefoxDriverFactory factory = FirefoxDriverFactory.newFirefoxDriverFactory()
		launched = factory.newFirefoxDriver()
		assert launched != null
	}

	/**
	 * Instantiate a FirefoxDriver to open a Firefox browser specifying a user profile "Picasso"
	 *
	 */
	@Ignore
	@Test
	void test_newFirefoxDriverWithProfile() {
		FirefoxDriverFactory factory = FirefoxDriverFactory.newInstance()
		WebDriver driver = factory.newFirefoxDriverWithProfile('Picasso')
		assertThat(driver, is(notNullValue()))

		FirefoxOptions options = factory.getEmployedOptions()
		assertNotNull(options)
		println("options: ${options.toString()}")

		println("ChromeDriver has been instantiated with profile Picasso")
		driver.navigate().to('http://example.com/')
		driver.quit()
	}
}
