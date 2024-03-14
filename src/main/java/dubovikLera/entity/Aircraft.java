package dubovikLera.entity;

import java.util.Objects;

public class Aircraft {
    private Long id;
    private String mode;

    public Aircraft(Long id, String mode) {
        this.id = id;
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
               "id=" + id +
               ", mode='" + mode + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return Objects.equals(id, aircraft.id) && Objects.equals(mode, aircraft.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mode);
    }
}
