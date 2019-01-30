defmodule ServerWeb.NewsChannelTest do
  alias ServerWeb.NewsChannel
  alias ServerWeb.UserSocket
  use ServerWeb.ChannelCase
  alias Persistence.Db
  alias Server.Api.News.Service
  alias Server.Api.User.Service, as: UserService

  @test_news "test news"

  defp connect(user) do
    UserService.insert(user, "pass")
    {:ok, socket} = connect(UserSocket, %{token: Phoenix.Token.sign(ServerWeb.Endpoint, "user salt", user)}, %{})
    {:ok, _, socket} = subscribe_and_join(socket, NewsChannel, "news:all")

    socket
  end

  defp insert_news_with(user, date_string) do
    socket = connect(user)
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
    socket
  end
  
  defp clean(user_ids) do
    user_ids
    |> Enum.map(&Service.delete/1)
  end

  test "joining replies with all news" do
    socket = connect("user1")
    insert_news_with("user1", "2019-01-01 10:00")

    {:ok, reply, _} = join(socket, NewsChannel, "news:all", %{:user_id => "user1"})

    assert %{news: [%{text: @test_news, user_id: "user1"}]} = reply
    clean(["user1"])
  end

  test "create broadcasts and persists new post message" do
    socket = connect("test_user")
    push(socket, "create", %{"text" => "create test news"})

    assert_broadcast("new_post", %{:body => "create test news"}, 500)
    clean(["test_user"])
  end

  test "create saves new post" do
    socket = connect("test_user")
    push(socket, "create", %{"text" => "save news"})
    assert_broadcast("new_post", %{:body => "save news"}, 500)

    news = Service.find("test_user")
    assert [{"test_user", %{text: "save news", user_id: "test_user"}}] = news
    clean(["test_user"])
  end

  test "sort replies with all news sorted by user id" do
    insert_news_with("user2", "2019-01-01 10:00")
    insert_news_with("user3", "2019-01-01 10:00")
    socket = insert_news_with("user1", "2019-01-01 10:00")

    ref = push(socket, "sort", %{"sort_by" => "user_id"})

    assert_reply(ref, :ok, %{news: [%{date_time: "2019-01-01 10:00", id: _, text: @test_news, user_id: "user1"},
                                    %{date_time: "2019-01-01 10:00", id: _, text: @test_news, user_id: "user2"},
                                    %{date_time: "2019-01-01 10:00", id: _, text: @test_news, user_id: "user3"}],
                                pages: 1})
    clean(["user1","user2","user3"])
  end

  test "all news come in pages" do
    insert_news_with("user1", "2019-01-01 10:00")
    insert_news_with("user2", "2019-01-01 10:01")
    insert_news_with("user3", "2019-01-01 10:02")
    insert_news_with("user4", "2019-01-01 10:03")
    insert_news_with("user5", "2019-01-01 10:04")
    socket = insert_news_with("user6", "2019-01-01 10:05")

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
    clean(["user1","user2","user3","user4","user5","user6"])
  end
end
