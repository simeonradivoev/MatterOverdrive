package matteroverdrive.animation;

/**
 * Created by Simeon on 5/31/2015.
 */
public abstract class AnimationSegment
{
	int begin;
	int length;

	public AnimationSegment(int begin, int length)
	{
		this.begin = begin;
		this.length = length;
	}

	public AnimationSegment(int length)
	{
		this.length = length;
	}
}
