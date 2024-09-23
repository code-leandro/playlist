# Projeto: Playlist Sugerida por Temperatura

Este projeto foi desenvolvido tem como objetivo, criar um serviço capaz de sugerir playlists musicais com base na temperatura de uma cidade fornecida. O serviço foi construído usando **Java** e o framework **Spring Boot** e está rodando em uma instância EC2 da AWS.

![Arquitetura do Projeto](https://leandro-hero.s3.us-east-2.amazonaws.com/arquitetura-playlist.png)

## Funcionalidades

A API desenvolvida permite sugerir playlists de acordo com a temperatura atual da cidade consultada. O comportamento é o seguinte:

- **Temperatura > 25ºC**: Sugerir músicas de Pop.
- **Temperatura entre 10ºC e 25ºC**: Sugerir músicas de Rock.
- **Temperatura < 10ºC**: Sugerir músicas Clássicas.

## Tecnologias Utilizadas
- **Java 21**: Linguagem principal escolhida por sua robustez e integração com frameworks poderosos.
- **Spring Boot**: Utilizado para a construção da API, devido à sua simplicidade e eficiência em criar aplicações Java rapidamente.
- **Redis**: Utilizado como cache, aumentando a resiliência do sistema. Caso a API externa de temperatura (Open Weather) esteja fora do ar, as temperaturas recentes podem ser recuperadas do cache.
- **API Open Weather**: Integração com o serviço [OpenWeather](https://openweathermap.org/) para obter a temperatura das cidades consultadas.

### Infraestrutura
- **Docker**: A aplicação e o Redis foram containerizados para garantir consistência no ambiente de execução.
- **AWS EC2**: A aplicação está hospedada em uma instância EC2 para fácil controle e escalabilidade manual.
- **AWS ECR - Amazon Elastic Container Registry**: local que fica armazenado a imagem docker da aplicação
- **Github**: o github foi escolhido como repositório de código da aplicação.
- **Github Action**: para facilitar o deploy da aplicação foi utilizado o Github Action. Quando é feito um merge de um Pull Request para main, o deploy é iniciado e atualiza a imagem docker atual da 
aplicação que está no ECR da AWS

### Melhorias na Infraestrutura
- **ECS - Amazon Elastic Container Service**: evoluir a infra de EC2 para ECS para melhorar a orquestração dos containers.
- **Load Balancing**: poderia ser criado um load balancing e um auto scaling para automatizar a escalabilidade da aplicação.

### Postman
- Arquivo da **collection** do Postman para testar a API: [Postman Collection](https://leandro-hero.s3.us-east-2.amazonaws.com/Leandro+Souza+-+Postman+Collection+-+Hero.postman_collection.json)
- Arquivo de **environment** com o IP da instância EC2: [Postman Environment AWS](https://leandro-hero.s3.us-east-2.amazonaws.com/AWS.postman_environment.json)
- Arquivo de **environment** para execução local: [Postman Environment Local](https://leandro-hero.s3.us-east-2.amazonaws.com/LOCAL.postman_environment.json)



## Execução Local

Certifique-se de que não há outra aplicação executando na porta 80 e que tenha o docker instalado na máquina.

### Clonar o repositório:
```
git clone https://github.com/code-leandro/playlist.git
```

### Passos:
1. Sugere-se criação de uma network do docker executando:
```
docker network create mynetwork
```

2. Execução do Redis

O Redis é um componente essencial da aplicação e está rodando na mesma instância EC2.
Execução local na rede mynetwork criada acima:
```
docker run -d --name redis-container -p 6379:6379 --network mynetwork redis
```

3. Aplicação
```
docker run -d --name playlist-container -p 80:80 -e HOST_REDIS=redis-container --network mynetwork 
```

4. Realizar chamadas para aplicação.
Exemplos:
```
curl "http://localhost/playlist?city=Divinopolis"
curl "http://localhost/playlist?city=Sao Paulo"
```
Ou seguir as requisições que estão do Postman do projeto acima disponível para download.


