package challenge.fiap.repositories;

import challenge.fiap.infrastructure.DatabaseConfig;
import challenge.fiap.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReportRepo extends _BaseRepo implements _CrudRepo<Report> {
    @Override
    public void add(Report object) {

        var query = "INSERT INTO TB_RELATORIOS ( " +
                "id," +
                "titulo," +
                "info," +
                "dt_grcao," +
                "tipo_relatorio," +
                "periodo_inicio," +
                "periodo_fim," +
                "total_falhas" +
                "falhas_por_sts_pendente" +
                "falhas_por_sts_cancelada" +
                "falhas_por_sts_concluida" +
                "falhas_por_tp_eletrica" +
                "falhas_por_tp_mecanica" +
                "falhas_por_tp_software" +
                "falhas_por_tp_outro" +
                "deletado )" +
                " VALUES " +
                "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";

        LOGGER.info("Criando relatório: {}", object.getId());

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, object.getId().toString());
            stmt.setString(2, object.getTitle());
            stmt.setString(3, object.getInfo());
            stmt.setTimestamp(4, Timestamp.valueOf(object.getGenerationDate()));
            stmt.setString(5, object.getReportType().toString());
            stmt.setTimestamp(6, object.getReportType() == REPORT_TYPE.GERAL ? null : Timestamp.valueOf(object.getPeriodInicialDate()));
            stmt.setTimestamp(7, object.getReportType() == REPORT_TYPE.GERAL ? null : Timestamp.valueOf(object.getPeriodFinalDate()));
            stmt.setInt(8, object.getTotalNumberOfFailures());
            stmt.setInt(9, object.getNumberOfFailuresByStatus().get(FAILURE_STATUS.PENDENTE));
            stmt.setInt(10, object.getNumberOfFailuresByStatus().get(FAILURE_STATUS.CANCELADA));
            stmt.setInt(11, object.getNumberOfFailuresByStatus().get(FAILURE_STATUS.CONCLUIDA));
            stmt.setInt(12, object.getNumberOfFailuresByType().get(FAILURE_TYPE.ELETRICA));
            stmt.setInt(13, object.getNumberOfFailuresByType().get(FAILURE_TYPE.MECANICA));
            stmt.setInt(14, object.getNumberOfFailuresByType().get(FAILURE_TYPE.SOFTWARE));
            stmt.setInt(15, object.getNumberOfFailuresByType().get(FAILURE_TYPE.OUTRO));
            stmt.executeUpdate();

            LOGGER.info("Relatório adicionado com sucesso: {}", object.getId());

        } catch (SQLException e) {
            LOGGER.error("Erro ao adicionar relatório: {}", e);
            throw new RuntimeException("Erro ao adicionar relatório", e);
        }
    }

    @Override
    public List<Report> get() {
        var reportList = new ArrayList<Report>();
        var query = "SELECT * FROM TB_RELATORIOS WHERE deleted = 0";

        LOGGER.info("Buscando relatorios não deletados no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(query);

            while (result.next()) {
                var report = createReport(result);
                reportList.add(report);
            }

            LOGGER.info("Relatorios ativos encontradas.");
            return reportList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar relatorios: {}", e.getMessage());
            throw new RuntimeException("Erro ao recuperar relatorios");
        }
    }

    @Override
    public Optional<Report> getById(UUID id) {
        var query = "SELECT * FROM TB_RELATORIOS WHERE deleted = 0 AND id = ?";

        LOGGER.info("Buscando relatorio ativo por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var report = createReport(result);

                LOGGER.info("Relatorio por id encontrado: {}", id);
                return Optional.of(report);
            } else {
                LOGGER.warn("Relatorio não encontrado: {}", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar relatorio por id: {}", id);
            throw new RuntimeException("Erro ao recuperar relatorio");
        }
    }

    @Override
    public void updateById(UUID id, Report object) {
        Report report;
        if (getById(id).isPresent()) {
            report = getById(id).get();
        } else {
            return;
        }

        var query = "UPDATE TB_RELATORIOS SET (" +
                "titulo = ?," +
                "info = ?," +
                "dt_grcao = ?," +
                "tipo_relatorio = ?," +
                "periodo_inicio = ?," +
                "periodo_fim = ?," +
                "total_falhas = ?," +
                "falhas_por_sts_pendente = ?," +
                "falhas_por_sts_cancelada = ?," +
                "falhas_por_sts_concluida = ?," +
                "falhas_por_tp_mecanica = ?," +
                "falhas_por_tp_eletrica = ?," +
                "falhas_por_tp_software = ?," +
                "falhas_por_tp_outro = ?," +
                "deleted = ?," +
                "WHERE id = ? ";

        LOGGER.info("Atualizando relatorio ativo por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);

            stmt.setString(1,report.getTitle());
            stmt.setString(2,report.getInfo());
            stmt.setTimestamp(3,Timestamp.valueOf(report.getGenerationDate()));
            stmt.setString(4,report.getReportType().toString());
            stmt.setTimestamp(5,Timestamp.valueOf(report.getPeriodInicialDate()));
            stmt.setTimestamp(6,Timestamp.valueOf(report.getPeriodFinalDate()));
            stmt.setInt(7,report.getTotalNumberOfFailures());
            stmt.setInt(8,report.getNumberOfFailuresByStatus().get(FAILURE_STATUS.PENDENTE));
            stmt.setInt(9,report.getNumberOfFailuresByStatus().get(FAILURE_STATUS.CANCELADA));
            stmt.setInt(10,report.getNumberOfFailuresByStatus().get(FAILURE_STATUS.CONCLUIDA));
            stmt.setInt(11,report.getNumberOfFailuresByType().get(FAILURE_TYPE.MECANICA));
            stmt.setInt(12,report.getNumberOfFailuresByType().get(FAILURE_TYPE.ELETRICA));
            stmt.setInt(13,report.getNumberOfFailuresByType().get(FAILURE_TYPE.SOFTWARE));
            stmt.setInt(14,report.getNumberOfFailuresByType().get(FAILURE_TYPE.OUTRO));
            stmt.setBoolean(15,(report.isDeleted()));
            stmt.setString(16,report.getId().toString());

            stmt.executeUpdate();

            LOGGER.info("Relatorio atualizado por id: {}", id);

        } catch (SQLException e) {
            LOGGER.error("Erro ao atualizar relatorio por id: {}", id);
            throw new RuntimeException("Erro ao atualizar relatorio");
        }
    }

    @Override
    public void removeById(UUID id) {

        var query = "UPDATE TB_RELATORIOS SET deleted = 1 WHERE id = ?";

        LOGGER.info("Removendo relatorio por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();

            LOGGER.info("Relatorio removido por id: {}", id);

        } catch (SQLException e) {
            LOGGER.error("Erro ao remover relatorio por id: {}", id);
            throw new RuntimeException("Erro ao remover relatorio");
        }

    }

    @Override
    public List<Report> getAll() {
        var reportList = new ArrayList<Report>();
        var query = "SELECT * FROM TB_RELATORIOS";

        LOGGER.info("Buscando relatorios no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.createStatement();
            var result = stmt.executeQuery(query);

            while (result.next()) {
                var report = createReport(result);
                reportList.add(report);
            }

            LOGGER.info("Relatorios encontrados.");
            return reportList;
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar todos os relatorios: {}", e.getMessage());
            throw new RuntimeException("Erro ao recuperar relatorios");
        }
    }

    @Override
    public Optional<Report> getByIdAdmin(UUID id) {
        var query = "SELECT * FROM TB_RELATORIOS WHERE id = ?";

        LOGGER.info("Buscando relatorio por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var report = createReport(result);

                LOGGER.info("Relatorio geral por id encontrado: {}", id);
                return Optional.of(report);
            } else {
                LOGGER.warn("Relatorio geral não encontrado: {}", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar relatorio geral por id: {}", id);
            throw new RuntimeException("Erro ao recuperar relatorio");
        }
    }

    @Override
    public void deleteById(UUID id) {
        var query = "DELETE FROM TB_RELATORIOS WHERE id = ?";

        LOGGER.info("Deletando relatorio por id no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();

            LOGGER.info("Relatorio deletado por id: {}", id);

        } catch (SQLException e) {
            LOGGER.error("Erro ao deletar relatorio por id: {}", id);
            throw new RuntimeException("Erro ao deletar relatorio");
        }
    }

    @Override
    public void remove(Report object) {
        var id = object.getId();
        removeById(id);
    }

    @Override
    public void delete(Report object) {
        var id = object.getId();
        deleteById(id);
    }

    @Override
    public void update(Report oldObject, Report newObject) {
        var idOld = oldObject.getId();
        updateById(idOld, newObject);
    }

    private Report createReport(ResultSet result) throws SQLException {
        var report = new Report();
        report.setId(UUID.fromString(result.getString("id")));
        report.setDeleted(result.getBoolean("deleted"));

        report.setTitle(result.getString("id"));
        report.setInfo(result.getString("info"));
        report.setReportType(REPORT_TYPE.fromString(result.getString("tipo_relatorio")));
        report.setPeriodInicialDate(result.getTimestamp("periodo_inicio").toLocalDateTime());
        report.setPeriodFinalDate(result.getTimestamp("periodo_final").toLocalDateTime());
        report.setTotalNumberOfFailures(result.getInt("total_falhas"));
        report.getNumberOfFailuresByStatus().put(FAILURE_STATUS.PENDENTE, result.getInt("falhas_por_sts_pendente"));
        report.getNumberOfFailuresByStatus().put(FAILURE_STATUS.CANCELADA, result.getInt("falhas_por_sts_cancelada"));
        report.getNumberOfFailuresByStatus().put(FAILURE_STATUS.CONCLUIDA, result.getInt("falhas_por_sts_concluida"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.MECANICA, result.getInt("falhas_por_tp_mecanica"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.ELETRICA, result.getInt("falhas_por_tp_eletrica"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.SOFTWARE, result.getInt("falhas_por_tp_software"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.OUTRO, result.getInt("falhas_por_tp_outro"));

        return report;
    }
}
