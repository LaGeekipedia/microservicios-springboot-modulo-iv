# Módulo IV — Persistencia en Microservicios

Proyecto académico desarrollado durante el curso **Microservicios en Spring Boot con Spring Cloud**, correspondiente al **Módulo IV: Persistencia y Bases de Datos en Microservicios**.

Este repositorio reúne siete aplicaciones Spring Boot. Su propósito es practicar una arquitectura de microservicios con bases de datos independientes, persistencia relacional y documental, configuración centralizada, descubrimiento de servicios, mensajería asíncrona y separación lógica de comandos y consultas (CQRS).

> **Alcance:** entorno local de desarrollo y evaluación académica. Las credenciales de ejemplo, el token fijo del Gateway y las configuraciones aquí descritas **no son apropiados para producción**.

## 1. Objetivo de la evaluación

Cada alumno deberá:

1. Clonar este repositorio desde GitHub.
2. Abrir y compilar los proyectos Spring Boot.
3. Crear y ejecutar cuatro contenedores en Docker Desktop, con las bases de datos y los puertos indicados.
4. Configurar las variables de entorno requeridas por cada microservicio en IntelliJ IDEA.
5. Levantar el ecosistema en el orden recomendado.
6. Comprobar el registro de servicios en Eureka, la persistencia en PostgreSQL y MongoDB y el envío/consumo de eventos con RabbitMQ.
7. Ejecutar y mostrar las pruebas de funcionamiento indicadas en esta guía.

## 2. Tecnologías y requisitos previos

- **Java 17** (JDK configurado en IntelliJ IDEA).
- **Spring Boot 3.5.0**, **Spring Cloud 2025.0.0**.
- **Maven** (puede utilizarse el Maven Wrapper `mvnw.cmd` incluido en cada proyecto).
- **Git**, **IntelliJ IDEA**, **Docker Desktop** y **Postman**.
- Conexión a Internet para descargar dependencias e imágenes y para consultar el repositorio de configuración centralizada.
- Puertos locales disponibles: `5050`, `8081`, `8082`, `8083`, `8084`, `8761`, `8888`, `5432`, `5433`, `27017`, `5672`, `15672`.

**Nota:** esta guía presupone que las aplicaciones Spring Boot se ejecutarán directamente en la computadora del alumno (no dentro de contenedores). Por ello las conexiones de los archivos de configuración utilizan `localhost`.

## 3. Proyectos incluidos

| Carpeta | Función | Puerto |
|---|---|---:|
| `config-server` | Obtiene propiedades de configuración desde GitHub | 8888 |
| `eureka-server` | Registro y descubrimiento de servicios | 8761 |
| `api-gateway` | Enrutamiento hacia Pagos y Demo; filtro de token académico | 8081 |
| `microservice-pagos` | Pagos en PostgreSQL y publicación de eventos RabbitMQ | 8082 |
| `microservice-clientes` | Clientes en PostgreSQL | 8083 |
| `microservice-catalogo` | Productos en MongoDB y CQRS con separación lógica | 8084 |
| `microservice-demo` | Consultas de demostración y consumo de eventos RabbitMQ | 5050 |

Cada carpeta contiene su propio `pom.xml`; **no existe un único `pom.xml` en la raíz**. Los proyectos se importan y ejecutan individualmente.

## 4. Clonar el repositorio

En una terminal, desde la ubicación donde se desee guardar el proyecto:

```powershell
git clone https://github.com/LaGeekipedia/microservicios-springboot-modulo-iv.git
cd microservicios-springboot-modulo-iv
```

Abrir en IntelliJ IDEA cada subproyecto mediante **File → Open** y seleccionar su carpeta o su `pom.xml`. Aceptar la confianza del proyecto **solo si se reconoce el origen** y permitir la importación de Maven. Utilizar **Reload All Maven Projects** cuando corresponda.

Para comprobar la compilación de un proyecto en Windows, desde la carpeta de ese proyecto:

```powershell
.\mvnw.cmd -DskipTests compile
```

Repetir la comprobación en los proyectos necesarios. Este comando omite la ejecución de pruebas, pero **no equivale a validar funcionalmente todo el ecosistema**.

