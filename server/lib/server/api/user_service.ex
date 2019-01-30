defmodule Server.Api.User.Service do
    alias Persistence.Db

    def insert(user, pass) do
        GenServer.call(Db, {:insert_user, user, encrypt(pass)})
    end

    def get(user) do
        GenServer.call(Db, {:lookup_user, user})
    end

    def encrypt(pass) do
        Pbkdf2.hash_pwd_salt(pass)
    end

    def pass_matches_hash?(pass, hash) do
        Pbkdf2.verify_pass(pass, hash)
    end

end
