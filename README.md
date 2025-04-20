## JsonPostArchiver

Scala application that fetches all posts from the [JSONPlaceholder](https://jsonplaceholder.typicode.com/posts) API and saves each post as a separate `.json` file named `<id>.json`.

---

## Tech Stack
- **scala 3**
- **http4s**
- **cats-effect 3**
- **circe**
- **pureconfig**
- **log4cats + logback**
- **munit-cats-effect**
- **sbt**

---

## How to Run
### Build the project
```
sbt compile
````
### Run application
```
sbt run
````
### Run tests
```
sbt test
````