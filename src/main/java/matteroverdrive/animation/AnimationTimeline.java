package matteroverdrive.animation;

import matteroverdrive.util.math.MOMathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/31/2015.
 */
public class AnimationTimeline <T extends AnimationSegment>
{
    private boolean loopable;
    protected int time;
    private int duration;
    private List<T> segments;
    private int lastSegmentBegin;

    public AnimationTimeline(boolean loopable,int duration)
    {
        segments = new ArrayList<>();
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

    public void addSegmentSequential(T segment)
    {
        segment.begin = lastSegmentBegin;
        lastSegmentBegin += segment.length;
        segments.add(segment);
    }

    public T getCurrentSegment()
    {
        for (T segment : segments) {
            if (MOMathHelper.animationInRange(time, segment.begin, segment.length)) {
                return segment;
            }
        }
        return null;
    }

    public void tick()
    {
        if (time < duration)
        {
            time++;
        } else if (loopable)
        {
            time = 0;
        }
    }

    public void setTime(int time)
    {
        this.time = time;
    }
}
