defmodule Server.ApiTest do
  use Server.DataCase

  alias Server.Api

  describe "news" do
    alias Server.Api.News

    @valid_attrs %{news: "some news", pages: 42}
    @update_attrs %{news: "some updated news", pages: 43}
    @invalid_attrs %{news: nil, pages: nil}

    def news_fixture(attrs \\ %{}) do
      {:ok, news} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Api.create_news()

      news
    end

    test "list_news/0 returns all news" do
      news = news_fixture()
      assert Api.list_news() == [news]
    end

    test "get_news!/1 returns the news with given id" do
      news = news_fixture()
      assert Api.get_news!(news.id) == news
    end

    test "create_news/1 with valid data creates a news" do
      assert {:ok, %News{} = news} = Api.create_news(@valid_attrs)
      assert news.news == "some news"
      assert news.pages == 42
    end

    test "create_news/1 with invalid data returns error" do
      assert {:error, _} = Api.create_news(@invalid_attrs)
    end

    test "update_news/2 with valid data updates the news" do
      news = news_fixture()
      assert {:ok, %News{} = news} = Api.update_news(news, @update_attrs)
      assert news.news == "some updated news"
      assert news.pages == 43
    end

    test "update_news/2 with invalid data returns error" do
      news = news_fixture()
      assert {:error, _} = Api.update_news(news, @invalid_attrs)
      assert news == Api.get_news!(news.id)
    end

    test "delete_news/1 deletes the news" do
      news = news_fixture()
      assert {:ok, %News{}} = Api.delete_news(news)
    end

    test "change_news/1 returns a news changeset" do
      news = news_fixture()
      assert _ = Api.change_news(news)
    end
  end
end
