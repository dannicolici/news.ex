defmodule Persistence.Db do
  use GenServer

  def start_link(state) do
    GenServer.start_link(__MODULE__, state, name: __MODULE__)
  end

  def init(_state) do
    news_table = :ets.new(:news_table, [:bag, :named_table, :public])
    user_table = :ets.new(:user_table, [:set, :named_table, :public])
    {:ok, {news_table, user_table}}
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

  def handle_call({:insert_user, key, value}, _from, state) do
    :ets.insert(:user_table, {key, value})
    {:reply, :ok, state}
  end

  def handle_call({:lookup_user, key}, _from, state) do
    {:reply, :ets.lookup(:user_table, key), state}
  end
end
