defmodule Persistence.Db do
  use GenServer

  def start_link(state) do
    GenServer.start_link(__MODULE__, state, name: __MODULE__)
  end

  def init(_state) do
    table = :ets.new(:news_table, [:bag, :named_table, :public])
    # insert 1 dummy row to have some data to display on join
    :ets.insert(
      :news_table,
      {"1", %{id: "1", user_id: "x_dummy", text: "default", date_time: "2018-12-05 10:00"}}
    )

    {:ok, table}
  end

  def handle_call({:delete_news, key}, _from, state) do
    :ets.delete(:news_table, key)
    {:reply, :ok, state}
  end

  def handle_call({:lookup_news, key}, _from, state) do
    {:reply, :ets.lookup(:news_table, key), state}
  end

  def handle_call(:all_news, _from, state) do
    {:reply, :ets.match(:news_table, {:"$0", :"$1"}), state}
  end

  def handle_call({:insert_news, key, value}, _from, state) do
    :ets.insert(:news_table, {key, value})
    {:reply, :ok, state}
  end
end
