package dubovikLera.dao;

import dubovikLera.entity.Airport;
import dubovikLera.exception.DaoException;
import dubovikLera.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirportDao implements Dao<Long, Airport> {
    private final static AirportDao INSTANCE = new AirportDao();

    private AirportDao() {

    }


    private final static String UPDATE_SQL = """
            update airport
            set country = ?, city = ?
            where code = ?
            """;


    private final static String FIND_ALL_SQL = """
            select code, country,city
            from airport
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where code = ?
            """;

    private static final String SAVE_SQL = """
            insert into airport
            (code, country, city)
            values (?,?,?)
            """;
    private static final String DELETE_SQL ="""
            delete from airport
            where code = ?;
            """;
    public static AirportDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean update(Airport airport) {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setString(1,airport.getCode());
            statement.setString(2, airport.getCountry());
            statement.setString(3, airport.getCity());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public Airport buildSeat(ResultSet result) throws SQLException {
        return new Airport(
                result.getString("code"),
                result.getString("country"),
                result.getString("city")
        );
    }

    @Override
    public List<Airport> findAll() {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(FIND_ALL_SQL)
        ) {
            List<Airport> airports = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                airports.add(buildSeat(result));
            }
            return airports;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Airport> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Airport airport = null;
            if (result.next()) {
                airport = buildSeat(result);

            }

            return Optional.ofNullable(airport);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    @Override
    public Airport save(Airport airport) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, airport.getCode());
            statement.setString(2, airport.getCountry());
            statement.setString(3, airport.getCity());


            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next())
                airport.setCode(keys.getString("code"));
            return airport;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
