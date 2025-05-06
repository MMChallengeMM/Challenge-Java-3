package challenge.fiap.models;

import java.util.Objects;
import java.util.UUID;

public abstract class _BaseEntity<T> {

    private UUID id = UUID.randomUUID();
    private boolean deleted = false;

    public void updateAttributes(T object) {
        try {
            for (var field : object.getClass().getDeclaredFields()) {
                if (field.getName().equals("id")) {
                    continue;
                }
                field.setAccessible(true);
                var newValue = field.get(object);
                if (newValue != null) {
                    field.set(this,newValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro ao atualizar atributos");
        }
    }

    public _BaseEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        _BaseEntity<?> that = (_BaseEntity<?>) o;
        return isDeleted() == that.isDeleted() && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), isDeleted());
    }

    @Override
    public String toString() {
        return "_BaseEntity{" +
                "id=" + id +
                ", deleted=" + deleted +
                '}';
    }
}
