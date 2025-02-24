public class Quarto implements Relatorio {
    private static final String[] tiposValidos = {"Solteiro", "Casal", "Suite", "Luxo"};
    private static final double[] precos = {100.0, 150.0, 200.0, 300.0};

    private int numero;
    private String tipo;
    private double preco;
    private boolean ocupado;

    public Quarto(int numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.preco = definirPreco(tipo);
        this.ocupado = false;
    }

    private double definirPreco(String tipo) {
        for (int i = 0; i < tiposValidos.length; i++) {
            if (tiposValidos[i].equals(tipo)) {
                return precos[i];
            }
        }
        throw new IllegalArgumentException("Tipo de quarto inválido.");
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPreco() {
        return preco;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    @Override
    public String gerarRelatorio() {
        String status = ocupado ? "Ocupado" : "Livre";
        return "- Número: " + getNumero() +
                " | Tipo: " + getTipo() +
                " | Preço: R$ " + String.format("%.2f", getPreco()) +
                " | Status: " + status;
    }

    @Override
    public String toString() {
        return getNumero() + ";" + getTipo() + ";" + getPreco() + ";" + isOcupado();
    }
}
