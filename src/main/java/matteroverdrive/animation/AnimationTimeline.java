package matteroverdrive.animation;

import matteroverdrive.util.math.MOMathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/31/2015.
 */
public class AnimationTimeline <T extends AnimationSegment>
{
    boolean loopable;
    int time;
    int duration;
    List<T> segments;
    int lastSegmentBegin;

    public AnimationTimeline(boolean loopable,int duration)
    {
        segments = new ArrayList<T>();
        this.loopable = loopable;
        this.duration = duration;
    }

    public double getPercent()
    {
        return (double)time / (double)duration;
    }

    public void addSegment(T segment)
    {
        segments.add(segment);
    }

    public void addSeqmentSequential(T segment)
    {
        segment.begin = lastSegmentBegin;
        lastSegmentBegin += segment.length;
        segments.add(segment);
    }

    public T getCurrentSegment()
    {
        for (int i = 0;i < segments.size();i++)
        {
            if (MOMathHelper.animationInRange(time,segments.get(i).begin,segments.get(i).length))
            {
                return segments.get(i);
            }
        }
        return null;
    }

    public void tick()
    {
        if (time < duration)
        {
            time++;
        }else if (loopable)
        {
            time = 0;
        }
    }

    public void setTime(int time)
    {
        this.time = time;
    }
}
