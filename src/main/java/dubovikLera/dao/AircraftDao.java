package dubovikLera.dao;

import dubovikLera.entity.Aircraft;
import dubovikLera.exception.DaoException;
import dubovikLera.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AircraftDao  implements Dao<Long, Aircraft> {

    private final static AircraftDao INSTANCE = new AircraftDao();

    private AircraftDao() {

    }
    public static AircraftDao getInstance() {
        return INSTANCE;
    }


    private final static String FIND_ALL_SQL = """
           select id, mode
           from aircraft
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where id = ?
            """;

    private final static String SAVE_SQL = """
            insert into aircraft
            (id, mode)
            values (?,?)
            """;

    private final static String DELETE_SQL = """
            delete from aircraft
            where id = ?
            """;
    private final static String UPDATE_SQL = """
            update aircraft
            set mode = ?
            where id = ?
             """;


    @Override
    public boolean update(Aircraft aircraft) {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setString(1, aircraft.getMode());
            statement.setLong(2,aircraft.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    private Aircraft buildAircraft(ResultSet result) throws SQLException {
        return new Aircraft(result.getLong("id"),
                result.getString("mode")

        );
    }
    @Override
    public List<Aircraft> findAll() {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(FIND_ALL_SQL)
        ) {
            List<Aircraft> aircrafts = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                aircrafts.add(buildAircraft(result));
            }
            return aircrafts;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Aircraft> findById(Long id) {
        try(var connection = ConnectionManager.get()){
            return findById(id, connection);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }



    public Optional<Aircraft> findById(Long aircraftId, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, aircraftId);
            var result = statement.executeQuery();

            Aircraft aircraft = null;
            if (result.next()) {
                aircraft = buildAircraft(result);

            }

            return Optional.ofNullable(aircraft);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Aircraft save(Aircraft aircraft) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, aircraft.getId());
            statement.setString(2, aircraft.getMode());


            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next())
                aircraft.setId(keys.getLong("id"));
            return aircraft;
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
