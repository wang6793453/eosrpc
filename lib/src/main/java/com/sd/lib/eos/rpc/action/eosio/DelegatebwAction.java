package com.sd.lib.eos.rpc.action.eosio;

/**
 * 购买抵押资源
 */
public class DelegatebwAction extends EosioAction<DelegatebwAction.Args>
{
    private final Args args;

    private DelegatebwAction(Builder builder)
    {
        this.args = new Args(builder);
    }

    @Override
    public final String getAction()
    {
        return "delegatebw";
    }

    @Override
    public final Args getArgs()
    {
        return this.args;
    }

    public static class Args
    {
        private final String from;
        private final String receiver;
        private final String stake_net_quantity;
        private final String stake_cpu_quantity;
        private final int transfer;

        private Args(Builder builder)
        {
            this.from = builder.from;
            this.receiver = builder.receiver;
            this.stake_net_quantity = builder.stake_net_quantity;
            this.stake_cpu_quantity = builder.stake_cpu_quantity;
            this.transfer = builder.transfer;
        }

        public String getFrom()
        {
            return from;
        }

        public String getReceiver()
        {
            return receiver;
        }

        public String getStake_net_quantity()
        {
            return stake_net_quantity;
        }

        public String getStake_cpu_quantity()
        {
            return stake_cpu_quantity;
        }

        public int getTransfer()
        {
            return transfer;
        }
    }

    public static class Builder
    {
        private String from;
        private String receiver;
        private String stake_net_quantity;
        private String stake_cpu_quantity;
        private int transfer;

        /**
         * 购买者账户
         *
         * @param from
         * @return
         */
        public Builder setFrom(String from)
        {
            this.from = from;
            return this;
        }

        /**
         * 接收者账户
         *
         * @param receiver
         * @return
         */
        public Builder setReceiver(String receiver)
        {
            this.receiver = receiver;
            return this;
        }

        /**
         * 设置网络抵押金额
         *
         * @param stake_net_quantity
         * @return
         */
        public Builder setStake_net_quantity(String stake_net_quantity)
        {
            this.stake_net_quantity = stake_net_quantity;
            return this;
        }

        /**
         * 设置cpu抵押金额
         *
         * @param stake_cpu_quantity
         * @return
         */
        public Builder setStake_cpu_quantity(String stake_cpu_quantity)
        {
            this.stake_cpu_quantity = stake_cpu_quantity;
            return this;
        }

        public DelegatebwAction build()
        {
            return new DelegatebwAction(this);
        }
    }
}
