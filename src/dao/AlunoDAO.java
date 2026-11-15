package dao;

import database.DatabaseConnection;
import model.Aluno;
import model.Curso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AlunoDAO {
    public static void create(Aluno aluno) {
        String sql = "INSERT INTO alunos (matricula, nome, idade, curso_codigo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setLong(1, aluno.getMatricula());
            statement.setString(2, aluno.getNome());
            statement.setInt(3, aluno.getIdade());
            statement.setInt(4, aluno.getCurso().getCodigoCurso());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível cadastrar o aluno.", e);
        }
    }

    public static Optional<Aluno> read(long matricula) {
        String sql = "SELECT a.matricula, a.nome, a.idade, c.codigo_curso, c.nome AS curso_nome, c.turno " +
                "FROM alunos a LEFT JOIN cursos c ON c.codigo_curso = a.curso_codigo WHERE a.matricula = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setLong(1, matricula);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Curso curso = null;
                    if (result.getObject("codigo_curso") != null) {
                        curso = new Curso(result.getInt("codigo_curso"),
                                result.getString("curso_nome"), result.getString("turno"));
                        curso.setDisciplinas(DisciplinaDAO.listByCourse(curso.getCodigoCurso()));
                    }
                    return Optional.of(new Aluno(result.getLong("matricula"), result.getString("nome"),
                            result.getInt("idade"), curso));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível consultar o aluno.", e);
        }
    }

    public static boolean update(long matriculaOriginal, Aluno aluno) {
        String sql = "UPDATE alunos SET matricula = ?, nome = ?, idade = ?, curso_codigo = ? WHERE matricula = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setLong(1, aluno.getMatricula());
            statement.setString(2, aluno.getNome());
            statement.setInt(3, aluno.getIdade());
            statement.setInt(4, aluno.getCurso().getCodigoCurso());
            statement.setLong(5, matriculaOriginal);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível atualizar o aluno.", e);
        }
    }

    public static boolean delete(long matricula) {
        String sql = "DELETE FROM alunos WHERE matricula = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setLong(1, matricula);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível remover o aluno.", e);
        }
    }
}
