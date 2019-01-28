defmodule ServerWeb.NewsChannelTest do
  alias ServerWeb.NewsChannel
  alias ServerWeb.UserSocket
  use ServerWeb.ChannelCase
  alias Persistence.Db
  alias Server.Api.News.Service

  @test_news "test news"

  setup do
    ["test_user","user1","user2","user3","user4","user5","user6","1"]
    |> Enum.map &Service.delete/1

    {:ok, socket} = connect(UserSocket, %{"user_id" => "test_user"}, %{})
    {:ok, _, socket} = subscribe_and_join(socket, NewsChannel, "news:all")

    {:ok, socket: socket}
  end

  test "joining replies with all news", %{socket: socket} do    
    insert_news_with("user1", "2019-01-01 10:00")

    {:ok, reply, _} = join(socket, NewsChannel, "news:all", %{:user_id => "test_user"})

    assert %{news: [%{text: @test_news, user_id: "user1"}]} = reply
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

    assert_reply(ref, :ok, %{news: [%{date_time: "2019-01-01 10:00", id: _, text: @test_news, user_id: "user1"},
                                    %{date_time: "2019-01-01 10:00", id: _, text: @test_news, user_id: "user2"},
                                    %{date_time: "2019-01-01 10:00", id: _, text: @test_news, user_id: "user3"}],
                                pages: 1})
  end

  test "all news come in pages", %{socket: socket} do    
    insert_news_with("user1", "2019-01-01 10:00")
    insert_news_with("user2", "2019-01-01 10:01")
    insert_news_with("user3", "2019-01-01 10:02")
    insert_news_with("user4", "2019-01-01 10:03")
    insert_news_with("user5", "2019-01-01 10:04")
    insert_news_with("user6", "2019-01-01 10:05")   

    ref = push(socket, "get-page", %{"page" => 1})

    assert_reply(ref, :ok, %{news: [%{date_time: "2019-01-01 10:00", id: _, text: @test_news, user_id: "user1"},
                                    %{date_time: "2019-01-01 10:01", id: _, text: @test_news, user_id: "user2"},
                                    %{date_time: "2019-01-01 10:02", id: _, text: @test_news, user_id: "user3"},
                                    %{date_time: "2019-01-01 10:03", id: _, text: @test_news, user_id: "user4"}],
                                pages: 2})

    ref = push(socket, "get-page", %{"page" => 2})

    assert_reply(ref, :ok, %{news: [%{date_time: "2019-01-01 10:04", id: _, text: @test_news, user_id: "user5"},
                                    %{date_time: "2019-01-01 10:05", id: _, text: @test_news, user_id: "user6"}],
                                pages: 2})
  end

  defp insert_news_with(user, date_string) do
    GenServer.call(
      Db,
      {:insert_news, user,
       %{
         id: "#{System.unique_integer([:monotonic, :positive])}",
         user_id: user,
         text: @test_news,
         date_time: date_string
       }}
    )
  end
end