## 5. Preparar la infraestructura en Docker Desktop

Crear **cuatro contenedores** utilizando imágenes oficiales. Se puede realizar el proceso mediante la interfaz gráfica de Docker Desktop, desde **Images → Run → Optional settings** (el texto exacto de las opciones puede variar según la versión).

Antes de crear contenedores, preparar volúmenes con nombre desde **Volumes → Create**, si la versión de Docker Desktop lo permite. Al ejecutar cada imagen, configurar el nombre del contenedor, los puertos publicados, las variables de entorno y el volumen indicado en las tablas siguientes.

**Importante:** los valores de contraseña que se muestran son **ejemplos académicos para una instalación nueva**. No utilizar contraseñas personales ni productivas. Si se elige otra contraseña, debe usarse exactamente la misma en Docker y en la configuración de ejecución del microservicio.

### 5.1 PostgreSQL: Pagos

| Parámetro | Valor |
|---|---|
| Imagen | `postgres:17` |
| Contenedor | `postgres-pagos` |
| Puerto publicado | `5432` (host) → `5432` (contenedor) |
| `POSTGRES_DB` | `pagos_db` |
| `POSTGRES_USER` | `pagos_user` |
| `POSTGRES_PASSWORD` | `Pagos2026!` (ejemplo académico) |
| Volumen con nombre | `postgres_pagos_data` |
| Ruta del volumen en el contenedor | `/var/lib/postgresql/data` |

### 5.2 PostgreSQL: Clientes

| Parámetro | Valor |
|---|---|
| Imagen | `postgres:17` |
| Contenedor | `postgres-clientes` |
| Puerto publicado | `5433` (host) → `5432` (contenedor) |
| `POSTGRES_DB` | `clientes_db` |
| `POSTGRES_USER` | `clientes_user` |
| `POSTGRES_PASSWORD` | `Clientes2026!` (ejemplo académico) |
| Volumen con nombre | `postgres_clientes_data` |
| Ruta del volumen en el contenedor | `/var/lib/postgresql/data` |

Ambas instancias escuchan **dentro** de su contenedor en `5432`. Los puertos del host (`5432` y `5433`) deben ser diferentes para permitir su ejecución simultánea.

### 5.3 MongoDB: Catálogo

| Parámetro | Valor |
|---|---|
| Imagen | `mongo:8.0` |
| Contenedor | `mongo-catalogo` |
| Puerto publicado | `27017` (host) → `27017` (contenedor) |
| `MONGO_INITDB_ROOT_USERNAME` | `admin` |
| `MONGO_INITDB_ROOT_PASSWORD` | `Mongo2026!` (ejemplo académico) |
| Volumen con nombre | `mongo_catalogo_data` |
| Ruta del volumen en el contenedor | `/data/db` |

El microservicio selecciona la base de datos **`catalogo_db`** en su URI de conexión; **no** es necesario definir una variable `MONGO_INITDB_DATABASE` para esta práctica. La autenticación se realiza contra `admin` (`authSource=admin`). Docker puede crear adicionalmente un volumen anónimo para `/data/configdb`; no es necesario reproducir el nombre aleatorio de ese volumen.

> Si se utiliza una contraseña MongoDB con caracteres especiales reservados en una URI, debe codificarse correctamente en la cadena de conexión. Para evitar errores durante esta evaluación, se recomienda mantener la contraseña de ejemplo o utilizar una contraseña alfanumérica.

### 5.4 RabbitMQ: Mensajería

| Parámetro | Valor |
|---|---|
| Imagen | `rabbitmq:management` |
| Contenedor | `rabbitmq` |
| Puerto AMQP | `5672` (host) → `5672` (contenedor) |
| Puerto de administración | `15672` (host) → `15672` (contenedor) |
| `RABBITMQ_DEFAULT_USER` | `guest` |
| `RABBITMQ_DEFAULT_PASS` | `guest` |
| Volumen con nombre recomendado | `rabbitmq_data` |
| Ruta del volumen en el contenedor | `/var/lib/rabbitmq` |

