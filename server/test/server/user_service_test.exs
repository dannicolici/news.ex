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
                    assert hashed_pass == pass
                end
    end
end