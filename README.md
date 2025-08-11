# 📊 Inditex Ranking Service

Servicio de ranking de productos con arquitectura limpia, desarrollado en Java 22 y Spring Boot, con soporte para ejecución local y despliegue en Docker.

---

## 🚀 Características principales

- **Arquitectura limpia** con separación en capas (`service`, `infrastructure`, `domain`, `dto`, `utils`).
- **Inyección de dependencias** mediante Spring Boot y Lombok (`@RequiredArgsConstructor`).
- **Validación** de productos antes de procesar el ranking.
- **Algoritmo de ranking personalizable** mediante la interfaz `RankingAlgorithm`.
  - **Tipo:** Normalización *Min-Max* + cálculo de *score ponderado*.
  - **Cómo funciona la fórmula:**
    
    El score se calcula así:  
    **Score = (PesoVentas × VentasNormalizadas) + (PesoStock × RatioStock)**
    
    - **PesoVentas** y **PesoStock**: representan la importancia que damos a las ventas y al stock (por ejemplo, 70% ventas y 30% stock).  
    - **VentasNormalizadas**: ajustamos las ventas reales a una escala de 0 a 1 usando *Min-Max normalization*, para que el producto con menos ventas sea 0 y el de más ventas sea 1.  
    - **RatioStock**: es el porcentaje de tallas disponibles (si hay 3 tallas y 2 tienen stock, el ratio es `2/3 ≈ 0.66`).  

    💡 Esto permite equilibrar popularidad y disponibilidad real para un ranking más justo.
    
  - **Ordenación:** Merge Sort (estable, predecible y eficiente).
  - **Complejidad:**
    - Normalización: **O(n)**.
    - Ordenación: **O(n log n)** tiempo y **O(n)** espacio.
  - **Motivos de elección:**
    - *Normalización Min-Max:* ligera y preserva proporciones relativas.
    - *Ponderación flexible:* fácil de ajustar según prioridades de negocio.
    - *Merge Sort:* ideal para listas medianas/grandes donde se requiere orden estable.


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
