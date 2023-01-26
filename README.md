# Demo de conexión a un Storage Account

_Es un proyecto donde se habilita endpoints el cual se usan las interfaces de SDK de Azure para el consumo de un Storage Account

## Comenzando 🚀
### Pre-requisitos 📋

* Docker
* Java 11


### Instalación 🔧

_Ejecutar los siguientes comandos:_

_Clonar el proyecto_

```
git clone https://github.com/rzbroncano/demo-storage-account.git
```

_Empaquetar el proyecto_

```
mvm clean package
```

_Crear imagen_

```
docker build -t demo/demo-storage-account .
```
_Crear archivo "enviroments_file" con las siguientes variables de entorno_

```
ACCOUNT_NAME=<storage_account_name>
ACCOUNT_KEY=<storage_account_key>
ENDPOINT=<endpoint_del_storage_account>

**Debes crear un storage account en tu suscripción de Azure**
```
_Ejecutar contenedor_

```
docker run -p 8080:8080 --env-file enviroments_file -t demo/demo-storage-account
```

## Construido con 🛠️

_Menciona las herramientas que utilizaste para crear tu proyecto_

* [Spring](https://spring.io/) - El framework web usado
* [Maven](https://maven.apache.org/) - Manejador de dependencias

## Contribuyendo 🖇️

Por favor lee el [CONTRIBUTING.md](https://gist.github.com/villanuevand/xxxxxx) para detalles de nuestro código de conducta, y el proceso para enviarnos pull requests.

## Autores ✒️

_Menciona a todos aquellos que ayudaron a levantar el proyecto desde sus inicios_

* **Renzo Broncano** - *Trabajo Inicial* - [villanuevand](https://github.com/rzbroncano)

También puedes mirar la lista de todos los [contribuyentes](https://github.com/your/project/contributors) quíenes han participado en este proyecto. 


---
⌨️ con ❤️ por [rzbroncano](https://github.com/rzbroncano) 😊
