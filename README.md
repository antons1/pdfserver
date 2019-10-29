# pdfserver

A plex inspired document server/client

## Development
You need Clojure and Leiningen installed before starting development

To start the server, run a REPL and run `(start)`

This will start the API on `localhost:3000` or a port you set in env `PORT`

Documents are listed from `[ROOT]/documents`, or another folder configured in
`:root-folder` in `config.edn`