package view;

import dao.CursoDAO;
import model.Aluno;
import model.Curso;

import java.util.List;

public class AlunoView {
    public static Aluno create() {
        List<Curso> cursos = CursoDAO.list();
        if (cursos.isEmpty()) {
            System.out.println("Cadastre um curso antes de cadastrar alunos.");
            return null;
        }
        return new Aluno(ConsoleInput.readPositiveLong("Matrícula: "),
                ConsoleInput.readText("Nome: "),
                ConsoleInput.readPositiveInt("Idade: "),
                chooseCourse(cursos));
    }

    public static Aluno update(Aluno aluno) {
        System.out.println("Matrícula atual: " + aluno.getMatricula());
        List<Curso> cursos = CursoDAO.list();
        return new Aluno(ConsoleInput.readPositiveLong("Nova matrícula: "),
                ConsoleInput.readText("Novo nome: "),
                ConsoleInput.readPositiveInt("Nova idade: "),
                chooseCourse(cursos));
    }

    public static long readMatricula() {
        return ConsoleInput.readPositiveLong("Matrícula do aluno: ");
    }

    public static void show(Aluno aluno) {
        System.out.println("Matrícula: " + aluno.getMatricula());
        System.out.println("Nome: " + aluno.getNome());
        System.out.println("Idade: " + aluno.getIdade());
        if (aluno.getCurso() == null) {
            System.out.println("Curso: não vinculado (cadastro legado).");
            return;
        }
        System.out.println("Curso: " + aluno.getCurso().getNome() + " (" +
                aluno.getCurso().getCodigoCurso() + "), turno " + aluno.getCurso().getTurno());
        System.out.println("Disciplinas do curso:");
        for (model.Disciplina disciplina : aluno.getCurso().getDisciplinas()) {
            System.out.println(" - " + disciplina.getNome() + " (" + disciplina.getCargaHoraria() + " horas)");
        }
    }

    private static Curso chooseCourse(List<Curso> cursos) {
        System.out.println("Cursos disponíveis:");
        for (Curso curso : cursos) {
            System.out.println(curso.getCodigoCurso() + " - " + curso.getNome() + " (" + curso.getTurno() + ")");
        }
        while (true) {
            int codigo = ConsoleInput.readPositiveInt("Código do curso: ");
            for (Curso curso : cursos) {
                if (curso.getCodigoCurso() == codigo) {
                    return curso;
                }
            }
            System.out.println("Código de curso inválido.");
        }
    }
}
