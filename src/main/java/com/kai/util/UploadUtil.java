package com.kai.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import jakarta.servlet.http.Part;

public class UploadUtil {

	private static final Path ROOT = Paths.get(Constant.DIR).toAbsolutePath().normalize();
	private static final long MAX_BYTES = 5L * 1024 * 1024;
	private static final SecureRandom RANDOM = new SecureRandom();

	private static final Map<String, String> ALLOWED_EXT = Map.of(
			"jpg", "jpg",
			"jpeg", "jpg",
			"png", "png",
			"gif", "gif",
			"webp", "webp");

	public static String saveImage(Part part) throws IOException {

		if (part == null || part.getSize() == 0) {
			return null;
		}
		if (part.getSize() > MAX_BYTES) {
			throw new IOException("Ảnh vượt quá 5MB");
		}

		String submitted = part.getSubmittedFileName();
		if (submitted == null || submitted.isBlank()) {
			throw new IOException("Tên file không hợp lệ");
		}

		String name = Paths.get(submitted).getFileName().toString();
		int dot = name.lastIndexOf('.');
		if (dot < 0) {
			throw new IOException("File không có phần mở rộng");
		}
		String ext = ALLOWED_EXT.get(name.substring(dot + 1).toLowerCase(Locale.ROOT));
		if (ext == null) {
			throw new IOException("Chỉ chấp nhận jpg, jpeg, png, gif, webp");
		}

		byte[] data;
		try (InputStream in = part.getInputStream()) {
			data = in.readAllBytes();
		}
		if (data.length == 0 || data.length > MAX_BYTES) {
			throw new IOException("Kích thước ảnh không hợp lệ");
		}

		BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
		if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
			throw new IOException("File không phải là ảnh hợp lệ");
		}

		Files.createDirectories(ROOT);

		String fname = System.currentTimeMillis() + "-"
				+ Math.abs(RANDOM.nextInt()) + "." + ext;
		Path target = ROOT.resolve(fname).normalize();
		if (!target.startsWith(ROOT)) {
			throw new IOException("Đường dẫn không hợp lệ");
		}

		Files.write(target, data);
		return fname;
	}

	public static void deleteQuietly(String fname) {
		if (fname == null || fname.isBlank() || fname.startsWith("http")) {
			return;
		}
		try {
			String safe = Paths.get(fname).getFileName().toString();
			if (!safe.matches("[A-Za-z0-9._-]{1,100}")) {
				return;
			}
			Path target = ROOT.resolve(safe).normalize();
			if (target.startsWith(ROOT)) {
				Files.deleteIfExists(target);
			}
		} catch (Exception ignored) {
		}
	}
}
