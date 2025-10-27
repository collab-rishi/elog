package tendering.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Sends a welcome email to a new user with personalized greeting and support
	 * info.
	 */
	public void sendWelcomeEmail(String email, String username) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email);
			message.setSubject("Welcome to ELogisticsTendering");

			message.setText("Hi " + username + ",\n\n" + "Welcome to ELogisticsTendering! 🚛\n\n"
					+ "We're thrilled to have you on board.\n"
					+ "Get started today and streamline your logistics experience.\n\n"
					+ "For assistance, contact us at support@elogisticstendering.com.\n\n" + "Regards,\n"
					+ "ELogisticsTendering Team");

			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("Failed to send welcome email: " + e.getMessage());
		}
	}

	/**
	 * Sends a one-time password (OTP) email for verification with instructions and
	 * warnings.
	 * 
	 * @return true if email sent successfully, false otherwise
	 */
	public boolean sendOtp(String email, String otp) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email);
			message.setSubject("Your OTP - ELogisticsTendering");

			message.setText("Hi,\n\n" + "Your verification code is: " + otp + "\n\n"
					+ "Please enter this code in the app to proceed.\n\n"
					+ "If you didn’t request this, please ignore this email.\n\n" + "Thanks,\n"
					+ "ELogisticsTendering Team");

			mailSender.send(message);
			return true;
		} catch (Exception e) {
			System.err.println("Failed to send OTP email: " + e.getMessage());
			return false;
		}
	}
}
