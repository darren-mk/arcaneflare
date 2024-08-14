# arcaneflare

## development

### install dependencies
```
toolbox create arcaneflare
toolbox enter arcaneflare
source resources/scripts/toolbox/install-deps.sh
```

### run application from cli
```
clj -X arcaneflare.core/run
```

### database

#### main
$ docker run \
	--name arcaneflare-local-db \
    -p 5432:5432 \
    -e POSTGRES_DB=arcaneflare \
    -e POSTGRES_USER=dev \
	-e POSTGRES_PASSWORD=abc \
	-d postgres
#### test
$ docker run \
	--name arcaneflare-test-local-db \
    -p 5433:5432 \
    -e POSTGRES_DB=arcaneflare-test \
    -e POSTGRES_USER=dev \
	-e POSTGRES_PASSWORD=abc \
	-d postgres

## running test 

### local
```
clj -X:test
```
