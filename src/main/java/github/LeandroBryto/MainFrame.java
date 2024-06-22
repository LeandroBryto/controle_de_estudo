package github.LeandroBryto;


import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private JComboBox<String> userComboBox;
    private JTextField languageField;
    private JButton startStudyButton;
    private JButton endStudyButton;
    private JLabel timerLabel;
    private LocalDateTime startTime;
    private Timer studyTimer;

    private ArrayList<StudyEvent> events;

    public MainFrame() {
        events = new ArrayList<>();

        setTitle("Sistema de Estudo - Engenharia de Software");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Layout usando JGoodies Forms
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new FormLayout("right:pref, 5dlu, pref", "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 10dlu, pref"));
        CellConstraints cc = new CellConstraints();

        // Componentes da interface
        mainPanel.add(new JLabel("Nome do Usuário:"), cc.xy(1, 1));
        userComboBox = new JComboBox<>(new String[]{"Usuário 1", "Usuário 2"}); // Adicione seus usuários aqui
        mainPanel.add(userComboBox, cc.xy(3, 1));

        mainPanel.add(new JLabel("Linguagem de Estudo:"), cc.xy(1, 3));
        languageField = new JTextField();
        mainPanel.add(languageField, cc.xy(3, 3));

        mainPanel.add(new JLabel("Início do Estudo:"), cc.xy(1, 5));
        JButton startTimeButton = new JButton("Selecionar Data/Hora");
        startTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateTimeString = JOptionPane.showInputDialog(MainFrame.this, "Informe a data e hora de início (dd/MM/yyyy HH:mm):");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                try {
                    startTime = LocalDateTime.parse(dateTimeString, formatter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Formato inválido! Use dd/MM/yyyy HH:mm.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(startTimeButton, cc.xy(3, 5));

        startStudyButton = new JButton("Iniciar Estudo");
        startStudyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startStudy();
            }
        });
        mainPanel.add(startStudyButton, cc.xy(3, 7));

        endStudyButton = new JButton("Encerrar Estudo");
        endStudyButton.setEnabled(false); // Inicialmente desabilitado
        endStudyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endStudy();
            }
        });
        mainPanel.add(endStudyButton, cc.xy(3, 9));

        timerLabel = new JLabel("Tempo de Estudo: 00:00:00");
        mainPanel.add(timerLabel, cc.xy(1, 9));

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void startStudy() {
        if (startTime == null) {
            JOptionPane.showMessageDialog(this, "Selecione a data e hora de início do estudo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Inicializa o cronômetro
        studyTimer = new Timer(1000, new ActionListener() {
            int seconds = 0;
            int minutes = 0;
            int hours = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }
                if (minutes == 60) {
                    minutes = 0;
                    hours++;
                }
                timerLabel.setText(String.format("Tempo de Estudo: %02d:%02d:%02d", hours, minutes, seconds));
            }
        });
        studyTimer.start();

        // Habilita o botão de encerrar estudo e desabilita o de iniciar estudo
        startStudyButton.setEnabled(false);
        endStudyButton.setEnabled(true);
    }

    private void endStudy() {
        // Para o cronômetro
        if (studyTimer != null && studyTimer.isRunning()) {
            studyTimer.stop();
        }

        // Salva os dados na lista de eventos
        String userName = (String) userComboBox.getSelectedItem();
        String language = languageField.getText();
        String studyDate = startTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String studyTime = timerLabel.getText().substring(16); // Pega o tempo de estudo no formato HH:mm:ss

        events.add(new StudyEvent(userName, language, studyDate, studyTime));
        JOptionPane.showMessageDialog(this, "Estudo salvo com sucesso!");

        // Limpa os campos e reinicia a interface para novo estudo
        startTime = null;
        languageField.setText("");
        timerLabel.setText("Tempo de Estudo: 00:00:00");
        startStudyButton.setEnabled(true);
        endStudyButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
