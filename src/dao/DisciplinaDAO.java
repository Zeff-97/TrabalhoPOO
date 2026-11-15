package dao;

import database.DatabaseConnection;
import model.Curso;
import model.Disciplina;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DisciplinaDAO {
    public static void create(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas (codigo_disciplina, nome, carga_horaria, curso_codigo) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, disciplina.getCodigoDisciplina());
            statement.setString(2, disciplina.getNome());
            statement.setInt(3, disciplina.getCargaHoraria());
            statement.setInt(4, disciplina.getCurso().getCodigoCurso());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível cadastrar a disciplina.", e);
        }
    }

    public static Optional<Disciplina> read(int codigo) {
        String sql = "SELECT d.codigo_disciplina, d.nome, d.carga_horaria, " +
                "c.codigo_curso, c.nome AS curso_nome, c.turno FROM disciplinas d " +
                "JOIN cursos c ON c.codigo_curso = d.curso_codigo WHERE d.codigo_disciplina = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, codigo);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Curso curso = new Curso(result.getInt("codigo_curso"),
                            result.getString("curso_nome"), result.getString("turno"));
                    return Optional.of(new Disciplina(result.getInt("codigo_disciplina"),
                            result.getString("nome"), result.getInt("carga_horaria"), curso));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível consultar a disciplina.", e);
        }
    }

    public static List<Disciplina> listByCourse(int codigoCurso) {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT codigo_disciplina, nome, carga_horaria FROM disciplinas " +
                "WHERE curso_codigo = ? ORDER BY codigo_disciplina";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, codigoCurso);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Curso curso = new Curso();
                    curso.setCodigoCurso(codigoCurso);
                    disciplinas.add(new Disciplina(result.getInt("codigo_disciplina"),
                            result.getString("nome"), result.getInt("carga_horaria"), curso));
                }
            }
            return disciplinas;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar as disciplinas do curso.", e);
        }
    }

    public static List<Disciplina> list() {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT d.codigo_disciplina, d.nome, d.carga_horaria, " +
                "c.codigo_curso, c.nome AS curso_nome, c.turno FROM disciplinas d " +
                "JOIN cursos c ON c.codigo_curso = d.curso_codigo ORDER BY d.codigo_disciplina";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Curso curso = new Curso(result.getInt("codigo_curso"),
                        result.getString("curso_nome"), result.getString("turno"));
                disciplinas.add(new Disciplina(result.getInt("codigo_disciplina"),
                        result.getString("nome"), result.getInt("carga_horaria"), curso));
            }
            return disciplinas;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar as disciplinas.", e);
        }
    }

    public static boolean update(Disciplina disciplina) {
        int cursoAtual = findCourseCode(disciplina.getCodigoDisciplina());
        if (cursoAtual == 0) {
            return false;
        }
        if (cursoAtual != disciplina.getCurso().getCodigoCurso() && countByCourse(cursoAtual) == 1) {
            throw new IllegalStateException("A disciplina é a última do curso atual e não pode ser transferida.");
        }
        String sql = "UPDATE disciplinas SET nome = ?, carga_horaria = ?, curso_codigo = ? WHERE codigo_disciplina = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setString(1, disciplina.getNome());
            statement.setInt(2, disciplina.getCargaHoraria());
            statement.setInt(3, disciplina.getCurso().getCodigoCurso());
            statement.setInt(4, disciplina.getCodigoDisciplina());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar a disciplina.", e);
        }
    }

    public static boolean delete(int codigo) {
        int codigoCurso = findCourseCode(codigo);
        if (codigoCurso == 0) {
            return false;
        }
        if (countByCourse(codigoCurso) == 1) {
            throw new IllegalStateException("Não é possível remover a última disciplina de um curso.");
        }
        String sql = "DELETE FROM disciplinas WHERE codigo_disciplina = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, codigo);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível remover a disciplina.", e);
        }
    }

    private static int findCourseCode(int codigoDisciplina) {
        String sql = "SELECT curso_codigo FROM disciplinas WHERE codigo_disciplina = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, codigoDisciplina);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt("curso_codigo") : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível localizar o curso da disciplina.", e);
        }
    }

    private static int countByCourse(int codigoCurso) {
        String sql = "SELECT COUNT(*) FROM disciplinas WHERE curso_codigo = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, codigoCurso);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível verificar as disciplinas do curso.", e);
        }
    }
}
