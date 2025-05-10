# Paralelização de Algoritmos de Ordenação: Uma Análise Acerca do Desempenho do Serial VS Parelelo.

## Equipe Envolvida
- Lucas Cardoso Xavier Santos | 2218942
- Maickon Brenner Marques Brandão | 2224203

## Resumo
O presente projeto possui a finalidade de realizar a implementação dos algoritmos de ordenação de valores mais utilizados e compará-los com se

## Introdução

## Metodologia
Nesta etapa, define-se quais algoritmos de ordenação serão implementados e analisados. Para o presente trabalho, os algoritmos selecionados foram: Bubble Sort, Insertion Sort, Merge Sort e Quick Sort.

Afim de garantir um ambiente adequado de experimentação, foi escolhida a linguagem de programação Java para elaboração e codificação destes algoritmos, juntamente com a integração de uma API desenvolvida em Python (criada pela própria equipe também), que recebe os arquivos CSV produzidos pelos algoritmos e por meio das bibliotecas NumPy e Matplotlib, geraram os gráficos necessários para realizar as análises para a pesquisa em questão. Para execução geral do projeto, foi desenvolvido um "Cliente", com interface gráfica simples através da biblioteca Swing (Imagem 01), que possui o papel de painel de controle da aplicação, realizando a inicialização e encerramento da API e botões para execução direta dos algoritmos citados. Ao final da execução do algoritmo escolhido, será apresentado o gráfico gerado, informando a relação entre a execução do algoritmo serial e paralelo, variando o tamanho do problema (no caso, o Array) e a quantidade de Threads para sua resolução.

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

## Conclusão
Tendo em vista os resultados discutidos, nota-se a relevância da computação paralela para otimização de algoritmos.

## Referências
- CORMEN, T. Algoritmos - Teoria e Prática. 3. ed. [s.l.] GEN LTC, 10DC.

## Anexos
Link para o projeto na plataforma GitHub:
[Link Direto](https://github.com/MaickonBrenner/Computacao_Paralela)

Outra maneira: https://github.com/MaickonBrenner/Computacao_Paralela

<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_BubbleSort.png" alt="Gráfico Bubble Sort">
<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_InsertionSort.png" alt="Gráfico Insertion Sort">
<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_MergeSort.png" alt="Gráfico Merge Sort">
<img src="https://github.com/MaickonBrenner/Computacao_Paralela/tree/main/Trabalho_AV2/Resultados/grafico_QuickSort.png" alt="Gráfico Quick Sort">
