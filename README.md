# Boardcamp

[Spring Boot](http://projects.spring.io/spring-boot/) App.


## Deploy URL:
// TODO alterar para URL correta após o deploy
[https://github.com/thiagomayrink/boardcamp-api](https://github.com/thiagomayrink/boardcamp-api)

## Rotas:
As rotas podem ser importadas para o thunder client atráves das collections localizadas na pasta: [.api-collections](https://github.com/thiagomayrink/boardcamp/tree/main/.api-collections) localizada na raíz do projeto.

## Executando o projeto local:

Para fazer o build e execução da aplicação serão necessários:

- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven 4](https://maven.apache.org)
- [Postgress](https://www.postgresqltutorial.com)

## Setup inicial
1. Com o postgress configurado, criar usuário, senha e uma database.
2. Preencher as variáveis de ambiente conforme descrito no arquivo [.env.example](https://github.com/thiagomayrink/boardcamp/blob/main/.env.example)
3. Renomear o arquivo **.env.example** para **.env**

## Executando a aplicação local

Existem várias formas de executar um app Spring Boot localmente. Uma delas é através do [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) executando o comando:

```shell
mvn spring-boot:run
```