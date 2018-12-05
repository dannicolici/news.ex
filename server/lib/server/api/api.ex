defmodule Server.Api do
  @moduledoc """
  The Api context.
  """

  # import Ecto.Query, warn: false
  alias Server.Repo

  alias Server.Api.News

  @doc """
  Returns the list of news.

  ## Examples

      iex> list_news()
      [%News{}, ...]

  """
  def list_news do
    raise "TODO"
  end

  @doc """
  Gets a single news.

  Raises if the News does not exist.

  ## Examples

      iex> get_news!(123)
      %News{}

  """
  def get_news!(id), do: raise "TODO"

  @doc """
  Creates a news.

  ## Examples

      iex> create_news(%{field: value})
      {:ok, %News{}}

      iex> create_news(%{field: bad_value})
      {:error, ...}

  """
  def create_news(attrs \\ %{}) do
    raise "TODO"
  end

  @doc """
  Updates a news.

  ## Examples

      iex> update_news(news, %{field: new_value})
      {:ok, %News{}}

      iex> update_news(news, %{field: bad_value})
      {:error, ...}

  """
  def update_news(%News{} = news, attrs) do
    raise "TODO"
  end

  @doc """
  Deletes a News.

  ## Examples

      iex> delete_news(news)
      {:ok, %News{}}

      iex> delete_news(news)
      {:error, ...}

  """
  def delete_news(%News{} = news) do
    raise "TODO"
  end

  @doc """
  Returns a data structure for tracking news changes.

  ## Examples

      iex> change_news(news)
      %Todo{...}

  """
  def change_news(%News{} = news) do
    raise "TODO"
  end
end
