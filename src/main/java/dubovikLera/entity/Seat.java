package dubovikLera.entity;

import java.util.Objects;

public class Seat {
    private Long id;
    private Aircraft aircraft;
    private String seatNo;

    public Seat(Long id, Aircraft aircraft, String seatNo) {
        this.id = id;
        this.aircraft = aircraft;
        this.seatNo = seatNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    @Override
    public String toString() {
        return "Seat{" +
               "id=" + id +
               ", aircraft=" + aircraft +
               ", seatNo='" + seatNo + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(id, seat.id) && Objects.equals(aircraft, seat.aircraft) && Objects.equals(seatNo, seat.seatNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, aircraft, seatNo);
    }
}