La interfaz administrativa se encuentra en <http://localhost:15672>. El código del curso utiliza `guest/guest` y está pensado exclusivamente para un entorno de práctica local. **RabbitMQ restringe normalmente el acceso del usuario `guest` a conexiones locales**; como las aplicaciones corren en Windows y RabbitMQ dentro de Docker, esta configuración debe validarse en una instalación limpia. Si aparece un error de autenticación o acceso remoto, será necesario configurar un usuario de práctica distinto y actualizar de forma coherente las propiedades del productor y del consumidor de eventos antes de utilizarlo en la evaluación.

### 5.5 Advertencia sobre volúmenes persistentes

Las variables `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` y las variables de usuario inicial de MongoDB se aplican durante la **inicialización de datos nuevos**. Si se reutiliza un volumen que ya contiene una base de datos, cambiar esas variables **no cambia automáticamente** usuarios, bases de datos o contraseñas existentes. No borrar volúmenes con información importante para intentar corregir credenciales.

### 5.6 Comprobar los cuatro contenedores

En **Docker Desktop → Containers**, verificar que se encuentran en ejecución:

- `postgres-pagos`
- `postgres-clientes`
- `mongo-catalogo`
- `rabbitmq`

## 6. Configuración centralizada (Config Server)

Las propiedades que consume el ecosistema se encuentran en otro repositorio público:

- Repositorio: <https://github.com/LaGeekipedia/demo-configuracion-microservicios>
- Rama utilizada: **`dev`**.
- Config Server: <http://localhost:8888>.

El proyecto `config-server` ya contiene la dirección Git y la rama `dev` en su `application.properties`. **No es necesario clonar el repositorio de configuración** para esta práctica; Config Server lo consulta mediante Internet.

Archivos principales de esa rama:

- `microservice-pagos.properties`
- `microservice-clientes.properties`
- `microservice-catalogo.properties`
- `microservice-demo.properties`

Los valores de los puertos, conexiones de bases de datos y RabbitMQ se cargan desde allí. Las contraseñas de PostgreSQL y MongoDB **no se publican** en esos archivos: se reciben mediante variables de entorno de los procesos Java.

> **Atención:** si la rama `dev` cambia en GitHub, la evaluación podría comportarse de otra manera. El instructor debe conservar o verificar la configuración utilizada durante la sesión.

## 7. Variables de entorno en IntelliJ IDEA

Después de crear los contenedores, abrir en IntelliJ IDEA **Run → Edit Configurations**, seleccionar la configuración de ejecución de cada aplicación y buscar **Environment variables**. Agregar la variable correspondiente, guardar con **Apply → OK** e iniciar la aplicación.

| Aplicación | Variable obligatoria | Ejemplo de valor |
|---|---|---|
| `MicroservicePagosApplication` | `PAGOS_DB_PASSWORD` | `Pagos2026!` |
| `MicroserviceClientesApplication` | `CLIENTES_DB_PASSWORD` | `Clientes2026!` |
| `MicroserviceCatalogoApplication` | `MONGO_PASSWORD` | `Mongo2026!` |

Las variables se establecen en **cada configuración de ejecución**; no basta con definirlas en la aplicación Config Server. Deben coincidir con las contraseñas elegidas al crear los contenedores.

Por ejemplo, en la configuración de **Pagos**, agregar:

```text
PAGOS_DB_PASSWORD=Pagos2026!
```

En **Clientes**, agregar:

```text
CLIENTES_DB_PASSWORD=Clientes2026!
```

En **Catálogo**, agregar:

```text
MONGO_PASSWORD=Mongo2026!
```

La configuración de RabbitMQ del curso usa `guest/guest` en los archivos centrales de Pagos y Demo. Si se cambia el usuario de RabbitMQ, **también deberán cambiarse esas propiedades** antes del arranque.

## 8. Orden de arranque de las aplicaciones

Con Docker Desktop en ejecución y los cuatro contenedores levantados, iniciar desde IntelliJ IDEA:

