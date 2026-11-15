package controller;

import dao.CursoDAO;
import model.Curso;
import view.CursoView;

import java.util.Optional;

public class CursoController {
    public static void create() {
        Curso curso = CursoView.create();
        CursoDAO.create(curso, curso.getDisciplinas());
        System.out.println("Curso e disciplinas cadastrados com sucesso.");
    }

    public static void read() {
        Optional<Curso> curso = CursoDAO.read(CursoView.readCode());
        if (curso.isPresent()) {
            CursoView.show(curso.get());
        } else {
            System.out.println("Curso não encontrado.");
        }
    }

    public static void list() {
        CursoView.list(CursoDAO.list());
    }

    public static void update() {
        Optional<Curso> encontrado = CursoDAO.read(CursoView.readCode());
        if (!encontrado.isPresent()) {
            System.out.println("Curso não encontrado.");
            return;
        }
        if (CursoDAO.update(CursoView.update(encontrado.get()))) {
            System.out.println("Curso atualizado com sucesso.");
        }
    }

    public static void delete() {
        if (CursoDAO.delete(CursoView.readCode())) {
            System.out.println("Curso removido com sucesso.");
        } else {
            System.out.println("Curso não encontrado.");
        }
    }
}
