defmodule ServerWeb.NewsView do
  use ServerWeb, :view
  alias ServerWeb.NewsView

  def render("index.json", %{news: news}) do
    %{data: render_many(news, NewsView, "news.json")}
  end

  def render("show.json", %{news: news}) do
    %{data: render_one(news, NewsView, "news.json")}
  end

  def render("news.json", %{news: news}) do
    %{pages: news.pages, news: news.news}
  end
end
