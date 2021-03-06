defmodule ServerWeb.Router do
  use ServerWeb, :router

  pipeline :browser do
    plug :accepts, ["html"]
    plug :fetch_session
    plug :fetch_flash
    plug :protect_from_forgery
    plug :put_secure_browser_headers
  end

  pipeline :api do
    plug :accepts, ["json"]
  end

  scope "/", ServerWeb do
    pipe_through :browser

    get "/", PageController, :index
    get "/register", PageController, :register
    get "/login", PageController, :login
    get "/news", PageController, :news
  end

  scope "/api/user", ServerWeb do
    pipe_through :api

    post "/register", PageController, :register_user
    post "/login", PageController, :login_user
  end

  # Other scopes may use custom stacks.
  # scope "/api", ServerWeb do
  #   pipe_through :api
  # end
end