1. **Config Server** — puerto `8888`.
2. **Eureka Server** — puerto `8761`.
3. **Microservice Clientes** — puerto `8083`.
4. **Microservice Catálogo** — puerto `8084`.
5. **Microservice Pagos** — puerto `8082` (consulta Clientes y publica eventos).
6. **Microservice Demo** — puerto `5050` (consume eventos y consulta Pagos).
7. **API Gateway** — puerto `8081`.

Para cada proyecto, localizar la clase principal `*Application`, iniciar la aplicación y verificar que no existan errores de conexión. El arranque de algunas aplicaciones podría realizarse en otro orden, pero esta secuencia facilita el diagnóstico de dependencias.

Abrir <http://localhost:8761> y comprobar que aparezcan registrados y en estado `UP` los servicios **API-GATEWAY**, **MICROSERVICE-PAGOS**, **MICROSERVICE-CLIENTES**, **MICROSERVICE-CATALOGO** y **MICROSERVICE-DEMO**. Config Server y Eureka Server no necesariamente aparecen como clientes registrados en ese panel.

## 9. Pruebas prácticas con Postman

Las siguientes rutas corresponden al código de este repositorio. En las solicitudes `POST` de Clientes y Pagos, los parámetros se envían mediante **Query Params**, no como cuerpo JSON. Las pruebas pueden realizarse contra los puertos directos de cada microservicio.

### 9.1 Crear y consultar un cliente (PostgreSQL)

**Crear cliente:**

```http
POST http://localhost:8083/clientes/crear?clienteId=C001&nombre=Ana%20Lopez&email=ana@example.com
```

**Consultar cliente:**

```http
GET http://localhost:8083/clientes/C001
```

Comprobar que el cliente creado pueda recuperarse desde la base `clientes_db`. Usar un identificador distinto si `C001` ya existe.

### 9.2 Crear y consultar un producto (MongoDB)

**Crear producto:**

```http
POST http://localhost:8084/productos/crear
Content-Type: application/json
```

```json
{
  "nombre": "Teclado mecanico",
  "categoria": "Tecnologia",
  "precio": 1250.00,
  "atributos": {
    "color": "Negro",
    "conexion": "USB"
  }
}
```

**Listar productos:**

```http
GET http://localhost:8084/productos
```

**Consultar producto por ID:** utilizar el `id` que regrese la respuesta de creación:

```http
GET http://localhost:8084/productos/{id}
```

**Actualizar todo el producto (PUT):**

```http
PUT http://localhost:8084/productos/{id}
Content-Type: application/json
```

```json
{
  "nombre": "Teclado mecanico RGB",
  "categoria": "Tecnologia",
  "precio": 1450.00,
  "atributos": {
    "color": "Negro",
    "conexion": "USB-C"
  }
}
```

**Actualizar algunos campos (PATCH):**

```http
PATCH http://localhost:8084/productos/{id}
Content-Type: application/json
```

```json
{
  "precio": 1399.00
}
```

**Eliminar producto:**

```http
DELETE http://localhost:8084/productos/{id}
```

Estas operaciones permiten observar la **separación lógica CQRS** implementada en el proyecto: `ProductoCommandService` atiende las escrituras y `ProductoQueryService` atiende las lecturas, ambos sobre la misma instancia de MongoDB.

### 9.3 Crear un pago y publicar un evento (PostgreSQL + RabbitMQ)

Primero crear el cliente `C001` como se explicó anteriormente. Después ejecutar:

```http
POST http://localhost:8082/pagos/crear?pagoId=P001&clienteId=C001&monto=100.00&moneda=MXN
```

Se espera una confirmación de creación del pago y publicación del evento. Verificar que el pago se haya guardado en `pagos_db` y que **Microservice Demo** haya recibido el evento de RabbitMQ en su consola.

Consultar el cliente desde Pagos para probar la comunicación HTTP entre microservicios:

```http
GET http://localhost:8082/pagos/clientes/C001
```

### 9.4 Probar el API Gateway y su token de demostración

El Gateway tiene rutas configuradas hacia **Pagos** y **Demo**. **No tiene rutas configuradas para Clientes ni Catálogo** en la versión actual del proyecto.

Para solicitar el mensaje del microservicio Pagos a través del Gateway:

