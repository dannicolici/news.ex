# About

This project is part of my elixir learning experience.
What's in here is a backend/db port of the clojure
repo: https://github.com/dannicolici/news

Please visit the original repo for details about
functionality. This is only focused on switching
the backend to phoenix/elixir, while keeping the
frontend in clojurescript.

## Setting up the project

- for the server, you need to install elixir: https://elixir-lang.org/install.html
- for the front end, you need clojure, so install leiningen 2.0.0 or above: https://leiningen.org/

## Running it

- compile to JS files first with: `lein cljsbuild once`
- to start the server (cowboy on localhost:4000), cd to the server dir and run: `mix phx.server`