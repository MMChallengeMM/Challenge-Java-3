package challenge.fiap.models;

import java.util.Objects;

public class Admin extends User {
    private int acessLevel;

    public Admin() {
    }

    public int getAcessLevel() {
        return acessLevel;
    }

    public void setAcessLevel(int acessLevel) {
        this.acessLevel = acessLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return getAcessLevel() == admin.getAcessLevel();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAcessLevel());
    }

    @Override
    public String toString() {
        return "Admin{" +
                "acessLevel=" + acessLevel +
                "} " + super.toString();
    }
}
