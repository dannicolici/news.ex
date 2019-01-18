defmodule Server.DataCase do
  use ExUnit.CaseTemplate

  using do
    quote do
      # Import conveniences for testing with Datas
      # use Phoenix.DataTest

      # The default endpoint for testing
      @endpoint ServerWeb.Endpoint
    end
  end

  setup _tags do
    :ok
  end
end
