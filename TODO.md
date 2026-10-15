# TODO — REWIND

Plano de desenvolvimento por fases. **Cada fase depende de autorização
explícita do utilizador.** Não avançar automaticamente entre fases.

**Fase atual:** FASE A (concluída nesta sessão)
**Próxima fase:** FASE B — *pendente de autorização*

**Legenda:** `[x]` concluído · `[ ]` pendente · `[!]` bloqueio

---

## FASE A — Preparação ✅

**Objetivo:** preparar a estrutura do projeto e a documentação de referência.

- [x] Inspecionar o diretório atual e identificar se já existe um projeto.
- [x] Preservar conteúdo existente (`~/projects/Gamehub` — projeto não
      relacionado, intocado).
- [x] Criar o diretório do projeto `projects/REWIND`.
- [x] Criar `GDD.md` com a especificação completa.
- [x] Criar `README.md` com apresentação e estado atual.
- [x] Criar `TODO.md` com as fases A–H.
- [x] Verificar a existência e conteúdo dos três documentos.
- [ ] Inicializar repositório Git — *não pedido nesta fase; fazer apenas com
      autorização*.

**Critério de conclusão:** os três documentos existem na raiz do projeto e
contêm conteúdo. Não existe código de jogo.

**Restrições respeitadas:** nenhum `.kt`, nenhuma configuração Android, nenhum
workflow, nenhum commit.

---

## FASE B — Aplicação Android mínima ⏸

**Aguarda autorização.**

- [!] Configurar o projeto Android (estrutura Gradle, `settings.gradle.kts`,
      `build.gradle.kts`, `AndroidManifest.xml`) — **necessidade registada**:
      o projeto ainda não tem qualquer configuração Android.
- [ ] Definir versões de Android SDK / Gradle / Java **após inspecionar o
      ambiente** (Java 17 disponível; Gradle e Android SDK por confirmar).
- [ ] Criar `MainActivity.kt` e um ecrã mínimo.
- [ ] Verificar se já existe workflow GitHub Actions antes de criar
      `.github/workflows/android.yml`.
- [ ] Preparar a compilação através do GitHub Actions.
- [ ] Gerar um APK de teste.
- [ ] Instalar e abrir a aplicação num dispositivo Android.

**Critério de conclusão:** a aplicação compila, instala e abre sem falhas.

---

## FASE C — Movimento ⏸

**Aguarda autorização (após FASE B).**

- [ ] Criar a arena.
- [ ] Desenhar o CORE.
- [ ] Implementar controlos táteis (toque e arrasto).
- [ ] Restringir o movimento aos limites da arena.
- [ ] Validar a consistência do controlo em diferentes resoluções.

**Critério de conclusão:** o jogador consegue movimentar-se de forma
consistente.

---

## FASE D — Primeira partida jogável ⏸

**Aguarda autorização (após FASE C).**

- [ ] Introduzir fragmentos de energia.
- [ ] Implementar pontuação (10 pts/fragmento; 1 pt/segundo — valores
      provisórios e configuráveis).
- [ ] Implementar colisões (CORE–fragmento, CORE–limites).
- [ ] Implementar fim de partida.
- [ ] Implementar reinício.

**Critério de conclusão:** partida completa de ponta a ponta, com reinício
imediato.

---

## FASE E — Mecânica REWIND ⏸

**Aguarda autorização (após FASE D).**

- [ ] Registar trajetórias (coordenadas normalizadas + referência temporal,
      com limite de amostras).
- [ ] Reproduzir trajetórias (interpolação, sem saltos visuais).
- [ ] Desenhar o ECHO (translúcido, cor distinta).
- [ ] Validar colisões com o ECHO (regra proposta: fim de partida — a validar
      em testes).
- [ ] Limitar o número de ECHOs.
- [ ] Testar em diferentes resoluções e dispositivos.

**Critério de conclusão:** o ECHO reproduz corretamente a trajetória
anterior e a colisão é percebida como justa.

---

## FASE F — Apresentação ⏸

**Aguarda autorização (após FASE E).**

- [ ] Menu principal (identidade REWIND, Play, melhor pontuação, definições).
- [ ] Ecrã de resultados (pontuação, recorde, tempo, fragmentos, jogar
      novamente, menu).
- [ ] Recordes persistidos.
- [ ] Pausa e suspensão em segundo plano.
- [ ] Efeitos visuais (rasto, recolha, transições).
- [ ] Som e definições, quando apropriado.

**Critério de conclusão:** fluxo completo de interface navegável e legível em
ecrãs pequenos.

---

## FASE G — Testes ⏸

**Aguarda autorização (após FASE F).**

- [ ] Testar em dispositivos reais.
- [ ] Avaliar os controlos.
- [ ] Verificar se a mecânica do ECHO é compreendida.
- [ ] Avaliar a dificuldade e a progressão.
- [ ] Corrigir problemas de desempenho.
- [ ] Recolher feedback de jogadores.

**Critério de conclusão:** lista de problemas conhecidos avaliada e corrigida
por prioridade.

---

## FASE H — Publicação ⏸

**Aguarda autorização (após FASE G).**

- [ ] Avaliar a monetização (gratuito com anúncios entre partidas; compra
      única para remover anúncios).
- [ ] Implementar anúncios apenas depois de validar a experiência principal.
- [ ] Rever privacidade e permissões.
- [ ] Gerar um AAB assinado.
- [ ] Verificar disponibilidade do título "REWIND".
- [ ] Preparar a publicação na Google Play.

**Critério de conclusão:** submissão na Google Play preparada.

---

## Documentação do agente — concluída

Sessão de preparação do agente (sem implementação do jogo).

- [x] Criar `AGENTS.md` (regras gerais de trabalho).
- [x] Criar `MVP_PLAN.md` (âmbito da primeira versão e etapas).
- [x] Criar Skill `android-kotlin` (`.opencode/skills/`).
- [x] Criar Skill `game-2d`.
- [x] Criar Skill `rewind-mechanics`.
- [x] Criar Skill `debug-testing`.
- [x] Criar Skill `android-build`.
- [x] Verificar frontmatter e correspondência nome/pasta das Skills.

**Restrições respeitadas:** nenhum código de jogo, nenhuma alteração de
configuração de compilação, nenhum commit.

---

## Regras de acompanhamento

- Atualizar este ficheiro quando uma tarefa for concluída ou quando surgir
  um bloqueio relevante.
- Não implementar funcionalidades de fases futuras sem autorização.
- Não declarar tarefas concluídas sem verificação.
