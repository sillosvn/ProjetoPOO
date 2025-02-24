public abstract class Funcionario extends Pessoa implements Pagamento {
    private double salario;

    public Funcionario(String nome, String cpf, String genero, double salario) {
        super(nome, cpf, genero);
        this.salario = salario;

    }

    public double getSalario() {
        return salario;
    }

    @Override
    public String toString() {
        return super.toString() + ";" + salario;
    }
}