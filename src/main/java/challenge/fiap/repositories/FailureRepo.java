package challenge.fiap.repositories;

import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;

import java.util.List;
import java.util.UUID;

public class FailureRepo extends _BaseRepo implements _CrudRepo<Failure> {
    @Override
    public void add(Failure object) {

    }

    @Override
    public List<Failure> get() {
        return List.of(
                new Failure("ovo", FAILURE_TYPE.ELETRICA),
                new Failure("carlos", FAILURE_TYPE.MECANICA),
                new Failure("molo", FAILURE_TYPE.OUTRO),
                new Failure("zebra", FAILURE_TYPE.OUTRO)
        );

    }

    @Override
    public Failure getById(UUID id) {
        return null;
    }

    @Override
    public void updateById(UUID id, Failure object) {

    }

    @Override
    public void removeById(UUID id) {

    }

    @Override
    public List<Failure> getAll() {
        return List.of();
    }

    @Override
    public Failure getByIdAdmin(UUID id) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public void remove(Failure object) {

    }

    @Override
    public void delete(Failure object) {

    }

    @Override
    public void update(Failure oldObject, Failure newObject) {

    }

    public void switchOnGeneralReport(List<Failure> failures) {

    }
}
