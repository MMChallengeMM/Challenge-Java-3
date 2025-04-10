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

            LOGGER.info("Falhas não apagados encontradas.");

        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falhas: {}", e.getMessage());
            System.out.println("Erro ao recuperar falhas");
        }
        return failureList;
    }

    @Override
    public Failure getById(UUID id) {
        var query = "SELECT * FROM TB_FALHAS WHERE deleted = 0 && id = ?";

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
        var oldFailure = getById(id);
        var newFailure = oldFailure.replaceBy(object);

        var query = "UPDATE TB_FALHAS SET ";

        LOGGER.info("Buscando falha ativa por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar falha por id: {}", id);
            throw new RuntimeException("Erro ao recuperar falha");
        }
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
