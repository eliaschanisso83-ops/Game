---
name: rewind-mechanics
description: Mecânica central do REWIND — registo temporal das trajetórias do CORE em coordenadas portáveis, reprodução determinística com interpolação, limitação e validação de amostras, gestão de ECHOs, colisão CORE-ECHO e testes de consistência. Usar ao implementar RecordingSystem, ReplaySystem ou Echo.
---

# Mecânica REWIND (registo e ECHO)

## O que a mecânica é (e o que não é)

O ECHO **reproduz dados registados**, ponto final.

- **Nunca** usar IA, previsão ou imitação do jogador. Sem aprendizagem, sem
  "comportamento inteligente".
- O ECHO **não** recolhe fragmentos e **não** altera a pontuação.
- O ECHO **não** reproduz acontecimentos da partida anterior além da
  trajetória: sem recolhas, sem pontuação, sem efeitos daquele jogo.
- O ECHO é **determinístico**: mesmos dados + mesmo tempo ⇒ mesma posição.

## A. Registo dos movimentos do CORE

- Registar durante `PLAYING` apenas (não em pausa, não em segundo plano).
- Amostra por **intervalo fixo de tempo** (ex.: a cada N ms) **ou** quando a
  posição mudar mais de um limiar mínimo — nunca por frame, para não
  depender da taxa de atualização.
- Registar apenas: tempo relativo ao início da partida + posição no espaço
  do jogo. Nada mais.
- O registo pertence à partida atual; só é promovido a "trajetória guardada"
  quando a partida termina com sucesso (GDD 6, ponto 9).

## B. Registo temporal das posições

- Tempo **relativo ao início da partida** (segundos desde `t=0`), monotónico
  crescente.
- Nunca usar tempo absoluto de sistema (`System.currentTimeMillis`) — é
  afetado pelo relógio do dispositivo.
- Derivar o tempo da mesma fonte usada pelo passo de simulação (Skill
  `game-2d`), para que registo e reprodução partilhem a mesma régua.
- Amostras devem ser ordenadas por tempo; validar monotonia ao guardar.

## C. Coordenadas adequadas a diferentes ecrãs

- Guardar em **coordenadas normalizadas** no espaço do jogo (ex.: 0.0–1.0
  em cada eixo, ou unidades do jogo com dimensões de arena normalizadas).
- Nunca guardar pixels do ecrã — deixaria de reproduzir em outro dispositivo.
- Converter ao registar (posição do jogo → normalizada) e ao reproduzir
  (normalizada → posição do jogo atual).
- **Um único ponto de conversão**, partilhado por registo e reprodução.
  Dois caminhos de conversão diferentes ⇒ trajetórias desfasadas.
- Raio do CORE/ECHO também deve ser derivado do espaço do jogo, para que a
  colisão seja equivalente em qualquer ecrã.

## D. Reprodução determinística

- Dado `tAtual`, localizar as amostras `i` e `i+1` que envolvem `tAtual` e
  interpolar.
- Manter um cursor por ECHO avançando apenas para a frente (não rebuscar do
  início a cada frame).
- Se `tAtual` for anterior à primeira amostra: CORE/ECHO ainda não aparece
  ou permanece na primeira posição — decidir uma regra e mantê-la
  consistente.
- Se `tAtual` ultrapassar a última amostra: manter a última posição (a
  trajetória terminou).
- Nada de aleatoriedade, nada de estado externo no caminho da reprodução.

## E. Interpolação das posições

```
t = (tAtual - t_i) / (t_i+1 - t_i)     com clamp a [0, 1]
p = p_i + (p_i+1 - p_i) * t
```

- Interpolar **sempre** entre amostras adjacentes; nunca saltar para a
  amostra mais próxima (evita "teleporte").
- `t_i+1 == t_i` (amostras duplicadas): usar `p_i+1` diretamente.
- Ao fim da trajetória, parar a interpolação — sem extrapolação.
- Interpolação linear é suficiente para a primeira versão; não introduzir
  splines sem necessidade demonstrada.

## F. Limitação das amostras guardadas

