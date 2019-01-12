defmodule Persistence.Db do
  use GenServer

  def start_link(state) do
    GenServer.start_link(__MODULE__, state, name: __MODULE__)
  end

  ## Callbacks

  def init(_state) do
    table = :ets.new(:news_table, [:bag, :named_table, :public])
    {:ok, table}
  end

  def handle_call({:delete_news, key}, _from, state) do
    :ets.delete(:news_table, key)
    {:reply, :ok, state}
  end

  def handle_call({:lookup_news, key}, _from, state) do
    {:reply, :ets.lookup(:news_table, key), state}
  end

  def handle_call({:insert_news, key, value}, _from, state) do
    :ets.insert(:news_table, {key, value})
    {:reply, :ok, state}
  end

end
