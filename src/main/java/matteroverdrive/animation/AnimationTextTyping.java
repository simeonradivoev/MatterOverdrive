package matteroverdrive.animation;

/**
 * Created by Simeon on 5/31/2015.
 */
public class AnimationTextTyping extends AnimationTimeline<AnimationSegmentText>
{
    public AnimationTextTyping(boolean loopable, int duration) {
        super(loopable, duration);
    }

    public String getString()
    {
        AnimationSegmentText segment = getCurrentSegment();
        if (segment != null)
        {
            return segment.getText(time);
        }
        return "";
    }
}
