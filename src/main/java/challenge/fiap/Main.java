package challenge.fiap;

import challenge.fiap.models.Failure;
import challenge.fiap.models.Report;

public class Main {
    public static void main(String[] args) {
        var falha = new Failure();
        var relatorio = new Report();

        System.out.println(falha.showDetails());
        System.out.println(relatorio.showDetails());
    }
}
