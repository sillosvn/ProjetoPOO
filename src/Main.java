import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.IOException;

public class Main {
    private static HashMap<String, Hospede> hospedes = new HashMap<>();
    private static HashMap<String, Funcionario> funcionarios = new HashMap<>();
    private static HashMap<Integer, Quarto> quartos = new HashMap<>();
    private static ArrayList<Reserva> reservas = new ArrayList<>();

    public static void main(String[] args) {
        hospedes = GerenciamentoArquivos.carregarHospedes("hospedes.csv");
        funcionarios = GerenciamentoArquivos.carregarFuncionarios("funcionarios.csv");
        quartos = GerenciamentoArquivos.carregarQuartos("quartos.csv");
        reservas = GerenciamentoArquivos.carregarReservas("reservas.csv", hospedes, quartos);

        JFrame Inicio = new JFrame("Painel Hotel");
        Inicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Inicio.setSize(700, 250);
        Inicio.setResizable(false);
        Inicio.getContentPane().setLayout(null);

        // LOGO
        ImageIcon logo = new ImageIcon("valorant.png");
        Inicio.setIconImage(logo.getImage());

        // HÓSPEDES
        JButton BotaoCriarHospede = new JButton("Hóspede");
        BotaoCriarHospede.setForeground(new Color(128, 0, 0));
        BotaoCriarHospede.setBackground(new Color(255, 255, 255));
        BotaoCriarHospede.setFont(new Font("Segoe UI", Font.BOLD, 14));
        BotaoCriarHospede.setBounds(30, 20, 180, 70);
        BotaoCriarHospede.setFocusPainted(false);
        Inicio.getContentPane().add(BotaoCriarHospede);
        
        BotaoCriarHospede.addActionListener(e -> {
            try {
                String nome, cpf, genero, celular;

                // NOME
                while (true) {
                    try {
                        nome = JOptionPane.showInputDialog(Inicio, "Digite o nome do hóspede:", "Nome", JOptionPane.QUESTION_MESSAGE);

                        if (nome == null) return;
                        if (nome.trim().isEmpty()) {
                            throw new IllegalArgumentException("Você não digitou um nome!");
                        }
                        if (!nome.matches("[\\p{L}\\s]+")) {
                            throw new IllegalArgumentException("Você só pode usar letras e espaços!");
                        }

                        break;
                    } catch (IllegalArgumentException iae) {
                        JOptionPane.showMessageDialog(Inicio, iae.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // CPF
                while (true) {
                    try {
                        cpf = JOptionPane.showInputDialog(Inicio, "Digite o CPF do hóspede:", "CPF", JOptionPane.QUESTION_MESSAGE);

                        if (cpf == null) return;
                        if (cpf.trim().isEmpty()) {
                            throw new IllegalArgumentException("Você não digitou um CPF!");
                        }
                        if (!cpf.matches("^\\d{11}$")) {
                            throw new IllegalArgumentException("Digite somente números (11 dígitos)!");
                        }
                        if (Pessoa.isCpfRegistrado(cpf)) {
                            JOptionPane.showMessageDialog(Inicio, "Este CPF já foi cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        break;
                    } catch (IllegalArgumentException iae) {
                        JOptionPane.showMessageDialog(Inicio, iae.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // GENERO
                while (true) {
                    try {
                        genero = JOptionPane.showInputDialog(Inicio, "Digite o gênero do hóspede (Masculino/Feminino):", "Gênero", JOptionPane.QUESTION_MESSAGE);

                        if (genero == null) return;
                        if (genero.trim().isEmpty()) {
                            throw new IllegalArgumentException("Você não digitou um gênero!");
                        }
                        if (!genero.equalsIgnoreCase("Masculino") && !genero.equalsIgnoreCase("Feminino")) {
                            throw new IllegalArgumentException("Digite apenas Masculino ou Feminino!");
                        }

                        break;
                    } catch (IllegalArgumentException iae) {
                        JOptionPane.showMessageDialog(Inicio, iae.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // CELULAR
                while (true) {
                    try {
                        celular = JOptionPane.showInputDialog(Inicio, "Digite o celular do hóspede (formato: (XX) XXXXX-XXXX):", "Celular", JOptionPane.QUESTION_MESSAGE);

                        if (celular == null) return;
                        if (celular.trim().isEmpty()) {
                            throw new IllegalArgumentException("Você não digitou um celular!");
                        }
                        if (!celular.matches("^\\(\\d{2}\\) \\d{5}-\\d{4}$")) {
                            throw new IllegalArgumentException("Digite um celular no formato: (XX) XXXXX-XXXX");
                        }

                        break;
                    } catch (IllegalArgumentException iae) {
                        JOptionPane.showMessageDialog(Inicio, iae.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                Hospede hospede = new Hospede(nome, cpf, genero, celular);
                hospedes.put(hospede.getCpf(), hospede);
                JOptionPane.showMessageDialog(Inicio, "Hóspede cadastrado com sucesso!", "Hóspede", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Inicio, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });


        // FUNCIONÁRIOS
        JButton BotaoCriarFuncionario = new JButton("Funcionário");
        BotaoCriarFuncionario.setForeground(new Color(128, 0, 0));
        BotaoCriarFuncionario.setBackground(new Color(255, 255, 255));
        BotaoCriarFuncionario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        BotaoCriarFuncionario.setBounds(470, 95, 180, 70);
        BotaoCriarFuncionario.setFocusPainted(false);
        Inicio.getContentPane().add(BotaoCriarFuncionario);
        BotaoCriarFuncionario.addActionListener(e -> {
            String[] cargos = {"Gerente", "Recepcionista"};
            JComboBox<String> comboBox = new JComboBox<>(cargos);

            int resultado = JOptionPane.showConfirmDialog(Inicio, comboBox,
                    "Selecione o cargo do funcionário", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                String escolha = (String) comboBox.getSelectedItem();

                if ("Gerente".equals(escolha)) {
                    try {
                        // NOME
                        String nome;
                        while (true) {
                            try {
                                nome = JOptionPane.showInputDialog(Inicio, "Digite o nome do gerente:");
                                if (nome == null) return;
                                if (nome.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um nome!");
                                if (!nome.matches("[\\p{L}\\s]+")) throw new IllegalArgumentException("Você só pode usar letras e espaços!");
                                break;
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // CPF
                        String cpf;
                        while (true) {
                            try {
                                cpf = JOptionPane.showInputDialog(Inicio, "Digite o CPF do gerente:");
                                if (cpf == null) return;
                                if (cpf.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um CPF!");
                                if (!cpf.matches("^\\d{11}$")) throw new IllegalArgumentException("Digite somente números (11 dígitos)!");
                                if (Pessoa.isCpfRegistrado(cpf)) throw new IllegalArgumentException("Este CPF já foi cadastrado!");
                                break;
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // GÊNERO
                        String genero;
                        while (true) {
                            try {
                                genero = JOptionPane.showInputDialog(Inicio, "Digite o gênero do gerente (Masculino/Feminino):");
                                if (genero == null) return;
                                if (genero.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um gênero!");
                                if (!genero.equalsIgnoreCase("Masculino") && !genero.equalsIgnoreCase("Feminino"))
                                    throw new IllegalArgumentException("Digite apenas Masculino ou Feminino!");
                                break;
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // SALÁRIO
                        double salario = 0;
                        while (true) {
                            try {
                                String salarioInput = JOptionPane.showInputDialog(Inicio, "Digite o salário do gerente:");
                                if (salarioInput == null) return;
                                if (salarioInput.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um salário!");
                                salario = Double.parseDouble(salarioInput);
                                if (salario <= 0) throw new IllegalArgumentException("O salário deve ser um valor positivo!");
                                break;
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(Inicio, "Digite um valor numérico válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // BÔNUS
                        double bonus = 0;
                        while (true) {
                            try {
                                String bonusInput = JOptionPane.showInputDialog(Inicio, "Digite o bônus do gerente:");
                                if (bonusInput == null) return;
                                if (bonusInput.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um bônus!");
                                bonus = Double.parseDouble(bonusInput);
                                if (bonus <= 0) throw new IllegalArgumentException("O bônus deve ser um valor positivo!");
                                break;
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(Inicio, "Digite um valor numérico válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        Gerente gerente = new Gerente(nome, cpf, genero, salario, bonus);
                        funcionarios.put(gerente.getCpf(), gerente);
                        JOptionPane.showMessageDialog(Inicio, "Gerente cadastrado com sucesso!");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else if ("Recepcionista".equals(escolha)) {
                    try {
                        // NOME
                        String nome;
                        while (true) {
                            try {
                                nome = JOptionPane.showInputDialog(Inicio, "Digite o nome do recepcionista:");
                                if (nome == null) return;
                                if (nome.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um nome!");
                                if (!nome.matches("[\\p{L}\\s]+")) throw new IllegalArgumentException("Você só pode usar letras e espaços!");
                                break;
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // CPF
                        String cpf;
                        while (true) {
                            try {
                                cpf = JOptionPane.showInputDialog(Inicio, "Digite o CPF do recepcionista:");
                                if (cpf == null) return;
                                if (cpf.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um CPF!");
                                if (!cpf.matches("^\\d{11}$")) throw new IllegalArgumentException("Digite somente números (11 dígitos)!");
                                if (Pessoa.isCpfRegistrado(cpf)) throw new IllegalArgumentException("Este CPF já foi cadastrado!");
                                break;
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // GÊNERO
                        String genero;
                        while (true) {
                            try {
                                genero = JOptionPane.showInputDialog(Inicio, "Digite o gênero do recepcionista (Masculino/Feminino):");
                                if (genero == null) return;
                                if (genero.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um gênero!");
                                if (!genero.equalsIgnoreCase("Masculino") && !genero.equalsIgnoreCase("Feminino"))
                                    throw new IllegalArgumentException("Digite apenas Masculino ou Feminino!");
                                break;
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // SALÁRIO
                        double salario = 0;
                        while (true) {
                            try {
                                String salarioInput = JOptionPane.showInputDialog(Inicio, "Digite o salário do recepcionista:");
                                if (salarioInput == null) return;
                                if (salarioInput.trim().isEmpty()) throw new IllegalArgumentException("Você não digitou um salário!");
                                salario = Double.parseDouble(salarioInput);
                                if (salario <= 0) throw new IllegalArgumentException("O salário deve ser um valor positivo!");
                                break;
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(Inicio, "Digite um valor numérico válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        Recepcionista recepcionista = new Recepcionista(nome, cpf, genero, salario);
                        funcionarios.put(recepcionista.getCpf(), recepcionista);
                        JOptionPane.showMessageDialog(Inicio, "Recepcionista cadastrado com sucesso!");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        // RESERVAS
        JButton BotaoCriarReserva = new JButton("Reserva");
        BotaoCriarReserva.setForeground(new Color(128, 0, 0));
        BotaoCriarReserva.setBackground(new Color(255, 255, 255));
        BotaoCriarReserva.setFont(new Font("Segoe UI", Font.BOLD, 14));
        BotaoCriarReserva.setBounds(470, 20, 180, 70);
        BotaoCriarReserva.setFocusPainted(false);
        Inicio.getContentPane().add(BotaoCriarReserva);

        BotaoCriarReserva.addActionListener(e -> {
            try {
                if (hospedes.isEmpty()) {
                    JOptionPane.showMessageDialog(Inicio, "Não há hóspedes cadastrados!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (quartos.isEmpty()) {
                    JOptionPane.showMessageDialog(Inicio, "Não há quartos disponíveis!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JComboBox<String> comboHospedes = new JComboBox<>();
                for (Hospede hospede : hospedes.values()) {
                    comboHospedes.addItem(hospede.getNome());
                }


                int escolhaHospede = JOptionPane.showConfirmDialog(
                        Inicio,
                        comboHospedes,
                        "Selecione um Hóspede",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (escolhaHospede != JOptionPane.OK_OPTION) return;

                String nomeHospedeSelecionado = (String) comboHospedes.getSelectedItem();
                Hospede hospedeSelecionado = null;
                for (Hospede hospede : hospedes.values()) {
                    if (hospede.getNome().equals(nomeHospedeSelecionado)) {
                        hospedeSelecionado = hospede;
                        break;
                    }
                }

                if (hospedeSelecionado == null) {
                    JOptionPane.showMessageDialog(Inicio, "Erro ao selecionar o hóspede!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JComboBox<String> comboQuartos = new JComboBox<>();
                for (Quarto quarto : quartos.values()) {
                    if (!quarto.isOcupado()) {
                        comboQuartos.addItem("Número: " + quarto.getNumero() + " | Tipo: " + quarto.getTipo() + " | Preço: R$ " + String.format("%.2f", quarto.getPreco()));
                    }
                }


                if (comboQuartos.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(Inicio, "Todos os quartos estão ocupados!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int escolhaQuarto = JOptionPane.showConfirmDialog(
                        Inicio,
                        comboQuartos,
                        "Selecione um Quarto",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (escolhaQuarto != JOptionPane.OK_OPTION) return;

                String descricaoQuartoSelecionado = (String) comboQuartos.getSelectedItem();
                Quarto quartoSelecionado = null;
                for (Quarto quarto : quartos.values()) {
                    String descricaoQuarto = "Número: " + quarto.getNumero() + " | Tipo: " + quarto.getTipo() + " | Preço: R$ " + String.format("%.2f", quarto.getPreco());
                    if (descricaoQuarto.equals(descricaoQuartoSelecionado)) {
                        quartoSelecionado = quarto;
                        break;
                    }
                }

                if (quartoSelecionado == null) {
                    JOptionPane.showMessageDialog(Inicio, "Erro ao selecionar o quarto!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Reserva novaReserva = new Reserva(hospedeSelecionado, quartoSelecionado);
                reservas.add(novaReserva);

                JOptionPane.showMessageDialog(Inicio, "Reserva criada com sucesso para " + hospedeSelecionado.getNome() + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Inicio, "Erro ao criar a reserva: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });


        // RELATÓRIO
        JButton BotaoRelatorio = new JButton("Relatórios");
        BotaoRelatorio.setForeground(new Color(128, 0, 0));
        BotaoRelatorio.setBackground(new Color(255, 255, 255));
        BotaoRelatorio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        BotaoRelatorio.setBounds(57, 176, 130, 24);
        BotaoRelatorio.setFocusPainted(false);
        Inicio.getContentPane().add(BotaoRelatorio);

        BotaoRelatorio.addActionListener(e -> {
            try {
                if (hospedes.isEmpty() && funcionarios.isEmpty() && quartos.isEmpty() && reservas.isEmpty()) {
                    throw new Exception("Não há informações disponíveis para gerar o relatório.");
                }

                String conteudoRelatorio = "";
                conteudoRelatorio += "--------------------------------------------------\n";
                conteudoRelatorio += "                       RELATÓRIO                   \n";
                conteudoRelatorio += "--------------------------------------------------\n";

                // HÓSPEDES
                if (!hospedes.isEmpty()) {
                    conteudoRelatorio += "HÓSPEDES:\n";
                    for (Hospede h : hospedes.values()) {
                        conteudoRelatorio += h.gerarRelatorio() + "\n";
                    }
                    conteudoRelatorio += "--------------------------------------------------\n";
                }

                // GERENTES
                if (!funcionarios.isEmpty()) {
                    boolean gerentesExistem = false;
                    conteudoRelatorio += "GERENTES:\n";
                    for (Funcionario f : funcionarios.values()) {
                        if (f instanceof Gerente) {
                            Gerente gerente = (Gerente) f;
                            conteudoRelatorio += gerente.gerarRelatorio() + "\n";
                            gerentesExistem = true;
                        }
                    }
                    if (!gerentesExistem) {
                        conteudoRelatorio += "Nenhum gerente cadastrado.\n";
                    }
                    conteudoRelatorio += "--------------------------------------------------\n";
                }

                // RECEPCIONISTAS
                if (!funcionarios.isEmpty()) {
                    boolean recepcionistasExistem = false;
                    conteudoRelatorio += "RECEPCIONISTAS:\n";
                    for (Funcionario f : funcionarios.values()) {
                        if (f instanceof Recepcionista) {
                            Recepcionista recepcionista = (Recepcionista) f;
                            conteudoRelatorio += recepcionista.gerarRelatorio() + "\n";
                            recepcionistasExistem = true;
                        }
                    }
                    if (!recepcionistasExistem) {
                        conteudoRelatorio += "Nenhum recepcionista cadastrado.\n";
                    }
                    conteudoRelatorio += "--------------------------------------------------\n";
                }

                // QUARTOS
                if (!quartos.isEmpty()) {
                    conteudoRelatorio += "QUARTOS:\n";
                    for (Quarto q : quartos.values()) {
                        conteudoRelatorio += q.gerarRelatorio() + "\n";
                    }
                    conteudoRelatorio += "--------------------------------------------------\n";
                }

                // RESERVAS
                if (!reservas.isEmpty()) {
                    conteudoRelatorio += "RESERVAS:\n";
                    for (Reserva r : reservas) {
                        conteudoRelatorio += r.gerarRelatorio() + "\n";
                    }
                    conteudoRelatorio += "--------------------------------------------------\n";
                }

                // DISTRIBUIÇÃO DE QUARTOS
                if (!quartos.isEmpty()) {
                    conteudoRelatorio += "             DISTRIBUIÇÃO DE QUARTOS:\n";
                    conteudoRelatorio += "--------------------------------------------------\n";
                    conteudoRelatorio += "| Tipo de Quarto  | Total | Disponível | Ocupado |\n";
                    conteudoRelatorio += "--------------------------------------------------\n";

                    String[] tiposDeQuarto = {"Solteiro", "Casal", "Suite", "Luxo"};
                    int[][] matrizQuartos = new int[tiposDeQuarto.length][3];

                    for (Quarto q : quartos.values()) {
                        for (int i = 0; i < tiposDeQuarto.length; i++) {
                            if (q.getTipo().equalsIgnoreCase(tiposDeQuarto[i])) {
                                matrizQuartos[i][0]++;
                                if (q.isOcupado()) {
                                    matrizQuartos[i][2]++;
                                } else {
                                    matrizQuartos[i][1]++;
                                }
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < tiposDeQuarto.length; i++) {
                        String linha = String.format("| %-15s | %5d | %10d | %7d |",
                                tiposDeQuarto[i],
                                matrizQuartos[i][0],
                                matrizQuartos[i][1],
                                matrizQuartos[i][2]);
                        conteudoRelatorio += linha + "\n";
                    }
                    conteudoRelatorio += "--------------------------------------------------\n";
                }

                // TOTAIS
                int totalGerentes = 0;
                int totalRecepcionistas = 0;

                for (Funcionario f : funcionarios.values()) {
                    if (f instanceof Gerente) {
                        totalGerentes++;
                    } else if (f instanceof Recepcionista) {
                        totalRecepcionistas++;
                    }
                }

                conteudoRelatorio += "TOTAIS:\n";
                conteudoRelatorio += "Hóspedes: " + hospedes.size() +
                        " | Gerentes: " + totalGerentes +
                        " | Recepcionistas: " + totalRecepcionistas +
                        " | Quartos: " + quartos.size() +
                        " | Reservas: " + reservas.size() + "\n";
                conteudoRelatorio += "--------------------------------------------------";

                JTextArea textArea = new JTextArea(conteudoRelatorio);
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(800, 900));

                JOptionPane.showMessageDialog(Inicio, scrollPane, "Relatório", JOptionPane.INFORMATION_MESSAGE);

                GerenciamentoArquivos.salvarRelatorio(conteudoRelatorio, "relatorio_hotel.csv");
            } catch (Exception iae) {
                JOptionPane.showMessageDialog(Inicio, iae.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // LIMPAR DADOS
        JButton BotaoLimpar = new JButton("Limpar Dados");
        BotaoLimpar.setForeground(new Color(128, 0, 0));
        BotaoLimpar.setBackground(new Color(255, 255, 255));
        BotaoLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        BotaoLimpar.setBounds(285, 176, 130, 24);
        BotaoLimpar.setFocusPainted(false);
        Inicio.getContentPane().add(BotaoLimpar);

        BotaoLimpar.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(Inicio,
                    "Tem certeza que deseja limpar todos os dados?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                hospedes.clear();
                funcionarios.clear();
                quartos.clear();
                reservas.clear();

                try (PrintWriter writer = new PrintWriter("relatorio_hotel.csv")) {
                    writer.print("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Inicio, "Erro ao limpar o arquivo relatorio_hotel.csv: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(Inicio, "Os dados foram limpos.");
            }
        });

        // SAIR
        JButton BotaoSair = new JButton("Sair");
        BotaoSair.setForeground(new Color(128, 0, 0));
        BotaoSair.setBackground(new Color(255, 255, 255));
        BotaoSair.setFont(new Font("Segoe UI", Font.BOLD, 12));
        BotaoSair.setBounds(495, 176, 130, 24);
        BotaoSair.setFocusPainted(false);
        Inicio.getContentPane().add(BotaoSair);

        BotaoSair.addActionListener(e -> {
            try {
                GerenciamentoArquivos.salvarHospedes("hospedes.csv", hospedes);
                GerenciamentoArquivos.salvarFuncionarios("funcionarios.csv", funcionarios);
                GerenciamentoArquivos.salvarQuartos("quartos.csv", quartos);
                GerenciamentoArquivos.salvarReservas("reservas.csv", reservas);

                JOptionPane.showMessageDialog(Inicio, "Dados salvos com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Inicio, "Erro ao salvar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        });



        // CRIAR QUARTO
        JButton BotaoCriarQuarto = new JButton("Quarto");
        BotaoCriarQuarto.setForeground(new Color(128, 0, 0));
        BotaoCriarQuarto.setBackground(new Color(255, 255, 255));
        BotaoCriarQuarto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        BotaoCriarQuarto.setBounds(30, 95, 180, 70);
        BotaoCriarQuarto.setFocusPainted(false);
        Inicio.getContentPane().add(BotaoCriarQuarto);

        BotaoCriarQuarto.addActionListener(e -> {
            try {
                String numeroInput, tipo;
                int numero = 0;
                double preco = 0.0;

                // NUMERO
                while (true) {
                    try {
                        numeroInput = JOptionPane.showInputDialog(Inicio, "Digite o numero do quarto: ");

                        if (numeroInput == null) return;
                        if (numeroInput.trim().isEmpty()) {
                            throw new IllegalArgumentException("Você nao digitou um número!");
                        }
                        if (!numeroInput.matches("^\\d+$")) {
                            throw new IllegalArgumentException("Digite somente números!");
                        }

                        numero = Integer.parseInt(numeroInput);

                        // CHECAR
                        if (quartos.containsKey(numero)) {
                            JOptionPane.showMessageDialog(Inicio, "O quarto com o número " + numero + " já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        break;
                    } catch (IllegalArgumentException iae) {
                        JOptionPane.showMessageDialog(Inicio, iae.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                //TIPO DO QUARTO
                String[] tiposDeQuarto = {"Solteiro", "Casal", "Suite", "Luxo"};
                JComboBox<String> comboBoxTipo = new JComboBox<>(tiposDeQuarto);
                int tipoEscolhido = JOptionPane.showOptionDialog(Inicio,
                        "Escolha o tipo do quarto:",
                        "Tipo do Quarto",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        tiposDeQuarto,
                        tiposDeQuarto[0]);

                if (tipoEscolhido == -1) return;

                tipo = tiposDeQuarto[tipoEscolhido];

                Quarto novoQuarto = new Quarto(numero, tipo);
                quartos.put(numero, novoQuarto);
                JOptionPane.showMessageDialog(Inicio, "Quarto " + numeroInput + " adicionado com sucesso!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Inicio, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // BACKGROUND
        JLabel background = new JLabel(new ImageIcon("foto1.png"));
        background.setBounds(-16, -20, 700, 299);
        Inicio.getContentPane().add(background);


        Inicio.setLocationRelativeTo(null);
        Inicio.setVisible(true);
    }
}