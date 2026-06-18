# Reeasy - Logistic Application Startup

Este proyecto es una aplicación de escaneo de botellas reciclables que cuenta con una arquitectura de microservicios, compuesta por un backend principal en Spring Boot, un servicio de análisis de imágenes de inteligencia artificial en Python (usando YOLO-cls) y una interfaz web.

## 📋 Requisitos Previos

Antes de poner en marcha el proyecto, asegúrate de tener instalado en tu sistema:

| Herramienta | Versión mínima | Uso |
|---|---|---|
| **Java Development Kit (JDK)** | 17+ | Backend Spring Boot |
| **Maven** | 3.8+ | Compilar Spring Boot (o usar `mvnw`) |
| **Python** | 3.10+ | Servicio de análisis de imágenes |
| **Node.js y npm** | 18+ | Frontend Vite/React |
| **Docker y Docker Compose** | Docker 24+ / Compose 2.0+ | Despliegue automatizado |
| **MySQL 8.0** | 8.0 | Base de datos (se levanta con Docker) |

---

## 🚀 Guía de Instalación y Ejecución

Puedes levantar el proyecto de dos formas: **manual** (cada servicio por separado) o **automatizada** (todo con Docker Compose).

---

### 🐳 Puesta en marcha automatizada (Docker Compose)

Levanta **los 3 servicios backend** (MySQL, analizador de imágenes y API Spring Boot) con un solo comando.

> La interfaz web se ejecuta por separado (ver sección manual).

```bash
cd demo
docker compose up -d
```

Esto construye las imágenes y levanta los contenedores en este orden:
1. **`db`** — MySQL 8.0 (puerto `3307`)
2. **`image-analyzer`** — Python/FastAPI con YOLO (puerto `11434`)
3. **`api`** — Spring Boot (puerto `8090`)

#### Verificar que todo funciona

```bash
# Ver estado de los contenedores
docker compose ps

# Ver logs en vivo
docker compose logs -f

# Probar que el API responde
curl http://localhost:8090/recycling/status

# Probar que el analizador de imágenes responde
curl http://localhost:11434/docs
```

#### Detener servicios

```bash
docker compose down
```

Para eliminar también los datos de la base de datos:
```bash
docker compose down -v
```

#### Variables de entorno

Las credenciales se definen en `demo/.env` (valores por defecto):

```
root_password=test
password=test
name=test
user=test
```

Los modelos YOLO se montan desde `imageAnalizer/model/` como volumen dentro del contenedor `image-analyzer`.

---

### 🔧 Puesta en marcha manual

Sigue estos pasos en el orden indicado para levantar el proyecto servicio por servicio. Se recomienda usar terminales distintas para cada uno.

#### 0. Base de Datos (MySQL)

Usando Docker Compose (solo la base de datos):

```bash
cd demo
docker compose up -d db
```

#### 1. Servicio de Análisis de Imágenes (Python / FastAPI)

Procesa las imágenes usando YOLO-cls para determinar el tipo de plástico.

```bash
cd imageAnalizer

# Crear y activar entorno virtual
python3 -m venv venv
source venv/bin/activate   # Linux/macOS
# venv\Scripts\activate    # Windows

# Instalar dependencias
pip install -r requirements.txt

# Iniciar servidor (puerto 11434)
python main.py
# o: uvicorn main:app --host 0.0.0.0 --port 11434 --reload
```

#### 2. Servicio Principal Backend (Spring Boot / Java)

API REST central que coordina la lógica de negocio y se comunica con la IA.

```bash
cd demo

# Asegúrate de que MySQL y el servicio Python ya estén corriendo

# Compilar (omite tests con -DskipTests)
./mvnw clean install -DskipTests

# Ejecutar (puerto 8090)
./mvnw spring-boot:run
```

> La conexión a BD se configura via `spring.datasource.*` en `application.yml`. Los valores por defecto apuntan a `localhost:3307` con credenciales `test`/`test`.

#### 3. Servicio Frontend (Web / React+Vite) (¡DESCONTINUADO!)

Interfaz de usuario del escáner web.

```bash
cd web-reeasy/vite-project
npm install
npm run dev
```

---

## 🏗️ Estructura del Proyecto

```
Reeasy/
├── docker-compose.yml          # Orquestación Docker (demo/)
├── .env                        # Variables de entorno (demo/)
├── demo/                       # Backend Spring Boot (Java)
│   ├── Dockerfile              # Multi-etapa para Docker
│   ├── application.yml         # Configuración de Spring Boot
│   ├── db-init/                # Scripts de inicialización SQL
│   └── src/                    # Código fuente Java
├── imageAnalizer/              # Microservicio de IA (Python/FastAPI)
│   ├── Dockerfile              # Imagen para Docker
│   ├── .dockerignore           # Excluye modelos del build
│   ├── model/                  # Modelos YOLO (montados como volumen en Docker)
│   ├── main.py                 # Aplicación FastAPI
│   └── requirements.txt
├── web-reeasy/                 # Interfaz web (Vite/React)
│   └── vite-project/           # Código fuente del frontend
└── ReEasy/                     # Colección Bruno para pruebas de API
```
