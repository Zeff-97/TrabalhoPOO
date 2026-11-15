package model;

public class Disciplina {
    private int codigoDisciplina;
    private String nome;
    private int cargaHoraria;
    private Curso curso;

    public Disciplina() {
    }

    public Disciplina(int codigoDisciplina, String nome, int cargaHoraria, Curso curso) {
        this.codigoDisciplina = codigoDisciplina;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.curso = curso;
    }

    public int getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(int codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
