package challenge.fiap.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.UUID;

public abstract class _BaseEntity<T> {

    protected Logger LOGGER = LogManager.getLogger(getClass());

    private UUID id = UUID.randomUUID();
    private boolean deleted = false;

    public abstract void updateAttributes(T object);

    public _BaseEntity() {
    }

    public _BaseEntity(UUID id, boolean deleted) {
        this.id = id;
        this.deleted = deleted;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _BaseEntity<?> that = (_BaseEntity<?>) o;
        return isDeleted() == that.isDeleted() && Objects.equals(LOGGER, that.LOGGER) && Objects.equals(getId(), that.getId());
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
