package com.sd.lib.eos.rpc.utils;

import android.util.Log;

import com.sd.lib.eos.rpc.api.model.GetActionsResponse;

import java.util.List;

public abstract class BoundEosActionsLoader extends EosActionsLoader
{
    private final int mOriginalStart;
    private final int mOriginalEnd;
    private final boolean mIsReverse;

    private int mMaxSize = -1;
    private int mStart = -1;
    private int mEnd = -1;
    private int mNextPosition = -1;

    public BoundEosActionsLoader(String accountName, int start, int end)
    {
        super(accountName);
        if (start < 0 && end < 0)
            throw new IllegalArgumentException("");

        mOriginalStart = start;
        mOriginalEnd = end;

        if (start < 0)
        {
            mIsReverse = true;
        } else if (end < 0)
        {
            mIsReverse = false;
        } else
        {
            mIsReverse = start > end;
        }
    }

    @Override
    protected String getLogTag()
    {
        return BoundEosActionsLoader.class.getSimpleName();
    }

    public final int getMaxSize()
    {
        return mMaxSize;
    }

    public final boolean isReverse()
    {
        return mIsReverse;
    }

    @Override
    public void reset()
    {
        setMaxSize(-1);
        setStart(-1);
        setEnd(-1);
        setNextPosition(-1);
        Log.e(getLogTag(), "reset");
    }

    @Override
    public final boolean hasNextPage()
    {
        checkInit();

        if (mMaxSize <= 0)
            return false;

        if (mNextPosition < 0 || mEnd < 0)
            return false;

        if (mIsReverse)
        {
            return mNextPosition >= mEnd;
        } else
        {
            return mNextPosition <= mEnd;
        }
    }

    public final int init() throws Exception
    {
        if (mMaxSize < 0)
        {
            Log.i(getLogTag(), "start init");
            final int size = initImpl();
            if (size <= 0)
            {
                setMaxSize(0);
            } else
            {
                setMaxSize(size);
            }

            if (mMaxSize > 0)
            {
                setStart(mOriginalStart);
                setEnd(mOriginalEnd);

                if (mIsReverse)
                {
                    if (mOriginalStart < 0 || mOriginalStart >= mMaxSize)
                        setStart(mMaxSize - 1);
                } else
                {
                    if (mOriginalEnd < 0 || mOriginalEnd >= mMaxSize)
                        setEnd(mMaxSize - 1);
                }
            }
        }
        return mMaxSize;
    }

    @Override
    public final List<GetActionsResponse.Action> loadPage(int pageSize) throws Exception
    {
        if (pageSize <= 0)
            throw new IllegalArgumentException("Illegal page size:" + pageSize);

        init();

        if (mMaxSize <= 0)
            return null;

        if (!isBoundLegal(mStart))
        {
            Log.e(getLogTag(), "start bound " + mStart + " out of range [0," + (mMaxSize - 1) + "]");
            return null;
        }
        if (!isBoundLegal(mEnd))
        {
            Log.e(getLogTag(), "end bound " + mEnd + " out of range [0," + (mMaxSize - 1) + "]");
            return null;
        }
        if (!isBoundLegal())
            return null;

        if (!hasNextPage())
            return null;

        final int position = mNextPosition;
        final int size = Math.min(Math.abs(position - mEnd) + 1, pageSize);
        Log.i(getLogTag(), "loadPage position:" + position + " size:" + size);

        final List<GetActionsResponse.Action> list = loadPageImpl(position, size);
        if (list == null || list.isEmpty())
            return null;

        Log.i(getLogTag(), "loadPage size:" + list.size());

        setNextPosition(provideNextPosition(list));
        if (mNextPosition == position)
            throw new RuntimeException("you must change next position after load page succcess");

        Log.i(getLogTag(), "loadPage finish next position:" + mNextPosition);
        return list;
    }

    protected abstract int initImpl() throws Exception;

    protected abstract List<GetActionsResponse.Action> loadPageImpl(int position, int pageSize) throws Exception;

    protected final int provideNextPosition(List<GetActionsResponse.Action> list)
    {
        if (list == null || list.isEmpty())
            throw new IllegalArgumentException("can not provide next position from empty list");

        if (list.size() == 1)
            return list.get(0).getAccount_action_seq() + (isReverse() ? -1 : 1);

        final int startSeq = list.get(0).getAccount_action_seq();
        final int endSeq = list.get(list.size() - 1).getAccount_action_seq();

        if (startSeq == endSeq)
            throw new IllegalArgumentException("list startSeq == endSeq");

        final boolean isReverseList = startSeq > endSeq;

        if (isReverseList)
            return isReverse() ? endSeq - 1 : startSeq + 1;
        else
            return isReverse() ? startSeq - 1 : endSeq + 1;
    }

    private void setMaxSize(int maxSize)
    {
        if (mMaxSize != maxSize)
        {
            mMaxSize = maxSize;
            Log.i(getLogTag(), "setMaxSize:" + maxSize);
        }
    }

    private void setStart(int start)
    {
        if (mStart != start)
        {
            mStart = start;
            Log.i(getLogTag(), "setStart:" + start);
            setNextPosition(start);
        }
    }

    private void setEnd(int end)
    {
        if (mEnd != end)
        {
            mEnd = end;
            Log.i(getLogTag(), "setEnd:" + end);
        }
    }

    private void setNextPosition(int nextPosition)
    {
        if (mNextPosition != nextPosition)
        {
            mNextPosition = nextPosition;
            Log.i(getLogTag(), "setNextPosition:" + nextPosition);
        }
    }

    private boolean isBoundLegal(int bound)
    {
        return bound >= 0 && bound < mMaxSize;
    }

    private boolean isBoundLegal()
    {
        if (mIsReverse)
        {
            if (mStart < mEnd)
            {
                Log.e(getLogTag(), "Illegal bound [" + mStart + "," + mEnd + "] isReverse:" + mIsReverse);
                return false;
            }
            return true;
        } else
        {
            if (mStart > mEnd)
            {
                Log.e(getLogTag(), "Illegal bound [" + mStart + "," + mEnd + "] isReverse:" + mIsReverse);
                return false;
            }
            return true;
        }
    }

    private void checkInit()
    {
        if (mMaxSize < 0)
            throw new RuntimeException("loader is not initialized");
    }
}
