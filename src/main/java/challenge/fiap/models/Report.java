package challenge.fiap.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Report extends _BaseEntity<Report> {
    private String title;
    private String info = null;
    private final LocalDateTime generationDate = LocalDateTime.now();
    private REPORT_TYPE reportType;
    private LocalDateTime periodInicialDate = null;
    private LocalDateTime periodFinalDate = null;
    private int totalNumberOfFailures;
    private final Map<FAILURE_STATUS, Integer> numberOfFailuresByStatus = new HashMap<>();
    private final Map<FAILURE_TYPE, Integer> numberOfFailuresByType = new HashMap<>();

    public Report() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setReportType(REPORT_TYPE reportType) {
        this.reportType = reportType;
    }

    public LocalDateTime getPeriodInicialDate() {
        return periodInicialDate;
    }

    public void setPeriodInicialDate(LocalDateTime periodInicialDate) {
        this.periodInicialDate = periodInicialDate;
    }

    public LocalDateTime getPeriodFinalDate() {
        return periodFinalDate;
    }

    public void setPeriodFinalDate(LocalDateTime periodFinalDate) {
        this.periodFinalDate = periodFinalDate;
    }

    public void setTotalNumberOfFailures(int totalNumberOfFailures) {
        this.totalNumberOfFailures = totalNumberOfFailures;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public REPORT_TYPE getReportType() {
        return reportType;
    }

    public int getTotalNumberOfFailures() {
        return totalNumberOfFailures;
    }

    public Map<FAILURE_STATUS, Integer> getNumberOfFailuresByStatus() {
        return numberOfFailuresByStatus;
    }

    public Map<FAILURE_TYPE, Integer> getNumberOfFailuresByType() {
        return numberOfFailuresByType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Report report = (Report) o;
        return getTotalNumberOfFailures() == report.getTotalNumberOfFailures() && Objects.equals(getTitle(), report.getTitle()) && Objects.equals(getInfo(), report.getInfo()) && getReportType() == report.getReportType() && Objects.equals(getGenerationDate(), report.getGenerationDate()) && Objects.equals(getNumberOfFailuresByStatus(), report.getNumberOfFailuresByStatus()) && Objects.equals(getNumberOfFailuresByType(), report.getNumberOfFailuresByType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTitle(), getInfo(), getReportType(), getGenerationDate(), getTotalNumberOfFailures(), getNumberOfFailuresByStatus(), getNumberOfFailuresByType());
    }

    @Override
    public String toString() {
        return "Report{" +
                "title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", reportType=" + reportType +
                ", generationDate=" + generationDate +
                ", totalNumberOfFailures=" + totalNumberOfFailures +
                ", numberOfFailuresByStatus=" + numberOfFailuresByStatus +
                ", numberOfFailuresByType=" + numberOfFailuresByType +
                "} " + super.toString();
    }
}
