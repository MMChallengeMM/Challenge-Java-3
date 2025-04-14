package challenge.fiap;

import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;
import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;
import challenge.fiap.repositories.FailureRepo;

public class Test {
    public static void main(String[] args) {
        var repo = new FailureRepo();
//        repo.add(new Failure("Queimou um fio", FAILURE_TYPE.ELETRICA));
//        repo.add(new Failure("Curto-circuito no painel de controle", FAILURE_TYPE.ELETRICA));
//        repo.add(new Failure("Falha no motor do ventilador", FAILURE_TYPE.MECANICA));
//        repo.add(new Failure("A aplicação travou ao carregar a página inicial", FAILURE_TYPE.SOFTWARE));
//        repo.add(new Failure("Problema de comunicação com o servidor externo", FAILURE_TYPE.OUTRO));
//
//        repo.add(new Failure("Sobrecarga na fiação elétrica", FAILURE_TYPE.ELETRICA));
//        repo.add(new Failure("Quebra da correia transportadora", FAILURE_TYPE.MECANICA));
//        repo.add(new Failure("Falha no sistema de autenticação de usuários", FAILURE_TYPE.SOFTWARE));
//        repo.add(new Failure("Falta de sincronização entre sistemas diferentes", FAILURE_TYPE.OUTRO));
//
//        repo.add(new Failure("Disjuntor desarmado devido a curto-circuito", FAILURE_TYPE.ELETRICA));
//        repo.add(new Failure("Dano na caixa de câmbio do veículo", FAILURE_TYPE.MECANICA));
//        repo.add(new Failure("Erro de cálculo na função de processamento", FAILURE_TYPE.SOFTWARE));
//        repo.add(new Failure("Problema na configuração do balanceamento de carga", FAILURE_TYPE.OUTRO));


        var relatorio = new Report("Crro","bolo", REPORT_TYPE.GERAL);

        System.out.println(relatorio.generateData());
        System.out.println(repo.get());
    }
}
