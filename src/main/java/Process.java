import java.util.Comparator;

/**
 * Created by Lanae on 3/9/2016.
 */

enum State {ARRIVED, READY, WAITING, RUNNING, EXECUTED}

class Process implements Comparable
{
    private int id;
    private double interArrivalTime = 0.0;
    private double origServiceTime = 0.0;
    private double currentServiceTime = 0.0;
    private double arrivalTime = 0.0;
    private double startTime = 0.0;
    private double endTime = 0.0;
    private double waitTime = 0.0;
    private double turnTime = 0.0;
    private  State pState = null;

    Process() {}

    Process(int id, double arrivalTime, double interArrivalTime)
    {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.interArrivalTime = interArrivalTime;
    }

    public int compareTo(Object o)
    {
        Process p = (Process) o;
        if (this.getId() == p.getId())
        {
            return 0;
        }
        else if (this.getId() > p.getId())
        {
            return 1;
        }
        else return -1;
    }

    void setId(int id)
    {
        this.id = id;
    }

    int getId()
    {
        return id;
    }

    void setInterArrivalTime(double interArrivalTime)
    {
        this.interArrivalTime = interArrivalTime;
    }

    double getInterArrivalTime()
    {
        return interArrivalTime;
    }

    private void setCurrentServiceTime(double currentServiceTime)
    {
        this.currentServiceTime = currentServiceTime;
    }

    private double getCurrentServiceTime()
    {
        return currentServiceTime;
    }

    void setOrigServiceTime(double origServiceTime)
    {
        this.origServiceTime = origServiceTime;
        this.setCurrentServiceTime(getOrigServiceTime());
    }

     double getOrigServiceTime()
    {
        return origServiceTime;
    }

    void setArrivalTime(double arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

     double getArrivalTime()
     {
         return arrivalTime;
     }

    private void setWaitTime(double waitTime)
    {
        this.waitTime = waitTime;
    }

     double getWaitTime()
    {
        return waitTime;
    }

    private void setTurnTime(double turnTime)
    {
        this.turnTime = turnTime;
    }

     double getTurnTime()
    {
        return turnTime;
    }

    private void setStartTime(double startTime)
    {
        this.startTime = startTime;
    }

    double getStartTime()
    {
        return startTime;
    }

    private void setEndTime(double endTime)
    {
        this.endTime = endTime;
    }

     double getEndTime()
    {
        return endTime;
    }

    void setpState(State pState)
    {
        this.pState = pState;
    }

    State getpState()
    {
        return pState;
    }



    boolean isReady(CPU cpu)
    {
        if ((getArrivalTime() <= cpu.getClock() ))
        {
            setpState(State.ARRIVED);
            return true;
        }
        else
        {
            return false;
        }
    }

    void run(CPU cpu, Scheduler scheduler)
    {
        cpu.setAvailable(false);
        setpState(State.RUNNING);

        if (getStartTime() == 0 && getId() != 0)
        {
            setStartTime(Application.cleanDouble(cpu.getClock()));
        }

        if (Application.remQuantum > 0)
        {
            if (getCurrentServiceTime() > Application.remQuantum)
            {
                setCurrentServiceTime(getCurrentServiceTime() - Application.remQuantum);
                cpu.setClock(cpu.getClock() + Application.remQuantum);
                Application.remQuantum = 0;
            }
            else
            {
                double temp = Application.remQuantum - getCurrentServiceTime();
                setCurrentServiceTime(0);
                cpu.setClock(cpu.getClock() + temp);
                Application.remQuantum =- temp;
            }

        }

        if (getCurrentServiceTime() < 1 && getCurrentServiceTime() > 0)
        {
            cpu.setClock(cpu.getClock() + getCurrentServiceTime());
            Application.remQuantum = getCurrentServiceTime();
            setCurrentServiceTime(0);
        }

        if (getCurrentServiceTime() >= 1)
        {
            setCurrentServiceTime((getCurrentServiceTime()) - Application.quantum);
            cpu.setClock(cpu.getClock() + Application.quantum);
        }

        if (getCurrentServiceTime() == 0)
        {
            setEndTime(Application.cleanDouble(cpu.getClock()));
            setTurnTime(Application.cleanDouble(getEndTime() - getStartTime()));
            setWaitTime(Application.cleanDouble(getTurnTime() - getOrigServiceTime()));
            setpState(State.EXECUTED);
            scheduler.addToFinishedList(this);
        }

        if (getCurrentServiceTime() > 0)
        {
            setpState(State.READY);
            scheduler.checkArrival(cpu);
            cpu.readyQueue.add(this);
        }

        cpu.setAvailable(true);
    }

    @Override
    public String toString()
    {
        return "Process id: " + getId() + "\n Interarrival Time : " + getInterArrivalTime() +
                "\n Arrival time : " + getArrivalTime() + "\n Service time : " + getOrigServiceTime()
                + "\n Start time : " + getStartTime() + "\n End time : " + getEndTime() + "\n Turnaround time: " + getTurnTime()
                + "\n Wait time: " + getWaitTime();
    }
}