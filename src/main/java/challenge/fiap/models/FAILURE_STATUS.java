package challenge.fiap.models;

public enum FAILURE_STATUS {
    PENDENTE(1),
    CANCELADA(2),
    CONCLUIDA(3);

    private final int num;

    FAILURE_STATUS(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public static FAILURE_STATUS fromNumber(int num) {
        for (FAILURE_STATUS tipo: FAILURE_STATUS.values()) {
            if (tipo.getNum() == num) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Número inválido: %d".formatted(num));
    }
}
