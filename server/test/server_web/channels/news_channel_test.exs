defmodule ServerWeb.NewsChannelTest do
  alias ServerWeb.NewsChannel
  alias ServerWeb.UserSocket
  use ServerWeb.ChannelCase
  alias Persistence.Db
  alias Server.Api.News.Service

  setup do
    Service.delete("test_user")
    Service.delete("user1")
    Service.delete("user2")
    Service.delete("user3")
    Service.delete("1")

    {:ok, socket} = connect(UserSocket, %{"user_id" => "test_user"}, %{})
    {:ok, _, socket} = subscribe_and_join(socket, NewsChannel, "news:all")

    {:ok, socket: socket}
  end

  test "joining replies with all news", %{socket: socket} do    
    insert_news_with("user1", "2019-01-01 10:00")

    {:ok, reply, _} = join(socket, NewsChannel, "news:all", %{:user_id => "test_user"})

    assert %{news: [%{text: "test news", user_id: "user1"}]} = reply
  end

  test "create broadcasts new post message", %{socket: socket} do
    push(socket, "create", %{"text" => "create test news"})

    assert_broadcast("new_post", %{:body => "create test news"})
  end

  test "create saves new post", %{socket: socket} do
    push(socket, "create", %{"text" => "save news"})
    assert_broadcast("new_post", %{:body => "save news"})

    news = Service.find("test_user")
    assert [{"test_user", %{text: "save news", user_id: "test_user"}}] = news
  end

  test "sort replies with all news sorted by user id", %{socket: socket} do
    insert_news_with("user2", "2019-01-01 10:00")
    insert_news_with("user3", "2019-01-01 10:00")
    insert_news_with("user1", "2019-01-01 10:00")

    ref = push(socket, "sort", %{"sort_by" => "user_id"})

    assert_reply(ref, :ok, %{news: [%{date_time: "2019-01-01 10:00", id: _, text: "test news", user_id: "user1"},
                                    %{date_time: "2019-01-01 10:00", id: _, text: "test news", user_id: "user2"},
                                    %{date_time: "2019-01-01 10:00", id: _, text: "test news", user_id: "user3"}],
                                pages: 1})
  end

  defp insert_news_with(user, date_string) do
    GenServer.call(
      Db,
      {:insert_news, user,
       %{
         id: "#{System.unique_integer([:monotonic, :positive])}",
         user_id: user,
         text: "test news",
         date_time: date_string
       }}
    )
  end
end
