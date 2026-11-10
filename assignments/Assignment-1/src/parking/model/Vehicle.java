package parking.model;
import java.util.Objects;
import java.time.LocalTime;
import java.time.Duration;
public abstract class Vehicle {
    private final String regNo;
    protected Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank()) throw new IllegalArgumentException("Registration number cannot be empty");
        this.regNo = regNo.trim().toUpperCase();
    }
    public final String getRegNo() { return regNo; }
    protected abstract double ratePerHour();
    public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        int hours = minutes / 60 + (minutes % 60 == 0 ? 0 : 1);
        return hours * ratePerHour();
    }
    public double calculateFee(LocalTime in, LocalTime out) {
        if (in == null || out == null) throw new IllegalArgumentException("Times are required");
        long minutes = Duration.between(in, out).toMinutes();
        if (minutes < 0) minutes += 24 * 60;
        if (minutes > Integer.MAX_VALUE) throw new IllegalArgumentException("Duration is too large");
        return calculateFee((int) minutes);
    }
    @Override public final boolean equals(Object other) {
        return this == other || (other instanceof Vehicle v && regNo.equals(v.regNo));
    }
    @Override public final int hashCode() { return Objects.hash(regNo); }
    @Override public String toString() { return getClass().getSimpleName() + "{regNo='" + regNo + "'}"; }
}