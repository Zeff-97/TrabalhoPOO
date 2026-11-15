package view;

import model.Curso;
import model.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class CursoView {
    public static Curso create() {
        Curso curso = new Curso(ConsoleInput.readPositiveInt("Código do curso: "),
                ConsoleInput.readText("Nome: "), ConsoleInput.readText("Turno: "));
        int quantidade = ConsoleInput.readPositiveInt("Quantidade de disciplinas (mínimo 1): ");
        List<Disciplina> disciplinas = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            System.out.println("Disciplina " + i + ":");
            disciplinas.add(new Disciplina(ConsoleInput.readPositiveInt("  Código: "),
                    ConsoleInput.readText("  Nome: "),
                    ConsoleInput.readPositiveInt("  Carga horária (horas): "), curso));
        }
        curso.setDisciplinas(disciplinas);
        return curso;
    }

    public static Curso update(Curso curso) {
        return new Curso(curso.getCodigoCurso(), ConsoleInput.readText("Novo nome: "),
                ConsoleInput.readText("Novo turno: "));
    }

    public static int readCode() {
        return ConsoleInput.readPositiveInt("Código do curso: ");
    }

    public static void show(Curso curso) {
        System.out.println("Código: " + curso.getCodigoCurso());
        System.out.println("Nome: " + curso.getNome());
        System.out.println("Turno: " + curso.getTurno());
        System.out.println("Disciplinas:");
        for (Disciplina disciplina : curso.getDisciplinas()) {
            System.out.println(" - " + disciplina.getCodigoDisciplina() + " - " + disciplina.getNome() +
                    " (" + disciplina.getCargaHoraria() + " horas)");
        }
    }

    public static void list(List<Curso> cursos) {
        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado.");
            return;
        }
        for (Curso curso : cursos) {
            System.out.println(curso.getCodigoCurso() + " - " + curso.getNome() + " (" + curso.getTurno() +
                    "), disciplinas: " + curso.getDisciplinas().size());
        }
    }
}
