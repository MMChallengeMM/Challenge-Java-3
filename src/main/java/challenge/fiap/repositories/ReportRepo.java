package challenge.fiap.repositories;

import challenge.fiap.models.Report;

import java.util.List;
import java.util.UUID;

public class ReportRepo extends _BaseRepo implements _CrudRepo<Report> {
    @Override
    public void add(Report object) {

    }

    @Override
    public List<Report> get() {
        return List.of();
    }

    @Override
    public Report getById(UUID id) {
        return null;
    }

    @Override
    public void updateById(UUID id, Report object) {

    }

    @Override
    public void removeById(UUID id) {

    }

    @Override
    public List<Report> getAll() {
        return List.of();
    }

    @Override
    public Report getByIdAdmin() {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public void remove(Report object) {

    }

    @Override
    public void delete(Report object) {

    }
}
