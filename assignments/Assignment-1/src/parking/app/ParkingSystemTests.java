package parking.app;
import parking.model.*;
import parking.service.ParkingFacility;
import java.time.*;
public class ParkingSystemTests {
    private static int passed, failed;
    public static void main(String[] args) {
        test("normal admission and exit", () -> { ParkingFacility p=new ParkingFacility(new int[]{1,1}); LocalDateTime t=LocalDateTime.of(2026,11,10,8,0); p.admit(new Car("TN01AA1111"),t); require(p.exit("TN01AA1111",t.plusHours(1))==30.0); require(p.getTotalOccupancy()==0); });
        test("last free slot and full facility", () -> { ParkingFacility p=new ParkingFacility(new int[]{1}); require(p.admit(new Car("TN01AA1111"),LocalDateTime.now())!=null); require(p.admit(new Car("TN01BB2222"),LocalDateTime.now())==null); });
        test("zero-minute parking", () -> { ParkingFacility p=new ParkingFacility(new int[]{1}); LocalDateTime t=LocalDateTime.of(2026,11,10,8,0); p.admit(new Car("TN01AA1111"),t); require(p.exit("TN01AA1111",t)==0.0); });
        test("exact hour boundary", () -> { ParkingFacility p=new ParkingFacility(new int[]{1}); LocalDateTime t=LocalDateTime.of(2026,11,10,8,0); p.admit(new Car("TN01AA1111"),t); require(p.exit("TN01AA1111",t.plusMinutes(60))==30.0); });
        test("duplicate registration", () -> { ParkingFacility p=new ParkingFacility(new int[]{2}); LocalDateTime t=LocalDateTime.now(); p.admit(new Car("TN01AA1111"),t); expect(IllegalArgumentException.class,()->p.admit(new Car("tn01aa1111"),t)); });
        test("invalid slot", () -> { ParkingFacility p=new ParkingFacility(new int[]{1}); require(!p.release(4,0)); require(!p.release(0,2)); require(!p.release(0,0)); });
        test("e-bike flat fee", () -> require(new EBike("TN01AA1111").calculateFee(300)==25.0));
        test("delivery van peak rate", () -> require(new DeliveryVan("TN01AA1111").calculateFee(LocalDateTime.of(2026,11,10,9,0),LocalDateTime.of(2026,11,10,10,0))==60.0));
        test("five floors with different capacities", () -> { ParkingFacility p=new ParkingFacility(new int[]{2,3,1,4,2}); require(p.getFloorCount()==5 && p.getSlotCount(1)==3 && p.getSlotCount(3)==4); });
        System.out.println("Tests passed: "+passed+", failed: "+failed);
        if (failed>0) throw new AssertionError("Some tests failed");
    }
    private static void test(String name,Runnable action) { try { action.run(); passed++; System.out.println("PASS: "+name); } catch(Throwable e) { failed++; System.out.println("FAIL: "+name+" - "+e.getMessage()); } }
    private static void require(boolean value) { if(!value) throw new AssertionError("Condition was false"); }
    private static void expect(Class<? extends Throwable> type,Runnable action) { try { action.run(); } catch(Throwable e) { if(type.isInstance(e)) return; throw new AssertionError("Unexpected exception: "+e); } throw new AssertionError("Expected exception was not thrown"); }
}