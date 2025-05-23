package Trabalho_AV3.Principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SerialCPU {
    public int buscarPalavra(String arquivo, String palavra) {
        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while((linha = br.readLine()) != null) {
                String[] palavras = linha.split("\\s+");
                for (String p : palavras) {
                    if(p.equalsIgnoreCase(palavra)) {
                        contador++;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Erro na leitura do arquivo." + e.getMessage());
        }
        return contador;
    }
}
