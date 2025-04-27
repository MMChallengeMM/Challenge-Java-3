package challenge.fiap.service;

import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;

import java.util.Arrays;

public class ReportService {

    public static boolean createReportCheck(Report report) {
        if (report.getTitle() == null) {
            return false;
        }

        if (Arrays.stream(REPORT_TYPE.values()).toList().contains(report.getReportType())) {
            return false;
        }

        if (report.getReportType() == REPORT_TYPE.PERIODO && report.getPeriod() == null) {
            return false;
        }

        if (report.getReportType() != REPORT_TYPE.PERIODO && report.getPeriod() != null) {
            return false;
        }

        if (report.getTotalNumberOfFailures() < 0 ) {
            return false;
        }

        return true;
    }
}
