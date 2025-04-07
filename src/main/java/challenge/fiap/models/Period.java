package challenge.fiap.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Period {
    private LocalDateTime inicialDate;
    private LocalDateTime finalDate;

    public String getFormatted() {
        var dateFormatter = DateTimeFormatter.ofPattern("%d/%M/%a");

        var inicialDateToString = inicialDate.format(dateFormatter);
        var finalDateToString = finalDate.format(dateFormatter);

        return inicialDateToString + " até " + finalDateToString;
    }

    public Period() {
    }

    public Period(LocalDateTime inicialDate, LocalDateTime finalDate) {
        if (inicialDate == null || finalDate == null || finalDate.isBefore(inicialDate)) {
            throw new RuntimeException("Período inválido");
        }
        this.inicialDate = inicialDate;
        this.finalDate = finalDate;
    }

    public LocalDateTime getInicialDate() {
        return inicialDate;
    }

    public LocalDateTime getFinalDate() {
        return finalDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(getInicialDate(), period.getInicialDate()) && Objects.equals(getFinalDate(), period.getFinalDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInicialDate(), getFinalDate());
    }

    @Override
    public String toString() {
        return "Period{" +
                "inicialDate=" + inicialDate +
                ", finalDate=" + finalDate +
                '}';
    }
}
