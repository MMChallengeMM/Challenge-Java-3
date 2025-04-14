package challenge.fiap.repositories;

import challenge.fiap.infrastructure.DatabaseConfig;
import challenge.fiap.models.FAILURE_STATUS;
import challenge.fiap.models.FAILURE_TYPE;
import challenge.fiap.models.REPORT_TYPE;
import challenge.fiap.models.Report;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class ReportRepo extends _BaseRepo implements _CrudRepo<Report> {
    @Override
    public void add(Report object) {

        var query = "INSERT INTO TB_RELATORIOS ( " +
                "ID," +
                "TITULO," +
                "INFORMACOES," +
                "DATA_GERACAO," +
                "TIPO_RELATORIO," +
                "PERIODO_INICIO," +
                "PERIODO_FIM," +
                "TOTAL_FALHAS" +
                "FALHAS_POR_STATUS_PENDENTE" +
                "FALHAS_POR_STATUS_RESOLVIDO" +
                "FALHAS_POR_STATUS_CONCLUIDO" +
                "FALHAS_POR_TIPO_ELETRICA" +
                "FALHAS_POR_TIPO_MECANICA" +
                "FALHAS_POR_TIPO_SOFTWARE" +
                "FALHAS_POR_TIPO_OUTRO" +
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
            stmt.setTimestamp(6, object.getReportType() == REPORT_TYPE.GERAL ? null : Timestamp.valueOf(object.getPeriod().getInicialDate()));
            stmt.setTimestamp(7, object.getReportType() == REPORT_TYPE.GERAL ? null : Timestamp.valueOf(object.getPeriod().getFinalDate()));
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
    public Report getByIdAdmin(UUID id) {
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

    @Override
    public void update(Report oldObject, Report newObject) {

    }
}
