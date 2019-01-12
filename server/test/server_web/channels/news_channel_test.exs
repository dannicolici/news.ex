defmodule ServerWeb.NewsChannelTest do
  alias ServerWeb.NewsChannel
  alias ServerWeb.UserSocket
  use ServerWeb.ChannelCase
  alias Persistence.Db

  setup do
    {:ok, socket} = connect(UserSocket, %{"user_id" => "test_user"}, %{})
    {:ok, _, socket} = subscribe_and_join(socket, NewsChannel, "news:all")

    {:ok, socket: socket}
  end

  test "joining replies with all news", %{socket: socket} do
    # TODO add real data, this is just to verify the join reply
    expected = %{
      pages: 1,
      news: [%{id: "1", user_id: "1", text: "default", date_time: "2018-12-05 10:00"}]
    }

    {:ok, reply, _} = join(socket, NewsChannel, "news:all", %{:user_id => "test_user"})

    assert MapSet.subset?(MapSet.new(reply), MapSet.new(expected))
  end

  test "create broadcasts new post message", %{socket: socket} do
    push(socket, "create", %{"text" => "create test news"})

    assert_broadcast("new_post", %{:body => "create test news"})
  end

  test "create saves new post", %{socket: socket} do
    GenServer.call(Db, {:delete_news, "test_user"})
    push(socket, "create", %{"text" => "save news"})
    assert_broadcast("new_post", %{:body => "save news"})

    news = GenServer.call(Db, {:lookup_news, "test_user"})
    assert news == [{"test_user", "save news"}]
  end

  test "sort replies with all news sorted", %{socket: socket} do
    # TODO add real data, this is just to verify that the sort message works
    expected = %{
      pages: 1,
      news: [
        %{id: "1", user_id: "1", text: "fancy sorting criteria", date_time: "2018-12-05 10:00"}
      ]
    }

    ref = push(socket, "sort", %{"sort_by" => "fancy sorting criteria"})

    assert_reply(ref, :ok, ^expected)
  end
end
