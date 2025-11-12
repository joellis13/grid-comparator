package com.jellisisland.test.automation.template.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Cucumber hooks to record start/end timestamps for scenarios and write simple CSV entries
 */
public class PerfHooks {

    private static final Path OUTPUT = Paths.get("build", "test-results", "perf-results.csv");
    private static final Map<String, Instant> startTimes = new HashMap<>();

    @Before
    public void beforeScenario(Scenario scenario) {
        startTimes.put(scenario.getId(), Instant.now());
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            Instant start = startTimes.remove(scenario.getId());
            if (start == null) return;
            Instant end = Instant.now();
            long durationMs = Duration.between(start, end).toMillis();
            Files.createDirectories(OUTPUT.getParent());
            boolean headerNeeded = !Files.exists(OUTPUT);
            try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT.toFile(), true))) {
                if (headerNeeded) {
                    out.println("scenarioId,scenarioName,durationMs");
                }
                out.printf("%s,%s,%d\n", scenario.getId(), scenario.getName().replace(',', ' '), durationMs);
            }
        } catch (Exception e) {
            // ignore
        }
    }
}
