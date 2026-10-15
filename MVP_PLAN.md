# MVP_PLAN — REWIND

**Plano da primeira versão jogável.** Este documento define o âmbito do MVP:
o que entra, o que fica de fora e como a implementação deverá ser dividida em
etapas pequenas.

> **Este é um plano, não um relatório de execução.**
> Nada descrito aqui está implementado. O projeto está em fase de
> documentação: não existe código Android, nem Gradle, nem workflow.

---

## 1. Objetivo do MVP

Provar que a mecânica central do REWIND — **registar os movimentos do CORE e
reproduzi-los como ECHO na partida seguinte** — é compreensível, justa e
divertida num dispositivo Android real.

Tudo o que não serve esse objetivo fica fora do MVP.

---

## 2. Incluído no MVP

| # | Funcionalidade | Notas |
| --- | --- | --- |
| 1 | Aplicação Android funcional | Abre e navega sem falhas. |
| 2 | Arena 2D | Vista de cima, limites visíveis. |
| 3 | CORE controlado pelo jogador | Forma circular, aparência energética. |
| 4 | Movimento por toque no ecrã | Toque + arrasto, resposta rápida. |
| 5 | Limites da arena | O CORE não sai da arena. |
| 6 | Fragmentos de energia | Aparecem na arena, são recolhidos ao toque. |
| 7 | Sistema de pontuação | 10 pts/fragmento; 1 pt/segundo sobrevivido (valores provisórios e configuráveis — GDD 5.5). |
| 8 | Registo dos movimentos | Amostras temporais com coordenadas adequadas a diferentes ecrãs. |
| 9 | Reprodução dos movimentos (ECHO) | Determinística, sem IA, com interpolação. |
| 10 | Deteção de colisões CORE–ECHO | Ver decisão pendente (secção 4). |
| 11 | Fim da partida | Ecrã com pontuação, recorde, tempo e fragmentos. |
| 12 | Reinício da partida | Reinício imediato a partir do fim de partida. |
| 13 | Melhor pontuação guardada localmente | Armazenamento local do dispositivo. |
| 14 | Interface simples e legível | Ecrãs mínimos, orientação vertical, utilizável com uma mão. |

---

## 3. Excluído do MVP

Não implementar, sem autorização explícita do utilizador:

- Multiplayer.
- Loja de equipamentos.
- Centenas de personagens.
- Campanha narrativa extensa.
- Sistemas complexos de combate.
- Inteligência artificial para imitar o jogador.
- Monetização.
- Publicidade.
- Compras integradas.
- Desafios diários (Daily Challenge, Challenge, Time Attack).
- Obstáculos fixos (GDD 4.4 — validação de CORE/fragmentos/ECHO primeiro).
- Mais do que o número mínimo de ECHOs necessário para validar a mecânica.
- Versões para outras plataformas.
- Recursos de áudio e efeitos visuais avançados (opcionais, fase posterior).
- Menu principal completo com definições (o MVP pode ter ecrãs mínimos;
  a apresentação completa é FASE F).

---

## 4. Decisão pendente: colisão CORE–ECHO

O **GDD.md (secção 4.2)** estabelece:

- A regra de colisão com o ECHO **ainda será validada em testes de
  jogabilidade**.
- A **proposta inicial** é terminar a partida quando o CORE colide com um
  ECHO.

**Estado:** proposta, não decisão final.

Enquanto a decisão não for tomada pelo proprietário do projeto:

1. Implementar a deteção de colisão CORE–ECHO como componente isolado e
   testável.
2. Deixar a **consequência** da colisão configurável num único ponto do
   código (ex.: constante/flag em `GameConfig`).
3. **Não** tratar a proposta como regra definitiva nem escrever comentários
   que a apresentem como decidida.
4. Registar a decisão, quando existir, no GDD.md e no TODO.md (FASE E).

---

## 5. Etapas de implementação

Ordem sugerida. Cada etapa só começa depois de a anterior estar concluída e
verificada. **A FASE B não começa sem autorização.**

---

### Etapa 1 — Projeto Android mínimo
*(corresponde à FASE B do TODO.md)*

- **Objetivo:** projeto que compila e abre num dispositivo.
- **Ficheiros provavelmente envolvidos:** `settings.gradle.kts`,
  `build.gradle.kts` (raiz e `app/`), `AndroidManifest.xml`,
  `MainActivity.kt`, `res/` (tema, ícone), `.github/workflows/android.yml`
  (apenas se não existir um workflow funcional).
- **Dependências:** nenhuma (primeira etapa).
- **Critérios de aceitação:** a aplicação compila, instala e abre sem
  falhas; o ecrã inicial é visível.
- **Testes necessários:** build Gradle local ou via GitHub Actions;
  instalação e abertura em dispositivo real.
- **Condição de conclusão:** APK gerado, instalado e aplicação aberta —
  com evidência registada (não afirmada).

