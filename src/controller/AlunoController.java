package controller;

import dao.AlunoDAO;
import model.Aluno;
import view.AlunoView;

import java.util.Optional;

public class AlunoController {
    public static void create() {
        Aluno aluno = AlunoView.create();
        if (aluno != null) {
            AlunoDAO.create(aluno);
            System.out.println("Aluno cadastrado com sucesso.");
        }
    }

    public static void read() {
        Optional<Aluno> aluno = AlunoDAO.read(AlunoView.readMatricula());
        if (aluno.isPresent()) {
            AlunoView.show(aluno.get());
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    public static void update() {
        Optional<Aluno> encontrado = AlunoDAO.read(AlunoView.readMatricula());
        if (!encontrado.isPresent()) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        Aluno atual = encontrado.get();
        if (AlunoDAO.update(atual.getMatricula(), AlunoView.update(atual))) {
            System.out.println("Aluno atualizado com sucesso.");
        }
    }

    public static void delete() {
        if (AlunoDAO.delete(AlunoView.readMatricula())) {
            System.out.println("Aluno removido com sucesso.");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }
}
