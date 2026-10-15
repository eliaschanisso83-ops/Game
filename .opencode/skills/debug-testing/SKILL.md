---
name: debug-testing
description: Processo de investigação e verificação de problemas no REWIND — reproduzir, recolher evidências, encontrar causa, alterar o mínimo, executar testes, verificar e documentar. Distingue sempre sucesso, falha, não executado e por confirmar. Usar ao corrigir bugs ou validar alterações.
---

# Depuração e testes

## Processo (seguir por esta ordem)

1. **Reproduzir** o problema, quando possível. Registar exatamente os passos
   e as condições (dispositivo, resolução, estado do jogo).
2. **Recolher evidências:** mensagem de erro completa, stacktrace, registos
   (`logcat`), captura de ecrã, valores do estado envolvido.
3. **Identificar a causa provável** — a causa, não o sintoma. Formular uma
   hipótese verificável.
4. **Inspecionar os ficheiros envolvidos** por completo antes de os alterar.
   Confirmar a hipótese no código.
5. **Propor a alteração mínima** que corrige a causa. Sem refactors
   paralelos, sem "melhorias" na mesma alteração.
6. **Executar os testes** disponíveis (e a compilação).
7. **Verificar o resultado** — confirmar que o problema desapareceu e que
   nada regrediu.
8. **Documentar o que foi confirmado**, o que falhou e o que ficou por
   testar. Atualizar `TODO.md` se o progresso real mudou.

## Níveis de evidência — usar sempre a etiqueta correta

| Etiqueta | Significado |
| --- | --- |
| **SUCESSO** | Executado e observado a funcionar. |
| **FALHA** | Executado e observado a falhar (registar o erro). |
| **NÃO EXECUTADO** | Não foi possível executar (ferramenta/dispositivo em falta). |
| **POR CONFIRMAR** | Alteração feita, resultado ainda não observado. |

Regras:

- **Nunca** declarar sucesso sem evidência.
- **Nunca** inventar resultados de compilação ou de testes.
- **Nunca** escrever "testado" ou "funciona" para algo que não foi executado.
- Se o ambiente não permite executar (sem SDK, sem dispositivo), dizê-lo
  claramente e deixar a tarefa como pendente.

## Testes relevantes para o REWIND

### Movimento
- CORE segue o dedo nos quatro sentidos e nos cantos.
- CORE não ultrapassa os limites da arena.
- Resultado equivalente em taxas de quadro diferentes (60/120 Hz) e em
  resoluções diferentes.
- `ACTION_CANCEL` não deixa o CORE em movimento persistido.
- Retoma de segundo plano não teletransporta o CORE.

### Colisões
- CORE–fragmento: recolha ocorre dentro da área esperada, pontuação +10.
- CORE–limite: clamp correto, sem sair nem ficar "preso".
- CORE–ECHO: deteção no frame esperado; consequência conforme configuração
  (regra proposta, não decidida — MVP_PLAN secção 4).
- Nenhuma colisão CORE–fragmento atribuída a ECHO.

### Reprodução de trajetórias
- Gravar → reproduzir: posições dentro de tolerância.
- Reprodução determinística e idempotente.
- Sem saltos visuais (interpolação aplicada).
- Trajetória gravada num ecrã reproduz corretamente noutro.
- Dados inválidos descartados sem crash.
- Limite de amostras respeitado; memória não cresce entre partidas.

### Estados do jogo
- Transições: `MENU → READY → PLAYING → PAUSED → GAME_OVER` e regresso.
- Pausa congela a simulação (pontuação, tempo e posições não avançam).
- Segundo plano suspende a simulação.
- Reinício limpa o estado da partida anterior (sem restos de ECHO/pontuação).

### Persistência
- Melhor pontuação sobrevive a fechar e reabrir a aplicação.
- Valor inválido/corrompido em preferências ⇒ valor por omissão, sem crash.
- Escrita não ocorre durante o ciclo de desenho.

## Ferramentas

- Compilação: ver Skill `android-build`.
- Registos Android: `adb logcat` quando houver dispositivo/`adb`
  disponível.
- Se não houver dispositivo ligado: executar o que for possível (unit tests,
  build) e registar o resto como **NÃO EXECUTADO**.

## Ao reportar

Estruturar a resposta:

1. Problema observado (com passos).
2. Evidência recolhida.
3. Causa encontrada (ficheiro e linha).
4. Alteração aplicada.
5. Verificação executada + etiqueta de evidência.
6. Resto por testar / próximos passos.

## Erros comuns a evitar

- "Corrigido" sem ter executado nada.
- Corrigir o sintoma sem encontrar a causa.
- Alterar vários ficheiros sem necessidade na mesma correção.
- Afirmar compilação sem correr o build.
- Testar só no emulador e concluir sobre dispositivos reais.
- Silenciar exceções para "fazer passar" o teste.
