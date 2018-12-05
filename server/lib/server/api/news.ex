defmodule Server.Api.News do
  alias Server.Api.Data

  @derive {Jason.Encoder, only: [:pages, :news]}
  defstruct pages: 0, news: []
end
