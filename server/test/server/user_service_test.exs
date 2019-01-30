defmodule Server.Api.UserServiceTest do
    use ExUnit.Case
    use ExUnitProperties
    alias Server.Api.User.Service

    property "user with password is stored with encrypted password" do
        check all user <- binary(),
                pass <- binary() do
                    Service.insert(user, pass)
                    [{u, hashed_pass}] = Service.get(user)

                    assert u == user
                    assert Service.pass_matches_hash? pass, hashed_pass
                end
    end
end
