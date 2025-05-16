package challenge.fiap;

import challenge.fiap.models.FAILURE_STATUS;
import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;
import challenge.fiap.repositories.FailureRepo;
import challenge.fiap.repositories.ReportRepo;

import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        var repo = new FailureRepo();
//        var f = new Failure();
//        f.setDescription("Queimou um fio da linha");
//        f.setFailureType(FAILURE_TYPE.ELETRICA);
//        var f1 = new Failure();
//        f1.setDescription("Quebrou a engrenagem do motor");
//        f1.setFailureType(FAILURE_TYPE.MECANICA);
//
//        var f2 = new Failure();
//        f2.setDescription("Curto-circuito na placa de circuito");
//        f2.setFailureType(FAILURE_TYPE.ELETRICA);
//
//        var f3 = new Failure();
//        f3.setDescription("Erro de null pointer no código");
//        f3.setFailureType(FAILURE_TYPE.SOFTWARE);
//
//        var f4 = new Failure();
//        f4.setDescription("Problema inesperado durante o processo");
//        f4.setFailureType(FAILURE_TYPE.OUTRO);
//
//        repo.add(f);
//        repo.add(f1);
//        repo.add(f2);
//        repo.add(f3);
//        repo.add(f4);

        var repo = new ReportRepo();

        var r = new Report();
        r.setTitle("Relatorio do Carls");
        r.setInfo("Relatorio criado por Carls");
        r.setReportType(REPORT_TYPE.GERAL);
        r.setTotalNumberOfFailures(new FailureRepo().getOffReport().size());
        var mapStatus = r.getNumberOfFailuresByStatus();
        mapStatus.put(FAILURE_STATUS.PENDENTE, 5);
        mapStatus.put(FAILURE_STATUS.CANCELADA, 5);
        mapStatus.put(FAILURE_STATUS.CONCLUIDA, 5);
        var mapType = r.getNumberOfFailuresByType();
        mapType.put(FAILURE_TYPE.MECANICA, 5);
        mapType.put(FAILURE_TYPE.ELETRICA, 5);
        mapType.put(FAILURE_TYPE.SOFTWARE, 5);
        mapType.put(FAILURE_TYPE.OUTRO, 5);


// Exemplo 1: Relatório de Manutenção
        Report r1 = new Report();
        r1.setTitle("Relatorio de Manutenção");
        r1.setInfo("Relatório gerado por Carls");
        r1.setReportType(REPORT_TYPE.GERAL);
        r1.setTotalNumberOfFailures(new FailureRepo().getOffReport().size());  // Mantém o total de falhas como o seu código original
        Map<FAILURE_STATUS, Integer> mapStatus1 = r1.getNumberOfFailuresByStatus();
        mapStatus1.put(FAILURE_STATUS.PENDENTE, 7);
        mapStatus1.put(FAILURE_STATUS.CANCELADA, 3);
        mapStatus1.put(FAILURE_STATUS.CONCLUIDA, 10);
        Map<FAILURE_TYPE, Integer> mapType1 = r1.getNumberOfFailuresByType();
        mapType1.put(FAILURE_TYPE.MECANICA, 8);
        mapType1.put(FAILURE_TYPE.ELETRICA, 4);
        mapType1.put(FAILURE_TYPE.SOFTWARE, 3);
        mapType1.put(FAILURE_TYPE.OUTRO, 5);

// Exemplo 2: Relatório de Falhas Críticas
        Report r2 = new Report();
        r2.setTitle("Relatório de Falhas Críticas");
        r2.setInfo("Gerado por Carls para falhas críticas");
        r2.setReportType(REPORT_TYPE.GERAL);
        r2.setTotalNumberOfFailures(new FailureRepo().getOffReport().size());  // Mantém o total de falhas como o seu código original
        Map<FAILURE_STATUS, Integer> mapStatus2 = r2.getNumberOfFailuresByStatus();
        mapStatus2.put(FAILURE_STATUS.PENDENTE, 2);
        mapStatus2.put(FAILURE_STATUS.CANCELADA, 1);
        mapStatus2.put(FAILURE_STATUS.CONCLUIDA, 8);
        Map<FAILURE_TYPE, Integer> mapType2 = r2.getNumberOfFailuresByType();
        mapType2.put(FAILURE_TYPE.MECANICA, 3);
        mapType2.put(FAILURE_TYPE.ELETRICA, 4);
        mapType2.put(FAILURE_TYPE.SOFTWARE, 2);
        mapType2.put(FAILURE_TYPE.OUTRO, 1);

