# AGENTS.md — REWIND

Instruções gerais para todas as sessões de desenvolvimento do projeto.
Em caso de conflito: **GDD.md** (conceito) > **MVP_PLAN.md** (âmbito da 1ª
versão) > **AGENTS.md** (regras de trabalho) > Skills (execução técnica).

## O projeto

- Nome: **REWIND** — jogo 2D nativo para Android (arcade / sobrevivência /
  estratégia), visto de cima.
- Linguagem principal: **Kotlin**. Desenho 2D inicial: **Android Canvas**.
- **Não** utilizar HTML, CSS ou JavaScript no núcleo do jogo.
- Plataforma alvo: Android apenas, até a versão estar concluída e validada.

## Antes de implementar

1. Consultar `GDD.md` para o conceito e os requisitos.
2. Consultar `MVP_PLAN.md` para decidir se a funcionalidade pertence à
   primeira versão.
3. Inspecionar o código existente antes de o modificar. Ler os ficheiros
   relevantes por completo.
4. Carregar a Skill adequada ao tipo de tarefa
   (`android-kotlin`, `game-2d`, `rewind-mechanics`, `debug-testing`,
   `android-build`).

## Regras de trabalho

- Alterações **pequenas e controladas**, verificáveis uma a uma.
- Não reescrever ficheiros completos sem necessidade justificada.
- Não substituir ficheiros existentes que ainda cumprem o seu propósito.
- Não adicionar dependências sem justificação técnica registada.
- Não introduzir frameworks nem bibliotecas externas sem autorização.
- Não avançar para funcionalidades de fases futuras sem autorização
  explícita do utilizador.
- Não criar código de uma fase enquanto a fase anterior estiver por concluir.
- Preservar o workflow de compilação existente; não o substituir sem
  justificação e autorização.
- Não fazer commits, pushes ou publicações remotas sem autorização explícita.
- Não publicar aplicações nem executar ações remotas (CI, lojas) sem
  autorização.

## Qualidade e verificação

- Executar os testes e a compilação disponíveis depois das alterações.
- **Nunca inventar resultados** de compilação ou de testes.
- **Nunca declarar uma tarefa concluída sem verificar o resultado.**
- Distinguir sempre: teste executado com sucesso · teste com falha · teste
  não executado · resultado por confirmar.
- Explicar os problemas encontrados com clareza e indicar a correção
  proposta.
- Se faltar informação, inspecionar primeiro o ambiente (ficheiros,
  configuração, ferramentas) antes de pedir ao utilizador para repetir
  dados disponíveis.
- Reportar limitações do ambiente (ex.: sem Android SDK, sem dispositivo
  ligado) em vez de contorná-las com afirmações não verificadas.

## Acompanhamento

- Atualizar `TODO.md` quando o progresso real mudar ou quando surgir um
  bloqueio relevante.
- `TODO.md` reflete o estado real do projeto, não a intenção.
- O estado do jogo está em `README.md`; o conceito está em `GDD.md`.
