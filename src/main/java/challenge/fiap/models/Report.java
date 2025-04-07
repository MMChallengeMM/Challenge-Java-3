package challenge.fiap.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Report extends _BaseEntity {
    private String title;
    private String info;
    private REPORT_TYPE reportType;
    private final LocalDateTime generationDate = LocalDateTime.now();
    private Period period = null;
    private int totalNumberOfFailures;
    private Map<FAILURE_STATUS, Integer> numberOfFailuresByStatus = new HashMap<>();
    private Map<FAILURE_TYPE, Integer> numberOfFailuresByType = new HashMap<>();

    @Override
    public String showDetails() {
        return toString();
    }

    public Report() {
    }

    public Report(String title, String info, REPORT_TYPE reportType) {
        this.title = title;
        this.info = info;
        this.reportType = reportType;

        numberOfFailuresByStatus.put(FAILURE_STATUS.PENDENTE, null);
        numberOfFailuresByStatus.put(FAILURE_STATUS.CANCELADA, null);
        numberOfFailuresByStatus.put(FAILURE_STATUS.CONCLUIDA, null);

        numberOfFailuresByType.put(FAILURE_TYPE.MECANICA, null);
        numberOfFailuresByType.put(FAILURE_TYPE.ELETRICA, null);
        numberOfFailuresByType.put(FAILURE_TYPE.SOFTWARE, null);
        numberOfFailuresByType.put(FAILURE_TYPE.OUTRO, null);
    }

    /**
     * Gera os dados do relatório e retorna o relatório completo.
     * @return O Relatório completo
     */
    public Report generateData() {
        // TODO: Fazer a lógica de gerar dados.

        switch (reportType) {
            case GERAL:
                System.out.println("GERAL");
                break;
            case PERIODO:
                System.out.println("PERIODO");
                break;
        }


        return this;
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

    public void setNumberOfFailuresByStatus(FAILURE_STATUS chave, int valor) {
        if (!numberOfFailuresByStatus.containsKey(chave)) {
            throw new IllegalArgumentException("Chave não permitida: " + chave);
        }
        numberOfFailuresByStatus.put(chave, valor);
    }

    public void setNumberOfFailuresByType(FAILURE_TYPE chave, int valor) {
        if (!numberOfFailuresByType.containsKey(chave)) {
            throw new IllegalArgumentException("Chave não permitida: " + chave);
        }
        numberOfFailuresByType.put(chave, valor);
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
                ", period=" + period +
                ", totalNumberOfFailures=" + totalNumberOfFailures +
                ", numberOfFailuresByStatus=" + numberOfFailuresByStatus +
                ", numberOfFailuresByType=" + numberOfFailuresByType +
                "} " + super.toString();
    }
}
