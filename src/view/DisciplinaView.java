package view;

import dao.CursoDAO;
import model.Curso;
import model.Disciplina;

import java.util.List;

public class DisciplinaView {
    public static Disciplina create() {
        List<Curso> cursos = CursoDAO.list();
        if (cursos.isEmpty()) {
            System.out.println("Cadastre um curso antes de cadastrar disciplinas.");
            return null;
        }
        return new Disciplina(ConsoleInput.readPositiveInt("Código da disciplina: "),
                ConsoleInput.readText("Nome: "),
                ConsoleInput.readPositiveInt("Carga horária (horas): "),
                chooseCourse(cursos));
    }

    public static Disciplina update(Disciplina disciplina) {
        List<Curso> cursos = CursoDAO.list();
        return new Disciplina(disciplina.getCodigoDisciplina(),
                ConsoleInput.readText("Novo nome: "),
                ConsoleInput.readPositiveInt("Nova carga horária (horas): "),
                chooseCourse(cursos));
    }

    public static int readCode() {
        return ConsoleInput.readPositiveInt("Código da disciplina: ");
    }

    public static void show(Disciplina disciplina) {
        System.out.println("Código: " + disciplina.getCodigoDisciplina());
        System.out.println("Nome: " + disciplina.getNome());
        System.out.println("Carga horária: " + disciplina.getCargaHoraria() + " horas");
        System.out.println("Curso: " + disciplina.getCurso().getNome() + " (" +
                disciplina.getCurso().getCodigoCurso() + ")");
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
