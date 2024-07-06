# set up for dev env
## database
### main
$ docker run \
	--name arcaneflare-local-db \
    -p 5432:5432 \
    -e POSTGRES_DB=arcaneflare \
    -e POSTGRES_USER=dev \
	-e POSTGRES_PASSWORD=abc \
	-d postgres
### test
$ docker run \
	--name arcaneflare-test-local-db \
    -p 5433:5432 \
    -e POSTGRES_DB=arcaneflare-test \
    -e POSTGRES_USER=dev \
	-e POSTGRES_PASSWORD=abc \
	-d postgres
