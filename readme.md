# Arcaneflare

## CLI

### Run Application

```shell
clojure -M:prod
```

Then you will see details of components started:

```shell
started:  #'arcaneflare.database.base/ds
started:  #'arcaneflare.handler/server
backend successfully started
```

### Running Tests

```shell
clojure -M:test
```

## Set up Container for Dependencies

Use Podman Compose to run the PostgreSQL database defined in `compose.yml`. The
application reads connection details from a `profiles.edn` file (ignored by
Git). A minimal `profiles.edn` for local development looks like:

```edn
{:local
 {:jwt {:secret "localsecret"}
  :pg {:dbtype "postgresql"
       :host "localhost"
       :port 5432
       :dbname "arcaneflare_main_db"
       :username "localuser"
       :password "localpass"}}}
```

### macOS

1. Install podman
   ```shell
   brew install podman
   ```

2. Install podman-compose
   ```shell
   brew install podman-compose
   ```

3. Initialize and start the Podman virtual machine:
   ```shell
   podman machine init
   podman machine start
   ```

4. Start the database container:
   ```shell
   podman compose up -d
   ```

4. When finished, stop the container:
   ```shell
   podman compose down
   ```

### Linux (Fedora)

1. podman is usually preinstalled. install podman-compose.
   ```shell
   sudo dnf install podman-compose
   ```

2. Start the database container:
   ```shell
   podman compose up -d
   ```

3. When finished, stop the container:
   ```shell
   podman compose down
   ```

## Editor Settings

### Visual Studio Code

#### Running Client and Server in Separate REPLs

This repository includes both the client and server code.
If you're working on both ends, it's often helpful to run separate REPLs for each.

To make this easier, you can create two separate VS Code workspaces:

In the File menu, select `Save Workspace As...`
Save the following two files (they are .gitignored and won’t affect the project):

* arcaneflare-client.code-workspace
* arcaneflare-server.code-workspace

Each workspace file will look like this:

```json
{ "folders": [{ "path": "." }] }
```

After saving, you can open each workspace separately using `File` → `Open Workspace from File...`
This allows you to run client and server REPLs independently in dedicated VS Code instances.
