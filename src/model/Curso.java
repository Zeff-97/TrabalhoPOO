package model;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    private int codigoCurso;
    private String nome;
    private String turno;
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Curso() {
    }

    public Curso(int codigoCurso, String nome, String turno) {
        this.codigoCurso = codigoCurso;
        this.nome = nome;
        this.turno = turno;
    }

    public int getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(int codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
