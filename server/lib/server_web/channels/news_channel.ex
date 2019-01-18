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
    {:ok, json(Service.get_all()), socket}
  end

  def handle_in("create", %{"text" => body}, socket) do
    Service.insert_news(body, socket.assigns.user_id)
    broadcast!(socket, "new_post", %{body: body})
    {:reply, {:ok, json(Service.get_all())}, socket}
  end

  def handle_in("sort", %{"sort_by" => criteria}, socket) do
    sorted_news = Service.get_all() 
                 |> Enum.sort_by fn [_, n] ->
                                    Map.fetch(n, String.to_existing_atom(criteria))
                                 end
    {:reply, {:ok, json(sorted_news)}, socket}
  end
end
