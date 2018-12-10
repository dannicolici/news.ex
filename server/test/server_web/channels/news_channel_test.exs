defmodule ServerWeb.NewsChannelTest do
  alias ServerWeb.NewsChannel
  alias ServerWeb.UserSocket
  use ServerWeb.ChannelCase

  setup do
    {:ok, _, socket} =
      socket(UserSocket, "user_id", %{some: :assign})
      |> subscribe_and_join(NewsChannel, "news:all")

    {:ok, socket: socket}
  end

  test "joining replies with all news", %{socket: socket} do
    # TODO add real data, this is just to verify the join reply
    expected = %{
      pages: 1,
      news: [%{id: "1", user_id: "1", text: "awsome", date_time: "2018-12-05 10:00"}]
    }

    {:ok, reply, _} = join(socket, NewsChannel, "news:all", %{})

    assert MapSet.subset?(MapSet.new(reply), MapSet.new(expected))
  end

  test "create broadcasts new post", %{socket: socket} do
    # TODO add real data, this is just to verify the broadcast
    push(socket, "create", %{"text" => "create test news"})
    assert_broadcast("new_post", %{:body => "create test news", :custom => "default_search_criteria"})
  end
end
