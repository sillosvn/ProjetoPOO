public class Recepcionista extends Funcionario implements Relatorio {
    public Recepcionista(String nome, String cpf, String genero, double salario) {
        super(nome, cpf, genero, salario);
    }

    @Override
    public double calcularPagamento() {
        return getSalario();
    }

    @Override
    public String gerarRelatorio() {
        return "- Nome: " + getNome() +
                " | CPF: " + getCpf() +
                " | Salário: R$ " + String.format("%.2f", getSalario());
    }

    @Override
    public String toString() {
        return getNome() + ";" + getCpf() + ";" + getGenero() + ";" + getSalario();
    }
}
