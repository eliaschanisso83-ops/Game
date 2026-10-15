---
name: android-build
description: Compilação do projeto Android REWIND — inspeção do Gradle e das versões de Java/SDK, execução do build, diagnóstico de erros, uso do GitHub Actions existente, verificação de APK/AAB e instalação em dispositivo. Usar antes e depois de alterações que afetem a compilação.
---

# Build Android

## Regra de ouro

**Inspecionar antes de assumir.** Nunca declarar versões de Gradle, Java,
AGP ou SDK sem as ter lido do projeto ou do ambiente. Se o projeto ainda não
tiver configuração Android, essa é uma tarefa pendente (FASE B), não uma
suposição a preencher.

## 1. Inspecionar o estado real

Antes de compilar ou de criar configuração:

- Existe `settings.gradle.kts` / `build.gradle.kts`? Qual a raiz do projeto
  Gradle?
- Existe `gradlew`/`gradlew.bat` (wrapper)? Qual a versão em
  `gradle/wrapper/gradle-wrapper.properties`?
- Existe `local.properties` com `sdk.dir`? Existe `ANDROID_HOME`/
  `ANDROID_SDK_ROOT` no ambiente?
- Existe `app/src/main/AndroidManifest.xml`? `compileSdk`, `minSdk`,
  `targetSdk` declarados?
- Versão do Java: `java -version` (o ambiente Termux deste projeto tinha
  **OpenJDK 17** — confirmar de novo antes de usar).
- Existe `.github/workflows/`? Que workflows e que comandos executam?

Registar o que foi encontrado. Se algo não existir, reportar — **não
inventar**.

## 2. Compilar

- Com wrapper no projeto: `./gradlew <tarefa>`.
- Sem wrapper: usar Gradle instalado apenas se existir; **não instalar
  ferramentas desnecessárias** — se não houver Gradle, reportar como
  bloqueio da FASE B.
- Tarefas habituais (quando o projeto as tiver):
  - `assembleDebug` → APK de teste.
  - `test` / `testDebugUnitTest` → testes unitários.
  - `lint` → análise estática.
  - `bundleRelease` → AAB (apenas em FASE H, com autorização).
- Compilar após alterações significativas; não assumir que compila.

## 3. Diagnóstico de erros de build

Processo:

1. Ler a mensagem **completa** — a primeira linha raramente é a causa.
2. Identificar o ficheiro/linha ou a dependência envolvida.
3. Distinguir categorias:
   - **Erros de sintaxe/Kotlin** → corrigir no ficheiro indicado.
   - **Erros de versão** (AGP/Gradle/JDK incompatíveis) → verificar a tabela
     de compatibilidade; nunca "resolver" mexendo em versões sem registar a
     justificação.
   - **SDK em falta** (`compileSdk` não encontrado) → falta de instalação de
     SDK: reportar, não omitir a linha de build.
   - **Dependências** (download falhou) → problema de rede ou repositório;
     não adicionar repositórios novos sem justificação.
   - **Lint/_warnings** → não suprimir cegamente.
4. Aplicar a correção **mínima**.
5. Re-executar o build e registar o resultado com evidência.

## 4. GitHub Actions

- **Verificar primeiro** se já existe um workflow funcional
  (`.github/workflows/*.yml`). Se existir, **não substituir** sem
  justificação e autorização (AGENTS.md).
- Se não existir e a FASE B estiver autorizada, criar um workflow mínimo
  que: faz checkout, configura JDK, corre o Gradle, publica o APK como
  artefacto.
- Analisar falhas de CI lendo o log do job — mesmo processo do ponto 3.
- **Não executar ações remotas sem autorização explícita** (sem triggers de
  workflow, sem dispatch manual, sem push).

## 5. Verificação de artefactos

Depois de um build com sucesso, **verificar fisicamente** que o artefacto
existe e é plausível:

- Caminho esperado: `app/build/outputs/apk/debug/app-debug.apk`
  (variável por configuração — ler o caminho real).
- Confirmar: existência, tamanho não nulo, data de modificação recente.
- Verificar a assinatura quando relevante (debug keystore).
- Apenas depois de confirmado, registar como **SUCESSO** (Skill
  `debug-testing`).

Para AAB: `app/build/outputs/bundle/...` — só na FASE H, com autorização.
Nunca gerar nem assinar AAB fora da fase de publicação.

## 6. Testes no dispositivo Android

- Dispositivo ligado e depuração USB/sem fio ativa: `adb devices` deve
  mostrar o dispositivo.
- Instalar: `adb install -r <caminho do APK>`.
- Arrancar e observar: o app abre sem crash; verificar também no logcat.
- Testar: arranque, movimento, pausa, segundo plano, retoma, fim e reinício
  de partida.
- **Sem dispositivo disponível:** compilar pode ser possível; instalação e
  teste em dispositivo ficam **NÃO EXECUTADOS** — dizer isso claramente.

## Limites desta sessão/plano

- Não publicar aplicações.
- Não executar ações remotas (CI, lojas) sem autorização.
- Não alterar configuração de compilação sem necessidade.
- Não instalar dependências, SDKs ou ferramentas desnecessárias.

## Critérios de qualidade

- Versões declaradas com base no que foi inspecionado, nunca presumidas.
- Build reproduzível: mesmo comando, mesmo resultado.
- Erros explicados com causa e correção proposta.
- Resultados de build sempre com evidência (comando + saída observada).

## Erros comuns a evitar

- Copiar um template de `build.gradle.kts` com versões inventadas.
- Afirmar "compila" sem correr o build.
- Substituir um workflow existente funcional.
- Instalar Gradle/SDK/Android Studio "por precaução".
- Tratar erro de ambiente (sem SDK) como erro de código, ou vice-versa.
- Registar APK como gerado sem verificar o ficheiro.
