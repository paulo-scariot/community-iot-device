# community-iot-device

A CIoTD é uma plataforma colaborativa para compartilhamento de acesso à dados de dispositivos IoT. O projeto utilizou o
Angular Material versão 18 e Java versão 21.

### Objetivo:

Realizar a integração de sistemas, através de uma plataforma colaborativa de cadastro de dispositivos IoT, para
fornecimento de insights para tomada de decisão de plantio.

### Instruções:

Contexto: Você está trabalhando em um projeto de auxílio à tomada de decisão para uma indústria agrária. A empresa
deseja montar uma Torre de Controle na qual possa visualizar, em tempo real, a volumetria de chuva no maior número de
localidades possíveis. Para isso, ela planeja utilizar uma plataforma colaborativa de registro e compartilhamento de
dados de dispositivos de IoT, cuja API é apresentada no anexo deste desafio. Construa uma aplicação web na qual seja
possível selecionar dispositivos IoT, na plataforma colaborativa, e que apresente uma listagem desses dispositivos com a
respectiva medição de suas métricas.

### Requisitos Funcionais:

1. Os usuários devem poder autenticar-se na aplicação para poder configurar e acessar os dados de monitoramento dos
   dispositivos
2. A aplicação deve ter uma funcionalidade de seleção de dispositivos, na qual são apresentados todos os dispositivos
   cadastrados na plataforma e o usuário pode configurar aqueles que deseja monitorar.
3. Para cada dispositivo que o usuário selecione para monitorar, a aplicação deve apresentar uma tela na qual seja
   possível indicar qual ou quais comandos disponibilizados pelo dispositivos devem ser utilizados;
4. A aplicação deve ter um dashboard de consulta, na qual são listados todos os dispositivos selecionados na interface
   definida em 2 e, para cada dispositivo, a resposta de cada um dos comandos selecionados em 3;
5. O acesso aos dados do dispositivo deve ser realizado utilizando o protocolo telnet, para a url cadastrada,
   enviando-se o comando selecionado e coletando a resposta.
6. A aplicação deve aplicar métodos para otimizar as requisições e reduzir o tempo de resposta.