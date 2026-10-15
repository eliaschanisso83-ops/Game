---
name: game-2d
description: Implementação de jogos 2D com Android Canvas — coordenadas, movimento, vetores, delta time, taxa de atualização, colisões, interpolação, desenho, ciclo update/draw, pausa e desempenho. Usar ao criar ou alterar a simulação e o render do REWIND.
---

# Jogo 2D com Android Canvas

## Objetivo

Simulação 2D vista de cima, estável e **independente da taxa de atualização
do ecrã**, desenhada com `Canvas` num `View` (ou `SurfaceView`).

## Coordenadas bidimensionais

- Origem no canto superior esquerdo: `x` cresce para a direita, `y` para
  baixo.
- Definir um **espaço do jogo** próprio (ex.: unidades do jogo) e um único
  ponto de conversão para pixels do ecrã. Não espalhar fatores de escala
  pelo código.
- Manter todo o estado da simulação no espaço do jogo; converter só no
  desenho e na leitura de toque.
- Regras de colisão e limites no espaço do jogo → resultados idênticos em
  qualquer resolução.

## Movimento, vetores e direção

- Representar posição e velocidade como pares `(x, y)`; vetor de direção
  normalizado para movimento a velocidade constante.
- `posição += velocidade * dt` — nunca `posição += velocidade` por frame.
- Normalizar o vetor de direção para evitar que diagonais sejam mais
  rápidas.
- Aplicar aceleração/desaceleração suaves (GDD 5.1): interpolar a
  velocidade atual em direção à velocidade alvo, em vez de a definir de
  forma abrupta.
- Limitar mudança de direção por passo para evitar viragens impossíveis.
- Clamp aos limites da arena **após** mover (nunca permitir posições fora).

## Delta time e taxa de atualização

Regra central: **a lógica não pode depender da velocidade de atualização do
ecrã.**

```
dt = tempoDesdeOÚltimoPasso (segundos)
limitar dt a um máximo (ex.: 0.25s) para saltos após suspensão
posicao += velocidade * dt
```

- Calcular `dt` com `System.nanoTime()`/`elapsedRealtimeNanos` (não com
  contagem de frames).
- **Descartar** o `dt` acumulado quando a app retoma de segundo plano ou de
  pausa — nunca aplicar um passo gigante.
- Ideal: **passo de simulação fixo** com acumulador (ex.: 60 Hz lógico) e
  interpolação entre estados para desenho. Se se usar passo variável,
  garantir que todas as grandezas são por unidade de tempo.
- Testar a consistência: percorrer o mesmo caminho em 60 Hz e 120 Hz deve
  dar resultados equivalentes.

## Ciclo de atualização e desenho

Separar dois momentos:

1. **Atualização (lógica):** ler entradas → mover → colidir → atualizar
   pontuação/registo → transições de estado.
2. **Desenho (render):** ler o estado → desenhar. Sem efeitos secundários
   no estado.

- O desenho nunca altera o estado do jogo.
- A lógica nunca depende de objetos de desenho (`Paint`, `Canvas`).
- Pausa: parar a atualização; desenhar a última cena congelada se
  necessário.

## Deteção de colisões

- **Círculo–círculo** (CORE, ECHO, fragmentos): colisão se
  `distancia² <= (r1 + r2)²` — usar raio ao quadrado, evitar `sqrt`.
- **Círculo–retângulo** (limites/obstáculos futuros): clamp do centro ao
  retângulo e comparar distâncias.
- Testar colisões no passo de simulação, não no desenho.
- Com velocidades altas, considerar teste de "swept" ou sub-passo para não
  atravessar um objeto fino (tunneling).
- Separar deteção (devolve *onde/quando*) da consequência (termina partida,
  recolhe) — a consequência da colisão CORE–ECHO é decisão pendente
  (MVP_PLAN secção 4).

## Interpolação

- Entre duas amostras registadas: `p = p0 + (p1 - p0) * t`, com
  `t = (tempoAtual - t0) / (t1 - t0)`.
- Interpolar sempre entre amostras; **nunca** atribuir a posição da amostra
  mais próxima de forma abrupta (evita saltos visuais).
- Em fim de amostragem: manter a última posição conhecida.

## Desenho de objetos

- Pré-alocar `Paint`, `RectF`, `Path`; reutilizar — **zero alocações por
  quadro**.
- Antialiasing onde fizer sentido; cores consistentes com a direção visual
  (GDD 10): fundo escuro, CORE ciano, ECHO violeta translúcido, fragmentos
  com cor de destaque.
- Não depender só de cor: forma e contorno devem distinguir elementos
  (GDD 10.1).
- Desenhar por ordem estável: fundo → arena → fragmentos → ECHOs → CORE →
  UI.

## Pausa e retoma

- `PAUSED`: atualização parada, tempo de simulação congelado.
- Retoma: reiniciar a referência de tempo; não aplicar o tempo em pausa.
- Transições de estado explícitas (GDD 12).

## Desempenho em dispositivos modestos

- Sem alocações no ciclo de desenho; sem `Bitmap`/`String` criados por frame.
- Limitar entidades (ECHOs) e amostras (GDD 13).
- Evitar `sqrt`, `sin` e logs no caminho quente.
- Manter taxa de atualização estável; medir, não presumir.
- Evitar `invalidate()` excessivo em UI; o loop do jogo é próprio.
- Testar em dispositivo real, não só no emulador.

## Critérios de qualidade

- Movimento idêntico em diferentes taxas de quadro e resoluções.
- Nenhuma lógica de jogo no `onDraw`.
- Zero alocações por quadro no caminho de desenho.
- Pausa e retoma sem "teleporte" do CORE.
- Colisões determinísticas para o mesmo estado de entrada.

## Erros comuns a evitar

- Mover a uma taxa fixa por frame (jogo mais rápido em ecrãs de 120 Hz).
- Aplicar o `dt` acumulado após suspensão.
- Colisões no código de desenho.
- Calcular `sqrt` em loop para distâncias.
- Criar `Paint`/`Rect` dentro de `onDraw`.
- Testar `distancia < r1 + r2` sem quadrado (custo) — ou pior, esquecer de
  somar os raios.
- Declarar "60 fps estáveis" sem medir.
