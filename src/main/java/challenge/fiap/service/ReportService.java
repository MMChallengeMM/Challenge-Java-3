package challenge.fiap.service;

import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;

import java.util.Arrays;

public class ReportService {

    public static boolean createReportCheck(Report report) {
        return report.getTitle() != null &&
                !Arrays.stream(REPORT_TYPE.values()).toList().contains(report.getReportType()) &&
                (report.getReportType() != REPORT_TYPE.PERIODO || (report.getPeriodInicialDate() != null && report.getPeriodFinalDate() != null)) &&
                (report.getReportType() == REPORT_TYPE.PERIODO || (report.getPeriodInicialDate() == null && report.getPeriodFinalDate() == null)) &&
                report.getTotalNumberOfFailures() >= 0 &&
                report.getNumberOfFailuresByStatus().values().stream().noneMatch(n -> n < 0) &&
                report.getNumberOfFailuresByType().values().stream().noneMatch(n -> n < 0);
    }
}
