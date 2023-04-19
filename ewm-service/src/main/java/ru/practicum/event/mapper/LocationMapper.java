package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;
import ru.practicum.event.dto.LocationDto;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getLat(),
                locationDto.getLon()
        );
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }
}