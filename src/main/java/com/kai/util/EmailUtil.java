package com.kai.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

	private static final String EMAIL_REGEX =
			"^[A-Za-z0-9._%+-]{1,64}@[A-Za-z0-9.-]{1,190}\\.[A-Za-z]{2,}$";

	private static Session buildSession() {
		final String username = AppConfig.get("mail.smtp.username");
		final String password = AppConfig.get("mail.smtp.password");

		Properties props = new Properties();
		props.put("mail.smtp.host", AppConfig.get("mail.smtp.host", "smtp.gmail.com"));
		props.put("mail.smtp.port", AppConfig.get("mail.smtp.port", "587"));
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
		props.put("mail.smtp.ssl.checkserveridentity", "true");
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.timeout", "10000");
		props.put("mail.smtp.writetimeout", "10000");

		return Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	public static void send(String to, String subject, String htmlBody)
			throws MessagingException, UnsupportedEncodingException {

		if (to == null || !to.matches(EMAIL_REGEX)) {
			throw new MessagingException("Dia chi email khong hop le");
		}

		Session session = buildSession();
		MimeMessage msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(
				AppConfig.get("mail.smtp.username"),
				AppConfig.get("mail.from.name", "MyServiceMVC"),
				"UTF-8"));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
		msg.setSubject(subject, "UTF-8");
		msg.setContent(htmlBody, "text/html; charset=UTF-8");
		msg.setSentDate(new Date());

		Transport.send(msg);
	}

	public static void sendOtp(String to, String fullname, String otp,
			int ttlMinutes, String purposeText)
			throws MessagingException, UnsupportedEncodingException {

		String name = escapeHtml(fullname == null || fullname.isBlank() ? "bạn" : fullname);

		String html = """
				<div style="font-family:Arial,sans-serif;max-width:520px;margin:auto;
				            border:1px solid #ddd;border-radius:8px;padding:24px">
				  <h2 style="color:#2196F3;margin-top:0">MyServiceMVC</h2>
				  <p>Xin chào <b>%s</b>,</p>
				  <p>Mã OTP để <b>%s</b> của bạn là:</p>
				  <p style="font-size:32px;letter-spacing:8px;font-weight:bold;
				            background:#f5f5f5;padding:14px;text-align:center;border-radius:6px">
				    %s
				  </p>
				  <p>Mã có hiệu lực trong <b>%d phút</b> và chỉ dùng được <b>một lần</b>.</p>
				  <p style="color:#888;font-size:13px">
				    Nếu bạn không yêu cầu thao tác này, hãy bỏ qua email.
				    Đừng chia sẻ mã cho bất kỳ ai, kể cả người tự xưng là nhân viên hỗ trợ.
				  </p>
				</div>
				""".formatted(name, escapeHtml(purposeText), otp, ttlMinutes);

		send(to, "[MyServiceMVC] Ma OTP cua ban", html);
	}

	public static void sendPasswordChangedNotice(String to, String fullname)
			throws MessagingException, UnsupportedEncodingException {

		String name = escapeHtml(fullname == null || fullname.isBlank() ? "bạn" : fullname);

		String html = """
				<div style="font-family:Arial,sans-serif;max-width:520px;margin:auto;
				            border:1px solid #ddd;border-radius:8px;padding:24px">
				  <h2 style="color:#2196F3;margin-top:0">MyServiceMVC</h2>
				  <p>Xin chào <b>%s</b>,</p>
				  <p>Mật khẩu tài khoản của bạn vừa được thay đổi thành công.</p>
				  <p style="color:#c62828">
				    Nếu <b>không phải bạn</b> thực hiện thao tác này, hãy đổi lại mật khẩu
				    ngay và kiểm tra hộp thư của bạn có bị truy cập trái phép hay không.
				  </p>
				</div>
				""".formatted(name);

		send(to, "[MyServiceMVC] Mat khau cua ban vua duoc thay doi", html);
	}

	private static String escapeHtml(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;");
	}
}
