package Principal;

import java.util.List;

public class SerialCPU {
    public int buscarPalavra(List<String> linhas, String palavra) {
        int contador = 0;
        for (String linha : linhas) {
            String[] palavras = linha.split("\\s+");
            for (String p : palavras) {
                if (p.equalsIgnoreCase(palavra)) {
                    contador++;
                }
            }
        }
        return contador;
    }
}
