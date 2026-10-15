# GDD — REWIND

**Game Design Document — Documento de Referência Oficial**

| Campo | Valor |
| --- | --- |
| Nome de trabalho | REWIND |
| Frase principal | "O teu passado muda o teu presente." |
| Género | Arcade, sobrevivência e estratégia |
| Plataforma inicial | Android |
| Tecnologia principal | Kotlin nativo para Android |
| Sistema de desenho | Android Canvas |
| Versão do documento | 1.0 (FASE A) |
| Estado | Especificação inicial — sem implementação |

> Este documento é a referência principal para o conceito e os requisitos do
> jogo. Toda a futura implementação deve obedecer ao aqui descrito, salvo
> alteração explícita e autorizada.

---

## 1. Objetivo desta fase

Esta fase (FASE A) tem como objetivo preparar a estrutura inicial do projeto
e criar a documentação de referência.

Tarefas da FASE A:

1. Verificar o ambiente de trabalho atual.
2. Verificar se já existe um repositório ou diretório de projeto.
3. Se já existir um projeto, inspecioná-lo antes de fazer alterações.
4. Se não existir, criar um diretório chamado REWIND.
5. Criar o documento `GDD.md` na raiz do projeto.
6. Preencher o documento com a especificação completa do jogo.
7. Criar um `README.md` com uma breve apresentação do projeto, o estado
   atual e os objetivos da primeira fase.
8. Criar um `TODO.md` com as fases de desenvolvimento previstas.
9. Preparar uma estrutura documental simples e organizada.
10. Apresentar um relatório final com os ficheiros criados e as próximas
    etapas.

Restrições da FASE A:

- Não criar ficheiros de código do jogo.
- Não implementar funcionalidades.
- Não começar a programar.
- Não instalar ferramentas desnecessárias.
- Não alterar configurações existentes sem necessidade.
- Preservar qualquer trabalho já existente.

---

## 2. Identidade do jogo

- **Nome de trabalho:** REWIND
- **Frase principal:** "O teu passado muda o teu presente."
- **Género:** Arcade, sobrevivência e estratégia.
- **Plataforma inicial:** Android.
- **Tecnologia principal:** Kotlin nativo para Android.
- **Sistema de desenho:** Android Canvas.

### 2.1. Ferramentas previstas

- **Termux** — ambiente de trabalho no próprio Android.
- **OpenCode** — agente de programação.
- **GitHub** — guardar e controlar o código.
- **GitHub Actions** — compilar o projeto remotamente.
- **Dispositivo Android** — testar o jogo.

### 2.2. Âmbito de plataformas

O jogo será desenvolvido primeiro para Android. Não devemos implementar
versões para outras plataformas antes de concluir e validar a versão Android.

### 2.3. Título

O título **REWIND** é provisório até verificarmos a sua disponibilidade para
publicação.

---

## 3. Conceito principal

REWIND é um jogo 2D de sobrevivência visto de cima.

O jogador controla uma entidade energética numa arena. O objetivo é recolher
fragmentos de energia, sobreviver e evitar obstáculos.

A característica principal é a **memória dos movimentos**.

Durante uma partida, o jogo regista os movimentos do jogador. Quando a
partida termina, esses movimentos podem ser reproduzidos na partida seguinte
através de uma entidade fantasma chamada **ECHO**.

O ECHO repete a trajetória anteriormente registada.

Consequentemente, o jogador precisa de considerar dois elementos ao mesmo
tempo:

- Os perigos presentes na arena.
- Os movimentos que fez no passado e que agora são reproduzidos como
  obstáculos.

O jogador pode precisar de alterar a sua rota para evitar colidir com o seu
próprio passado.

### 3.1. Princípios de desenho da mecânica

- A mecânica deve criar decisões estratégicas compreensíveis, não
  dificuldades aleatórias ou injustas.
- O ECHO não deve ser tratado como uma inteligência artificial complexa.
  Na primeira versão, deve apenas reproduzir de forma previsível os
  movimentos registados.

---

## 4. Personagens e elementos

### 4.1. CORE

CORE é a entidade controlada pelo jogador.

Características:

- Forma circular.
- Aparência energética.
- Movimento controlado por toque.
- Rasto visual durante o movimento.
- Colisão com os limites da arena.
- Capacidade de recolher fragmentos de energia.

A aparência inicial será minimalista, com uma identidade visual futurista.

### 4.2. ECHO

ECHO é a representação dos movimentos anteriores do CORE.

Características:

- Forma circular ou silhueta semelhante ao CORE.
- Aparência translúcida.
- Cor visual distinta do jogador.
- Reprodução dos movimentos registados.
- Movimento determinístico.
- Capacidade de participar nas regras de colisão.

Regras:

