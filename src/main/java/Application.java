import java.util.*;


public class Application 
{
    static double quantum = 1.0;
    static double remQuantum = 0.0;
    static double contextSwitch = 0.0;
    static double arrivalControl = 5.0;
    private static int maxProcesses = 500;
    Application(double qt, double cs, int mp, double at)
    {
        quantum = qt;
        contextSwitch = cs;
        maxProcesses = mp;
        arrivalControl = at;
    }
    public static void main(String[] args)
    {
        Menu frame = new Menu();
        frame.setVisible(true);
        ArrayList<Process> pList = new ArrayList<Process>();
        Process firstProcess = new Process(0, 0.0, 0.0);
        firstProcess.setOrigServiceTime(genServiceTime());
        pList.add(0, firstProcess);

        for (int i = 1; i < maxProcesses; i++)
        {
            Process process = new Process();
            process.setInterArrivalTime(genInterArrivalTime());
            process.setOrigServiceTime(genServiceTime());
            process.setId(i);
            pList.add(i, process);
            process.setArrivalTime(cleanDouble(pList.get(i-1).getArrivalTime() + process.getInterArrivalTime()));
        }

        Scheduler scheduler = new Scheduler(pList);
        CPU cpu = new CPU();

        cpu.run(scheduler);

    }

    // function for truncating doubles to two decimal places
    static double cleanDouble(double dirtyDouble)
    {
        int temp = (int)(dirtyDouble * 100.0);
        return ((double)temp)/100.0;
    }

    private static double genInterArrivalTime()
    {
        return cleanDouble(-arrivalControl * Math.log(1 - Math.random() ));
    }

    private static double genServiceTime()
    {
        return cleanDouble(2 + ( (5-2) * Math.random() ) );
    }

    private static double averageServiceTime(ArrayList<Process> finishedProcesses)
    {
        double sum = 0.0;
        double count = 0.0;
        for (Process p : finishedProcesses)
        {
            sum += p.getOrigServiceTime();
            count++;
        }
        return cleanDouble(sum / count);
    }

    private static double averageInterArrivalTime(ArrayList<Process> finishedProcesses)
    {
        double sum = 0.0;
        double count = 0.0;
        for (Process p : finishedProcesses)
        {
            sum += p.getInterArrivalTime();
            count++;
        }
        return cleanDouble(sum / count);
    }
    private static double averageWaitTime(ArrayList<Process> finishedProcesses)
    {
        double sum = 0.0;
        double count = 0.0;
        for (Process p : finishedProcesses)
        {
            sum += p.getWaitTime();
            count++;
        }
        return cleanDouble(sum / count);
    }

    private static double averageTurnaroundTime(ArrayList<Process> finishedProcesses)
    {
        double sum = 0.0;
        double count = 0.0;
        for (Process p : finishedProcesses)
        {
            sum += p.getTurnTime();
            count++;
        }
        return cleanDouble(sum / count);
    }

    static void printFinal20Header()
    {
        String leftStringAlignFormat = "| %-16s | %-16s | %-16s | %-16s | %-16s | %-16s | %-16s | %-16s |%n";
        System.out.format(leftStringAlignFormat, "ProcessID", "Service", "InterArrvl", "Arrival" , "Start",
                "End", "Turnaround", "Wait");
    }

    static void printStartEndWait(Process p)
    {
        String leftNumAlignFormat = "| %-16d | %-16.2f | %-16.2f | %-16.2f | %-16.2f | %-16.2f | %-16.2f | %-16.2f |%n";
        String divider = "------------------------------------------------------------------------------------------------"
                + "---------------------------------------------------------";
         System.out.println(divider);
        System.out.format(leftNumAlignFormat, p.getId(), p.getOrigServiceTime(), p.getInterArrivalTime(),
                p.getArrivalTime(), p.getStartTime(), p.getEndTime(), p.getTurnTime(), p.getWaitTime());
    }

    static void printAverages(ArrayList<Process> pList)
    {
        String leftStringAlignFormat = "%n%n%n| %-16s | %-16s | %-16s | %-16s | %-16s |%n";
        String leftNumAlignFormat = "| %-16d | %-16.2f | %-16.2f | %-16.2f | %-16.2f | %n ";
        String divider = "------------------------------------------------------------------------------------------------";
        System.out.format(leftStringAlignFormat, "NumProcesses", "AvgService", "AvgInterArrival" ,
                "AvgWaitTime", "AvgTrnTime");
        System.out.println(divider);
        System.out.format(leftNumAlignFormat, pList.size(), averageServiceTime(pList), averageInterArrivalTime(pList),
                averageWaitTime(pList), averageTurnaroundTime(pList));
    }

    static void printTotalProcGenerated()
    {
        System.out.println("\n\n\nThe total number of processes running in CPU: " + maxProcesses);
    }

    static void printInterArrivalConstant()
    {
        System.out.println("The constant used for the Inter-Arrival random generation formula is: " + arrivalControl);
    }
}
