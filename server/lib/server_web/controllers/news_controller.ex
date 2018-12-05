defmodule ServerWeb.NewsController do
  use ServerWeb, :controller

  alias Server.Api
  alias Server.Api.News

  action_fallback ServerWeb.FallbackController

  def index(conn, _params) do
    news = Api.list_news()
    render(conn, "index.json", news: news)
  end

  def create(conn, %{"news" => news_params}) do
    with {:ok, %News{} = news} <- Api.create_news(news_params) do
      conn
      |> put_status(:created)
      |> put_resp_header("location", Routes.news_path(conn, :show, news))
      |> render("show.json", news: news)
    end
  end

  def show(conn, %{"id" => id}) do
    news = Api.get_news!(id)
    render(conn, "show.json", news: news)
  end

  def show(conn, %{"user_id" => id}) do
    news = Api.get_news!(id) # TODO get for user
    render(conn, "show.json", news: news)
  end

  def show(conn) do
    news = Api.get_news!(0); # TODO get all
    render(conn, "show.json", news: news)
  end
end
