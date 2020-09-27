# Challenge MELI

### Aplicación:
La presente aplicación provee información sobre una dirección IP y brinda estadísticas sobre la utilización del servicio.

### Requisitos:
- Java 8 (testeado en 1.8.0_231)
- Maven 3 (testeado en 3.6.2)
- Docker (testeado en 2.1.0.4)

### Aclaraciones antes de instalar la aplicación:
La aplicación corre en el puerto 8080. Se puede modificar el port binding en el archivo `docker-compose.yml`.

Redis corre en el puerto 6379 (el puerto está bindeado al puerto del host por si se quiere inspeccionar la bd desde un cliente, pero no es necesario para el funcionamiento de la app). Si hay conflicto, modificar o eliminar el puerto en el archivo `docker-compose.yml` (`ports` property).

Se utiliza un Volume para que la información de Redis quede persistida. Se creará una carpeta `data` cuando se levante el container (en el directorio del proyecto). Hay que habilitar *Shared Drives* en Docker para que el volume funcione.

### Pasos para instalar:
1) Clonar el repo en local: `git clone https://github.com/pabca/challenge.git`
2) Ir al directorio del proyecto: `cd challenge`
3) Buildear el proyecto con `mvn clean install`. Este comando descargará las dependencias, correrá los unit tests y generará un paquete `challenge-1.0.0.jar` en la carpeta `./target`.
4) Ejecutar el comando `docker-compose up --build -d`. Este comando hará el build de las Docker images de redis y de la aplicación. Luego levantará 2 containers para correr dichas imágenes.
5) Cuando finalice, se puede verificar que los containers estén corriendo con el comando `docker ps`. 
6) Abrir una ventana del navegador e ingresar `http://localhost:8080`.
7) Se cargará una interfaz web donde se podrá utilizar la aplicación.

### Comandos útiles:
Una vez que la aplicación está corriendo, se puede parar utilizando el comando `docker-compose down`.

Para volver a levantar la aplicación, no hace falta volver a crear las Docker images, por lo tanto utilizar el comando `docker-compose up -d`.

Para ver los logs de la aplicación, utilizar `docker logs challenge_api_1`. Nota: `challenge_api_1` es el nombre que utilizar Docker para el container que corre la aplicación, si el comando no funciona, averiguar el id o nombre del container utilizando `docker ps` y reemplazar en el comando `docker logs <containerId|containerName>`.

### Aplicación:
La aplicación está conformada por una UI y una API.

La API está desarrollada en Java, utilizando Spring Boot. Expone unos endpoints que son llamados desde la UI.

La UI es bastante simple, se desarrolló en HTML y JavaScript. Se aprovecha Tomcat para servir contenido estático (archivos .html y .js). Su objetivo es proveer una interfaz para utilizar la API.

Se utiliza Redis para almacenar información sobre las invocaciones al servicio (para generar las estadísticas).

### Interfaz de usuario (UI):
La aplicación presenta una simple UI para interactuar con ella.

Hay 3 botones/acciones:

1) **Obtener información**: permite obtener información sobre la dirección IP ingresada en la caja de texto.
2) **Actualizar estadísticas**: las estadísticas sobre distancias promedio, mínima y máxima se cargan al entrar a la UI, pero luego hay que utilizar esta acción para actualizarlas.
3) **Borrar estadísticas**: permite reiniciar las estadísticas. Toda la información sobre invocaciones al servicio previas se borrará.

Si se produce algún error, se mostrará el mensaje debajo de todo.

### API:
> (Nota: todos los endpoints deben preceder `http://localhost:8080` para hacer los llamados).
- `GET /location?originIp=<ipAddress>`: retorna información sobre el país del cual prodece la dirección IP.
- `GET /stats`: retorna estadísticas (distancia promedio, distancia mínima y distancia máxima).
- `DELETE /stats`: reiniciar las estadísticas.
- `GET /stats/all`: retorna información sobre todas las invocaciones al servicio (nota: esta información no está disponible en la UI ya que no era parte del enunciado).

Ejemplos:
- `GET` http://localhost:8080/location?originIp=201.127.37.14
- `GET` http://localhost:8080/stats/all
- `GET` http://localhost:8080/stats
- `DELETE` http://localhost:8080/stats

### Posibles improvements:
- Se podría cachear en Redis la respuesta de la API que provee información sobre el país (es información estática que no cambia).
- También se podría cachear la respuesta de la API que retorna información sobre el tipo de cambio de la moneda. Dejaría de ser *real-time*, pero se podría utilizar un TTL acorde (e.g. 10 minutos) que resulte acorde a la información.
> Nota: sin embargo, la llamada que más tiempo tarda es la que provee la ubicación de la dirección IP, y esa llamada no se puede evitar (no se puede garantizar que una IP provenga siempre de la misma ubicación).
- Mejorar la presentación de la UI.

### Known TODOs:
- Agregar unit tests para los Services.

### Notas:
- Este archivo, al igual que los mensajes que se muestran en la UI, están en castellano. El código y la documentación dentro del mismo están en inglés.
- Para algunas monedas, no hay información disponible sobre su cotización. En esos casos, se muestra el mensaje correspondiente.
