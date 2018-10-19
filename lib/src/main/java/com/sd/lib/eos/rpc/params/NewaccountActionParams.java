package com.sd.lib.eos.rpc.params;


import com.sd.lib.eos.rpc.params.model.KeyModel;
import com.sd.lib.eos.rpc.params.model.PermissionModel;
import com.sd.lib.eos.rpc.utils.Utils;

/**
 * 创建新账户
 */
public class NewaccountActionParams extends BaseParams<NewaccountActionParams.Args, NewaccountActionParams.Builder>
{
    private final Args args;

    private NewaccountActionParams(Builder builder)
    {
        super(builder);
        this.args = new Args(builder);
    }

    @Override
    public final String getAction()
    {
        return "newaccount";
    }

    @Override
    public Args getArgs()
    {
        return this.args;
    }

    public static class Args extends ActionParams.Args
    {
        private final String creator;
        private final String name;
        private final PermissionModel owner;
        private final PermissionModel active;

        private Args(Builder builder)
        {
            this.creator = builder.creator;
            this.name = builder.name;
            this.owner = builder.owner;
            this.active = builder.active;
        }

        public String getCreator()
        {
            return creator;
        }

        public String getName()
        {
            return name;
        }

        public PermissionModel getOwner()
        {
            return owner;
        }

        public PermissionModel getActive()
        {
            return active;
        }
    }

    public static class Builder extends BaseParams.Builder<Builder>
    {
        private String creator;
        private String name;
        private PermissionModel owner;
        private PermissionModel active;

        /**
         * 设置创建者账户
         *
         * @param creator
         * @return
         */
        public Builder setCreator(String creator)
        {
            this.creator = creator;
            return this;
        }

        /**
         * 设置新账户名称
         *
         * @param name
         * @return
         */
        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder setOwner(int threshold, String key, int weight)
        {
            this.owner = new PermissionModel(threshold, new KeyModel(key, weight));
            if (active == null)
                setActive(threshold, key, weight);
            return this;
        }

        public Builder setActive(int threshold, String key, int weight)
        {
            this.active = new PermissionModel(threshold, new KeyModel(key, weight));
            return this;
        }

        public NewaccountActionParams build()
        {
            Utils.checkEmpty(creator, "");
            Utils.checkEmpty(name, "");
            Utils.checkNotNull(owner, "");
            Utils.checkNotNull(active, "");
            return new NewaccountActionParams(this);
        }
    }
}