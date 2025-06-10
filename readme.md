# Arcaneflare

## CLI

### Run Application

```shell
clojure -M:prod
```

### Running Tests

```shell
clojure -M:test
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
