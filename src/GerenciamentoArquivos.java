import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GerenciamentoArquivos {

    // LER
    private static ArrayList<String[]> lerCSV(String filename) {
        ArrayList<String[]> dados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";");
                dados.add(campos);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + filename + ": " + e.getMessage());
        }
        return dados;
    }

    // SALVAR
    private static void escreverCSV(String filename, ArrayList<String> linhas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (String linha : linhas) {
                pw.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo " + filename + ": " + e.getMessage());
        }
    }

    // LER HOSPEDES
    public static HashMap<String, Hospede> carregarHospedes(String filename) {
        HashMap<String, Hospede> hospedes = new HashMap<>();
        ArrayList<String[]> dados = lerCSV(filename);

        for (String[] campos : dados) {
            if (campos.length == 4) {
                String nome = campos[0];
                String cpf = campos[1];
                String genero = campos[2];
                String celular = campos[3];
                Hospede hospede = new Hospede(nome, cpf, genero, celular);
                hospedes.put(cpf, hospede);
            }
        }
        return hospedes;
    }

    // SALVAR HOSPEDES
    public static void salvarHospedes(String filename, HashMap<String, Hospede> hospedes) {
        ArrayList<String> linhas = new ArrayList<>();
        for (Hospede hospede : hospedes.values()) {
            linhas.add(hospede.toString());
        }
        escreverCSV(filename, linhas);
    }

    // LER HOSPEDES
    public static HashMap<String, Funcionario> carregarFuncionarios(String filename) {
        HashMap<String, Funcionario> funcionarios = new HashMap<>();
        ArrayList<String[]> dados = lerCSV(filename);

        for (String[] campos : dados) {
            if (campos.length >= 5) {
                String tipo = campos[0];
                String nome = campos[1];
                String cpf = campos[2];
                String genero = campos[3];
                double salario = Double.parseDouble(campos[4]);

                if (tipo.equals("Gerente") && campos.length == 6) {
                    double bonus = Double.parseDouble(campos[5]);
                    Gerente gerente = new Gerente(nome, cpf, genero, salario, bonus);
                    funcionarios.put(cpf, gerente);
                } else if (tipo.equals("Recepcionista")) {
                    Recepcionista recepcionista = new Recepcionista(nome, cpf, genero, salario);
                    funcionarios.put(cpf, recepcionista);
                }
            }
        }
        return funcionarios;
    }

    // SALVAR FUNCIONARIOS
    public static void salvarFuncionarios(String filename, HashMap<String, Funcionario> funcionarios) {
        ArrayList<String> linhas = new ArrayList<>();
        for (Funcionario funcionario : funcionarios.values()) {
            String tipo = funcionario.getClass().getSimpleName();
            linhas.add(tipo + ";" + funcionario.toString());
        }
        escreverCSV(filename, linhas);
    }

    // LER QUARTOS
    public static HashMap<Integer, Quarto> carregarQuartos(String filename) {
        HashMap<Integer, Quarto> quartos = new HashMap<>();
        ArrayList<String[]> dados = lerCSV(filename);

        for (String[] campos : dados) {
            if (campos.length == 4) {
                int numero = Integer.parseInt(campos[0]);
                String tipo = campos[1];
                double preco = Double.parseDouble(campos[2]);
                boolean ocupado = Boolean.parseBoolean(campos[3]);

                Quarto quarto = new Quarto(numero, tipo);
                quarto.setOcupado(ocupado);
                quartos.put(numero, quarto);
            }
        }
        return quartos;
    }

    // SALVAR QUARTOS
    public static void salvarQuartos(String filename, HashMap<Integer, Quarto> quartos) {
        ArrayList<String> linhas = new ArrayList<>();
        for (Quarto quarto : quartos.values()) {
            linhas.add(quarto.toString());
        }
        escreverCSV(filename, linhas);
    }

    // LER RESERVAS
    public static ArrayList<Reserva> carregarReservas(String filename, HashMap<String, Hospede> hospedes, HashMap<Integer, Quarto> quartos) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        ArrayList<String[]> dados = lerCSV(filename);

        for (String[] campos : dados) {
            if (campos.length >= 5) {
                String cpfHospede = campos[1];
                Hospede hospede = hospedes.get(cpfHospede);

                if (hospede != null) {
                    Reserva reserva = new Reserva(hospede);
                    int index = 2;

                    while (index + 2 < campos.length) {
                        int numeroQuarto = Integer.parseInt(campos[index]);
                        Quarto quarto = quartos.get(numeroQuarto);

                        if (quarto != null) {
                            quarto.setOcupado(true);
                            reserva.adicionarQuartoSemVerificar(quarto);
                        }
                        index += 3;
                    }
                    reservas.add(reserva);
                }
            }
        }
        return reservas;
    }

    // SALVAR RESERVAS
    public static void salvarReservas(String filename, ArrayList<Reserva> reservas) {
        ArrayList<String> linhas = new ArrayList<>();
        for (Reserva reserva : reservas) {
            linhas.add(reserva.toString());
        }
        escreverCSV(filename, linhas);
    }

    // SALVAR RELATORIO
    public static void salvarRelatorio(String conteudoRelatorio, String filename) {
        ArrayList<String> linhas = new ArrayList<>();
        linhas.add(conteudoRelatorio);
        escreverCSV(filename, linhas);
    }
}