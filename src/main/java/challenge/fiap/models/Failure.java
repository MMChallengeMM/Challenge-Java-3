package challenge.fiap.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Failure extends _BaseEntity<Failure> {
    private String description = null;
    private LocalDateTime generationDate = LocalDateTime.now();
    private boolean onGeneralReport = false;
    private FAILURE_TYPE failureType;
    private FAILURE_STATUS failureStatus = FAILURE_STATUS.PENDENTE;

    public Failure() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public boolean isOnGeneralReport() {
        return onGeneralReport;
    }

    public void setOnGeneralReport(boolean onGeneralReport) {
        this.onGeneralReport = onGeneralReport;
    }

    public FAILURE_TYPE getFailureType() {
        return failureType;
    }

    public void setFailureType(FAILURE_TYPE failureType) {
        this.failureType = failureType;
    }

    public FAILURE_STATUS getFailureStatus() {
        return failureStatus;
    }

    public void setFailureStatus(FAILURE_STATUS failureStatus) {
        this.failureStatus = failureStatus;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Failure failure = (Failure) o;
        return isOnGeneralReport() == failure.isOnGeneralReport() && Objects.equals(getDescription(), failure.getDescription()) && Objects.equals(getGenerationDate(), failure.getGenerationDate()) && getFailureType() == failure.getFailureType() && getFailureStatus() == failure.getFailureStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription(), getGenerationDate(), isOnGeneralReport(), getFailureType(), getFailureStatus());
    }

    @Override
    public String toString() {
        return "Failure{" +
                "description='" + description + '\'' +
                ", generationDate=" + generationDate +
                ", onGeneralReport=" + onGeneralReport +
                ", failureType=" + failureType +
                ", failureStatus=" + failureStatus +
                "} " + super.toString();
    }
}
