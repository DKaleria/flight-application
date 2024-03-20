package dubovikLera;

import dubovikLera.dao.*;
import dubovikLera.dto.TicketFilter;
import dubovikLera.entity.Ticket;
import dubovikLera.utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
//        System.out.println(getTicketsByFlightId(8L));
//        System.out.println(getFlightsBetween(LocalDate.of(2020, 04, 01).atStartOfDay(),
//                LocalDate.of(2020, 8, 01).atStartOfDay()));
//        System.out.println();
//        checkMetaData();


        var ticketDao = TicketDao.getINSTANCE();
//        Ticket ticket = new Ticket();
//        ticket.setPassportNo("dfl23");
//        ticket.setPassengerName("Pavel");
//        ticket.setFlightId(4L);
//        ticket.setSeatNo("30");
//        ticket.setCost(BigDecimal.TEN);

//        System.out.println(ticketDao.save(ticket));
//        System.out.println(ticketDao.delete(58L));
//
//        var filter = new TicketFilter(null, "A1",
//                2, 3);
//        System.out.println(ticketDao.findAll(filter));

//        Ticket ticket = ticketDao.findById(5L).get();
//        System.out.println(ticket);
//        ticket.setSeatNo("40D");
//        System.out.println(ticketDao.update(ticket));
//        System.out.println(ticket);

//        System.out.println(ticketDao.findById(3L));

//        var flightDao = FlightDao.getInstance();
//        System.out.println(flightDao.findAll());

        System.out.println(ticketDao.getMostFrequentNames());
        System.out.println("--------------------------------");
        System.out.println(ticketDao.getPassengerTicketCounts());
        System.out.println("--------------------------------");
        ticketDao.updateTicketById(1L, "829345", "Антон Шишко", "C1");


        ticketDao.updateFlightAndTicket(2L, LocalDate.of(2025, 4, 10).atStartOfDay(),
                LocalDate.of(2025, 9, 2).atStartOfDay(), 2L
                );
        var seatDao = SeatDao.getInstance();
        System.out.println(seatDao.findById(2L));
        System.out.println("--------------------------------");

        var aircraftDao = AircraftDao.getInstance();
        System.out.println(aircraftDao.findById(1L));


        System.out.println("--------------------------------");
        var airportDao = AirportDao.getInstance();
        System.out.println(airportDao.findAll());
    }


    public static List<Long> getTicketsByFlightId(Long flightId) {
        List<Long> tickets = new ArrayList<>();
        String sql = """
                select * from ticket t
                where t.flight_id= ?;
                """;

        try (Connection connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {

            statement.setFetchSize(2);


            statement.setLong(1, flightId);
            var result = statement.executeQuery();
            while (result.next()) {
                tickets.add(result.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tickets;

    }

    public static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) {
        List<Long> flights = new ArrayList<>();
        String sql = """
                select * from flight f
                where departure_date between  ? and ?;
                """;

        try (Connection connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(start));
            statement.setTimestamp(2, Timestamp.valueOf(end));
            var result = statement.executeQuery();
            while (result.next()) {
                flights.add(result.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return flights;
    }

    public static void checkMetaData() throws SQLException {
        try (Connection connection = ConnectionManager.get()) {
            var metaData = connection.getMetaData();
            var catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                System.out.println(catalogs.getString(1));
            }

        }
    }
}
