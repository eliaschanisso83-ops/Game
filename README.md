# REWIND

> **"O teu passado muda o teu presente."**

## Descrição

REWIND é um jogo 2D de sobrevivência visto de cima, de género arcade,
sobrevivência e estratégia. O jogador controla uma entidade energética (CORE)
numa arena, recolhe fragmentos de energia e sobrevive. A mecânica central é a
**memória dos movimentos**: os movimentos de cada partida são registados e
reproduzidos na partida seguinte através de uma entidade fantasma, o **ECHO**,
obrigando o jogador a adaptar a sua rota para não colidir com o seu próprio
passado.

## Plataforma

- **Alvo:** Android (versão vertical, utilizável com uma mão).
- Outras plataformas ficam fora de âmbito até a versão Android estar concluída
  e validada.

## Tecnologias previstas

| Área | Tecnologia |
| --- | --- |
| Linguagem | Kotlin nativo para Android |
| Desenho 2D | Android Canvas |
| Ambiente de trabalho | Termux (Android) |
| Agente de programação | OpenCode |
| Controlo de versão | GitHub |
| Integração contínua | GitHub Actions (compilação remota) |
| Testes | Dispositivo Android real |

## Estado atual

**FASE A — Preparação e documentação.**

- Ambiente de trabalho inspecionado.
- Documentação de referência criada: `GDD.md`, `README.md`, `TODO.md`.
- Documentação de agente criada: `AGENTS.md`, `MVP_PLAN.md` e as Skills em
  `.opencode/skills/`.
- Estrutura de código do jogo **ainda não criada**.

### ⚠️ Aviso

**O desenvolvimento do jogo ainda não começou.** Não existe código de jogo,
não existe aplicação Android e não há nada para compilar ou instalar nesta
fase. Nenhuma funcionalidade foi implementada.

## Documentação

| Documento | Função |
| --- | --- |
| [GDD.md](GDD.md) | Especificação completa do jogo — referência oficial para todas as fases. |
| [README.md](README.md) | Apresentação do projeto e estado atual (este ficheiro). |
| [TODO.md](TODO.md) | Fases A–H, tarefas e critérios de conclusão. |
| [AGENTS.md](AGENTS.md) | Regras gerais de trabalho para todas as sessões. |
| [MVP_PLAN.md](MVP_PLAN.md) | Âmbito da primeira versão jogável e etapas de implementação. |

## Próximos passos

A **FASE B (Aplicação Android mínima)** só será iniciada mediante autorização
explícita. Consulte [TODO.md](TODO.md) para o detalhe das fases.