- O ECHO **não** recolhe fragmentos.
- O ECHO **não** deve repetir todos os acontecimentos da partida. A sua
  função principal é reproduzir a trajetória registada.
- A regra de colisão com o ECHO ainda deverá ser validada durante os testes
  de jogabilidade.
- A proposta inicial é **terminar a partida quando o CORE colide com um
  ECHO**. Esta regra deverá ser implementada apenas quando a fase
  correspondente for autorizada.

### 4.3. Fragmentos de energia

São objetos que aparecem dentro da arena.

Função:

- Incentivar o jogador a deslocar-se.
- Criar decisões de percurso.
- Aumentar a pontuação.
- Obrigar o jogador a equilibrar risco e recompensa.

Pontuação inicial proposta: **10 pontos por fragmento recolhido**.
O valor poderá ser ajustado depois dos testes.

### 4.4. Obstáculos

Os obstáculos fixos fazem parte da evolução futura do jogo.

Não são necessários na primeira versão jogável. Primeiro devemos validar a
interação entre CORE, fragmentos e ECHO.

---

## 5. Mecânicas de jogo

### 5.1. Movimento

O jogador utiliza o toque e o arrastamento do dedo no ecrã.

O sistema de movimento deverá:

- Responder rapidamente ao toque.
- Evitar mudanças de direção excessivamente bruscas.
- Aplicar aceleração e desaceleração suaves, se isso melhorar o controlo.
- Impedir que o jogador saia dos limites da arena.
- Funcionar em diferentes dimensões de ecrã.
- Evitar movimentos inconsistentes entre dispositivos.

A sensação de controlo deverá ser validada com testes reais.

### 5.2. Recolha de energia

Quando o CORE toca num fragmento:

- O fragmento desaparece.
- A pontuação aumenta.
- Pode ser reproduzido um efeito visual.
- Pode ser reproduzido um efeito sonoro.

Os efeitos audiovisuais podem ser adicionados gradualmente.

### 5.3. Registo de movimentos

Durante a partida, o jogo deverá registar a posição do CORE ao longo do
tempo.

O registo deve:

- Associar cada posição a uma referência temporal.
- Utilizar coordenadas normalizadas ou um método equivalente que permita
  reproduzir os movimentos em diferentes ecrãs.
- Evitar armazenar dados ilimitados.
- Permitir uma reprodução suave.
- Guardar apenas os dados necessários.
- Ser validado antes de ser reutilizado.

### 5.4. Reprodução dos movimentos

Na partida seguinte, o ECHO deverá reproduzir a trajetória anterior.

O sistema deverá:

- Respeitar a ordem temporal dos movimentos.
- Interpolar posições quando necessário.
- Evitar saltos visuais.
- Manter um comportamento previsível.
- Limitar a quantidade de dados armazenados.
- Evitar criar um número ilimitado de entidades ECHO.

A reprodução deve depender dos dados registados e **não** de uma
inteligência artificial que tente imitar o jogador.

### 5.5. Pontuação

Sistema inicial proposto:

- **10 pontos** por fragmento recolhido.
- **1 ponto** por segundo sobrevivido.

Estes valores são provisórios e devem ser configuráveis.

### 5.6. Fim da partida

O ecrã de fim da partida deverá apresentar:

- Pontuação obtida.
- Melhor pontuação.
- Tempo de sobrevivência.
- Quantidade de fragmentos recolhidos.
- Botão para jogar novamente.
- Botão para regressar ao menu.

---

## 6. Ciclo principal de jogo

1. O jogador abre o jogo.
2. Entra no menu principal.
3. Inicia uma partida.
4. Controla o CORE na arena.
5. Recolhe fragmentos de energia.
6. O jogo regista os movimentos.
7. A partida termina segundo as regras estabelecidas.
8. A pontuação é apresentada.
9. A trajetória é validada e guardada.
10. O jogador inicia outra partida.
11. O ECHO reproduz a trajetória anterior.
12. O jogador adapta os seus movimentos.
13. O ciclo repete-se.

A mecânica deverá ser **fácil de compreender e difícil de dominar
completamente**.

---

## 7. Progressão da dificuldade

A progressão será introduzida gradualmente.

| Etapa | Conteúdo |
| --- | --- |
| 1 | Aprender os controlos e recolher fragmentos. |
| 2 | Introduzir o primeiro ECHO. |
| 3 | Recolher fragmentos evitando o ECHO. |
| 4 | Introduzir obstáculos fixos. |
| 5 | Introduzir mais ECHOs, apenas se a jogabilidade continuar justa e compreensível. |
| 6 | Avaliar desafios especiais e outros modos de jogo. |

- A quantidade máxima de ECHOs não está definida.
- Não aumentar a dificuldade apenas para tornar o jogo artificialmente
  frustrante.

