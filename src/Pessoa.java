import java.util.HashSet;

public abstract class Pessoa {
    private String nome;
    private String cpf;
    private String genero;

    private static HashSet<String> cpfsRegistrados = new HashSet<>();

    public Pessoa(String nome, String cpf, String genero) {
        this.nome = nome;
        this.cpf = cpf;
        this.genero = genero;
        cpfsRegistrados.add(cpf);

    }

    public static boolean isCpfRegistrado(String cpf) {
        return cpfsRegistrados.contains(cpf);
    }

    public String getNome() {
        return nome;
    }

    public String getGenero() {
        return genero;
    }

    public String getCpf() {
        return cpf;
    }


    @Override
    public String toString() {
        return "- Nome: " + nome + " | CPF: " + cpf + " | Gênero: " + genero;
    }
}
