suprema

Este projeto utiliza o Quarkus, o framework Supersonic Subatomic Java, para construir e rodar a API. A seguir estão as instruções para rodar a aplicação em dev mode, rodar os testes, e usar o Docker com o Postgres.

Rodando a aplicação em Dev Mode

Para rodar a aplicação em dev mode, que permite live coding (alterações de código em tempo real), basta executar o comando Maven para iniciar a aplicação. Em dev mode, a aplicação será disponibilizada em http://localhost:8080. Além disso, o Quarkus possui uma interface para desenvolvimento, chamada Dev UI, que pode ser acessada em http://localhost:8080/q/dev/.

Rodando os testes

Para rodar os testes da aplicação, basta executar o comando Maven para executar todos os testes configurados no projeto.

Rodando o Docker com Postgres

Para rodar o Docker com o Postgres, onde o banco de dados estará configurado, basta utilizar o comando docker-compose up. Esse comando irá iniciar os containers necessários para a aplicação, incluindo o Postgres.

Acessando a API Documentada com Swagger

Após rodar a aplicação, a API estará documentada e disponível no endereço http://localhost:8080/swagger-ui.
