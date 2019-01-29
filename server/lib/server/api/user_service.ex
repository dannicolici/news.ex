defmodule Server.Api.User.Service do
    alias Persistence.Db

    def insert(user, pass) do
        GenServer.call(Db, {:insert_user, user, Pbkdf2.hash_pwd_salt(pass)})
    end

    def get(user) do
        GenServer.call(Db, {:lookup_user, user})
    end

    def encrypt(pass) do
        pass
    end

end
