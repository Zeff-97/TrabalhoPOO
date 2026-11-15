package controller;

import dao.DisciplinaDAO;
import model.Disciplina;
import view.DisciplinaView;

import java.util.Optional;

public class DisciplinaController {
    public static void create() {
        Disciplina disciplina = DisciplinaView.create();
        if (disciplina != null) {
            DisciplinaDAO.create(disciplina);
            System.out.println("Disciplina cadastrada com sucesso.");
        }
    }

    public static void read() {
        Optional<Disciplina> disciplina = DisciplinaDAO.read(DisciplinaView.readCode());
        if (disciplina.isPresent()) {
            DisciplinaView.show(disciplina.get());
        } else {
            System.out.println("Disciplina não encontrada.");
        }
    }

    public static void update() {
        Optional<Disciplina> encontrada = DisciplinaDAO.read(DisciplinaView.readCode());
        if (!encontrada.isPresent()) {
            System.out.println("Disciplina não encontrada.");
            return;
        }
        if (DisciplinaDAO.update(DisciplinaView.update(encontrada.get()))) {
            System.out.println("Disciplina atualizada com sucesso.");
        }
    }

    public static void delete() {
        if (DisciplinaDAO.delete(DisciplinaView.readCode())) {
            System.out.println("Disciplina removida com sucesso.");
        } else {
            System.out.println("Disciplina não encontrada.");
        }
    }

    public static void list() {
        for (Disciplina disciplina : DisciplinaDAO.list()) {
            DisciplinaView.show(disciplina);
            System.out.println("---------------");
        }
    }
}
