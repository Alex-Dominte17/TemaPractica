package org.example;

import java.util.List;

public class Publication {
    private static final List<String> CITIES = List.of("Bucharest", "Cluj", "Iasi", "Timisoara");
    private static final List<String> DIRECTIONS = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW");
    private static final List<String> DATES = List.of("2.02.2023", "3.02.2023", "4.02.2023");

    private final int stationId;
    private final String city;
    private final int temp;
    private final double rain;
    private final int wind;
    private final String direction;
    private final String date;

    public Publication() {
        this.stationId = RandomUtils.randomInt(1, 100);
        this.city = RandomUtils.randomElement(CITIES);
        this.temp = RandomUtils.randomInt(-20, 40);
        this.rain = RandomUtils.randomDouble(0.0, 10.0);
        this.wind = RandomUtils.randomInt(0, 100);
        this.direction = RandomUtils.randomElement(DIRECTIONS);
        this.date = RandomUtils.randomElement(DATES);
    }

    @Override
    public String toString() {
        return String.format("{(stationid,%d);(city,\"%s\");(temp,%d);(rain,%.1f);(wind,%d);(direction,\"%s\");(date,%s)}",
                stationId, city, temp, rain, wind, direction, date);
    }
}
