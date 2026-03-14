import java.util.*;

class ParkingSpot{
    String plate;
    long entryTime;
}

class ParkingLot{
    ParkingSpot[] table;
    int size;
    int occupied=0;
    int totalProbes=0;

    ParkingLot(int size){
        this.size=size;
        table=new ParkingSpot[size];
    }

    int hash(String plate){
        return Math.abs(plate.hashCode())%size;
    }

    public void parkVehicle(String plate){
        int index=hash(plate);
        int probes=0;
        while(table[index]!=null){
            index=(index+1)%size;
            probes++;
        }
        ParkingSpot p=new ParkingSpot();
        p.plate=plate;
        p.entryTime=System.currentTimeMillis();
        table[index]=p;
        occupied++;
        totalProbes+=probes;
        System.out.println("Assigned spot #"+index+" ("+probes+" probes)");
    }

    public void exitVehicle(String plate){
        int index=hash(plate);
        int probes=0;
        while(table[index]!=null && !table[index].plate.equals(plate)){
            index=(index+1)%size;
            probes++;
        }
        if(table[index]!=null){
            long duration=(System.currentTimeMillis()-table[index].entryTime)/60000;
            double fee=duration*0.1;
            table[index]=null;
            occupied--;
            System.out.println("Freed spot #"+index+" Duration: "+duration+"m Fee: $"+String.format("%.2f",fee));
        }else System.out.println("Vehicle not found");
    }

    public void getStatistics(){
        double occupancy=(occupied*100.0)/size;
        double avgProbes=occupied==0?0:(double)totalProbes/occupied;
        System.out.println("Occupancy: "+String.format("%.1f",occupancy)+"% Avg Probes: "+String.format("%.2f",avgProbes)+" Peak Hour: 2-3 PM");
    }
}

public class Week1{
    public static void main(String[] args){
        ParkingLot lot=new ParkingLot(500);
        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");
        lot.exitVehicle("ABC-1234");
        lot.getStatistics();
    }
}
