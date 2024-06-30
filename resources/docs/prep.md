# set up for dev env
## database
$ docker run \
	--name arcaneflare-local-db \
    -p 5432:5432 \
    -e POSTGRES_DB=arcaneflare \
    -e POSTGRES_USER=dev \
	-e POSTGRES_PASSWORD=abc \
	-d postgres
