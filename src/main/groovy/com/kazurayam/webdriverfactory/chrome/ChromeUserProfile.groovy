package com.kazurayam.webdriverfactory.chrome

import com.kazurayam.webdriverfactory.UserProfile

import java.nio.file.Files
import java.nio.file.Path

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * A representation of a Chrome Profile instance.
 * 
 * @author kazurayam
 */
class ChromeUserProfile implements Comparable<ChromeUserProfile> {

	static final String PREFERENCES_FILE_NAME = 'Preferences'

	private final Path userDataDir
	private final ProfileDirectoryName profileDirectoryName

	private UserProfile userProfile
	private String preferences

	/**
	 * @param userDataDir "~/Library/Application Support/Google/Chrome/"
	 * @param profileName "Default", "Profile 1", "Profile 2", "Profile 3", ...
	 */
    ChromeUserProfile(Path userDataDir, ProfileDirectoryName profileDirectoryName) {
		Objects.requireNonNull(userDataDir)
		Objects.requireNonNull(profileDirectoryName)
		if (! Files.exists(userDataDir)) {
			throw new IllegalArgumentException("${userDataDir} is not found")
		}
		Path profilePath = userDataDir.resolve(profileDirectoryName.toString())
		if (! Files.exists(profilePath)) {
			throw new IllegalArgumentException("${p} is not found")
		}
		this.userDataDir = userDataDir
		this.profileDirectoryName = profileDirectoryName

		Path preferencesPath = profilePath.resolve(PREFERENCES_FILE_NAME)
		if (!Files.exists(preferencesPath)) {
			throw new IOException("${preferencesPath} is not found")
		}
		this.preferences = JsonOutput.prettyPrint(preferencesPath.toFile().text)

		Map m = new JsonSlurper().parseText(this.preferences)
		String name = m['profile']['name']
		this.userProfile = new UserProfile(name)
		assert this.userProfile != null
	}

	Path getUserDataDir() {
		return this.userDataDir
	}

	ProfileDirectoryName getProfileDirectoryName() {
		return this.profileDirectoryName
	}

	Path getProfileDirectory() {
		return this.getUserDataDir().resolve(this.getProfileDirectoryName().getName())
	}

	UserProfile getUserProfile() {
		return this.userProfile
	}

	String getPreferences() {
		return this.preferences
	}

	/**
	 * order by UserProfileName
	 *
	 * @param other
	 * @return
	 */
	@Override
	int compareTo(ChromeUserProfile other) {
		return this.getUserProfile() <=> other.getUserProfile()
	}

	@Override
	boolean equals(Object obj) {
		if (! obj instanceof ChromeUserProfile) {
			return false
		}
		ChromeUserProfile other = (ChromeUserProfile)obj
		return this.getUserDataDir() == other.getUserDataDir() &&
				this.getProfileDirectoryName() == other.getProfileDirectoryName()
	}

	@Override
	int hashCode() {
		int hash = 7
		hash = 31 * hash + (int) this.getUserDataDir().hashCode()
		hash = 31 * hash + (int) this.getProfileDirectoryName().hashCode()
		return hash
	}

	@Override
	String toString() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append("\"userProfile\":\"")
		sb.append(this.getUserProfile().toString())
		sb.append("\"")
		sb.append(",")
		//
		sb.append("\"userDataDir\":\"")
		sb.append(this.getUserDataDir().toString())
		sb.append("\"")
		sb.append(",")
		sb.append("\"profileDirectoryName\":\"")
		sb.append(this.getProfileDirectoryName())
		sb.append("\"")
		sb.append("}")
		//
		return JsonOutput.prettyPrint(sb.toString())
	}
}
