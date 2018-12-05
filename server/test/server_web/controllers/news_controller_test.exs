defmodule ServerWeb.NewsControllerTest do
  use ServerWeb.ConnCase

  alias Server.Api
  alias Server.Api.News

  @create_attrs %{
    news: "some news",
    pages: 42
  }
  @update_attrs %{
    news: "some updated news",
    pages: 43
  }
  @invalid_attrs %{news: nil, pages: nil}

  def fixture(:news) do
    {:ok, news} = Api.create_news(@create_attrs)
    news
  end

  setup %{conn: conn} do
    {:ok, conn: put_req_header(conn, "accept", "application/json")}
  end

  describe "index" do
    test "lists all news", %{conn: conn} do
      conn = get(conn, Routes.news_path(conn, :index))
      assert json_response(conn, 200)["data"] == []
    end
  end

  describe "create news" do
    test "renders news when data is valid", %{conn: conn} do
      conn = post(conn, Routes.news_path(conn, :create), news: @create_attrs)
      assert {:ok, id} = json_response(conn, 201)["data"]

      conn = get(conn, Routes.news_path(conn, :show, id))

      assert %{
               "news" => "some news",
               "pages" => 42
             } = json_response(conn, 200)["data"]
    end

    test "renders errors when data is invalid", %{conn: conn} do
      conn = post(conn, Routes.news_path(conn, :create), news: @invalid_attrs)
      assert json_response(conn, 422)["errors"] != %{}
    end
  end

  defp create_news(_) do
    news = fixture(:news)
    {:ok, news: news}
  end
end
