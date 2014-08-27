import directionalChanges.DirectionalChanges;
import directionalChanges.algorithm.events.EEvent;
import directionalChanges.algorithm.runs.IRun;

import java.util.ArrayList;
import java.util.List;

public class DCListener implements DirectionalChanges.OnDirectionalChangesListener {

    private List<EEvent>        events;


    public DCListener()
    {
        events = new ArrayList<EEvent>();
    }

    @Override
    public void onDownwardRun(IRun downwardRun) {
        int     start;
        int     end;

        if (downwardRun != null && downwardRun.getEvent() != null)
        {
            start = downwardRun.getEvent().getStartingPointIndex();
            end = downwardRun.getEvent().getEndingPointIndex();
            for (; start <= end; start++)
                events.add(EEvent.DOWNTURN);
        }
        if (downwardRun != null && downwardRun.getOvershootEvent() != null)
        {
            start = downwardRun.getOvershootEvent().getStartingPointIndex();
            end = downwardRun.getOvershootEvent().getEndingPointIndex();
            for (; start <= end; start++)
                events.add(EEvent.DOWNTURN_OVERSHOOT);
        }
    }

    @Override
    public void onUpwardRun(IRun upwardRun) {
        int     start;
        int     end;

        if (upwardRun != null && upwardRun.getEvent() != null)
        {
            start = upwardRun.getEvent().getStartingPointIndex();
            end = upwardRun.getEvent().getEndingPointIndex();
            for (; start <= end; start++)
                events.add(EEvent.UPTURN);
        }
        if (upwardRun != null && upwardRun.getOvershootEvent() != null)
        {
            start = upwardRun.getOvershootEvent().getStartingPointIndex();
            end = upwardRun.getOvershootEvent().getEndingPointIndex();
            for (; start <= end; start++)
                events.add(EEvent.UPTURN_OVERSHOOT);
        }
    }

    public List<EEvent> getEvents()
    {
        return events;
    }

}