---

## 8. Modos de jogo

A primeira versão terá apenas um modo:

### SURVIVAL

O objetivo será sobreviver, recolher fragmentos e melhorar a pontuação.

Modos reservados para o futuro (**não implementar** nesta fase):

- Challenge.
- Time Attack.
- Daily Challenge.

---

## 9. Interface

### 9.1. Menu principal

Deverá incluir:

- Nome e identidade visual do REWIND.
- Botão Play.
- Melhor pontuação.
- Definições.
- Informação de privacidade, quando necessária.

### 9.2. Ecrã de jogo

Deverá incluir:

- Arena.
- CORE.
- ECHOs ativos.
- Fragmentos.
- Pontuação.
- Tempo de sobrevivência.
- Botão de pausa.

### 9.3. Pausa

- A simulação deve parar quando o jogador pausa o jogo.
- Também deve ser suspensa adequadamente quando a aplicação passa para
  segundo plano.
- O jogo não deve continuar a simular movimentos enquanto estiver suspenso.

### 9.4. Fim da partida

- Deverá apresentar os resultados e permitir reiniciar rapidamente.
- A interface deve ser legível em ecrãs pequenos.
- A orientação inicial será **vertical**, com controlo pensado para
  utilização com uma mão.

---

## 10. Direção visual e sonora

### 10.1. Direção visual

Futurista, minimalista e com elementos geométricos.

Proposta inicial:

- Fundo escuro.
- CORE em ciano.
- ECHO em violeta.
- Fragmentos com uma cor de destaque.
- Texto claro.
- Efeitos luminosos moderados.

Não depender exclusivamente das cores para distinguir os elementos. As formas
e os contornos também devem ajudar (acessibilidade e leitura em ecrãs
pouco calibrados).

### 10.2. Efeitos visuais futuros

- Rasto do CORE.
- Aparecimento do ECHO.
- Recolha de fragmentos.
- Colisões.
- Transição para o fim da partida.

### 10.3. Áudio futuro

- Som de recolha.
- Som de colisão.
- Som de aparecimento do ECHO.
- Música opcional.
- Opção para desligar o som.

Não é necessário criar recursos de áudio nesta fase.

---

## 11. Arquitetura técnica prevista

O jogo será nativo para Android, desenvolvido em Kotlin. Android Canvas será
utilizado para o desenho 2D inicial.

Estrutura prevista (proposta, não obrigação rígida):

```
app/
  src/
    main/
      java/
        com/
          rewind/
            MainActivity.kt
            game/
              GameView.kt
              GameEngine.kt
              GameState.kt
              GameConfig.kt
            entities/
              Player.kt
              Echo.kt
              Collectible.kt
              Obstacle.kt
            systems/
              CollisionSystem.kt
              RecordingSystem.kt
              ReplaySystem.kt
              ScoreSystem.kt
            data/
              GamePreferences.kt
      AndroidManifest.xml
      res/

build.gradle.kts
settings.gradle.kts
README.md
GDD.md
TODO.md

.github/
  workflows/
    android.yml
```

Notas:

- Esta estrutura pode ser ajustada durante o desenvolvimento quando existir
  uma justificação técnica concreta.
- **Não criar agora** os ficheiros Kotlin indicados.
- Não criar um workflow de compilação sem verificar primeiro se já existe um
  workflow funcional no repositório.
- Se o projeto ainda não tiver configuração Android, registar essa
  necessidade no `TODO.md` para a fase de implementação.

---

## 12. Estados do jogo

A arquitetura deverá considerar os seguintes estados:

```
MENU → READY → PLAYING → PAUSED → GAME_OVER
```

- As transições entre estados deverão ser explícitas.
- O sistema de desenho **não** deve ser responsável por toda a lógica do
  jogo.
- A lógica, o estado, os controlos e o desenho devem ficar separados de
  forma razoável para facilitar a manutenção.
- Não criar esta implementação agora. Apenas documentar.

---

## 13. Desempenho e compatibilidade

O jogo deverá:

- Funcionar em dispositivos Android compatíveis.
- Adaptar-se a diferentes resoluções.
- Evitar alocações excessivas no ciclo de desenho.
- Limitar a quantidade de ECHOs.
- Controlar a quantidade de amostras registadas.
- Evitar fugas de memória.
- Tratar corretamente pausa, retoma e segundo plano.
- Manter uma taxa de atualização estável.

Os requisitos exatos de versão Android, SDK, Gradle e Java deverão ser
decididos depois de inspecionar o ambiente e o projeto existente.

**Não inventar versões ou declarar compatibilidade sem testes.**

---

## 14. Estratégia de desenvolvimento

O desenvolvimento será dividido em fases pequenas. Cada fase depende de
autorização explícita; não avançar automaticamente.

