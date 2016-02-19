package matteroverdrive.data.matter;

/**
 * Created by Simeon on 1/17/2016.
 */
public interface IMatterEntryHandler<T> extends Comparable<IMatterEntryHandler<T>>
{
    int modifyMatter(T obj,int originalMatter);
    int getPriority();
    boolean finalModification(T obj);
}
