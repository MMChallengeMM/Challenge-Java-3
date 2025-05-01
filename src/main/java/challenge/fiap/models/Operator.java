package challenge.fiap.models;

import java.util.Objects;

public class Operator extends User {
    private String sector;

    @Override
    public String toString() {
        return "Operator{" +
                "sector='" + sector + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Operator operator = (Operator) o;
        return Objects.equals(getSector(), operator.getSector());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSector());
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Operator() {
    }
}
