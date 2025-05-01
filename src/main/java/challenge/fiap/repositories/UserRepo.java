package challenge.fiap.repositories;

import challenge.fiap.infrastructure.DatabaseConfig;
import challenge.fiap.models.Admin;
import challenge.fiap.models.Operator;
import challenge.fiap.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepo extends _BaseRepo implements _CrudRepo<User>{
    @Override
    public void add(User object) {

        var query = "INSERT INTO TB_USUARIOS (" +
                "id," +
                "username," +
                "password," +
                "email," +
                "deleted," +
                "accessLevel," +
                "sector) " +
                "VALUES (?, ?, ?, ? , 0, ?, ?)";

        LOGGER.info("Criando usuario: {}", object.getId());

        try (var conn = DatabaseConfig.getConnection()) {

            var stmt = conn.prepareStatement(query);
            stmt.setString(1, object.getId().toString());
            stmt.setString(2, object.getUsername());
            stmt.setString(3, object.getPassword());
            stmt.setString(4, object.getEmail());
            stmt.setBoolean(5, object.isDeleted());
            stmt.setInt(6, object instanceof Admin ? ((Admin) object).getAcessLevel() : -1);
            stmt.setString(7, object instanceof Operator ? ((Operator) object).getSector() : null);

            LOGGER.info("Usuário criado com sucesso: {}", object.getId());

        } catch (SQLException e) {
            LOGGER.error("Erro ao adicionar usuário: {}", e.getMessage());
            throw new RuntimeException("Erro ao adicionar usuário");
        }
    }

    @Override
    public List<User> get() {

        var userList = new ArrayList<User>();
        var query = "SELECT * FROM TB_USUARIOS WHERE deleted = 0";

        LOGGER.info("Buscando usuário ativos no banco de dados.");

        try (var conn = DatabaseConfig.getConnection()) {

            var result = conn.createStatement().executeQuery(query);

            while (result.next()) {
                var user = createUser(result);
                userList.add(user);
            }

            LOGGER.info("Usuários ativos encontrados");
            return userList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar usuários ativos: {}", e.getMessage());
            throw new RuntimeException("Erro ao buscar usuários");
        }
    }


    @Override
    public Optional<User> getById(UUID id) {
        var query = "SELECT * FROM TB_USUARIOS WHERE deleted = 0 AND id = ?";

        LOGGER.info("Buscando usuário ativo por id: {}", id);

        try (var stmt = DatabaseConfig.getConnection().prepareStatement(query)) {
            stmt.setString(1, id.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var user = createUser(result);
                LOGGER.info("Usuário ativo encontrado por id: {}", id);
                return Optional.of(user);
            } else {
                LOGGER.warn("Usúario ativo não encontrado");
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar usuário ativo por id: {}", e.getMessage());
            throw new RuntimeException("Erro ao buscar usuário por id");
        }
    }

    @Override
    public void updateById(UUID id, User object) {
        User user;
        if (getById(id).isPresent()) {
            user = getById(id).get();
        } else {
            return;
        }

        var query = "UPDATE TB_USUARIOS SET username = ?, password = ?, email = ?, deleted = ?, acessLevel = ?, sector = ? WHERE id = ?";

        LOGGER.info("Atualizando usuário por id: {}", id);

        try (var stmt = DatabaseConfig.getConnection().prepareStatement(query)) {

            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getPassword());
            stmt.setString(3,user.getEmail());
            stmt.setBoolean(4,user.isDeleted());
            stmt.setInt(5,user instanceof Admin ? ((Admin) user).getAcessLevel() : -1);
            stmt.setString(6,user instanceof Operator ? ((Operator) user).getSector() : null);
            stmt.setString(7,id.toString());
            stmt.executeUpdate();

            LOGGER.info("Usuário atualizado com sucesso");
        } catch (SQLException e) {
            LOGGER.error("Erro ao atualizar usuário por id: {}", e.getMessage());
            throw new RuntimeException("Erro ao atualizar usuário por id");
        }

    }

    @Override
    public void removeById(UUID id) {

        var query = "UPDATE SET deleted = 1 WHERE id = ?";

        LOGGER.info("Removendo lógicamente o usuário: {}", id);

        try (var stmt = DatabaseConfig.getConnection().prepareStatement(query)){
            stmt.setString(1,id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Erro ao remover usuário por id: {}", e.getMessage());
            throw new RuntimeException("Erro ao remover usuário por id");
        }

    }

    @Override
    public List<User> getAll() {
        var userList = new ArrayList<User>();
        var query = "SELECT * FROM TB_USUARIOS";

        LOGGER.info("Buscando usuários no banco de dados.");

        try (var conn = DatabaseConfig.getConnection()) {

            var result = conn.createStatement().executeQuery(query);

            while (result.next()) {
                var user = createUser(result);
                userList.add(user);
            }

            LOGGER.info("Usuários encontrados");
            return userList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar usuários: {}", e.getMessage());
            throw new RuntimeException("Erro ao buscar usuários");
        }
    }

    @Override
    public Optional<User> getByIdAdmin(UUID id) {
        var query = "SELECT * FROM TB_USUARIOS WHERE id = ?";

        LOGGER.info("Buscando usuário por id: {}", id);

        try (var stmt = DatabaseConfig.getConnection().prepareStatement(query)) {
            stmt.setString(1, id.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var user = createUser(result);
                LOGGER.info("Usuário encontrado por id: {}", id);
                return Optional.of(user);
            } else {
                LOGGER.warn("Usúario não encontrado");
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar usuário por id: {}", e.getMessage());
            throw new RuntimeException("Erro ao buscar usuário por id");
        }
    }

    @Override
    public void deleteById(UUID id) {
        var query = "DELETE FROM TB_USUARIOS WHERE id = ?";

        LOGGER.info("Deletando usuário por id: {}", id);

        try (var stmt = DatabaseConfig.getConnection().prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();

            LOGGER.info("Usário deletado");

        } catch (SQLException e) {
            LOGGER.error("Erro ao deletar usuário por id: {}", e.getMessage());
            throw new RuntimeException("Erro ao deletar usuário por id");
        }
    }

    @Override
    public void remove(User object) {
        removeById(object.getId());
    }

    @Override
    public void delete(User object) {
        deleteById(object.getId());
    }

    @Override
    public void update(User oldObject, User newObject) {
        updateById(oldObject.getId(), newObject);
    }

    private User createUser(ResultSet result) throws SQLException {
        if (result.getInt("acessLevel") >= 0 && result.getString("sector") == null) {
            var adm = new Admin();
            adm.setId(UUID.fromString(result.getString("id")));
            adm.setAcessLevel(result.getInt("acessLevel"));
            adm.setDeleted(result.getBoolean("deleted"));
            adm.setEmail(result.getString("email"));
            adm.setUsername(result.getString("username"));
            adm.setPassword(result.getString("password"));
            return adm;
        } else {
            var operator = new Operator();
            operator.setId(UUID.fromString(result.getString("id")));
            operator.setSector(result.getString("sector"));
            operator.setDeleted(result.getBoolean("deleted"));
            operator.setEmail(result.getString("email"));
            operator.setUsername(result.getString("username"));
            operator.setPassword(result.getString("password"));
            return operator;
        }
    }
}
