---
name: android-kotlin
description: Desenvolvimento Android nativo em Kotlin para o projeto REWIND — organização de classes, separação de responsabilidades, ciclo de vida, eventos de toque, estado, armazenamento local, erros e fugas de memória. Usar ao escrever ou alterar código Kotlin/Android do jogo.
---

# Android + Kotlin no REWIND

## Princípio base

Código Android **nativo**, em Kotlin, com Android Canvas. Sem HTML/CSS/JS no
núcleo do jogo. Sem frameworks de terceiros sem justificação e autorização.

**Antes de sugerir qualquer API ou dependência:** inspecionar a configuração
real do projeto (`build.gradle.kts`, `AndroidManifest.xml`, versões
declaradas). O projeto pode ainda não ter configuração Android — nesse caso,
reportar isso em vez de assumir versões.

## Separação de responsabilidades

Manter quatro camadas distintas (GDD 12):

| Camada | Responsabilidade | Exemplo |
| --- | --- | --- |
| Estado | Dados do jogo num momento | `GameState`, `GameConfig` |
| Lógica | Atualização da simulação | `GameEngine` + `systems/*` |
| Controlos | Entrada do jogador | handler de toque → intenção de movimento |
| Desenho | Renderização apenas | `GameView` (não contém regras do jogo) |

Regras:

- `GameView` **não** deve conter regras de jogo, pontuação nem colisões.
- As transições de estado (`MENU`, `READY`, `PLAYING`, `PAUSED`,
  `GAME_OVER`) devem ser explícitas e centralizadas — nunca implícitas em
  efeitos secundários do desenho.
- Classes pequenas com uma responsabilidade; evitar "God Views".

## Ciclo de vida Android

- `onPause()`/`onStop()`: **suspender a simulação**. O jogo nunca continua a
  correr em segundo plano (GDD 9.3).
- `onResume()`: retomar sem aplicar um delta de tempo gigante acumulado —
  descartar o tempo decorrido durante a suspensão.
- `onDestroy()`: libertar referências (threads, callbacks, `Handler`).
- Preferir a API de ciclo de vida do AndroidX quando disponível no projeto;
  verificar antes de usar.
- Não guardar `Context`/`Activity` em objetos de longa duração (fuga de
  memória). Se for inevitável, usar `applicationContext`.

## Eventos de toque

- Ler eventos em `onTouchEvent` (ou `GestureDetector` se existir no
  projeto) e **traduzir para intenções** (ex.: vetor de direção), não
  executar lógica de jogo diretamente no handler.
- Tratar `ACTION_DOWN`, `ACTION_MOVE`, `ACTION_UP`/`ACTION_CANCEL`.
- Tratar `ACTION_CANCEL` (chamada de sistema) como fim do arrasto — o CORE
  não pode "preso" a mover-se.
- Converter coordenadas de ecrã para o espaço do jogo num único ponto, de
  forma consistente.
- Área de toque generosa; o jogo é jogável com uma mão em orientação
  vertical.

## Gestão de estado

- Estado imutável ou de acesso controlado; evitar estado escondido em
  múltiplos objetos.
- Atualizar o estado no passo de simulação, **não** durante o desenho.
- Pausa congela o estado; retoma não repete passos em duplicado.

## Armazenamento local

- Dados simples (recorde, definições): `SharedPreferences` — é suficiente e
  não exige dependências novas.
- Gravar de forma síncrona e mínima; não escrever em `onDraw`.
- Não persistir trajetórias inteiras sem limite — ver Skill
  `rewind-mechanics`.
- Nunca armazenar dados sensíveis ou chaves no código.

## Tratamento de erros

- Falhas de I/O de preferências: degradar para valores por omissão e
  registar, sem crash.
- Não engolir exceções silenciosamente; registar a causa.
- Dados corrompidos de registo de trajetória: descartar os dados e continuar
  sem ECHO em vez de falhar o arranque.
- Nenhuma afirmação de "funciona" sem verificação.

## Prevenção de fugas de memória

- Sem alocações por quadro no ciclo de desenho (reutilizar objetos,
  `Path`/`Rect` pré-alocados).
- Não criar lambdas/listas por frame.
- Limitar listas de entidades e de amostras (GDD 13).
- Evitar referências cíclicas entre `View` e engine.
- Verificar com ferramentas do Android Studio/Perfilador quando disponível;
  se não estiver disponível, dizê-lo claramente.

## Compatibilidade com o projeto

- Seguir o estilo e a estrutura já existentes quando os houver.
- Não reescrever ficheiros completos sem necessidade.
- Não adicionar dependências sem justificação técnica registada.
- Não alterar a configuração de compilação sem necessidade.
- Reportar a inexistência de configuração Android como tarefa pendente.

## Critérios de qualidade

- Compilação sem erros; warnings novos justificados.
- Simulação pausada em segundo plano e em pausa.
- Nenhum objecto de Android de longa duração retido sem necessidade.
- Separação lógica/desenho visível na estrutura de ficheiros.
- Alterações pequenas, verificáveis individualmente.

## Erros comuns a evitar

- Lógica de jogo dentro de `onDraw`.
- Assumir versões de SDK/Gradle sem inspecionar o projeto.
- Guardar `Activity` em singletons/objetos estáticos.
- Continuar a simular após `onPause`.
- Delta de tempo gigante após retoma (teletransporte do CORE).
- Introduzir um framework (DI, Rx, motores) para resolver um problema que
  uma classe simples resolve.
- Declarar "testado" sem executar nada.
