defmodule ServerWeb.NewsChannel do
  use Phoenix.Channel

  def join("news:all", _message, socket) do
    {:ok, socket}
  end

  # def join("room:" <> _private_room_id, _params, _socket) do
  #   {:error, %{reason: "unauthorized"}}
  # end

  def handle_in("new_post", %{"body" => body}, socket) do
    broadcast!(socket, "new_post", %{body: body})
    {:noreply, socket}
  end
end