```http
GET http://localhost:8081/api/pagos/pagos/mensaje
Authorization: Bearer miclave123
```

Para solicitar el mensaje de Demo a través del Gateway:

```http
GET http://localhost:8081/api/demo/mensaje
Authorization: Bearer miclave123
```

El filtro `AuthFilter` aplica globalmente a las solicitudes que atraviesan el Gateway. Si se omite la cabecera `Authorization`, responde `401 Unauthorized`; si se envía un token diferente, responde `403 Forbidden`. **El token es fijo, visible en el código y solo sirve para demostración académica**: no representa autenticación segura para sistemas reales.

## 10. Evidencias sugeridas para la evaluación

El alumno deberá poder mostrar:

- Los cuatro contenedores en ejecución en Docker Desktop.
- Las tres variables de entorno configuradas en IntelliJ IDEA, **sin exhibir contraseñas personales**.
- Los cinco servicios registrados con estado `UP` en Eureka.
- La creación y consulta de un cliente y un producto mediante Postman.
- Una operación de actualización de producto (`PUT` o `PATCH`).
- La creación de un pago y la recepción del evento correspondiente en el microservicio Demo.
- Una petición correcta y una petición rechazada por token a través del Gateway.

El instructor determinará los criterios de calificación y el formato de entrega de evidencias durante la sesión.

## 11. Solución de problemas frecuentes

| Situación | Qué revisar |
|---|---|
| IntelliJ no reconoce las clases | Importar el `pom.xml` del subproyecto correspondiente y recargar Maven; comprobar JDK 17 y las carpetas `src/main/java`. |
| Config Server no obtiene propiedades | Conexión a Internet, repositorio Git público, rama `dev` y puerto `8888`. |
| Error de contraseña en PostgreSQL | Coincidencia entre `POSTGRES_PASSWORD` y la variable `PAGOS_DB_PASSWORD` o `CLIENTES_DB_PASSWORD`. |
| Error de autenticación MongoDB | Usuario `admin`, variable `MONGO_PASSWORD`, URI de MongoDB y `authSource=admin`; revisar caracteres especiales. |
| Puerto ocupado | Comprobar que otro contenedor o aplicación no esté utilizando el puerto del host. |
| Microservicio no aparece en Eureka | Verificar que Eureka esté iniciado y que el servicio tenga disponibles las propiedades de Config Server. |
| No se recibe un evento RabbitMQ | Verificar RabbitMQ, credenciales, cola `pagos.nuevos.q`, exchange `pagos.exchange`, productor Pagos y consumidor Demo. |
| RabbitMQ rechaza `guest` | Revisar la restricción de acceso local del usuario `guest`; para una conexión no local utilizar un usuario de práctica distinto y ajustar configuración del productor y consumidor. |
| Un cambio de contraseña no surte efecto | Comprobar si se reutilizó un volumen inicializado con credenciales anteriores; no eliminar datos sin respaldo. |
| Gateway responde `401` o `403` | Enviar la cabecera `Authorization: Bearer miclave123` exclusivamente para la prueba académica. |

## 12. Consideraciones finales

- Este repositorio contiene **código fuente**, no las bases de datos ni los volúmenes de Docker de la computadora del instructor.
- Los contenedores se crean **manualmente** como parte del ejercicio. No se requiere Docker Compose para esta evaluación.
- Las contraseñas incluidas aquí son únicamente de ejemplo para instalaciones de laboratorio; deben sustituirse en usos reales.
- El archivo `dependencias-resilience4j.properties` del repositorio de configuración contiene fragmentos XML de referencia, **no** propiedades ejecutables de Spring Boot.
- La configuración académica permite estudiar persistencia y comunicación entre microservicios; no pretende ser una implementación de seguridad o despliegue de producción.

---

**Curso:** Microservicios en Spring Boot con Spring Cloud  
**Módulo:** IV — Persistencia y Bases de Datos en Microservicios  
**Repositorio:** [LaGeekipedia/microservicios-springboot-modulo-iv](https://github.com/LaGeekipedia/microservicios-springboot-modulo-iv)
