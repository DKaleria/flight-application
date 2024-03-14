package dubovikLera.dao;

import dubovikLera.entity.Seat;
import dubovikLera.exception.DaoException;
import dubovikLera.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeatDao implements Dao<Long, Seat> {
    private final static SeatDao INSTANCE = new SeatDao();

    private final AircraftDao aircraftDao = AircraftDao.getInstance();


    public static SeatDao getInstance() {
        return INSTANCE;
    }

    private SeatDao() {

    }

    private final static String UPDATE_SQL = """
            update seat
            set aircraft_id = ?, seat_no = ?
            where id = ?
            """;


    private final static String FIND_ALL_SQL = """
            select s.id, s.aircraft_id, s.seat_no, a.id, a.mode
            from seat  s
            join aircraft a on a.id = s.aircraft_id
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where s.id = ?
            """;

    private static final String SAVE_SQL = """
            insert into seat
            (aircraft_id, seat_no)
            values (?,?)
            """;
    private static final String DELETE_SQL ="""
            delete from seat
            where id = ?;
            """;
    @Override
    public boolean update(Seat seat) {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setLong(1, seat.getAircraft().getId());
            statement.setString(2, seat.getSeatNo());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Seat buildSeat(ResultSet result) throws SQLException {
        return new Seat(
                result.getLong("id"),

                aircraftDao.findById(result.getLong("aircraft_id"),
                        result.getStatement().getConnection()).orElse(null),
                result.getString("seat_no")
        );
    }

    @Override
    public List<Seat> findAll() {
        try (
                var connection = ConnectionManager.get();
                var statement = connection.prepareStatement(FIND_ALL_SQL)
        ) {
            List<Seat> seats = new ArrayList<>();

            var result = statement.executeQuery();
            while (result.next()) {
                seats.add(buildSeat(result));
            }
            return seats;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Seat> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();

            Seat seat = null;
            if (result.next()) {
                seat = buildSeat(result);

            }

            return Optional.ofNullable(seat);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    @Override
    public Seat save(Seat seat) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, seat.getAircraft().getId());
            statement.setString(2, seat.getSeatNo());


            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next())
                seat.setId(keys.getLong("id"));
            return seat;
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
