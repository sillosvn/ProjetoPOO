public class Hospede extends Pessoa implements Relatorio {
    private String celular;


    public Hospede(String nome, String cpf, String genero, String celular) {
        super(nome, cpf, genero);
        this.celular = celular;
    }

    public String getCelular() {
        return celular;
    }

    @Override
    public String gerarRelatorio() {
        return "- Nome: " + getNome() + " | CPF: " + getCpf() + " | Gênero: " + getGenero() + " | Celular: " + getCelular();
    }

    @Override
    public String toString() {
        return getNome() + ";" + getCpf() + ";" + getGenero() + ";" + getCelular();
    }
}