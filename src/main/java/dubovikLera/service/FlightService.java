package dubovikLera.service;

import dubovikLera.dao.FlightDao;
import dubovikLera.dto.FlightDto;

import java.util.List;
import java.util.stream.Collectors;

public class FlightService {
    private static final FlightService INSTANCE = new FlightService();
    private final FlightDao flightDao = FlightDao.getInstance();

    private FlightService() {

    }

    public static FlightService getInstance() {
        return INSTANCE;
    }

    public List<FlightDto> findAll() {
        return flightDao.findAll().stream().map(flight -> new FlightDto(
                flight.getId(),
                "%s - %s -%s".formatted(
                        flight.getArrivalAirportCode(),
                        flight.getDepartureAirportCode(),
                        flight.getStatus())
        )).collect(Collectors.toList());
    }
}
