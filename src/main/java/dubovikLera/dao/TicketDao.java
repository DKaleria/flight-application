package dubovikLera.dao;

import dubovikLera.dto.TicketFilter;
import dubovikLera.entity.Flight;
import dubovikLera.entity.FlightStatus;
import dubovikLera.entity.Ticket;
import dubovikLera.exception.DaoException;
import dubovikLera.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TicketDao implements Dao<Long, Ticket> {
    private final static TicketDao INSTANCE = new TicketDao();
    private final FlightDao flightDao = FlightDao.getInstance();

    private final static String SAVE_SQL = """
            insert into ticket
            (passport_no, passenger_name, flight_id, seat_no, cost)
            values (?,?,?,?,?);
            """;

    private final static String DELETE_SQL = """
            delete from ticket
            where id = ?;
            """;
    private final static String UPDATE_SQL = """
            update ticket
            set passport_no = ?, passenger_name = ?,
            flight_id = ?, seat_no = ?, cost = ?
            where id = ?
                    
             """;


    private final static String FIND_ALL_SQL = """
            select t.id, t.passport_no, t.passenger_name, t.flight_id, t.seat_no, t.cost  ,
            f.flight_no FROM ticket t
            join flight f on f.id = t.flight_id
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where t.id = ?;
            """;

    private final static String GET_MOST_FREQUENT_NAME_SQL =
            """
                    select passenger_name
                    from ticket
                    group by passenger_name
                    having count(*) > 1;
                    """;

    private final static String GET_PASSENGER_TICKET_COUNTS_SQL =
            """
                    select passenger_name, count(*) ticket_count
                    from ticket
                    group by passenger_name;
                        """;

    private final static String UPDATE_TICKET_BY_ID_SQL = """
            update ticket 
            set passport_no = ?, passenger_name = ?, seat_no = ?
            where id = ?
            """;

    private final static String UPDATE_FLIGHT_AND_TICKET_SQL = """
            begin;
            update flight
            set departure_date = ?, arrival_date = ?
            where id = ?;
            update ticket
            set flight_id = ?
            where flight_id = ?;           
            commit;
            """;


    public Ticket buildTicket(ResultSet result) throws SQLException {
     /*   var flight = new Flight(result.getLong("flight_id"),
                result.getString("flight_no"));
//                result.getTimestamp("departure_date").toLocalDateTime(),
//                result.getString("departure_airport_code"),
//
//                result.getTimestamp("arrival_date").toLocalDateTime(),
//                result.getString("arrival_airport_code"),
//                result.getInt("aircraft_id"),
//                FlightStatus.valueOf(result.getString("status")));


      */
        return new Ticket(
                result.getLong("id"),
                result.getString("passport_no"),
                result.getString("passenger_name"),
                flightDao.findById(
                        result.getLong("flight_id"),
                        result.getStatement().getConnection()).orElse(null),
                result.getString("seat_no"),
                result.getBigDecimal("cost"));

    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Ticket ticket = null;
            if (result.next()) {
                ticket = buildTicket(result);

            }

            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    public List<Ticket> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Ticket> tickets = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                tickets.add(buildTicket(result));
            }

            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    public List<Ticket> findAll(TicketFilter filter) {

        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if (filter.passengerName() != null) {
            parameters.add(filter.passengerName());
            whereSql.add("passenger_name = ?");
        }

        if (filter.seatNo() != null) {
            parameters.add("%" + filter.seatNo() + "%");
            whereSql.add("seat_no like ?");
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var where = whereSql.stream().collect(Collectors.joining(
                " and ",
                parameters.size() > 2 ? " where " : " ",
                " limit ? offset ?"
        ));

        String sql = FIND_ALL_SQL + where;


        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            List<Ticket> tickets = new ArrayList<>();

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(statement);

            var result = statement.executeQuery();
            while (result.next()) {
                tickets.add(buildTicket(result));
            }

            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ticket.getPassportNo());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeatNo());
            statement.setBigDecimal(5, ticket.getCost());

            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next())

                ticket.setId(keys.getLong("id"));
            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, ticket.getPassportNo());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeatNo());
            statement.setBigDecimal(5, ticket.getCost());
            statement.setLong(6, ticket.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    public static TicketDao getINSTANCE() {
        return INSTANCE;
    }

    private TicketDao() {

    }


    public List<String> getMostFrequentNames() {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(GET_MOST_FREQUENT_NAME_SQL)
        ) {
            var result = statement.executeQuery();
            List<String> names = new ArrayList<>();

            while (result.next()) {
                String name = result.getString("passenger_name");
                names.add(name);
            }
            return names;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Map<String, Integer> getPassengerTicketCounts() {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(GET_PASSENGER_TICKET_COUNTS_SQL)
        ) {
            var result = statement.executeQuery();
            Map<String, Integer> passengerTicketCounts = new HashMap<>();
            while (result.next()) {
                String name = result.getString("passenger_name");
                int count = result.getInt("ticket_count");
                passengerTicketCounts.put(name, count);
            }
            return passengerTicketCounts;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void updateTicketById(Long id, String passportNo, String passengerName, String seatNo) {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(UPDATE_TICKET_BY_ID_SQL)
        ) {
            statement.setString(1, passportNo);
            statement.setString(2, passengerName);
            statement.setString(3, seatNo);
            statement.setLong(4, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    public void updateFlightAndTicket(Long flightId, LocalDateTime departureDate,
                                      LocalDateTime arrivalDate, Long newFlightId) {
        Connection connection = null;
        try {
            connection = ConnectionManager.get();
            var statement = connection.prepareStatement(UPDATE_FLIGHT_AND_TICKET_SQL);

            connection.setAutoCommit(false);

            statement.setTimestamp(1, Timestamp.valueOf(departureDate));
            statement.setTimestamp(2, Timestamp.valueOf(arrivalDate));
            statement.setLong(3, flightId);
            statement.setLong(4, newFlightId);
            statement.setLong(5, flightId);

            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }


    }
}
