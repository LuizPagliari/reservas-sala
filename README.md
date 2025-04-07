# Sistema de Reserva de Salas - Microserviços

Este projeto implementa um sistema de reserva de salas utilizando uma arquitetura de microserviços. O sistema foi desenvolvido como parte de um projeto acadêmico para demonstrar os conceitos de microserviços, comunicação entre serviços e persistência de dados.

## Arquitetura

O sistema é composto por três microserviços independentes:

### 1. Microserviço de Usuários
- **Porta**: 8080
- **Responsabilidade**: Gerenciamento de usuários do sistema
- **Funcionalidades**:
  - Cadastro, atualização e exclusão de usuários
  - Diferentes tipos de usuários (ADMIN, PROFESSOR, ALUNO)
  - Verificação de status de ativação

### 2. Microserviço de Salas
- **Porta**: 8082
- **Responsabilidade**: Gerenciamento de salas disponíveis para reserva
- **Funcionalidades**:
  - Cadastro, atualização e exclusão de salas
  - Informações sobre capacidade e recursos
  - Controle de disponibilidade

### 3. Microserviço de Reservas
- **Porta**: 8081
- **Responsabilidade**: Gerenciamento de reservas de salas
- **Funcionalidades**:
  - Criação, atualização e cancelamento de reservas
  - Verificação de conflitos de horários
  - Integração com os serviços de usuários e salas

## Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento dos microserviços
- **Spring Data JPA**: Persistência de dados
- **PostgreSQL**: Banco de dados relacional
- **Docker**: Containerização dos serviços
- **Docker Compose**: Orquestração dos containers
- **Swagger/OpenAPI**: Documentação e testes das APIs
- **Lombok**: Redução de código boilerplate

## Pré-requisitos

- Docker e Docker Compose instalados
- Git para clonar o repositório

## Como Executar

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/LuizPagliari/reserva-sala.git
   cd reserva-sala
   ```

2. **Inicie os containers com Docker Compose**:
   ```bash
   docker compose up -d
   ```

3. **Acesse as APIs através do Swagger UI**:
   - Usuários: http://localhost:8080/swagger-ui.html
   - Salas: http://localhost:8082/swagger-ui.html
   - Reservas: http://localhost:8081/swagger-ui.html

## Estrutura do Projeto

```
reserva-sala/
├── usuario/                 # Microserviço de usuários
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/faculdade/usuario/
│   │   │   │       ├── config/           # Configurações
│   │   │   │       ├── controller/       # Controladores REST
│   │   │   │       ├── model/            # Entidades
│   │   │   │       ├── repository/       # Repositórios JPA
│   │   │   │       └── service/          # Lógica de negócio
│   │   │   └── resources/
│   │   │       └── application.properties # Configurações
│   ├── Dockerfile                         # Configuração Docker
│   └── pom.xml                            # Dependências Maven
├── sala/                    # Microserviço de salas
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/faculdade/sala/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   │       └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── reserva-sala/            # Microserviço de reservas
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/faculdade/reservasala/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   │       └── application.properties
│   ├── Dockerfile
│   └── pom.xml
└── docker-compose.yml       # Configuração Docker Compose
```

## Fluxo de Dados

1. O usuário acessa o microserviço de reservas para criar uma nova reserva
2. O microserviço de reservas verifica se o usuário existe no microserviço de usuários
3. O microserviço de reservas verifica se a sala existe e está disponível no microserviço de salas
4. Se todas as verificações passarem, a reserva é criada
5. O microserviço de reservas atualiza o status da sala no microserviço de salas
