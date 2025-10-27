package tendering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VehicleDetailsRequest {

    @NotBlank(message = "Vehicle number is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{1,2}[A-Z]{1,2}[0-9]{4}$", 
             message = "Vehicle number must be in Indian format (e.g., MH12AB1234)")
    private String vehicleNumber;

    @NotBlank(message = "Driver name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Driver name can only contain letters and spaces")
    private String driverName;

    @NotBlank(message = "Driver contact is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Please provide a valid 10-digit Indian mobile number")
    private String driverContact;

    @NotBlank(message = "Driver license number is required")
    private String dlNumber; 

    // Default constructor
    public VehicleDetailsRequest() {}

    // All-args constructor
    public VehicleDetailsRequest(String vehicleNumber, String driverName, String driverContact, String dlNumber) {
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.dlNumber = dlNumber;
    }

    // Getters and Setters
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getDlNumber() {
        return dlNumber;
    }

    public void setDlNumber(String dlNumber) {
        this.dlNumber = dlNumber;
    }

    @Override
    public String toString() {
        return "VehicleDetailsRequest{" +
                "vehicleNumber='" + vehicleNumber + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverContact='" + driverContact + '\'' +
                ", dlNumber='" + dlNumber + '\'' +  
                '}';
    }
}
