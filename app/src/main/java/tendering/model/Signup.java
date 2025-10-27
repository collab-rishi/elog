package tendering.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Signup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String password;

    @Convert(converter = UserRoleConverter.class)
    @Column(nullable = false)
    private UserRole role;

    @Column
    private String location;

    @Column(name = "fcm_token")
    private String fcmToken;

    // Default constructor
    public Signup() {}

    // All-args constructor
    public Signup(String email, String companyName, String mobileNumber, String location, String password, UserRole role, String fcmToken) {
        this.email = email;
        this.companyName = companyName;
        this.mobileNumber = mobileNumber;
        this.location = location;
        this.password = password;
        this.role = role;
        this.fcmToken = fcmToken;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Override
    public String toString() {
        return "Signup{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", companyName='" + companyName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", location='" + location + '\'' +
                ", role=" + role +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
