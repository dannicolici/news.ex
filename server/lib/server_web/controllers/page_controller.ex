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

  def register_user(conn, %{"id" => id, "fname" => fname, "lname" => lname, "pwd" => pwd}) do
    case UserService.get(id) do
      [{_user_id, _pass}] ->
        json(conn, %{error: "user exists"})
      [] ->
        # some validation would be in order (not empty fields, etc)
        UserService.insert(id, pwd)
        [{user_id, _pass}] = UserService.get(id)
        json(conn, %{token: Phoenix.Token.sign(conn, "user salt", user_id)})
      end
  end

  def login_user(conn, %{"id" => id, "pwd" => pwd}) do
    case UserService.get(id) do
      [] ->
        json(conn, %{error: "user does not exist"})
      [{user_id, hashed_pass}] ->
        case UserService.pass_matches_hash?(pwd, hashed_pass) do
          true ->
            json(conn, %{token: Phoenix.Token.sign(conn, "user salt", user_id)})
          false ->
            json(conn, %{error: "wrong password"})
        end
      end
  end
end
