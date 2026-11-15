package dao;

import database.DatabaseConnection;
import model.Curso;
import model.Disciplina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursoDAO {
    public static void create(Curso curso, List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            throw new IllegalArgumentException("Um curso deve ter ao menos uma disciplina.");
        }
        Connection connection = DatabaseConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement courseStatement = connection.prepareStatement(
                    "INSERT INTO cursos (codigo_curso, nome, turno) VALUES (?, ?, ?)");
                 PreparedStatement disciplineStatement = connection.prepareStatement(
                         "INSERT INTO disciplinas (codigo_disciplina, nome, carga_horaria, curso_codigo) " +
                                 "VALUES (?, ?, ?, ?)")) {
                courseStatement.setInt(1, curso.getCodigoCurso());
                courseStatement.setString(2, curso.getNome());
                courseStatement.setString(3, curso.getTurno());
                courseStatement.executeUpdate();

                for (Disciplina disciplina : disciplinas) {
                    disciplineStatement.setInt(1, disciplina.getCodigoDisciplina());
                    disciplineStatement.setString(2, disciplina.getNome());
                    disciplineStatement.setInt(3, disciplina.getCargaHoraria());
                    disciplineStatement.setInt(4, curso.getCodigoCurso());
                    disciplineStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível cadastrar o curso e suas disciplinas.", e);
        }
    }

    public static Optional<Curso> read(int codigo) {
        String sql = "SELECT codigo_curso, nome, turno FROM cursos WHERE codigo_curso = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, codigo);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(withDisciplines(new Curso(result.getInt("codigo_curso"),
                            result.getString("nome"), result.getString("turno"))));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível consultar o curso.", e);
        }
    }

    public static List<Curso> list() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT codigo_curso, nome, turno FROM cursos ORDER BY codigo_curso";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                cursos.add(withDisciplines(new Curso(result.getInt("codigo_curso"),
                        result.getString("nome"), result.getString("turno"))));
            }
            return cursos;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível listar os cursos.", e);
        }
    }

    public static boolean update(Curso curso) {
        String sql = "UPDATE cursos SET nome = ?, turno = ? WHERE codigo_curso = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setString(1, curso.getNome());
            statement.setString(2, curso.getTurno());
            statement.setInt(3, curso.getCodigoCurso());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar o curso.", e);
        }
    }

    public static boolean delete(int codigo) {
        Connection connection = DatabaseConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement studentCount = connection.prepareStatement(
                    "SELECT COUNT(*) FROM alunos WHERE curso_codigo = ?");
                 PreparedStatement deleteDisciplines = connection.prepareStatement(
                         "DELETE FROM disciplinas WHERE curso_codigo = ?");
                 PreparedStatement deleteCourse = connection.prepareStatement(
                         "DELETE FROM cursos WHERE codigo_curso = ?")) {
                studentCount.setInt(1, codigo);
                try (ResultSet result = studentCount.executeQuery()) {
                    if (result.next() && result.getInt(1) > 0) {
                        throw new IllegalStateException("O curso não pode ser removido enquanto possuir alunos vinculados.");
                    }
                }
                deleteDisciplines.setInt(1, codigo);
                deleteDisciplines.executeUpdate();
                deleteCourse.setInt(1, codigo);
                boolean removed = deleteCourse.executeUpdate() > 0;
                connection.commit();
                return removed;
            } catch (SQLException | IllegalStateException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível remover o curso.", e);
        }
    }

    private static Curso withDisciplines(Curso curso) {
        curso.setDisciplinas(DisciplinaDAO.listByCourse(curso.getCodigoCurso()));
        return curso;
    }
}