// Exemplo 3: Relatório de Falhas em Produção
        Report r3 = new Report();
        r3.setTitle("Relatório de Falhas em Produção");
        r3.setInfo("Relatório do sistema de produção, gerado por Carls");
        r3.setReportType(REPORT_TYPE.GERAL);
        r3.setTotalNumberOfFailures(new FailureRepo().getOffReport().size());  // Mantém o total de falhas como o seu código original
        Map<FAILURE_STATUS, Integer> mapStatus3 = r3.getNumberOfFailuresByStatus();
        mapStatus3.put(FAILURE_STATUS.PENDENTE, 4);
        mapStatus3.put(FAILURE_STATUS.CANCELADA, 2);
        mapStatus3.put(FAILURE_STATUS.CONCLUIDA, 12);
        Map<FAILURE_TYPE, Integer> mapType3 = r3.getNumberOfFailuresByType();
        mapType3.put(FAILURE_TYPE.MECANICA, 6);
        mapType3.put(FAILURE_TYPE.ELETRICA, 3);
        mapType3.put(FAILURE_TYPE.SOFTWARE, 4);
        mapType3.put(FAILURE_TYPE.OUTRO, 5);

// Exemplo 4: Relatório de Testes
        Report r4 = new Report();
        r4.setTitle("Relatório de Testes");
        r4.setInfo("Relatório gerado a partir dos testes realizados por Carls");
        r4.setReportType(REPORT_TYPE.GERAL);
        r4.setTotalNumberOfFailures(new FailureRepo().getOffReport().size());  // Mantém o total de falhas como o seu código original
        Map<FAILURE_STATUS, Integer> mapStatus4 = r4.getNumberOfFailuresByStatus();
        mapStatus4.put(FAILURE_STATUS.PENDENTE, 3);
        mapStatus4.put(FAILURE_STATUS.CANCELADA, 4);
        mapStatus4.put(FAILURE_STATUS.CONCLUIDA, 6);
        Map<FAILURE_TYPE, Integer> mapType4 = r4.getNumberOfFailuresByType();
        mapType4.put(FAILURE_TYPE.MECANICA, 2);
        mapType4.put(FAILURE_TYPE.ELETRICA, 5);
        mapType4.put(FAILURE_TYPE.SOFTWARE, 4);
        mapType4.put(FAILURE_TYPE.OUTRO, 2);

// Exemplo 5: Relatório de Análise de Causa Raiz
        Report r5 = new Report();
        r5.setTitle("Relatório de Análise de Causa Raiz");
        r5.setInfo("Relatório elaborado para análise de falhas e suas causas");
        r5.setReportType(REPORT_TYPE.GERAL);
        r5.setTotalNumberOfFailures(new FailureRepo().getOffReport().size());  // Mantém o total de falhas como o seu código original
        Map<FAILURE_STATUS, Integer> mapStatus5 = r5.getNumberOfFailuresByStatus();
        mapStatus5.put(FAILURE_STATUS.PENDENTE, 8);
        mapStatus5.put(FAILURE_STATUS.CANCELADA, 1);
        mapStatus5.put(FAILURE_STATUS.CONCLUIDA, 9);
        Map<FAILURE_TYPE, Integer> mapType5 = r5.getNumberOfFailuresByType();
        mapType5.put(FAILURE_TYPE.MECANICA, 4);
        mapType5.put(FAILURE_TYPE.ELETRICA, 6);
        mapType5.put(FAILURE_TYPE.SOFTWARE, 3);
        mapType5.put(FAILURE_TYPE.OUTRO, 5);


        repo.add(r);
        repo.add(r1);
        repo.add(r2);
        repo.add(r3);
        repo.add(r4);


//        System.out.println(repo.getById(UUID.randomUUID()).orElse(new Failure()));

    }
}
