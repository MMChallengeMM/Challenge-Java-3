package challenge.fiap.models;

public enum FAILURE_TYPE {
    MECANICA(1),
    ELETRICA(2),
    SOFTWARE(3),
    OUTRO(4);

    private final int num;

    FAILURE_TYPE(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public static FAILURE_TYPE fromNumber(int num) {
        for (FAILURE_TYPE tipo: FAILURE_TYPE.values()) {
            if (tipo.getNum() == num) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Número inválido: %d".formatted(num));
    }
}
