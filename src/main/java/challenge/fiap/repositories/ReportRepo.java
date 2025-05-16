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

        var query = "INSERT INTO RELATORIOS ( " +
                "ID," +
                "TITLE," +
                "INFO," +
                "GENERATION_DATE," +
                "REPORT_TYPE," +
                "PERIOD_INICIAL_DATE," +
                "PERIOD_FINAL_DATE," +
                "TOTAL_NUMBER_OF_FAILURES," +
                "NUMBER_OF_FAILURES_PENDENTE," +
                "NUMBER_OF_FAILURES_CANCELADA," +
                "NUMBER_OF_FAILURES_CONCLUIDA," +
                "NUMBER_OF_FAILURES_ELETRICA," +
                "NUMBER_OF_FAILURES_MECANICA," +
                "NUMBER_OF_FAILURES_SOFTWARE," +
                "NUMBER_OF_FAILURES_OUTRO," +
                "DELETED )" +
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

            if (object.getReportType() == REPORT_TYPE.GERAL) {
                var FREPO = new FailureRepo();
                var failures = FREPO.getOffReport();
                failures.forEach(f -> f.setOnGeneralReport(true));
                FREPO.updateFailureList(failures);
            }

        } catch (SQLException e) {
            LOGGER.error("Erro ao adicionar relatório: {}", e);
            throw new RuntimeException("Erro ao adicionar relatório", e);
        }
    }

    @Override
    public List<Report> get() {
        var reportList = new ArrayList<Report>();
        var query = "SELECT * FROM RELATORIOS WHERE DELETED = 0";

        LOGGER.info("Buscando relatorios não deleteds no banco de dados.");

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
    public Optional<Report> getById(UUID ID) {
        var query = "SELECT * FROM RELATORIOS WHERE DELETED = 0 AND ID = ?";

        LOGGER.info("Buscando relatorio ativo por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var report = createReport(result);

                LOGGER.info("Relatorio por ID encontrado: {}", ID);
                return Optional.of(report);
            } else {
                LOGGER.warn("Relatorio não encontrado: {}", ID);
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar relatorio por ID: {}", ID);
            throw new RuntimeException("Erro ao recuperar relatorio");
        }
    }

    @Override
    public void updateById(UUID ID, Report object) {
        Report report;
        if (getById(ID).isPresent()) {
            report = getById(ID).get();
        } else {
            return;
        }

        var query = "UPDATE RELATORIOS SET (" +
                "TITLE = ?," +
                "INFO = ?," +
                "GENERATION_DATE = ?," +
                "REPORT_TYPE = ?," +
                "PERIOD_INICIAL_DATE = ?," +
                "PERIOD_FINAL_DATE = ?," +
                "TOTAL_NUMBER_OF_FAILURES = ?," +
                "NUMBER_OF_FAILURES_PENDENTE = ?," +
                "NUMBER_OF_FAILURES_CANCELADA = ?," +
                "NUMBER_OF_FAILURES_CONCLUIDA = ?," +
                "NUMBER_OF_FAILURES_MECANICA = ?," +
                "NUMBER_OF_FAILURES_ELETRICA = ?," +
                "NUMBER_OF_FAILURES_SOFTWARE = ?," +
                "NUMBER_OF_FAILURES_OUTRO = ?," +
                "DELETED = ?," +
                "WHERE ID = ? ";

        LOGGER.info("Atualizando relatorio ativo por ID no banco de dados.");

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

            LOGGER.info("Relatorio atualizado por ID: {}", ID);

        } catch (SQLException e) {
            LOGGER.error("Erro ao atualizar relatorio por ID: {}", ID);
            throw new RuntimeException("Erro ao atualizar relatorio");
        }
    }

    @Override
    public void removeById(UUID ID) {

        var query = "UPDATE RELATORIOS SET DELETED = 1 WHERE ID = ?";

        LOGGER.info("Removendo relatorio por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            stmt.executeUpdate();

            LOGGER.info("Relatorio removido por ID: {}", ID);

        } catch (SQLException e) {
            LOGGER.error("Erro ao remover relatorio por ID: {}", ID);
            throw new RuntimeException("Erro ao remover relatorio");
        }

    }

    @Override
    public List<Report> getAll() {
        var reportList = new ArrayList<Report>();
        var query = "SELECT * FROM RELATORIOS";

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
    public Optional<Report> getByIdAdmin(UUID ID) {
        var query = "SELECT * FROM RELATORIOS WHERE ID = ?";

        LOGGER.info("Buscando relatorio por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            var result = stmt.executeQuery();

            if (result.next()) {
                var report = createReport(result);

                LOGGER.info("Relatorio geral por ID encontrado: {}", ID);
                return Optional.of(report);
            } else {
                LOGGER.warn("Relatorio geral não encontrado: {}", ID);
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Erro ao recuperar relatorio geral por ID: {}", ID);
            throw new RuntimeException("Erro ao recuperar relatorio");
        }
    }

    @Override
    public void deleteById(UUID ID) {
        var query = "DELETE FROM RELATORIOS WHERE ID = ?";

        LOGGER.info("Deletando relatorio por ID no banco de dados.");

        try (var connection = DatabaseConfig.getConnection()) {
            var stmt = connection.prepareStatement(query);
            stmt.setString(1, ID.toString());
            stmt.executeUpdate();

            LOGGER.info("Relatorio DELETED por ID: {}", ID);

        } catch (SQLException e) {
            LOGGER.error("Erro ao deletar relatorio por ID: {}", ID);
            throw new RuntimeException("Erro ao deletar relatorio");
        }
    }

    @Override
    public void remove(Report object) {
        var ID = object.getId();
        removeById(ID);
    }

    @Override
    public void delete(Report object) {
        var ID = object.getId();
        deleteById(ID);
    }

    @Override
    public void update(Report oldObject, Report newObject) {
        var idOld = oldObject.getId();
        updateById(idOld, newObject);
    }

    private Report createReport(ResultSet result) throws SQLException {
        var report = new Report();
        report.setId(UUID.fromString(result.getString("ID")));
        report.setDeleted(result.getBoolean("DELETED"));

        report.setTitle(result.getString("ID"));
        report.setInfo(result.getString("INFO"));
        report.setReportType(REPORT_TYPE.fromString(result.getString("REPORT_TYPE")));
        report.setPeriodInicialDate(result.getTimestamp("PERIOD_INICIAL_DATE").toLocalDateTime());
        report.setPeriodFinalDate(result.getTimestamp("periodo_final").toLocalDateTime());
        report.setTotalNumberOfFailures(result.getInt("TOTAL_NUMBER_OF_FAILURES"));
        report.getNumberOfFailuresByStatus().put(FAILURE_STATUS.PENDENTE, result.getInt("NUMBER_OF_FAILURES_PENDENTE"));
        report.getNumberOfFailuresByStatus().put(FAILURE_STATUS.CANCELADA, result.getInt("NUMBER_OF_FAILURES_CANCELADA"));
        report.getNumberOfFailuresByStatus().put(FAILURE_STATUS.CONCLUIDA, result.getInt("NUMBER_OF_FAILURES_CONCLUIDA"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.MECANICA, result.getInt("NUMBER_OF_FAILURES_MECANICA"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.ELETRICA, result.getInt("NUMBER_OF_FAILURES_ELETRICA"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.SOFTWARE, result.getInt("NUMBER_OF_FAILURES_SOFTWARE"));
        report.getNumberOfFailuresByType().put(FAILURE_TYPE.OUTRO, result.getInt("NUMBER_OF_FAILURES_OUTRO"));

        return report;
    }
}
