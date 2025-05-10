# Paralelização de Algoritmos de Ordenação: Uma Análise Acerca do Desempenho do Serial VS Parelelo.

## Equipe Envolvida
- Lucas Cardoso Xavier Santos | 2218942
- Maickon Brenner Marques Brandão | 2224203

## Resumo
Este projeto tem como objetivo comparar o desempenho de algoritmos de ordenação clássicos em suas versões sequenciais e paralelas, utilizando a linguagem Java. Foram implementados os algoritmos Bubble Sort, Insertion Sort, Merge Sort e Quick Sort, com técnicas de paralelização baseadas em ExecutorService e ForkJoinPool. As execuções foram realizadas com diferentes tamanhos de entrada e quantidades de threads, e os tempos de execução foram registrados em arquivos CSV para posterior análise estatística e visual por meio de gráficos. Este trabalho busca demonstrar os benefícios e limitações da computação paralela aplicada à ordenação de dados, contribuindo para a compreensão prática de como o paralelismo afeta o desempenho de algoritmos em ambientes multicore.

## Introdução
Neste trabalho, são exploradas diferentes estratégias de ordenação através da implementação dos algoritmos Bubble Sort, Insertion Sort, Merge Sort e Quick Sort em versões sequenciais e paralelas. As implementações foram realizadas em Java, aproveitando ferramentas como ExecutorService e ForkJoinPool para execução multithread. Para facilitar a análise dos resultados, foi criada uma interface gráfica simples em Swing, integrando os algoritmos com uma API Python que gera gráficos a partir de arquivos CSV. Essa estrutura permitiu uma comparação visual e estatística do desempenho entre as versões, considerando variáveis como tamanho dos dados e número de threads utilizadas.

## Metodologia
Nesta etapa, define-se quais algoritmos de ordenação serão implementados e analisados. Para o presente trabalho, os algoritmos selecionados foram: Bubble Sort, Insertion Sort, Merge Sort e Quick Sort.

Afim de garantir um ambiente adequado de experimentação, foi escolhida a linguagem de programação Java para elaboração e codificação destes algoritmos, juntamente com a integração de uma API desenvolvida em Python (criada pela própria equipe também), que recebe os arquivos CSV produzidos pelos algoritmos e por meio das bibliotecas NumPy e Matplotlib, geraram os gráficos necessários para realizar as análises para a pesquisa em questão. Para execução geral do projeto, foi desenvolvido um "Cliente", com interface gráfica simples através da biblioteca Swing, que possui o papel de painel de controle da aplicação, realizando a inicialização e encerramento da API e botões para execução direta dos algoritmos citados. Ao final da execução do algoritmo escolhido, será apresentado o gráfico gerado, informando a relação entre a execução do algoritmo serial e paralelo, variando o tamanho do problema (no caso, o Array) e a quantidade de Threads para sua resolução.

Vejamos agora como se decorreu o processo de criação e desenvolvimento em cada algoritmo.

### Bubble Sort
O Bubble Sort é um algoritmo simples que compara e troca elementos vizinhos para ordenar um array. Na versão paralela, utilizamos ExecutorService para executar várias partes do algoritmo simultaneamente, distribuindo as iterações entre threads.

### Insertion Sort
O Insertion Sort ou algoritmo de ordenação por inserção é um modelo simples para ordenar listas de valores. Para sua implementação paralela, foi utilizado o **Executor Service**, sendo um melhor indicado para essa solução, isso devido a sua natureza que tem um comportamento sequencial e depende fortemente da ordem dos elementos adjacentes, o que dificulta sua paralelização eficiente, logo, ao invés de tentar paralelizar cada inserção de elementos, divide-se o array em múltiplas partes e depois ordena cada uma separadamente antes de combiná-las ao final do processo.

### Merge Sort
O Merge Sort é um algoritmo de ordenação que parte do princípio de "dividir para conquistar", ou seja, ele recebe um problema e divide em duas partes, que irão sendo resolvidas recursivamente até o conjunto de valores de entrada esteja ordenados corretamente e no fim combinando ambas as partes. Para sua implementação paralela, foi utilizado o **ForkJoinPool**, sendo uma excelente opção devido o Merge Sort ser naturalmente adequado para paralelização, facilitando a utilização de múltiplos threads.

### Quick Sort
O Quick Sort é um algoritmo eficiente que divide o array em partes menores e as ordena. Na versão paralela, utilizamos ForkJoinPool, que permite executar chamadas recursivas em paralelo com múltiplas threads.

## Resultados e Discussão
Analisando os resultados obtidos com a execução dos algoritmos, 

Nos algoritmos Bubble Sort (Imagem 01) e Insertion Sort (Imagem 02), tiverem um ganho significativo em perfomance, diminuindo bastante o tempo de execução em relação as versões sequenciais.

Agora nos algoritmos MergeSort (Imagem 03) e QuickSort (Imagem 04), ocorrerem situações bastante semelhantes, onde iniciamente as suas respectivas versão sequencias eram dominantes em relação ao tempo de execução, neste caso, visualizamos uma perspectiva onde o problema é "pequeno demais" para ser paralelizado, onde apenas a versão serial daria de conta. Todavia, isso acaba gerando o overhead na versão paralela, ou seja, um custo adicional no algoritmo, no entando a medida em foi-se aumentando o tamanho do Array e a quantidade de Threads fornecidas para as versões paralelas, o cenário foi mudando significativamente, reduzindo bastante o tempo para a ordenação. Outro fato curioso foi que a medida que o número de Threads chegava perto de 16, estes algoritmos acabavam não tendo o resultado tão satisfatório. No MergeSort, a quantidade mais estável com melhor tempo de execução foi em 4 Threads, já para o QuickSort, estabilizou entre 4 à 8 Threads.

## Conclusão
Com base nos resultados obtidos, fica nítido a importância da computação paralela na otimização de algoritmos de ordenação. A capacidade de distribuir a carga de processamento entre múltiplos núcleos permite ganhos significativos de desempenho, reduzindo o tempo de execução e melhorando a eficiência computacional. Entretanto, um ponto relevante adquirido com o desenvolvimento deste trabalho foi que não necessariamente o aumento expressivo de Threads para realização de uma tarefa pode gerar um alto desempenho na execução dos algoritmos, ou seja, haverão situções, como vistas no trabalho, que este excesso pode acabar atralhando na execução e por consequência, gerar ciclos de execução mais longos do que deveriam.

Dessa forma, a exploração de técnicas paralelas representa não apenas uma estratégia de aceleração e aprimorar o desempenho, mas também de haver uma análise para compreender onde serão aplicados esses sistemas, isso com a finalidade desenvolver sistemas atendam melhor as necessidades para cada cenário.

## Referências
- CORMEN, T. Algoritmos - Teoria e Prática. 3. ed. [s.l.] GEN LTC, 10DC.
- DEVMEDIA. Threads: paralelizando tarefas com os diferentes recursos do Java. Disponível em: https://www.devmedia.com.br/threads-paralelizando-tarefas-com-os-diferentes-recursos-do-java/34309.

## Anexos
Link para o projeto na plataforma GitHub:
[Link Direto](https://github.com/MaickonBrenner/Computacao_Paralela)

Outra maneira: https://github.com/MaickonBrenner/Computacao_Paralela

#### Imagem 01
<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_BubbleSort.png" alt="Gráfico Bubble Sort">

#### Imagem 02
<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_InsertionSort.png" alt="Gráfico Insertion Sort">

#### Imagem 03
<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_MergeSort.png" alt="Gráfico Merge Sort">

#### Imagem 04
<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_QuickSort.png" alt="Gráfico Quick Sort">
