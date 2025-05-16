package challenge.fiap.repositories;

import challenge.fiap.infrastructure.DatabaseConfig;
import challenge.fiap.models.FAILURE_STATUS;
import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FailureRepo extends _BaseRepo implements _CrudRepo<Failure> {
    @Override
    public void add(Failure object) {

        var query = "INSERT INTO FALHAS (" +
                "ID," +
                "DESCRIPTION," +
                "FAILURE_STATUS," +
                "FAILURE_TYPE," +
                "GENERATION_DATE," +
                "DELETED," +
                "ON_GENERAL_REPORT)" +
                " VALUES (?, ?, ?, ?, ?, 0, ?)";

        LOGGER.info("Criando falha: {}", object.getId());

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, object.getId().toString());
            stmt.setString(2, object.getDescription());
            stmt.setString(3, object.getFailureStatus().toString());
            stmt.setString(4, object.getFailureType().toString());
            stmt.setTimestamp(5, Timestamp.valueOf(object.getGenerationDate()));
            stmt.setBoolean(6, object.isOnGeneralReport());
            stmt.executeUpdate();

            LOGGER.info("Falha adicionada com sucesso: {}", object.getId());

        } catch (SQLException e) {
            LOGGER.error("Erro ao adicionar falha: {}", e.getMessage());
            throw new RuntimeException("Erro ao adicionar falha");
        }
    }

    @Override
    public List<Failure> get() {

        var failureList = new ArrayList<Failure>();
        var query = "SELECT * FROM FALHAS WHERE DELETED = 0";

        LOGGER.info("Buscando falhas não deletadas no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(query);

            while (result.next()) {
                var failure = createFailure(result);
                failureList.add(failure);
            }

            LOGGER.info("Falhas ativas encontradas.");
            return failureList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falhas: {}", e.getMessage());
            throw new RuntimeException("Erro ao recuperar falhas");
        }
    }

    public List<Failure> getOffReport() {

        var failureList = new ArrayList<Failure>();
        var query = "SELECT * FROM FALHAS WHERE DELETED = 0 AND ON_GENERAL_REPORT = 0";

        LOGGER.info("Buscando falhas não deletadas a reportar no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(query);

            while (result.next()) {
                var failure = createFailure(result);
                failureList.add(failure);
            }

            LOGGER.info("Falhas ativas a reportar encontradas.");
            return failureList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falhas a reportar: {}", e.getMessage());
            throw new RuntimeException("Erro ao recuperar falhas a reportar");
        }
    }

    @Override
    public Optional<Failure> getById(UUID ID) {
        var query = "SELECT * FROM FALHAS WHERE DELETED = 0 AND ID = ?";

        LOGGER.info("Buscando falha ativa por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var failure = createFailure(result);

                LOGGER.info("Falha por ID encontrado: {}", ID);
                return Optional.of(failure);
            } else {
                LOGGER.warn("Falha não encontrada: {}", ID);
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falha por ID: {}", ID);
            throw new RuntimeException("Erro ao recuperar falha");
        }
    }


    @Override
    public void updateById(UUID ID, Failure object) {
        Failure failure;
        if (getById(ID).isPresent()) {
            failure = getById(ID).get();
        } else {
            return;
        }

        var query = "UPDATE FALHAS SET DESCRIPTION = ?, FAILURE_STATUS = ?, FAILURE_TYPE = ?, GENERATION_DATE = ?, DELETED = ?, ON_GENERAL_REPORT = ? WHERE ID = ? ";

        LOGGER.info("Atualizando falha ativa por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1,failure.getDescription());
            stmt.setString(2,failure.getFailureStatus().toString());
            stmt.setString(3,failure.getFailureType().toString());
            stmt.setTimestamp(4, Timestamp.valueOf(failure.getGenerationDate()));
            stmt.setBoolean(5,failure.isDeleted());
            stmt.setBoolean(6,failure.isOnGeneralReport());
            stmt.setString(7,failure.getId().toString());
            stmt.executeUpdate();

            LOGGER.info("Falha atualizada por ID: {}", ID);

        } catch (SQLException e) {
            LOGGER.error("Erro ao atualizar falha por ID: {}", ID);
            throw new RuntimeException("Erro ao atualizar falha");
        }
    }

    public void updateFailureList(List<Failure> failures) {
        for (var failure : failures) {
            updateById(failure.getId(), failure);
        }
    }

    @Override
    public void removeById(UUID ID) {

        var query = "UPDATE FALHAS SET DELETED = 1 WHERE ID = ?";

        LOGGER.info("Removendo falha por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            stmt.executeUpdate();

            LOGGER.info("Falha removida por ID: {}", ID);

        } catch (SQLException e) {
            LOGGER.error("Erro ao remover falha por ID: {}", ID);
            throw new RuntimeException("Erro ao remover falha");
        }
    }

    @Override
    public List<Failure> getAll() {
        var failureList = new ArrayList<Failure>();
        var query = "SELECT * FROM FALHAS";

        LOGGER.info("Buscando falhas no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(query);

            while (result.next()) {
                var failure = createFailure(result);
                failureList.add(failure);
            }

            LOGGER.info("Falhas encontradas.");
            return failureList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar todas as falhas: {}", e.getMessage());
            throw new RuntimeException("Erro ao recuperar falhas");
        }
    }

    @Override
    public Optional<Failure> getByIdAdmin(UUID ID) {
        var query = "SELECT * FROM FALHAS WHERE ID = ?";

        LOGGER.info("Buscando falha por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var failure = createFailure(result);

                LOGGER.info("Falha geral por ID encontrada: {}", ID);
                return Optional.of(failure);
            } else {
                LOGGER.warn("Falha geral não encontrada: {}", ID);
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falha geral por ID: {}", ID);
            throw new RuntimeException("Erro ao recuperar falha");
        }
    }

    @Override
    public void deleteById(UUID ID) {
        var query = "DELETE FROM FALHAS WHERE ID = ?";

        LOGGER.info("Deletando falha por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            stmt.executeUpdate();

            LOGGER.info("Falha deletada por ID: {}", ID);

        } catch (SQLException e) {
            LOGGER.error("Erro ao deletar falha por ID: {}", ID);
            throw new RuntimeException("Erro ao deletar falha");
        }
    }

    @Override
    public void remove(Failure object) {
        var ID = object.getId();
        removeById(ID);
    }

    @Override
    public void delete(Failure object) {
        var ID = object.getId();
        deleteById(ID);
    }

    @Override
    public void update(Failure oldObject, Failure newObject) {
        var idOld = oldObject.getId();
        updateById(idOld, newObject);
    }

    private Failure createFailure(ResultSet result) throws SQLException {
        var failure = new Failure();
        failure.setId(UUID.fromString(result.getString("ID")));
        failure.setDeleted(result.getBoolean("DELETED"));

        failure.setDescription(result.getString("DESCRIPTION"));
        failure.setFailureStatus(FAILURE_STATUS.valueOf(result.getString("FAILURE_STATUS")));
        failure.setFailureType(FAILURE_TYPE.valueOf(result.getString("FAILURE_TYPE")));
        failure.setGenerationDate((result.getTimestamp("GENERATION_DATE")).toLocalDateTime());
        failure.setOnGeneralReport(result.getBoolean("ON_GENERAL_REPORT"));

        return failure;
    }
}