---

### Etapa 2 — Arena e CORE
*(corresponde à FASE C do TODO.md)*

- **Objetivo:** desenhar a arena e permitir mover o CORE com o dedo,
  respeitando os limites.
- **Ficheiros provavelmente envolvidos:** `game/GameView.kt`,
  `game/GameEngine.kt`, `game/GameState.kt`, `game/GameConfig.kt`,
  `entities/Player.kt`.
- **Dependências:** Etapa 1.
- **Critérios de aceitação:** o CORE responde ao toque de forma imediata e
  suave; nunca ultrapassa os limites; comportamento consistente em
  diferentes resoluções.
- **Testes necessários:** teste manual de arrasto nos quatro sentidos e nos
  cantos; verificação em pelo menos duas dimensões de ecrã; verificação de
  suspensão/retoma da aplicação.
- **Condição de conclusão:** o jogador consegue movimentar-se de forma
  consistente (critério da FASE C).

---

### Etapa 3 — Fragmentos, pontuação e fim de partida
*(corresponde à FASE D do TODO.md)*

- **Objetivo:** primeira partida jogável de ponta a ponta, sem ECHO.
- **Ficheiros provavelmente envolvidos:** `entities/Collectible.kt`,
  `systems/CollisionSystem.kt`, `systems/ScoreSystem.kt`,
  `data/GamePreferences.kt`, `game/GameState.kt`.
- **Dependências:** Etapa 2.
- **Critérios de aceitação:** fragmentos aparecem e são recolhidos; a
  pontuação segue o GDD 5.5; a partida termina por uma condição clara;
  reinício imediato; recorde guardado localmente.
- **Testes necessários:** recolha (positiva e negativa), contagem de
  pontos, fim de partida, reinício, persistência do recorde após fechar e
  reabrir a aplicação.
- **Condição de conclusão:** partida completa e reiniciável, com recorde
  persistido e verificado.

---

### Etapa 4 — Registo e reprodução (ECHO)
*(corresponde à FASE E do TODO.md)*

- **Objetivo:** registar a trajetória do CORE e reproduzi-la como ECHO na
  partida seguinte.
- **Ficheiros provavelmente envolvidos:** `systems/RecordingSystem.kt`,
  `systems/ReplaySystem.kt`, `entities/Echo.kt`, `game/GameConfig.kt`.
- **Dependências:** Etapa 3.
- **Critérios de aceitação:** o ECHO reproduz a trajetória anterior de forma
  determinística e suave; não recolhe fragmentos; não usa IA; o número de
  amostras e de ECHOs é limitado; a colisão CORE–ECHO é detetada e a sua
  consequência segue a secção 4 deste documento.
- **Testes necessários:** gravar e reproduzir a mesma trajetória (verificar
  consistência), teste em resoluções diferentes, limite de amostras,
  contagem de ECHOs, deteção de colisão.
- **Condição de conclusão:** o ECHO reproduz corretamente e a colisão é
  implementada de forma testável (fairness validada na FASE G).

---

### Etapa 5 — Interface mínima completa
*(corresponde à FASE F do TODO.md — apenas o essencial para o MVP)*

- **Objetivo:** navegar entre menu, jogo, pausa e fim de partida.
- **Ficheiros provavelmente envolvidos:** `MainActivity.kt`,
  `game/GameState.kt`, ecrãs/UI.
- **Dependências:** Etapa 4.
- **Critérios de aceitação:** menu → jogo → fim → menu; pausa interrompe a
  simulação; segundo plano suspende a simulação; texto legível em ecrãs
  pequenos.
- **Testes necessários:** transições de estado, pausa/retoma, suspensão da
  aplicação, legibilidade.
- **Condição de conclusão:** fluxo completo navegável sem caminhos mortos.

---

### Etapa 6 — Validação do MVP
*(corresponde à FASE G do TODO.md)*

- **Objetivo:** confirmar que a mecânica é compreendida e justa.
- **Ficheiros envolvidos:** os que resultarem da correção de problemas.
- **Dependências:** Etapa 5.
- **Critérios de aceitação:** testes em dispositivo real; controlos
  avaliados; compreensão do ECHO verificada; dificuldade avaliada;
  problemas de desempenho corrigidos; feedback recolhido.
- **Testes necessários:** sessões de jogo reais, medição de desempenho,
  verificação de fugas de memória.
- **Condição de conclusão:** lista de problemas conhecidos avaliada e
  corrigida por prioridade; decisão sobre avançar para publicação.

---

## 6. Regras do plano

- Não afirmar que o jogo está implementado enquanto o não estiver.
- Não saltar etapas: cada uma depende da anterior e de autorização.
- Quando a realidade divergir do plano, atualizar este documento e o
  `TODO.md`.
- Funcionalidades novas que não estejam aqui consideradas **pertencem a uma
  fase posterior** e exigem autorização.
