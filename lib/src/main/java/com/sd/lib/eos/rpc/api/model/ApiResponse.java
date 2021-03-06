package com.sd.lib.eos.rpc.api.model;

public class ApiResponse<S>
{
    private final S success;
    private final ErrorResponse error;

    public ApiResponse(S success)
    {
        this.success = success;
        this.error = null;
    }

    public ApiResponse(ErrorResponse error)
    {
        this.success = null;
        this.error = error;
    }

    public S getSuccess()
    {
        return success;
    }

    public ErrorResponse getError()
    {
        return error;
    }

    public boolean isSuccessful()
    {
        return success != null && error == null;
    }
}
