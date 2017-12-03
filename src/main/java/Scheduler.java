import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Lanae on 3/31/2016.
 */
class Scheduler
{
    private ArrayList<Process> pList = null;
    private ArrayList<Process> finishedProcesses= new ArrayList<Process>();
    private int finishedCount = 0;

    Scheduler(ArrayList<Process> pList)
    {
        this.pList = pList;
    }

    ArrayList<Process> getFinishedProcesses()
    {
        sortPList(finishedProcesses);
        return finishedProcesses;
    }

    private void sortPList(ArrayList<Process> pList)
    {
        Collections.sort(pList, (Process p1, Process p2) -> p1.compareTo(p2));
    }

    void addToFinishedList(Process process)
    {
        finishedProcesses.add(process);
        finishedCount++;
    }

    int getFinishedCount()
    {
        return finishedCount;
    }

    void checkArrival(CPU cpu)
    {
        if (pList == null)
        {
            return;
        }
        for (int i = 0; i < pList.size(); i++)
        {
            if (pList.get(i).isReady(cpu))
            {
                pList.get(i).setpState(State.READY);
                cpu.readyQueue.add(pList.get(i));
                pList.remove(i);
                i--;
            }
        }
    }
}
