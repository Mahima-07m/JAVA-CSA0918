package parking.model;
import java.time.LocalDate;
public final class VisitorPass {
    private final String visitorName;
    private final LocalDate validOn;
    private final double passFee;
    public VisitorPass(String name, LocalDate date, double fee) {
        if (name == null || name.isBlank() || date == null || fee < 0) throw new IllegalArgumentException("Invalid visitor pass details");
        visitorName = name.trim(); validOn = date; passFee = fee;
    }
    public String getVisitorName() { return visitorName; }
    public LocalDate getValidOn() { return validOn; }
    public double getPassFee() { return passFee; }
    public boolean isValidOn(LocalDate date) { return validOn.equals(date); }
}