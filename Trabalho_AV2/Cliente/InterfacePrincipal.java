package Trabalho_AV2.Cliente;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.image.BufferedImage;

public class InterfacePrincipal extends JFrame {
    private JButton btnStart, btnStopServer, btnInsertionSort;
    private Process serverProcess;
    
    public InterfacePrincipal() {
        setTitle("Análise de Algoritmos de Ordenação");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2, 10, 10));

        btnStart = new JButton("Iniciar Server");
        btnStopServer = new JButton("Encerrar Server");
        btnInsertionSort = new JButton("Iniciar InsertionSort");

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarServerPython();
            }
        });
        btnStopServer.addActionListener(e -> encerrarServerPython());
        btnInsertionSort.addActionListener(e -> executarAlgoritmo("InsertionSort"));

        add(btnStart);
        add(btnStopServer);
        add(btnInsertionSort);
    }

    private void iniciarServerPython() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "Trabalho_AV2/Api/server.py");
            serverProcess = processBuilder.start();
            Thread.sleep(5000);
            JOptionPane.showMessageDialog(this, "Server iniciado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void encerrarServerPython() {
        if (serverProcess != null) {
            serverProcess.destroy(); // Encerra o processo do server.py
            JOptionPane.showMessageDialog(this, "Server encerrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "O server não está rodando!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executarAlgoritmo(String nome) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "Trabalho_AV2." + nome + ".Main");
            Process processo = processBuilder.start();
            processo.waitFor();
            JOptionPane.showMessageDialog(this, nome + " executado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            enviarCSV(nome);
        } catch  (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enviarCSV(String nome) {
        try {
            File csvFile = new File("Trabalho_AV2/Resultados/" + nome + "_resultados.csv");
            String url = "http://127.0.0.1:5000/grafico";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary");

            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            writer.append("------WebKitFormBoundary\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + csvFile.getName() + "\"\r\n");
            writer.append("Content-Type: text/csv\r\n\r\n");
            writer.flush();

            try (FileInputStream fileInputStream = new FileInputStream(csvFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            outputStream.flush();
    
            // Fechar multipart
            writer.append("\r\n------WebKitFormBoundary--\r\n");
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();
        
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // try (InputStream in = connection.getInputStream()) {
                //     Files.copy(in, Paths.get("grafico_recebido.png"));
                // }
                JOptionPane.showMessageDialog(this, "Gráfico gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                BufferedImage image = ImageIO.read(connection.getInputStream());
                JLabel label = new JLabel(new ImageIcon(image));
                JOptionPane.showMessageDialog(null, label, "Gráfico " + nome, JOptionPane.PLAIN_MESSAGE);
    
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao gerar gráfico! Código: " + responseCode, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfacePrincipal frame = new InterfacePrincipal();
            frame.setVisible(true);
        });
    }
}
