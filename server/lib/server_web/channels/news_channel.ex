defmodule ServerWeb.NewsChannel do
  use Phoenix.Channel
  use Phoenix.Socket
  alias Server.Api.News.Service

  @page_size 4

  def json(latest_news, page \\ 0) do
    response = %{
      news: %{
        pages: count_pages(Enum.count(latest_news)),
        news: select_page(latest_news, page)
      }
    }

    ServerWeb.NewsView.render("news.json", response)
  end

  defp count_pages(news_size) do
    trunc(Float.ceil(news_size / @page_size))
  end

  defp select_page(news, page) do
    news 
    |> Enum.map(fn [_, n] -> n end) 
    |> Enum.slice(page * @page_size, @page_size)
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
    sorted_news = Service.get_all(criteria)
    {:reply, {:ok, json(sorted_news)}, socket}
  end

  def handle_in("get-page", %{"page" => page}, socket) do
    news = Service.get_all()
    {:reply, {:ok, json(news, page - 1)}, socket}
  end
end
