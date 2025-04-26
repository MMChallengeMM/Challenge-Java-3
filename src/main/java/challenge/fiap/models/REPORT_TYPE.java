package challenge.fiap.models;

public enum REPORT_TYPE {
    GERAL (1),
    PERIODO (2);

    private final int num;

    REPORT_TYPE(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public static REPORT_TYPE fromNumber(int num) {
        for (REPORT_TYPE tipo: REPORT_TYPE.values()) {
            if (tipo.getNum() == num) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Número inválido: %d".formatted(num));
    }

    public static REPORT_TYPE fromString(String text) {
        for (REPORT_TYPE tipo: REPORT_TYPE.values()) {
            if (tipo.toString().equals(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Texto inválido: %s".formatted(text));
    }
}
