package com.sd.lib.eos.rpc.params;

import com.sd.lib.eos.rpc.core.output.model.AuthorizationModel;

import java.util.List;

public interface ActionParams<A extends ActionParams.Args>
{
    String getCode();

    String getAction();

    A getArgs();

    String getAuthorizationActor();

    void setAuthorizationPermission(String permission);

    List<AuthorizationModel> getAuthorization();

    class Args
    {
    }
}
