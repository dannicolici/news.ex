defmodule Server.Api.News.Service do
  alias Persistence.Db

  def get_all() do
    GenServer.call(Db, :all_news)
  end

  def delete(key) do      
    GenServer.call(Db, {:delete_news, key})
  end

  def find(key) do
    GenServer.call(Db, {:lookup_news, key})
  end

  def insert_news(body, user) do
    GenServer.call(
      Db,
      {:insert_news, user,
       %{
         id: "#{System.unique_integer([:monotonic, :positive])}",
         user_id: user,
         text: body,
         date_time: now_string()
       }}
    )
  end

  defp now_string() do
    {{y, m, d}, {h, mi, _}} = :calendar.local_time()
    "#{y}-#{m |> pad}-#{d |> pad} #{h |> pad}:#{mi |> pad}"
  end

  defp pad(number) do
    number
    |> Integer.to_string()
    |> String.pad_leading(2, "0")
  end
end
