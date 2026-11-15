package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {
    private static final String SERVER_URL =
            "jdbc:mysql://localhost:3306/?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DATABASE_URL =
            "jdbc:mysql://localhost:3306/faculdade?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    private DatabaseConnection() {
    }

    public static synchronized void initialize() {
        try (Connection serverConnection = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement statement = serverConnection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS faculdade");
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar/acessar o banco de dados faculdade.", e);
        }

        getConnection();
        createTables();
    }

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível conectar ao MySQL em localhost:3306.", e);
        }
    }

    private static void createTables() {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS cursos (" +
                    "codigo_curso INT PRIMARY KEY, nome VARCHAR(120) NOT NULL, turno VARCHAR(40) NOT NULL)");
            migrateCourseCode(statement);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS disciplinas (" +
                    "codigo_disciplina INT PRIMARY KEY, nome VARCHAR(120) NOT NULL, " +
                    "carga_horaria INT NOT NULL, curso_codigo INT NOT NULL)");
            migrateDisciplineCode(statement);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS alunos (" +
                    "matricula BIGINT PRIMARY KEY, nome VARCHAR(120) NOT NULL, idade INT NOT NULL, " +
                    "curso_codigo INT NULL)");
            ensureStudentCourseColumn(statement);
            statement.executeUpdate("ALTER TABLE alunos MODIFY COLUMN matricula BIGINT NOT NULL");
            statement.executeUpdate("ALTER TABLE cursos MODIFY COLUMN nome VARCHAR(120) NOT NULL, " +
                    "MODIFY COLUMN turno VARCHAR(40) NOT NULL");
            statement.executeUpdate("ALTER TABLE disciplinas MODIFY COLUMN nome VARCHAR(120) NOT NULL");
            statement.executeUpdate("ALTER TABLE alunos MODIFY COLUMN nome VARCHAR(120) NOT NULL");
            ensureForeignKey(statement, "disciplinas", "app_disciplina_curso_fk");
            ensureForeignKey(statement, "alunos", "app_aluno_curso_fk");
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível criar as tabelas do sistema.", e);
        }
    }

    private static void migrateCourseCode(Statement statement) throws SQLException {
        if (!columnExists("cursos", "codigo_curso") && columnExists("cursos", "codigo")) {
            dropCourseForeignKeys(statement, "disciplinas");
            dropCourseForeignKeys(statement, "alunos");
            statement.executeUpdate("ALTER TABLE cursos CHANGE COLUMN codigo codigo_curso INT NOT NULL");
        }
    }

    private static void migrateDisciplineCode(Statement statement) throws SQLException {
        if (!columnExists("disciplinas", "codigo_disciplina") && columnExists("disciplinas", "codigo")) {
            statement.executeUpdate("ALTER TABLE disciplinas CHANGE COLUMN codigo codigo_disciplina INT NOT NULL");
        }
    }

    private static void ensureStudentCourseColumn(Statement statement) throws SQLException {
        if (!columnExists("alunos", "curso_codigo")) {
            statement.executeUpdate("ALTER TABLE alunos ADD COLUMN curso_codigo INT NULL");
        }
    }

    private static void ensureForeignKey(Statement statement, String table, String constraintName)
            throws SQLException {
        try (PreparedStatement query = getConnection().prepareStatement(
                "SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA = DATABASE() " +
                        "AND TABLE_NAME = ? AND COLUMN_NAME = 'curso_codigo' " +
                        "AND REFERENCED_TABLE_NAME = 'cursos' AND REFERENCED_COLUMN_NAME = 'codigo_curso'")) {
            query.setString(1, table);
            try (ResultSet result = query.executeQuery()) {
                result.next();
                if (result.getInt(1) == 0) {
                    statement.executeUpdate("ALTER TABLE " + table + " ADD CONSTRAINT " + constraintName +
                            " FOREIGN KEY (curso_codigo) REFERENCES cursos(codigo_curso) ON DELETE RESTRICT");
                }
            }
        }
    }

    private static boolean columnExists(String table, String column) throws SQLException {
        try (PreparedStatement query = getConnection().prepareStatement(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() " +
                        "AND TABLE_NAME = ? AND COLUMN_NAME = ?")) {
            query.setString(1, table);
            query.setString(2, column);
            try (ResultSet result = query.executeQuery()) {
                result.next();
                return result.getInt(1) > 0;
            }
        }
    }

    private static void dropCourseForeignKeys(Statement statement, String table) throws SQLException {
        try (PreparedStatement query = getConnection().prepareStatement(
                "SELECT DISTINCT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND REFERENCED_TABLE_NAME = 'cursos'")) {
            query.setString(1, table);
            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    String constraintName = result.getString(1).replace("`", "``");
                    statement.executeUpdate("ALTER TABLE " + table + " DROP FOREIGN KEY `" + constraintName + "`");
                }
            }
        }
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new IllegalStateException("Não foi possível fechar a conexão com o banco.", e);
            }
        }
    }
}
