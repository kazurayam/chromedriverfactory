package com.kazurayam.webdriverfactory.firefox

import org.openqa.selenium.firefox.FirefoxOptions

class FirefoxOptionsBuilderImpl extends FirefoxOptionsBuilder {

	FirefoxOptionsBuilderImpl() {}

	FirefoxOptionsBuilderImpl(Map<String, Object> preferences) {
		this.preferences = preferences
	}

	/**
	 *
	 */
	@Override
	FirefoxOptions build() {
		FirefoxOptions options = new FirefoxOptions()

		// set location of the Chrome Browser's binary
		options.setBinary(FirefoxDriverUtils.getFirefoxBinaryPath().toString());

		// set my chrome preferences
		//options.setExperimentalOption('prefs', preferences)

		// The following lines are copy&pasted from
		// https://github.com/SeleniumHQ/selenium/issues/4961

		return options
	}
}
