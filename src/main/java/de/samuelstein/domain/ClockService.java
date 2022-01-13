package de.samuelstein.domain;

import org.tinylog.Logger;

import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ClockService {
    private final ZoneId zoneId;
    private final NavigableMap<Integer, String> hoursGermanString = new TreeMap<>();
    private final NavigableMap<Integer, String> minutesGermanString = new TreeMap<>();


    public ClockService(Locale locale, ZoneId zoneId) {
        var hourLabels = ResourceBundle.getBundle("Hours", locale);
        var minutesLabels = ResourceBundle.getBundle("Minutes", locale);

        hourLabels.keySet().forEach(k -> hoursGermanString.put(Integer.valueOf(k), hourLabels.getString(k)));
        minutesLabels.keySet().forEach(k -> minutesGermanString.put(Integer.valueOf(k), minutesLabels.getString(k)));

        this.zoneId = zoneId;
        Logger.info("Loaded: '{}' labels for clock", locale.getDisplayName());
    }

    public String getTimeAsString() {
        final var now = OffsetTime.now(zoneId);

        return String.format("%s %s", getMinutesAsString(now.getMinute()), getHourAsString(now.getHour(), now.getMinute()));
    }

    private String getHourAsString(final int hour, final int minute) {
        var calculatedHour = hour;
        if (hour > 12) {
            calculatedHour -= 12;
        }
        if (minute > 20) {
            calculatedHour += 1;
        }
        return hoursGermanString.get(calculatedHour);
    }

    private String getMinutesAsString(final int minute) {
        return minutesGermanString.floorEntry(minute).getValue();
    }

}
