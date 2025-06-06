# Comparação de Algoritmos de Contagem de Palavras: Serial, Paralelo com CPU e Paralelo com GPU

## Equipe Envolvida

* Lucas Cardoso Xavier Santos | 2218942
* Maickon Brenner Marques Brandão | 2224203

## Resumo

Este projeto tem como objetivo analisar e comparar o desempenho de três abordagens distintas para o problema de contagem de palavras: uma implementação sequencial, uma paralela com múltiplas threads em CPU, e uma versão que explora paralelismo em GPU utilizando a biblioteca JOCL (Java bindings for OpenCL). As implementações foram feitas em Java, com registros de desempenho armazenados em arquivos CSV e gráficos gerados via JavaFX. A análise visa compreender as vantagens e limitações de cada abordagem em diferentes cenários de carga de dados, destacando padrões de desempenho e situações ideais de uso para cada técnica.

## Introdução

Com o crescimento dos volumes de dados processados por aplicações modernas, a eficiência dos algoritmos de processamento textual, como a contagem de palavras, torna-se cada vez mais relevante. Neste trabalho, três abordagens foram analisadas:

* **Serial (Sequencial):** Implementação básica utilizando uma única thread.
* **Paralela com CPU:** Utilizando `ExecutorService` com múltiplas threads para dividir o processamento do texto.
* **Paralela com GPU:** Utilizando a biblioteca JOCL para executar kernels OpenCL que realizam a contagem de palavras de forma massivamente paralela.

A proposta central é comparar o desempenho e a escalabilidade das três abordagens de contagem de palavras — sequencial, paralela em CPU e paralela em GPU — utilizando conjuntos de dados de tamanhos variados, a fim de determinar qual técnica é mais eficiente em diferentes contextos de uso e identificar o impacto da paralelização no tempo de processamento.

## Metodologia

### Implementação de Algoritmos

Foram desenvolvidas três versões do algoritmo de contagem (ou busca) de palavras:

* **Versão Sequencial:** Lê o texto linha por linha, contando as ocorrências da palavra-alvo em cada linha de forma direta, utilizando abordagens tradicionais com `String` e laços de repetição.

* **Versão Paralela com CPU:** Divide o texto em linhas e atribui blocos de linhas a múltiplas threads utilizando `ExecutorService`. Cada thread processa seu conjunto de linhas de forma independente e os resultados são combinados ao final. Essa abordagem aproveita o paralelismo em sistemas multicore.

* **Versão Paralela com GPU (OpenCL):** O texto é tratado como um vetor contínuo de bytes. Os índices de início de cada linha são armazenados separadamente. Esse conteúdo é transferido para a memória da GPU, onde um *kernel* OpenCL é executado em paralelo. Cada instância do kernel verifica se a palavra está presente em uma linha específica. A implementação foi feita com a biblioteca JOCL (Java bindings para OpenCL).

---

### Framework de Teste

Um sistema de testes foi desenvolvido com interface gráfica em JavaFX. A aplicação permite:

* Visualizar os arquivos de texto disponíveis diretamente na interface;
* Executar automaticamente a busca por palavras-chave pré-definidas;
* Ver os resultados (ocorrências e tempos) exibidos em tempo real para cada algoritmo e gráfico.


---

### Registro de Dados

Após cada execução, o tempo de processamento é salvo automaticamente em um arquivo `.csv`, com informações como:

* A quantidade de ocorrências encontradas de cada palavra;
* O tempo total de execução para cada algoritmo e palavra.

---


## Resultados e Discussão

A seguir, discutimos os resultados com base nos tempos de execução coletados:

* **Serial:** Apresentou bom desempenho para arquivos pequenos, mas não escalou bem com o aumento do volume de dados, resultando em tempos altos para arquivos grandes.
* **Paralela com CPU:** Demonstrou melhora significativa no tempo de execução à medida que o número de threads aumentava (até um certo ponto), especialmente para arquivos médios e grandes.
* **Paralela com GPU:** Obteve os menores tempos de execução nos cenários com arquivos grandes, evidenciando a vantagem do paralelismo massivo. No entanto, para arquivos pequenos, o overhead de comunicação entre CPU e GPU tornou o desempenho inferior à versão com CPU.

## Conclusão

A análise dos dados coletados evidencia que o uso de paralelismo pode trazer ganhos expressivos de desempenho em tarefas de contagem de palavras, especialmente quando o volume de dados é significativo. A versão paralela com CPU mostrou-se mais eficiente do que a sequencial em praticamente todos os cenários testados. Já a versão GPU superou todas as outras em arquivos grandes, mas não apresentou ganhos relevantes em cargas menores, devido ao overhead de setup e comunicação.

Esses resultados reforçam a importância de escolher a abordagem adequada conforme o contexto da aplicação, considerando tanto o volume de dados quanto os recursos computacionais disponíveis.

## Referências



## Anexos

Link para o projeto na plataforma GitHub:
[Link Direto](https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV3)

---
