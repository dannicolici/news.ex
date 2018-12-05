defmodule Server.Api.Data do

  @derive {Jason.Encoder, only: [:id, :user_id, :text, :date_time]}
  defstruct id: "", user_id: "", text: "", date_time: ""
end
