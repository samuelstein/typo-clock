package de.samuelstein.main;

import de.samuelstein.domain.ClockService;
import org.tinylog.Logger;

import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var locale = new Locale("de", "DE");
        final var clockService = new ClockService(locale, ZoneId.of("CET"));

        Logger.info("Starting typo clock!");

        final var executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> Logger.info(clockService.getTimeAsString()), 0, 5, TimeUnit.MINUTES);
    }
}
