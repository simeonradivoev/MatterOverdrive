package com.MO.MatterOverdrive.animation;

import java.util.List;

/**
 * Created by Simeon on 5/31/2015.
 */
public class AnimationTextTyping extends AnimationTimeline<TextAnimationSegment>
{
    public AnimationTextTyping(boolean loopable, int duration) {
        super(loopable, duration);
    }

    public String getString()
    {
        TextAnimationSegment segment = getCurrentSegment();
        if (segment != null)
        {
            return segment.getText(time);
        }
        return "";
    }
}
