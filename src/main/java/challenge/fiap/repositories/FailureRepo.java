package challenge.fiap.repositories;

import challenge.fiap.infrastructure.DatabaseConfig;
import challenge.fiap.models.FAILURE_STATUS;
import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.Failure;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FailureRepo extends _BaseRepo implements _CrudRepo<Failure> {
    @Override
    public void add(Failure object) {

        var query = "INSERT INTO TB_FALHAS ( id_falha, desc_falha, status, tipo, dt_hr, deleted, emRelatorioGeral) VALUES (?, ?, ?, ?, ?, 0, ?)";

        LOGGER.info("Criando falha: {}", object.getId());

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, object.getId().toString());
            stmt.setString(2, object.getDescription());
            stmt.setString(3, object.getFailureStatus().toString());
            stmt.setString(4, object.getFailureType().toString());
            stmt.setDate(5, Date.valueOf(object.getGenerationDate().toString()));
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
        var query = "SELECT * FROM TB_FALHAS WHERE deleted = 0";

        LOGGER.info("Buscando falhas não deletadas no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(query);

            while (result.next()) {
                var failure = new Failure();
                failure.setId(UUID.fromString(result.getString("id_falha")));
                failure.setDeleted(result.getBoolean("deleted"));

                failure.setDescription(result.getString("desc_falha"));
                failure.setFailureStatus(FAILURE_STATUS.valueOf(result.getString("status")));
                failure.setFailureType(FAILURE_TYPE.valueOf(result.getString("tipo")));
                failure.setGenerationDate(LocalDateTime.from(result.getDate("dt_hr").toLocalDate()));
                failure.setOnGeneralReport(result.getBoolean("emRelatorioGeral"));
                failureList.add(failure);
            }

            LOGGER.info("Falhas ativas encontradas.");
            return failureList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falhas: {}", e.getMessage());
            throw new RuntimeException("Erro ao recuperar falhas");
        }
    }

    @Override
    public Failure getById(UUID id) {
        var query = "SELECT * FROM TB_FALHAS WHERE deleted = 0 && id_falha = ?";

        LOGGER.info("Buscando falha ativa por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var failure = new Failure();
                failure.setId(UUID.fromString(result.getString("id_falha")));
                failure.setDeleted(result.getBoolean("deleted"));

                failure.setDescription(result.getString("desc_falha"));
                failure.setFailureStatus(FAILURE_STATUS.valueOf(result.getString("status")));
                failure.setFailureType(FAILURE_TYPE.valueOf(result.getString("tipo")));
                failure.setGenerationDate(LocalDateTime.from(result.getDate("dt_hr").toLocalDate()));
                failure.setOnGeneralReport(result.getBoolean("emRelatorioGeral"));

                LOGGER.info("Falha por id encontrado: {}", id);
                return failure;
            } else {
                LOGGER.warn("Falha não encontrada: {}", id);
                throw new IllegalArgumentException("Falha não encontrada");
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falha por id: {}", id);
            throw new RuntimeException("Erro ao recuperar falha");
        }
    }

    @Override
    public void updateById(UUID id, Failure object) {
        var failure = getById(id).replaceBy(object);

        var query = "UPDATE TB_FALHAS SET id_falha = ?, desc_falha = ?, status = ?, tipo = ?, dt_hr = ?, deleted = ?, emRelatorioGeral = ? WHERE id_falha = ? ";

        LOGGER.info("Atualizando falha ativa por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1,failure.getId().toString());
            stmt.setString(2,failure.getDescription());
            stmt.setString(3,failure.getFailureStatus().toString());
            stmt.setString(4,failure.getFailureType().toString());
            stmt.setDate(5, Date.valueOf(failure.getGenerationDate().toLocalDate()));
            stmt.setBoolean(6,failure.isDeleted());
            stmt.setBoolean(7,failure.isOnGeneralReport());
            stmt.setString(8,failure.getId().toString());
            stmt.executeUpdate();

            LOGGER.info("Falha atualizada por id: {}", id);

        } catch (SQLException e) {
            LOGGER.error("Erro ao atualizar falha por id: {}", id);
            throw new RuntimeException("Erro ao atualizar falha");
        }
    }

    @Override
    public void removeById(UUID id) {

        var query = "UPDATE TB_FALHAS SET deleted = 1 WHERE id_falha = ?";

        LOGGER.info("Removendo falha por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();

            LOGGER.info("Falha removida por id: {}", id);

        } catch (SQLException e) {
            LOGGER.error("Erro ao remover falha por id: {}", id);
            throw new RuntimeException("Erro ao remover falha");
        }
    }

    @Override
    public List<Failure> getAll() {
        var failureList = new ArrayList<Failure>();
        var query = "SELECT * FROM TB_FALHAS";

        LOGGER.info("Buscando falhas no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(query);

            while (result.next()) {
                var failure = new Failure();
                failure.setId(UUID.fromString(result.getString("id_falha")));
                failure.setDeleted(result.getBoolean("deleted"));

                failure.setDescription(result.getString("desc_falha"));
                failure.setFailureStatus(FAILURE_STATUS.valueOf(result.getString("status")));
                failure.setFailureType(FAILURE_TYPE.valueOf(result.getString("tipo")));
                failure.setGenerationDate(LocalDateTime.from(result.getDate("dt_hr").toLocalDate()));
                failure.setOnGeneralReport(result.getBoolean("emRelatorioGeral"));
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
    public Failure getByIdAdmin(UUID id) {
        var query = "SELECT * FROM TB_FALHAS WHERE id_falha = ?";

        LOGGER.info("Buscando falha por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var failure = new Failure();
                failure.setId(UUID.fromString(result.getString("id_falha")));
                failure.setDeleted(result.getBoolean("deleted"));

                failure.setDescription(result.getString("desc_falha"));
                failure.setFailureStatus(FAILURE_STATUS.valueOf(result.getString("status")));
                failure.setFailureType(FAILURE_TYPE.valueOf(result.getString("tipo")));
                failure.setGenerationDate(LocalDateTime.from(result.getDate("dt_hr").toLocalDate()));
                failure.setOnGeneralReport(result.getBoolean("emRelatorioGeral"));

                LOGGER.info("Falha geral por id encontrada: {}", id);
                return failure;
            } else {
                LOGGER.warn("Falha geral não encontrada: {}", id);
                throw new IllegalArgumentException("Falha não encontrada");
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falha geral por id: {}", id);
            throw new RuntimeException("Erro ao recuperar falha");
        }
    }

    @Override
    public void deleteById(UUID id) {
        var query = "DELETE FROM TB_FALHAS WHERE id_falha = ?";

        LOGGER.info("Deletando falha por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();

            LOGGER.info("Falha deletada por id: {}", id);

        } catch (SQLException e) {
            LOGGER.error("Erro ao deletar falha por id: {}", id);
            throw new RuntimeException("Erro ao deletar falha");
        }
    }

    @Override
    public void remove(Failure object) {
        var id = object.getId();
        removeById(id);
    }

    @Override
    public void delete(Failure object) {
        var id = object.getId();
        deleteById(id);
    }

    @Override
    public void update(Failure oldObject, Failure newObject) {
        var idOld = oldObject.getId();
        updateById(idOld, newObject);
    }

    public void switchOnGeneralReport(List<Failure> failures) {

    }
}
