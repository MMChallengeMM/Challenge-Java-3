package challenge.fiap.repositories;

import challenge.fiap.models.User;

import java.util.List;
import java.util.UUID;

public class UserRepo extends _BaseRepo implements _CrudRepo<User>{
    @Override
    public void add(User object) {

    }

    @Override
    public List<User> get() {
        return List.of();
    }

    @Override
    public User getById(UUID id) {
        return null;
    }

    @Override
    public void updateById(UUID id, User object) {

    }

    @Override
    public void removeById(UUID id) {

    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getByIdAdmin(UUID id) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public void remove(User object) {

    }

    @Override
    public void delete(User object) {

    }
}
