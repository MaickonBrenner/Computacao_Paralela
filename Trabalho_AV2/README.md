# Paralelização de Algoritmos de Ordenação: Uma Análise Acerca do Desempenho do Serial VS Parelelo.

## Equipe Envolvida
- Lucas Cardoso Xavier | 
- Maickon Brenner Marques Brandão | 2224203

## Resumo
O presente projeto possui a finalidade de realizar a implementação dos algoritmos de ordenação de valores mais utilizados e compará-los com se

## Introdução

## Metodologia

### Insertion Sort
O Insertion Sort ou algoritmo de ordenação por inserção é um modelo simples para ordenar listas de valores.

Para sua implementação paralela, foi utilizado o **Executor Service**, sendo um melhor indicado para essa solução, isso devido a sua natureza que tem um comportamento sequencial e depende fortemente da ordem dos elementos adjacentes, o que dificulta sua paralelização eficiente, logo, ao invés de tentar paralelizar cada inserção de elementos, divide-se o array em múltiplas partes e depois ordena cada uma separadamente antes de combiná-las ao final do processo.

### Merge Sort
O Merge Sort é um algoritmo de ordenação que parte do princípio de "dividir para conquistar", ou seja, ele recebe um problema e divide em duas partes, que irão sendo resolvidas recursivamente até o conjunto de valores de entrada esteja ordenados corretamente e no fim combinando ambas as partes. Isso é possível através do método da árvore de recursão, que de ser forma, 

Para sua implementação paralela, foi utilizado o **ForkJoinPool**, sendo uma excelente opção devido o Merge Sort ser naturalmente adequado para paralelização, facilitando a utilização de múltiplos threads.

## Resultados e Discussão

## Conclusão

## Referências
- CORMEN, T. Algoritmos - Teoria e Prática. 3. ed. [s.l.] GEN LTC, 10DC.

## Anexos