- **Limite rígido** de amostras por trajetória (definir em `GameConfig`).
- Ao atingir o limite: parar de registar **ou** reduzir a taxa de amostragem
  — nunca crescer sem limite. Escolher uma estratégia e documentá-la.
- Trajetórias muito longas também podem ser limitadas por duração máxima.
- Registos antigos: manter apenas o necessário (por omissão: a trajetória da
  partida anterior), libertar as restantes.
- Dados persistidos em disco, se for o caso, também são limitados.

## G. Validação dos dados

Validar antes de carregar/registar uma trajetória para uso:

- Não vazia (mínimo de amostras para ser reproduzível).
- Tempos monotónicos crescentes e não negativos.
- Coordenadas dentro dos limites esperados (0–1 ou espaço da arena).
- Contagem dentro do limite definido.

Regras de falha: **descartar a trajetória e seguir sem ECHO** — nunca
crashar o arranque nem reproduzir dados inválidos. Registar o motivo.

## H. Gestão dos ECHOs

- Número **limitado** de ECHOs simultâneos (definir em `GameConfig`).
  O limite não está fixado no GDD — começar pelo mínimo que valida a
  mecânica (1) e só aumentar com autorização e testes (GDD 7).
- Cada ECHO: um cursor temporal + a trajetória que lhe pertence.
- Criar ECHOs no início da partida; destruir no fim/reinício.
- Todos os ECHOs avançam no **mesmo relógio da simulação** — não ter um
  relógio por ECHO.
- Quando o limite é atingido: substituir a trajetória mais antiga ou não
  criar novo ECHO — decisão explícita, documentada em `GameConfig`.

## I. Deteção de colisões

- CORE–ECHO: círculo–círculo (Skill `game-2d`), testada no passo de
  simulação, com o mesmo espaço do jogo usado no desenho.
- ECHO–fragmento: **não existe**. O ECHO não interage com fragmentos.
- Deteção ≠ consequência: implementar a deteção como componente testável e
  deixar a consequência (terminar partida?) num único ponto configurável.
  **A regra é proposta, não decidida** — ver MVP_PLAN secção 4 e GDD 4.2.
- Considerar sub-passo se a velocidade relativa puder atravessar o ECHO
  entre quadros.

## J. Testes de consistência

Executar e registar resultados (nunca afirmar sem executar):

1. **Gravar → reproduzir:** gravar trajetória conhecida; reproduzir;
   comparar posição interpolada com o esperado dentro de tolerância.
2. **Idempotência:** reproduzir duas vezes a mesma trajetória ⇒ mesmas
   posições.
3. **Portabilidade de ecrã:** reproduzir trajetória gravada num ecrã com
   dimensões diferentes ⇒ trajetória com forma equivalente.
4. **Taxa de atualização:** reproduzir a 60 Hz e a 120 Hz ⇒ resultado
   equivalente.
5. **Limites:** registar mais amostras que o limite ⇒ contagem limitada,
   sem crescimento.
6. **Validação:** carregar dados corrompidos ⇒ descarte gracioso, sem crash.
7. **Fim da trajetória:** `tAtual` após a última amostra ⇒ posição estável,
   sem extrapolação.
8. **Limite de ECHOs:** exceder o limite ⇒ número de entidades não cresce.
9. **Colisão:** cenário controlado CORE/paths cruzados ⇒ deteção ocorre no
   frame esperado.

Se um teste não puder ser executado neste ambiente, registar como **não
executado** — nunca como aprovado.

## Sinais de alarme

- Trajetória com saltos visuais ⇒ interpolação em falta ou amostras fora de
  ordem.
- ECHO desfasado do movimento original ⇒ conversão de coordenadas
  divergente entre registo e reprodução, ou relógios diferentes.
- Memória a crescer entre partidas ⇒ amostras/ECHOs não libertados.
- Comportamento diferente entre dispositivos ⇒ dependência de pixels de
  ecrã ou de taxa de quadro.
- Reprodução diferente em duas execuções ⇒ aleatoriedade ou estado externo
  no caminho do replay.
