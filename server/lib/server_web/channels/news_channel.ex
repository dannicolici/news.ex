defmodule ServerWeb.NewsChannel do
  use Phoenix.Channel
  use Phoenix.Socket
  alias Server.Api.News.Service

  def json(latest_news) do
    response = %{
      news: %{
        pages: 1,
        news: latest_news |> Enum.map(fn [_, n] -> n end)
      }
    }

    ServerWeb.NewsView.render("news.json", response)
  end

  def join("news:all", _message, socket) do
    {:ok, json(Service.get_all_news()), socket}
  end

  def handle_in("create", %{"text" => body}, socket) do
    Service.insert_news(body, socket.assigns.user_id)
    broadcast!(socket, "new_post", %{body: body})
    {:reply, {:ok, json(Service.get_all_news())}, socket}
  end

  def handle_in("sort", %{"sort_by" => _criteria}, socket) do
    # TODO: real sorting using criteria
    {:reply, {:ok, json(Service.get_all_news())}, socket}
  end
end
