package com.jellisisland.test.automation.template.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;

/**
 * Simple performance reporter that captures navigation timing from the browser and writes CSV lines
 * to build/test-results/perf-results.csv for later analysis.
 */
public class PerfReporter {

    private static final Path OUTPUT = Paths.get("build", "test-results", "perf-results.csv");

    public static void recordNavigationTiming(WebDriver driver) {
        if (!(driver instanceof JavascriptExecutor)) return;
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Try PerformanceNavigationTiming first (modern) then fallback
        String script = "var p = performance.getEntriesByType('navigation')[0] || performance.timing; if(!p) return null; return JSON.stringify(p);";
        Object raw = js.executeScript(script);
        if (raw == null) return;
        String json = raw.toString();

        try {
            Files.createDirectories(OUTPUT.getParent());
            boolean headerNeeded = !Files.exists(OUTPUT);
            try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT.toFile(), true))) {
                if (headerNeeded) {
                    out.println("timestamp,loadEventEnd,domContentLoadedEventEnd,fetchStart,redirectCount,json");
                }
                long ts = Instant.now().toEpochMilli();
                // Attempt to extract common fields via JS to avoid adding JSON parser dependency
                String extractScript = "(function(){var p=performance.getEntriesByType('navigation')[0]||performance.timing; if(!p) return [null,null,null,null,0]; return [p.loadEventEnd||null,p.domContentLoadedEventEnd||null,p.fetchStart||null,p.redirectCount||0];})()";
                Object arr = js.executeScript(extractScript);
                if (arr instanceof java.util.List) {
                    java.util.List<?> list = (java.util.List<?>) arr;
                    out.printf("%d,%s,%s,%s,%s,%s\n", ts,
                            safe(list.get(0)), safe(list.get(1)), safe(list.get(2)), safe(list.get(3)), escape(json));
                } else {
                    out.printf("%d,,,,, %s\n", ts, escape(json));
                }
            }
        } catch (Exception e) {
            // swallow - this is diagnostic only
        }
    }

    private static String safe(Object o) {
        return o == null ? "" : o.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return '"' + s.replace("\"", "'" ) + '"';
    }
}

