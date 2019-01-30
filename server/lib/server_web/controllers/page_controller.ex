defmodule ServerWeb.PageController do
  use ServerWeb, :controller
  alias Server.Api.User.Service, as: UserService

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

  def user(conn, %{"id" => id, "fname" => fname, "lname" => lname, "pwd" => pwd}) do
    case UserService.get(id) do
      [{_user_id, _pass}] ->
        json(conn, %{error: "user exists"})
      [] ->
        UserService.insert(id, pwd)
        [{user_id, pass}] = UserService.get(id)
        json(conn, %{token: Phoenix.Token.sign(conn, "user salt", user_id)})
      end
  end
end
