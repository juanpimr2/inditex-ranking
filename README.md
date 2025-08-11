# 📊 Inditex Ranking Service

Servicio de ranking de productos con arquitectura limpia, desarrollado en Java 22 y Spring Boot, con soporte para ejecución local y despliegue en Docker.

---

## 🚀 Características principales

- **Arquitectura limpia** con separación en capas (`service`, `infrastructure`, `domain`, `dto`, `utils`).
- **Inyección de dependencias** mediante Spring Boot y Lombok (`@RequiredArgsConstructor`).
- **Validación** de productos antes de procesar el ranking.
- **Algoritmo de ranking personalizable** mediante la interfaz `RankingAlgorithm`.
- **Uso de Optional y logging estructurado** para trazabilidad.
- **Test unitarios** con JUnit 5 y Mockito.
- **Soporte Docker** para despliegue rápido.

---

## 📂 Estructura del proyecto

```
inditex-ranking/
├── .dockerignore
├── .gitattributes
├── docker-compose.yml          # Orquestación de servicios
├── Dockerfile                   # Imagen Docker del servicio
├── pom.xml                      # Dependencias y configuración Maven
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── io/github/juanpimr2/inditex_ranking/
    │   │       ├── app/controller/               # Controladores REST
    │   │       │   └── ProductController.java
    │   │       ├── config/                        # Configuración de beans y OpenAPI
    │   │       ├── domain/                        # Entidades de dominio (Product, RankedProduct...)
    │   │       ├── dto/                           # Clases de transferencia de datos (RankRequest, RankResponse, Weights)
    │   │       ├── infrastructure/
    │   │       │   ├── query/                     # Queries y adaptadores externos
    │   │       │   ├── repo/                      # Repositorios (InMemoryProductRepository)
    │   │       │   ├── scoring/                   # Lógica de cálculo de puntuación
    │   │       │   ├── sorting/                   # Algoritmos de ordenación
    │   │       ├── service/
    │   │       │   ├── algorithm/                 # Implementaciones de RankingAlgorithm
    │   │       │   ├── base/                      # BaseService genérico
    │   │       │   ├── ranking/                   # Servicio principal de ranking
    │   │       ├── utils/constants/               # Constantes del proyecto
    │   │       └── InditexRankingApplication.java # Clase principal Spring Boot
    │   └── resources/
    │       └── application.yml                    # Configuración del proyecto
    └── test
        └── java/io/github/juanpimr2/inditex_ranking/
            ├── ...                                # Tests unitarios y de integración
```

---

## 🔄 Flujo de ejecución

1. **Petición** HTTP `POST /ranking` con lista de productos y pesos opcionales (`salesUnits`, `stockRatio`).
2. **RankingServiceImpl** valida la entrada usando `ProductValidator`.
3. Si no hay productos, se obtienen mediante `ProductGetAllProductsQuery`.
4. El algoritmo (`RankingAlgorithmImpl`) calcula la puntuación y ordena los productos usando `MergeSortService`.
5. Se devuelve la lista ordenada de `RankedProduct`.

---

## ⚙️ Configuración

Archivo `application.yml`:

```yaml
server:
  port: 8080

logging:
  level:
    root: INFO
    io.github.juanpimr2.inditex_ranking: DEBUG
```

---

## 🧪 Ejecución local

### Requisitos previos

- Java 22
- Maven 3.9+
- Docker y Docker Compose (opcional para despliegue containerizado)

### Compilación y tests

```bash
mvn clean install
```

### Ejecución

```bash
mvn spring-boot:run
```

### Swagger
```bash
localhost:8080/swagger-ui/index.html
```

---

## 🐳 Ejecución con Docker

### Construir imagen

```bash
docker build -t inditex-ranking .
```

### Ejecutar con Docker

```bash
docker run -p 8080:8080 inditex-ranking
```

### Orquestar con Docker Compose

```bash
docker-compose up --build
```

---

## 📬 Endpoints principales

- **POST** `/ranking` → Genera un ranking con productos proporcionados o desde el repositorio.
- **GET** `/products` → Lista de productos de ejemplo.

---

## ✍️ Autor

**JuanPiMR2**  
Repositorio: [GitHub](https://github.com/juanpimr2)
