defmodule ServerWeb.NewsChannel do
  use Phoenix.Channel
  alias Server.Api.News
  alias Server.Api.Data

  def join("news:all", _message, socket) do
    fake_news = %{
      news: %{
        pages: 1,
        news: [%{id: "1", user_id: "1", text: "awsome", date_time: "2018-12-05 10:00"}]
      }
    }

    response =
      ServerWeb.NewsView.render(
        "news.json",
        fake_news
      )

    {:ok, response, socket}
  end

  # def join("room:" <> _private_room_id, _params, _socket) do
  #   {:error, %{reason: "unauthorized"}}
  # end

  def handle_in("create", %{"text" => body}, socket) do
    broadcast!(socket, "new_post", %{body: body})
    {:noreply, socket}
  end
end
