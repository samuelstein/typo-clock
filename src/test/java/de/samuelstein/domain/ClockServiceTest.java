package de.samuelstein.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mockStatic;

class ClockServiceTest {
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Berlin");
    private final ClockService clockService = new ClockService(Locale.GERMAN, ZONE_ID);


    @ParameterizedTest
    @MethodSource("getTestdata")
    void testHourGeneration(OffsetTime mockedOffsetTime, String expectedTimeString) {
        try (final var offsetTimeMockedStatic = mockStatic(OffsetTime.class)) {
            offsetTimeMockedStatic.when(() -> OffsetTime.now(ZONE_ID)).thenReturn(mockedOffsetTime);
            assertThat(clockService.getTimeAsString()).isEqualTo(expectedTimeString);
        }
    }

    static Stream<Arguments> getTestdata() {
        final var zoneOffset = ZONE_ID.getRules().getOffset(LocalDateTime.now(ZONE_ID));
        return Stream.of(
                arguments(OffsetTime.of(0, 0, 0, 0, zoneOffset), "zwölf"),
                arguments(OffsetTime.of(12, 0, 0, 0, zoneOffset), "zwölf"),
                arguments(OffsetTime.of(13, 0, 0, 0, zoneOffset), "eins"),
                arguments(OffsetTime.of(2, 5, 0, 0, zoneOffset), "fünf nach zwei"),
                arguments(OffsetTime.of(3, 10, 0, 0, zoneOffset), "zehn nach drei"),
                arguments(OffsetTime.of(16, 15, 0, 0, zoneOffset), "viertel nach vier"),
                arguments(OffsetTime.of(16, 20, 0, 0, zoneOffset), "zwanzig nach vier"),
                arguments(OffsetTime.of(16, 25, 0, 0, zoneOffset), "fünf vor halb fünf"),
                arguments(OffsetTime.of(18, 30, 0, 0, zoneOffset), "halb sieben"),
                arguments(OffsetTime.of(18, 35, 0, 0, zoneOffset), "fünf nach halb sieben"),
                arguments(OffsetTime.of(18, 40, 0, 0, zoneOffset), "zehn nach halb sieben"),
                arguments(OffsetTime.of(19, 45, 0, 0, zoneOffset), "dreiviertel acht"),
                arguments(OffsetTime.of(19, 50, 0, 0, zoneOffset), "zehn vor acht"),
                arguments(OffsetTime.of(19, 55, 0, 0, zoneOffset), "fünf vor acht"));
    }

}