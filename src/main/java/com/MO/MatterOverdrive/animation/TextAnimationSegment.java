package com.MO.MatterOverdrive.animation;

import com.MO.MatterOverdrive.util.MOStringHelper;

/**
 * Created by Simeon on 5/31/2015.
 */
public class TextAnimationSegment extends AnimationSegment
{
    String string;
    int animationType;

    public TextAnimationSegment(String string,int begin, int length,int animationType)
    {
        super(begin,length);
        this.string = string;
        this.animationType = animationType;
    }

    public TextAnimationSegment(String string, int length,int animationType)
    {
        this(string,0,length, animationType);
    }

    public TextAnimationSegment setLengthPerCharacter(int lenght)
    {
        this.length = string.length() * lenght;
        return this;
    }

    public String getText(int time) {
        if (animationType == 1) {
            return MOStringHelper.typingAnimation(string, (time - begin), length);
        }
        return string;
    }
}
