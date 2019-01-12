defmodule ServerWeb.NewsChannel do
  use Phoenix.Channel
  use Phoenix.Socket
  alias Server.Api.News
  alias Server.Api.Data
  alias Persistence.Db

  def news_from_source(criteria) do
    news_from_source = %{
      news: %{
        pages: 1,
        news: [%{id: "1", user_id: "1", text: criteria, date_time: "2018-12-05 10:00"}]
      }
    }

    ServerWeb.NewsView.render("news.json", news_from_source)
  end

  def join("news:all", _message, socket) do
    {:ok, news_from_source("default"), socket}
  end

  def handle_in("create", %{"text" => body}, socket) do
    GenServer.call(Db, {:insert_news, socket.assigns.user_id, body})

    broadcast!(socket, "new_post", %{body: body})

    {:noreply, socket}
  end

  def handle_in("sort", %{"sort_by" => criteria}, socket) do
    {:reply, {:ok, news_from_source(criteria)}, socket}
  end
end
