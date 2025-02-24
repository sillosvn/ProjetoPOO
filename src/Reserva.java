import java.util.HashSet;
import javax.swing.*;

public class Reserva implements Relatorio {
    private Hospede hospede;
    private HashSet<Quarto> quartos;

    public Reserva(Hospede hospede){
        this.hospede = hospede;
        this.quartos = new HashSet<>();
    }

    public Reserva(Hospede hospede, Quarto quarto) {
        this.hospede = hospede;
        this.quartos = new HashSet<>();
        adicionarQuarto(quarto);
    }

    public boolean adicionarQuarto(Quarto quarto) {
        try {
            if (quarto.isOcupado()) {
                throw new Exception("O quarto " + quarto.getNumero() + " já está ocupado!");
            }
            quartos.add(quarto);
            quarto.setOcupado(true);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void adicionarQuartoSemVerificar(Quarto quarto) {
        quartos.add(quarto);
        quarto.setOcupado(true);
    }

    @Override
    public String gerarRelatorio(){
        String relatorio = "Reserva realiza por: " + hospede.getNome() + "\n";
        relatorio += "CPF do Hóspede: " + hospede.getCpf() + "\n";
        relatorio += "Reservas realizadas:\n";

        for (Quarto quarto : quartos){
            relatorio += "Número: " + quarto.getNumero() + "| Tipo: " + quarto.getTipo() + " | Preço: " + String.format("%.2f", quarto.getPreco()) + "\n";
        }

        return relatorio;
    }

    @Override
    public String toString() {
        String dados = hospede.getNome() + ";" +
                hospede.getCpf() + ";";

        for (Quarto quarto : quartos) {
            dados += quarto.getNumero() + ";" +
                    quarto.getTipo() + ";" +
                    String.format("%.2f", quarto.getPreco()) + ";";
        }

        return dados;
    }
}