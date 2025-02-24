public class Gerente extends Funcionario implements Relatorio {
    private double bonus;

    public Gerente(String nome, String cpf, String genero, double salario, double bonus) {
        super(nome, cpf, genero, salario);
        this.bonus = bonus;
    }

    public double getBonus() {
        return bonus;
    }

    @Override
    public double calcularPagamento() {
        return getSalario() + getBonus();
    }


    @Override
    public String gerarRelatorio() {
        return "- Nome: " + getNome() +
                " | CPF: " + getCpf() +
                " | Salário: R$ " + String.format("%.2f", getSalario()) +
                " | Bônus: R$ " + String.format("%.2f", getBonus()) +
                " | Salário Total: R$ " + String.format("%.2f", calcularPagamento());
    }

    @Override
    public String toString() {
        return getNome() + ";" + getCpf() + ";" + getGenero() + ";" + getSalario() + ";" + getBonus();
    }
}
