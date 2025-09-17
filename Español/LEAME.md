# CraftsMine (TEP)
Bienvenido a este proyecto que ha hecho lo imposible 😉! Imagínate un servidor compatible con Minecraft Pocket o como algunos le dicen Craftsman con Minecraft Bedrock!

Muchos me decían que era imposible pero ya me conocen así que accepte el reto y aquí estoy!

**ESTE ES EL PRIMER SOFTWARE QUE HACE QUE LOS JUGADORES DE MINECRAFT POCKET(0.15.x) ENTREN A SERVIDORES MINECRAFT BEDROCK(1.7.0)!!**

## Requisitos:
Tener instalado Java8 o superior.

Tener instalado Maven.

Conexión a Internet.

Tener abierto el puerto que vas a usar para abrir el servidor.

## Este es un??...
Esto es un **SOFTWARE** No es un **plugin** de ningún software de servidor de Minecraft.

## Como lo Consigo?
Simplemente entrando a Lanzamientos y descargando el jar que más gustes!

## Como Funciona?
Este proxy traduce los datapacks de Minecraft pocket a Minecraft Bedrock y viceversa algo así:

MCPE->CraftsMine Proxy Traductor->Servidor Minecraft bedrock.

Servidor Minecraft bedrock->CraftsMine Proxy Traductor->MCPE

**Recuerda que este Software envia al jugador de Minecraft pocket como Minecraft bedrock 1.7!**

## Que Cosas Funcionan?
aquí les dejo una lista de cosas que funcionan en este **Software**!

**----------------------------------**

✔️ **Funciona Bien**

❕ **Funciona con Bugs**

❌ **No Funciona**

**----------------------------------**

  - ✔️ Sonidos
  - ✔️ Login de Datapackets de servidor(Apreton de Manos)
  - ✔️ Skin
  - ✔️ Chat
  - ✔️ Comandos
  - ✔️ Player Spawn
  - ✔️ Tiempo
  - ✔️ Clima
  - ✔️ Cambio de modo de juego(Supervivencia, Creativo, Aventura y Espectador)
  - ✔️ ScoreBoard
  - ✔️ Bossbars
  - ❕ Efecto de Posiciones
  - ❕ Animaciones
  - ❕ Partículas
  - ❕ Playerlist
  - ❕ Interacción de jugador
  - ❕ Vida del Jugador
  - ❕ Hambre del Jugador
  - ❕ Respawn
  - ❕ Multi Mundo
  - ❕ PVP
  - ❌ Xbox auth
  - ❌ Generación de terreno
  - ❌ Movimiento
  - ❌ Entidades (incluyendo jugadores)
  - ❌ Inventario
  - ❌ Niveles
  - ❌ UI/Forms

  Y más...

## Versiones Probadas de MCPE:
  MCPE 0.15.0 hasta MCPE 0.15.10

## Como instalar:
Simplemente coloque el archivo jar como inicializador del programa por ejemplo en un panel Pterodactyl simplemente pongalo en el directorio principal y renombre a **server.jar** o como se llame el archivo jar que **inicia el servidor**! recuerda que el programa está escrito en **Java**!

Para que el jugador de Minecraft pocket se una con jugadores de Minecraft bedrock 1.21 o la versión actual puedes usar [Nukkit PetteriM1 Edition](https://github.com/PetteriM1/NukkitPetteriM1Edition/)

## Configuración:
La configuración está en el directorio principal del servidor es el archivo config.yml (recuerda encender el servidor y apagar para que se genere la configuración!)

#### bindAddress
Es la IP donde se abre el servidor CraftsMine (la mayoría de veces no es necesario modificar)

#### port
Es el puerto donde se abre el servidor CraftsMine

#### motd
Es el mensaje del día que mostrara el servidor CraftsMine

#### submotd
Es el sub mensaje del día que parece abajo del motd cuando esta en lan CraftsMine

#### maxplayers
Es el número máximo de jugadores que accepta el servidor CraftsMine

#### shutdownMessage
Es el mensaje que muestra al apagar el proxy

#### bedrockAddress
Es el ip del servidor de destino Minecraft bedrock

#### bedrockPort
Es el puerto del servidor de destino Minecraft bedrock

#### debug
Si está activado envia mensajes de debug a la consola

#### gcCollectionDiconnectPlayer
Si está activado libera memoria cada vez que un jugador se desconecta del servidor

#### consoleLog
Si está activado genera un archivo console.log

#### gcCollectionAuto
Si está activado libera memoria cada ciclo de gcCollectionTime

#### gcCollectionTime
tiempo en milisegundos para el ciclo de gcCollectionAuto

#### langPlayer
código del idioma que se usará para los jugadores de 0.15.x

#### synchServer
sincronizar motd y jugadores con el proxy

#### timeSynchServer
tiempo en milisegundos para refrescar el synchServer(también se sincroniza al entrar un jugador al proxy)

#### versionBedrockServer
escojer versión de Minecraft bedrock (1.7, 1.8, 1.9 o 1.10)

## Apoyar al proyecto

Si quieres apoyar el proyecto puedes hacernos una donación en este [link](https://creadoresgames.blogspot.com/p/donaciones.html) por transferencia bancaria o por Paypal!
Muchas gracias ☺️

## Créditos:

  - [Barrel CREA Edition](https://github.com/Trollhunters501/Barrel-CREA-Edition/)
  - [MBNukkit](https://github.com/Trollhunters501/MBNukkit/)
  - [DragonProxy](https://github.com/robske110/DragonProxy/)
  - [Protocol Cloudburstmc](https://github.com/CloudburstMC/Protocol/)
  - [BedrockServerQuery](https://github.com/justin-eckenweber/BedrockServerQuery)
