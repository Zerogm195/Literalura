# Literalura

**Literalura** es una aplicación desarrollada en **Java Spring Boot** que permite consultar información sobre libros y autores. Utiliza la API externa de **Gutendex** para obtener datos relacionados con libros de dominio público.

## Funcionalidades

- Consultar libros por título.
- Consultar libros por autor.
- Consultar libros por idioma.
- Filtrar libros por el rango de años en que los autores estaban vivos.
- Consumir datos de la API de Gutendex para obtener información sobre libros y autores.

## Tecnologías

- Java 23
- Spring Boot
- Spring Web
- Spring Data JPA
- REST API (Gutendex)
- Maven

## Instalación

### Clonar el repositorio

```bash
git clone https://github.com/Zerogm195/literalura.git
```

### Configuración
Configura tu archivo application.properties con los detalles de la base de datos (si usas JPA).
Asegúrate de que la API de Gutendex esté accesible para obtener los datos de los libros.
### Ejecución
Navega al directorio del proyecto.
```bash
cd literalura
```
Ejecuta el siguiente comando para iniciar la aplicación:
```bash
mvn spring-boot:run
```
La aplicación estará disponible en http://localhost:8080.

### Contribuciones
¡Las contribuciones son bienvenidas! Si deseas mejorar esta aplicación, abre un issue o crea un pull request.
