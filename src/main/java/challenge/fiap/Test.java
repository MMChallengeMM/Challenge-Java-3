package challenge.fiap;

import challenge.fiap.models.Failure;
import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;

public class Test {
    public static void main(String[] args) {
        var relatorio = new Report("Crro","bolo", REPORT_TYPE.GERAL);

        System.out.println(relatorio.generateData());
    }
}
