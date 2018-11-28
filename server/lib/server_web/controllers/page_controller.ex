defmodule ServerWeb.PageController do
  use ServerWeb, :controller

  def index(conn, _params) do
    render(conn, "index.html", item: "menu.core", title: "Menu")
  end

  def register(conn, _params) do
    render(conn, "index.html", item: "menu.register", title: "Register")
  end

  def login(conn, _params) do
    render(conn, "index.html", item: "menu.login", title: "Login")
  end

  def news(conn, _params) do
    render(conn, "index.html", item: "news.core", title: "News")
  end
end
