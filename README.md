# Documentação da API

## **UserResource**

### **Base Path:** `/users/`

#### **1. GET `/users/`**
- **Descrição:** Retorna uma lista paginada de usuários.
- **Parâmetros de Query:**
  - `page` (opcional, padrão: 1): Número da página.
  - `size` (opcional, padrão: 20): Tamanho da página.
- **Resposta:**
  - **Status 200:** Retorna um objeto contendo os usuários.
  - **Status 500:** Erro interno do servidor.

#### **2. GET `/users/search`**
- **Descrição:** Retorna uma lista paginada de usuários filtrados.
- **Parâmetros de Query:**
  - `name` (opcional): Nome do usuário.
  - `email` (opcional): Email do usuário.
  - `access` (opcional): Nível de acesso (somente para Admins).
  - `sector` (opcional): Setor (somente para Operadores).
  - `page` (opcional, padrão: 1): Número da página.
  - `size` (opcional, padrão: 20): Tamanho da página.
  - `orderby` (opcional, padrão: `name`): Campo de ordenação (`name` ou `email`).
  - `ascending` (opcional, padrão: `false`): Ordenação crescente ou decrescente.
- **Resposta:**
  - **Status 200:** Retorna os usuários filtrados.
  - **Status 400:** Parâmetros inválidos.
  - **Status 500:** Erro interno do servidor.

#### **3. GET `/users/{id}`**
- **Descrição:** Retorna um usuário pelo ID.
- **Parâmetros de Path:**
  - `id` (obrigatório): ID do usuário.
- **Resposta:**
  - **Status 200:** Retorna o usuário.
  - **Status 404:** Usuário não encontrado.
  - **Status 500:** Erro interno do servidor.

#### **4. PUT `/users/{id}`**
- **Descrição:** Atualiza um usuário pelo ID.
- **Parâmetros de Path:**
  - `id` (obrigatório): ID do usuário.
- **Body:** Objeto JSON representando o novo estado do usuário.
- **Resposta:**
  - **Status 200:** Retorna o usuário atualizado.
  - **Status 404:** Usuário não encontrado.
  - **Status 500:** Erro interno do servidor.

#### **5. POST `/users/`**
- **Descrição:** Adiciona um novo usuário.
- **Body:** Objeto JSON representando o usuário a ser criado.
- **Resposta:**
  - **Status 200:** Retorna o usuário criado.
  - **Status 400:** Dados inválidos.
  - **Status 500:** Erro interno do servidor.

---

## **ReportResource**

### **Base Path:** `/relatorios`

#### **1. GET `/relatorios`**
- **Descrição:** Retorna uma lista paginada de relatórios.
- **Parâmetros de Query:**
  - `page` (opcional, padrão: 1): Número da página.
  - `size` (opcional, padrão: 20): Tamanho da página.
- **Resposta:**
  - **Status 200:** Retorna os relatórios.
  - **Status 500:** Erro interno do servidor.

#### **2. GET `/relatorios/search`**
- **Descrição:** Retorna uma lista paginada de relatórios filtrados.
- **Parâmetros de Query:**
  - `text` (opcional): Texto para busca no título ou informações.
  - `type` (opcional): Tipo do relatório (`GERAL` ou `PERIODO`).
  - `generationFrom` (opcional): Ano inicial de geração.
  - `generationTo` (opcional): Ano final de geração.
  - `periodFrom` (opcional): Ano inicial do período.
  - `periodTo` (opcional): Ano final do período.
  - `failuresOnUpTo` (opcional): Número máximo de falhas.
  - `page` (opcional, padrão: 1): Número da página.
  - `size` (opcional, padrão: 20): Tamanho da página.
  - `orderby` (opcional, padrão: `date`): Campo de ordenação (`date` ou `title`).
  - `ascending` (opcional, padrão: `false`): Ordenação crescente ou decrescente.
- **Resposta:**
  - **Status 200:** Retorna os relatórios filtrados.
  - **Status 400:** Parâmetros inválidos.
  - **Status 500:** Erro interno do servidor.

#### **3. GET `/relatorios/{id}`**
- **Descrição:** Retorna um relatório pelo ID.
- **Parâmetros de Path:**
  - `id` (obrigatório): ID do relatório.
- **Resposta:**
  - **Status 200:** Retorna o relatório.
  - **Status 404:** Relatório não encontrado.
  - **Status 500:** Erro interno do servidor.

#### **4. PUT `/relatorios/{id}`**
- **Descrição:** Atualiza um relatório pelo ID.
- **Parâmetros de Path:**
  - `id` (obrigatório): ID do relatório.
- **Body:** Objeto JSON representando o novo estado do relatório.
- **Resposta:**
  - **Status 200:** Retorna o relatório atualizado.
  - **Status 404:** Relatório não encontrado.
  - **Status 500:** Erro interno do servidor.

#### **5. POST `/relatorios`**
- **Descrição:** Adiciona um novo relatório.
- **Body:** Objeto JSON representando o relatório a ser criado.
- **Resposta:**
  - **Status 200:** Retorna o relatório criado.
  - **Status 400:** Dados inválidos.
  - **Status 500:** Erro interno do servidor.

---

## **FailureResource**

### **Base Path:** `/falhas`

#### **1. GET `/falhas`**
- **Descrição:** Retorna uma lista paginada de falhas.
- **Parâmetros de Query:**
  - `page` (opcional, padrão: 1): Número da página.
  - `size` (opcional, padrão: 20): Tamanho da página.
- **Resposta:**
  - **Status 200:** Retorna as falhas.
  - **Status 500:** Erro interno do servidor.

#### **2. GET `/falhas/search`**
- **Descrição:** Retorna uma lista paginada de falhas filtradas.
- **Parâmetros de Query:**
  - `type` (opcional): Tipo da falha (`MECANICA`, `ELETRICA`, `SOFTWARE`, `OUTRO`).
  - `from` (opcional): Ano inicial de geração.
  - `to` (opcional): Ano final de geração.
  - `page` (opcional, padrão: 1): Número da página.
  - `size` (opcional, padrão: 20): Tamanho da página.
  - `orderby` (opcional, padrão: `date`): Campo de ordenação (`date` ou `description`).
  - `ascending` (opcional, padrão: `false`): Ordenação crescente ou decrescente.
- **Resposta:**
  - **Status 200:** Retorna as falhas filtradas.
  - **Status 400:** Parâmetros inválidos.
  - **Status 500:** Erro interno do servidor.

#### **3. GET `/falhas/{id}`**
- **Descrição:** Retorna uma falha pelo ID.
- **Parâmetros de Path:**
  - `id` (obrigatório): ID da falha.
- **Resposta:**
  - **Status 200:** Retorna a falha.
  - **Status 404:** Falha não encontrada.
  - **Status 500:** Erro interno do servidor.

#### **4. PUT `/falhas/{id}`**
- **Descrição:** Atualiza uma falha pelo ID.
- **Parâmetros de Path:**
  - `id` (obrigatório): ID da falha.
- **Body:** Objeto JSON representando o novo estado da falha.
- **Resposta:**
  - **Status 200:** Retorna a falha atualizada.
  - **Status 404:** Falha não encontrada.
  - **Status 500:** Erro interno do servidor.

#### **5. POST `/falhas`**
- **Descrição:** Adiciona uma nova falha.
- **Body:** Objeto JSON representando a falha a ser criada.
- **Resposta:**
  - **Status 200:** Retorna a falha criada.
  - **Status 400:** Dados inválidos.
  - **Status 500:** Erro interno do servidor.