package dubovikLera.dao;

import dubovikLera.entity.Flight;
import dubovikLera.entity.Ticket;
import dubovikLera.exception.DaoException;
import dubovikLera.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight> {

    private final static FlightDao INSTANCE = new FlightDao();
    private final static String FIND_ALL_SQL = """
            select *
            from flight
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where id = ?
            """;

    @Override
    public boolean update(Flight ticket) {
        return false;
    }

    @Override
    public List<Flight> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Flight> flights = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                flights.add(buildFlight(result));
            }

            return flights;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        try(var connection = ConnectionManager.get()){
            return findById(id, connection);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Flight buildFlight(ResultSet result) throws SQLException {
        return new Flight(result.getLong("id"),
                result.getString("flight_no")

        );
    }

    public Optional<Flight> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Flight flight = null;
            if (result.next()) {
                flight = buildFlight(result);

            }

            return Optional.ofNullable(flight);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Ticket save(Flight ticket) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }
}