| Fase | Nome | Conteúdo | Critério de conclusão |
| --- | --- | --- | --- |
| A | Preparação | Inspecionar o ambiente; estrutura documental; `GDD.md`; `README.md`; `TODO.md`. | Documentação criada e verificada. |
| B | Aplicação Android mínima | Configurar o projeto Android (se necessário); compilação via GitHub Actions; APK de teste; instalar e abrir no dispositivo. | A aplicação compila, instala e abre sem falhas. |
| C | Movimento | Arena; desenhar o CORE; controlos táteis; restringir movimento aos limites. | O jogador consegue movimentar-se de forma consistente. |
| D | Primeira partida jogável | Fragmentos; pontuação; colisões; fim de partida; reinício. | Partida completa e reiniciável de ponta a ponta. |
| E | Mecânica REWIND | Registar trajetórias; reproduzir trajetórias; desenhar o ECHO; validar colisões com o ECHO; testar diferentes resoluções. | ECHO reproduz corretamente e colisões são justas. |
| F | Apresentação | Menu principal; ecrã de resultados; recordes; pausa; efeitos visuais; som e definições quando apropriado. | Fluxo completo de interface navegável. |
| G | Testes | Dispositivos reais; controlos; compreensão da mecânica; dificuldade; desempenho; feedback. | Lista de problemas avaliada e corrigida. |
| H | Publicação | Monetização; anúncios apenas após validar a experiência; compra única para remover anúncios; privacidade e permissões; AAB assinado; Google Play. | Submissão preparada. |

---

## 15. Monetização futura

- A intenção comercial é distribuir o jogo **gratuitamente**.
- Possibilidades futuras:
  - Anúncios entre partidas.
  - Compra única para remover anúncios.
- **Não** apresentar anúncios durante a ação principal do jogo.
- **Não** implementar monetização nesta fase.
- A monetização só deverá ser considerada depois de o jogo demonstrar que a
  mecânica central funciona e proporciona uma experiência satisfatória.
- Não prometer rendimentos nem presumir sucesso comercial sem dados reais.

---

## 16. Riscos a validar

| # | Risco |
| --- | --- |
| 1 | O jogador não compreender a função do ECHO. |
| 2 | Os controlos serem imprecisos. |
| 3 | A colisão com o ECHO ser demasiado punitiva. |
| 4 | Demasiados ECHOs tornarem a arena impossível. |
| 5 | A mecânica perder interesse após algumas partidas. |
| 6 | A dificuldade aumentar sem proporcionar decisões justas. |
| 7 | O projeto crescer demasiado antes de existir uma versão jogável. |
| 8 | A publicidade prejudicar a experiência. |
| 9 | Problemas de desempenho em dispositivos modestos. |

As decisões deverão ser baseadas em testes e não apenas na nossa impressão
de que uma funcionalidade parece boa.

---

## 17. Regras de trabalho do agente

Aplicam-se a todas as futuras sessões de desenvolvimento do REWIND.

1. `GDD.md` será a referência principal para o conceito e os requisitos do
   jogo.
2. `README.md` explicará o projeto e o seu estado atual.
3. `TODO.md` acompanhará as fases, tarefas e critérios de aceitação.
4. Antes de alterar código existente, inspecionar os ficheiros relevantes.
5. Não substituir ficheiros existentes sem necessidade.
6. Não inventar resultados de compilação ou testes.
7. Não declarar uma tarefa concluída sem verificar o resultado.
8. Trabalhar em alterações pequenas e verificáveis.
9. Explicar erros com clareza e indicar a correção proposta.
10. Se faltar informação importante, inspecionar primeiro o ambiente antes
    de pedir ao utilizador para repetir informação que já está disponível.
11. Não instalar dependências desnecessárias.
12. Não introduzir bibliotecas externas sem justificação.
13. Não implementar funcionalidades de fases futuras sem autorização.
14. Atualizar o `TODO.md` quando uma tarefa for concluída ou quando surgir
    um bloqueio relevante.
15. Preservar a compatibilidade com o processo de compilação existente,
    sempre que possível.
16. Não fazer commits, pushes ou publicações remotas sem autorização
    explícita do utilizador.
17. Não começar a implementação do jogo durante a FASE A.

---

## 18. Pendências explícitas

- Configuração Android do projeto (Gradle, SDK, Manifest) — **por criar**,
  registada no `TODO.md` para a FASE B.
- Workflow `.github/workflows/android.yml` — **não criado** nesta fase;
  verificar pré-existência antes de avançar.
- Disponibilidade do título "REWIND" — a verificar antes da publicação
  (FASE H).
- Validação da regra de colisão CORE/ECHO — pendente de testes (FASE E).
