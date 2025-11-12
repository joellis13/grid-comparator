package com.jellisisland.test.automation.template.util;


import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

/**
 * Sets Serenity system properties at test startup to switch between local, Selenium Grid (non-containerized), or Moon.
 * This class uses a static initializer to apply changes before Serenity initializes drivers. It reads the property
 * "remote.execution" which can be set via -Dremote.execution=grid|moon|local
 */
public class WebDriverModeConfigurator {

    static {
        try {
            EnvironmentVariables env = SystemEnvironmentVariables.createEnvironmentVariables();
            String mode = System.getProperty("remote.execution", env.getProperty("remote.execution", "local"));
            if ("grid".equalsIgnoreCase(mode) || "moon".equalsIgnoreCase(mode)) {
                String remoteUrl = System.getProperty("remote.webdriver.url", env.getProperty("remote.webdriver.url"));
                if (remoteUrl != null && !remoteUrl.isEmpty()) {
                    // Tell Serenity to use remote WebDriver and set the remote URL
                    System.setProperty("webdriver.driver", "remote");
                    System.setProperty("webdriver.remote.url", remoteUrl);
                    // For Moon we may want to provide Chrome version capability via system property
                    String chromeVersion = System.getProperty("chrome.version", env.getProperty("chrome.version"));
                    if (chromeVersion != null && !chromeVersion.isEmpty()) {
                        System.setProperty("chrome.version", chromeVersion);
                    }
                }
            }
        } catch (Exception e) {
            // best effort
        }
    }

    // Force class loading
    public static void init() { }
}

