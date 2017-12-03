import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Lanae on 3/29/2016.
 */
class CPU
{
    private double clock = 0.0;
    Queue<Process> readyQueue = new LinkedList<Process>();
    private  boolean available = true;
    private boolean finished = false;

    CPU() {}

    boolean isAvailable()
    {
        return available;
    }

    void setAvailable(boolean available)
    {
        this.available = available;
    }

    private void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    void setClock(double clock)
    {
        this.clock = clock;
    }

    double getClock()
    {
        return clock;
    }

    private void incrementClock(Scheduler scheduler)
    {
        scheduler.checkArrival(this);
        if (readyQueue.size() != 0)
        {
            readyQueue.remove().run(this, scheduler);
        }
        else
        {
            // this handles idle CPU time when there is no current process in the ready queue
            for (int i = 0; i < 10 * Application.arrivalControl; i++)
            {
                setClock(this.clock + Application.quantum);
                //System.out.println("No processes in queue. Clock is now " + getClock());
                scheduler.checkArrival(this);
                //System.out.println("Checked arrival queue. Current size =  " + readyQueue.size());
                if (readyQueue.size() != 0)
                {
                    readyQueue.remove().run(this, scheduler);
                    return;
                }
            }
            // eventually shut off CPU after a certain time if no new processes arrive
            if (readyQueue.size() == 0)
            {
                setFinished(true);
            }
        }
    }

    private void addContextSwitch()
    {
        setClock(getClock() + Application.contextSwitch);
    }

    void run(Scheduler scheduler)
    {
        while (!finished && scheduler.getFinishedCount() < 100)
        {
            incrementClock(scheduler);
            addContextSwitch();
        }

        Application.printFinal20Header();
        for (int i = 0; i <20; i++)
        {
            Application.printStartEndWait(scheduler.getFinishedProcesses().get(i));
        }
        Application.printTotalProcGenerated();
        Application.printInterArrivalConstant();
        Application.printAverages(scheduler.getFinishedProcesses());
    }
}
