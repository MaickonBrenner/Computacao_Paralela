from flask import Flask, request, send_file
import numpy as np
import matplotlib.pyplot as plt
import os

app = Flask(__name__)

@app.route('/')
def home():
    return "API Flask Server rodando."

@app.route('/grafico', methods=['POST'])
def gerarGrafico():
    print("Requisição recebida!")  

    arquivo = request.files.get('file')
    if arquivo is None:
        print("Erro: Nenhum arquivo foi enviado!")
        return "Erro: Nenhum arquivo foi enviado!", 400

    print(f"Arquivo recebido: {arquivo.filename}")

    # Ler o CSV usando NumPy
    dados = np.genfromtxt(arquivo, delimiter=",", dtype=None, encoding="utf-8", skip_header=1)

    # Extração de colunas
    tipo_array = np.array([linha[0] for linha in dados])
    threads_array = np.array([linha[2] for linha in dados], dtype=int)
    tempo_array = np.array([linha[3] for linha in dados], dtype=float) # 3 - Milisegudos, 4 Nanosegundos

    # Criando gráfico
    plt.figure(figsize=(8, 5))
    tipos_unicos = np.unique(tipo_array)

    for tipo in tipos_unicos:
        indices = np.where(tipo_array == tipo)  # Encontrar índices de cada tipo
        plt.plot(threads_array[indices], tempo_array[indices], marker="o", label=tipo)

    plt.xlabel("Quantidade de Threads")
    plt.ylabel("Tempo (ms)")
    plt.title("Comparativo Serial VS Paralelo")
    plt.legend()

    # Salvar o gráfico
    caminho = os.path.join(os.getcwd(), "Trabalho_AV2/Resultados/grafico.png")
    plt.savefig(caminho)
    print(f"Gráfico salvo em: {caminho}")

    return send_file(caminho, mimetype="image/png")

if __name__ == '__main__':
    app.run(debug=True)
