package com.nmmc.hrms.Utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.logging.log4j.util.Strings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.google.gson.Gson;

@Component
public class Util {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	private final String[] HEADERS_LIST = { 
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR" 
	};
	
	
	
	
	
	public boolean validateChecksumForAPI(String checksum, String data, String secret) throws NoSuchAlgorithmException {
        return generateChecksumForAPI(data, secret).equals(checksum);
	}
	
	public String generateChecksumForAPI(String data, String secret) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(new String(data + "|" + secret).getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
			    sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String encodeByteArrayToBase64(byte[] bytea) {
		byte[] encoded = Base64.encodeBase64(bytea);
		return new String(encoded, StandardCharsets.US_ASCII);
	}

	public String encodeActualFileToBase64Binary(File file) {
	    try {
	    	if(file != null) {
	    		byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
	    		return new String(encoded, StandardCharsets.US_ASCII);
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	public String encodeFileToBase64Binary(String fileName) {
		try {
			if(fileName != null) {
				File file = new File(fileName);
				byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				return new String(encoded, StandardCharsets.US_ASCII);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String encodeFileToBase64BinaryWithPath(String filePath, String fileName) {
		try {
			if(fileName != null) {
				File file = new File(filePath + File.separator + fileName);
				byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
				return new String(encoded, StandardCharsets.US_ASCII);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getMimeType(String base64) {
		String regPattern = "data:(.+?);base64,";
		Pattern TAG_REGEX = Pattern.compile(regPattern, Pattern.DOTALL);
		
		Matcher matcher = TAG_REGEX.matcher(base64);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
	}
	
	public String cleanBase64(String base64) {
		return base64.replace("data:"+getMimeType(base64)+";base64,", "");
	}
	
	 private String getFileExtension(String base64) {
	        String[] parts = base64.split(",");
	        String header = parts[0];
	        if (header.contains("pdf")) {
	            return ".pdf";
	        } else if (header.contains("png") || header.contains("PNG")) {
	            return ".png";
	        } else if (header.contains("jpeg") ||header.contains("JPEG") || header.contains("jpg")|| header.contains("JPG")) {
	            return ".jpg";
	        } else {
	            // Add more cases for other file types if needed
	            return ".bin"; // Default extension for unknown types
	        }
	    }
	
//	public String decodeBase64ToFile(FileTypes fileType, String filePath, String base64) {
//		String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
//		base64 = cleanBase64(base64);
//		String fileName = fileType + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+"_"+UUID.randomUUID()+extension;
//		String fileFullUrl = filePath + File.separator+ fileName;
//		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
//			byte[] decoder = Base64.decodeBase64(base64);
//			fos.write(decoder);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return fileName;
//	}
//	
//	
//	
	public String decodeBase64ToFileForSop(String fileType, String filePath, String base64) {
		String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
		base64 = cleanBase64(base64);
		String fileName = fileType + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+extension;
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	public String decodeBase64ToFileApprovalLetter(String filePath,String applicationNo,String fileType, String base64) {
		String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
		base64 = cleanBase64(base64);
		String fileName = applicationNo + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS").format(new Date())+extension;
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	
	
	
	public String decodeBase64ToFileForAnnouncementWithSameFileName(String filePath, String base64,String fileName) {
		base64 = cleanBase64(base64);
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	
	
	
	public String decodeBase64ToFileForImportResults(String filePath, String base64, String fileName) {
		String extension = ".DMP.enc";
		base64 = cleanBase64(base64);
		fileName = fileName + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+extension;
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	public String decodeBase64ToFileForNotification(String filePath, String base64, String fileName) {
		String extension = ".csv";
		base64 = cleanBase64(base64);
		fileName = fileName + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm").format(new Date())+extension;
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	public String decodeBase64ToFileForUploadMaster(String filePath, String base64, String fileName) {
		String extension = ".csv";
		base64 = cleanBase64(base64);
		fileName = fileName + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS").format(new Date())+extension;
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	public String decodeBase64ToFileForCorrigendum(String filePath, String base64, String fileName) {
//		String extension =".pdf";
//		 String extension = getFileExtension(base64); 
		 String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
		 base64 = cleanBase64(base64);
		fileName = fileName + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+extension;
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	public String createChecksumMahaOnlineAndPAN(String data, String secret) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] result = md.digest(new String(data + "|" + secret).getBytes());
		StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
	}
	
	public void writeFileToOutputStream(File file, HttpServletResponse response, OutputStream os) throws IOException {
		response.setContentType(Files.probeContentType(file.toPath()));
		response.setHeader("Content-disposition", "attachment; filename=\"" + file.getName() + "\"");
		FileCopyUtils.copy(Files.readAllBytes(file.toPath()), os);
		os.flush();
		os.close();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, MalformedURLException, IOException {
		
		String mimeTypes = new File("C:\\Users\\DELL\\Downloads\\12_18-10-2023-20-26-25_12_18-10-2023-20-26-20_0585a16e-d8e3-457f-9bfd-f1ae9ab325dc_").toURL().openConnection().getContentType();
        
        String fileExtensions = ContentTypes.getExtensionFromMimeType(mimeTypes);
        System.out.println(fileExtensions);
        fileExtensions = fileExtensions.substring(1);
        System.out.println(fileExtensions);
		
//		String imagePath = "C:\\\\Users\\\\DELL\\\\Downloads\\\\12_18-10-2023-20-26-25_12_18-10-2023-20-26-20_0585a16e-d8e3-457f-9bfd-f1ae9ab325dc_";
		String imagePath = "C:\\Users\\DELL\\Downloads\\1_20-02-2023-20-14-16_1_20-02-2023-20-14-13_9e3d3adb-8c5a-4b34-9777-611d889e03f1_ILMS_1676904251352.jpg";

		String mimeType;
		try {
		    mimeType = new File(imagePath).toURL().openConnection().getContentType();
		} catch (IOException e) {
		    mimeType = null;
		}

		String fileExtension;
		if (mimeType != null) {
		    fileExtension = ContentTypes.getExtensionFromMimeType(mimeType);
		    System.out.println("If Block Executed>>>>>>>>>>>>"+fileExtension);
		} else {
		    fileExtension = FilenameUtils.getExtension(imagePath).toLowerCase();
		    System.out.println("else Block Executed>>>>>>>>>>>>"+fileExtension);
		}


        String secret = "Qatha&^as20w";
        String pan = "MUKPS3505C";
        
        
        try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(new String(pan + "|" + secret).getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
			    sb.append(String.format("%02x", b));
			}
			System.out.println(sb.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
//		System.out.println(new Gson().toJson(ApplicationAfterDrawBCModel.init(b)));
		
//		String fileName = "sdfsfd_2312312.png";
//		String extension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
//		System.out.println(extension);DHAINJE
//		
//		java.util.List<Integer> lst = Arrays.asList(new Integer [] {
//				53184
//		});
//		int j = 0;
//		Util util = new Util();
//		for (Integer i : lst) {
//			System.out.println("arr.push({key:"+i+", value:'"+util.generateChecksumForAPI(i.toString(), "Q#$^*!)0w@fg")+"'});");
////			System.out.println(i+"\t"+util.generateChecksumForAPI(i.toString(), "Q#$^*!)0w@fg"));//moderation
//			j++;
//			if(j > 100) {
//				break;
//			}
//		}
		
		Long fkRegId = 34842l;
		
//		String data = fkRegId+"";
		String data = "1230001832|POSSESSION_LETTER";
//		String transactionId = "090223423000000018";
//		Long requestDate = new Date().getTime();
//		System.out.println("transactionId - "+transactionId);
//		System.out.println("requestDate - "+requestDate);
//		String data = transactionId+requestDate;
		Util util = new Util();
		System.out.println(util.generateChecksumForAPI(data, "Hu8$$wk1^**"));
	}
//	
	
//	public static void main(String[] args) {
//		String str = readTextFile("C:\\Users\\ue\\Desktop\\prechecks", "top-14022023.txt", 5);
//		System.out.println(str);
//	}
	private static final String  HASH_ALGORITHM = "MD5";
	
	

	/**
	 * hash the password if it is required.
	 * 
	 * @param textPassword
	 * @throws Exception 
	 */
//	public static String hashPassword(final String textPassword) throws Exception {
//		
//		final byte defaultBytes[] = textPassword.getBytes();
//		MessageDigest algorithm;
//		try {
//			algorithm = MessageDigest.getInstance(HASH_ALGORITHM);
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception(e);
//		}
//		
//		algorithm.reset();
//		algorithm.update(defaultBytes);
//		final byte messageDigest[] = algorithm.digest();
//		
//		/*StringBuffer hexString = new StringBuffer();
//		for (int i = 0; i < messageDigest.length; i++) {
//			hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
//		}*/
//		
//		return Hex.encodeHexString(messageDigest);
//	}
	
//	public static void main(String[] args) throws Exception {
//		System.out.println(hashPassword("Vaishalik@0204"));
//	}
	
	public String readTextFile(String folderPath, String fileName, Integer lineLimit) {
		String output = "";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(folderPath + File.separator + fileName));
			String line = reader.readLine();
			Integer i = 0;
			
			output += line != null ? line +"\n" : "";
			
			while (line != null && (lineLimit == null || (lineLimit != null && i < lineLimit))) {
				line = reader.readLine();
				output += line != null ? line +"\n" : "";
				i++;
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	public String getIpAddressFromRequest(HttpServletRequest request) {
		for (String header : HEADERS_LIST) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr(); 
	}

	
	public void convertByteArrayToFile(byte[] byteArray, String filePath, String fileName) throws Exception {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath.concat(File.separator).concat(fileName)));
			fileOutputStream.write(byteArray);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Unable to write File.");
		}
	}
	
	public String generateHashcodeForFile(String filePath) {
		String hashCode = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(filePath)));

			int theByte = 0 ;
			while((theByte = in.read() )!=-1) {
				md.update((byte)theByte);
			}
			in.close();
			byte[] theDigest = md.digest();

			byte[] encoded = (Base64.encodeBase64(theDigest));
			hashCode = new String(encoded, StandardCharsets.US_ASCII);
			System.out.println("s1 in hash code in====== " +hashCode);
		}catch(Exception ex) {
			System.out.println("exception in  hash code ----- " +ex);
		}	
		
		return hashCode;
	}
//	public String generateHashcodeForText(String text) {
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA-256");
//			byte[] result = md.digest(text.getBytes(StandardCharsets.UTF_8));
//			StringBuilder sb = new StringBuilder();
//			for (byte b : result) {
//				sb.append(String.format("%02x", b));
//			}
//			return sb.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
//	public String convertCamelCase(String str) {
//		return String.join(" ", Arrays.asList(CaseUtils.toCamelCase(str, true, null).split("_")));
//	}
	
	public void saveFileFromUrl(String uri, String destinationFile) throws IOException {
		URL url = new URL(uri);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
	
	public String decodeBase64ToFileWithFileName(String filePath, String fileName, String base64) {
		base64 = cleanBase64(base64);
		if(!new File(filePath).exists()) {
			new File(filePath).mkdirs();
		}
		
		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	public String decodeBase64ToFileWithFileNameForExtension(String filePath, String fileName, String base64) {
		String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
		base64 = cleanBase64(base64);
		if(!new File(filePath).exists()) {
			new File(filePath).mkdirs();
		}
		
		String fileFullUrl = filePath + File.separator+ fileName+extension;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName+extension;
	}

	public void downloadFileFromUrl(String filePath, String fileName, String urlStr) {
		if(!new File(filePath).exists()) {
			new File(filePath).mkdirs();
		}
		try {
			URL url = new URL(urlStr);
			try (InputStream in = url.openStream()) {
				String fileFullUrl = filePath + File.separator+ fileName;
				Files.copy(in, Paths.get(fileFullUrl), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String encodeFileToBase64StringFromUrl(String url) {
		try {
			URL imageUrl = new URL(url);
	        URLConnection ucon = imageUrl.openConnection();
	        InputStream is = ucon.getInputStream();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int read = 0;
	        while ((read = is.read(buffer, 0, buffer.length)) != -1) {
	            baos.write(buffer, 0, read);
	        }
	        baos.flush();
	        byte[] encoded = Base64.encodeBase64(baos.toByteArray());
	        is.close();
			baos.close();
			return new String(encoded, StandardCharsets.US_ASCII);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		
//		Util util = new Util();
//		System.out.println(util.generateHashcodeForFile("D:\\Downloads\\P23_APP_D24022023T104120.DMP.enc"));
//	}
//	public String decodeBase64ToFile(Long regKey, FileTypes fileType, String filePath, String base64) {
//		String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
//		base64 = cleanBase64(base64);
//		String fileName = fileType + "_" + regKey + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+extension;
//		
//		if(!new File(filePath).exists()) {
//			new File(filePath).mkdirs();
//		}
//		
//		String fileFullUrl = filePath + File.separator+ fileName;
//		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
//			byte[] decoder = Base64.decodeBase64(base64);
//			fos.write(decoder);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return fileName;
//	}
	
//	public MessageResponse extractZipFile(File zipFile, String extractionPath) throws IOException {
//        byte[] buffer = new byte[1024];
//
//        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
//            ZipEntry entry = zipInputStream.getNextEntry();
//            while (entry != null) {
//                File entryFile = new File(extractionPath, entry.getName());
//                if (entry.isDirectory()) {
//                    entryFile.mkdirs();
//                } else {
//                    try (FileOutputStream fos = new FileOutputStream(entryFile)) {
//                        int length;
//                        while ((length = zipInputStream.read(buffer)) > 0) {
//                            fos.write(buffer, 0, length);
//                        }
//                    }
//                }
//                entry = zipInputStream.getNextEntry();
//            }
//        }
//        
//        zipFile.delete();
//		 return FileUploadResponse.success(1);
//    }
	
	public String decodeBase64ToFileForApplicationDetails(String filePath,  String base64,String getSelectedFileName,String docName,Long applicationNo) {
		String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
		base64 = cleanBase64(base64);
		if(!new File(filePath).exists()) {
			new File(filePath).mkdirs();
		}
		 String fileName = applicationNo+"_"+ new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS").format(new Date())+extension;

		String fileFullUrl = filePath + File.separator+ fileName;
		try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
			byte[] decoder = Base64.decodeBase64(base64);
			fos.write(decoder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public byte[] decodeBase64ToByteArray(String base64) {
		base64 = cleanBase64(base64);
		return Base64.decodeBase64(base64);
	}
	
	
	
//	   public String convertImageToBase64Pdf(String imagePath) {
//	        try {
//	            PDDocument document = new PDDocument();
//	            PDPage page = new PDPage();
//	            document.addPage(page);
//	            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
//
//	            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//	                float scale = 1f;
//	                contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
//	            }
//
//	            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//	            document.save(byteArrayOutputStream);
//	            document.close();
//
//	            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
//	            return java.util.Base64.getEncoder().encodeToString(pdfBytes);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	        return null;
//	    }
	   
	   
//	   public String convertImageToBase64Pdf(String imagePath) {
//		    try {
//		        PDDocument document = new PDDocument();
//		        PDPage page = new PDPage();
//		        document.addPage(page);
//		        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
//		        String mimeType = new File(imagePath).toURL().openConnection().getContentType();
//		        String fileExtension = ContentTypes.getExtensionFromMimeType(mimeType);//FilenameUtils.getExtension(imagePath).toLowerCase();   
////		        String fileExtension = FilenameUtils.getExtension(imagePath).toLowerCase();
//		        fileExtension = fileExtension.substring(1);
//		        PDImageXObject pdImage = null;
//		        switch (fileExtension) {
//		            case "jpg":
//		            case "jpeg":
//		                pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image/jpeg");
//		                break;
//		            case "png":
//		                pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image/png");
//		                break;
//		            default:
//		                throw new IllegalArgumentException("Unsupported image format: " + fileExtension);
//		        }
//
//		        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//		            float scale = 0.3f;
//		            
//		            float imageWidth = pdImage.getWidth() * scale;
//		            float imageHeight = pdImage.getHeight() * scale;
//		            
//		            float x = (page.getMediaBox().getWidth() - imageWidth) / 2;
//		            float y = (page.getMediaBox().getHeight() - imageHeight) / 2;
//		            
////		            contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
//		            contentStream.drawImage(pdImage, x, y, imageWidth, imageHeight);
//
//		        }
//
//		        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		        document.save(byteArrayOutputStream);
//		        document.close();
//
//		        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
//		        return java.util.Base64.getEncoder().encodeToString(pdfBytes);
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		    }
//		    return null;
//		}

	   public String decodeBase64ToFileForAppeal(String filePath, String base64, Long applicationId, String fileName) {
			String extension = ".pdf";
			base64 = cleanBase64(base64);
			fileName = fileName + "_" + applicationId + "_"
					+ new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()) + extension;
			if (!new File(filePath).exists()) {
				new File(filePath).mkdirs();
			}
			String fileFullUrl = filePath + File.separator + fileName;
			try (FileOutputStream fos = new FileOutputStream(fileFullUrl)) {
				byte[] decoder = Base64.decodeBase64(base64);
				fos.write(decoder);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileName;
		}
		
		
		
		public String decodeBase64ToFileForSpousePan(String filePath, String base64, String panNumber, Long registrationId) {
			String extension = ".pdf";
			base64 = cleanBase64(base64);
			String FileName = null;
			FileName = panNumber + "_" +registrationId + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()) + extension;
			if (!new File(filePath).exists()) {
				new File(filePath).mkdirs();
			}
			String fileFullUrl = filePath + File.separator + FileName;
			try (FileOutputStream fos = new FileOutputStream(fileFullUrl)) {
				byte[] decoder = Base64.decodeBase64(base64);
				fos.write(decoder);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return FileName;
		}

		
//		public String decodeBase64ToFileAadharPhoto(String fullFilePath,String filePath, String base64,String fileName) {
//			base64 = cleanBase64(base64);
//			if (!new File(filePath).exists()) {
//				new File(filePath).mkdirs();
//			}
//			try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
//				byte[] decoder = Base64.decodeBase64(base64);
//				fos.write(decoder);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return fileName;
//		}
		
		
		   public String decodeBase64ToFileAadharPhoto(String filePath, String base64, Long registrationId,String name) {
				String extension = ".jpg";
				String fileName = null;
				base64 = cleanBase64(base64);
				fileName = name + "_" + registrationId + "_"
						+ new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()) + extension;
				if (!new File(filePath).exists()) {
					new File(filePath).mkdirs();
				}
				String fileFullUrl = filePath + File.separator + fileName;
				try (FileOutputStream fos = new FileOutputStream(fileFullUrl)) {
					byte[] decoder = Base64.decodeBase64(base64);
					fos.write(decoder);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return fileFullUrl;
			}
		
		   
		public void deleteOldSpousePanDetails(String panNumber) {
			String deleteQuery = "DELETE FROM PAN_DETAIL WHERE PAN='"+panNumber+"'";
		    entityManager.createNativeQuery(deleteQuery).executeUpdate();
			entityManager.flush();
			entityManager.clear();
		}

		public void updateFileName(String fullFilePath) {
		    File file = new File(fullFilePath);
		    String fileName =file.getName();
		    String filePath = file.getParent();
		    
		    int lastDotIndex = fileName.lastIndexOf('.');
		    String nameWithoutExtension = fileName.substring(0, lastDotIndex); 
		    String extension = fileName.substring(lastDotIndex + 1);	
		    
         if (file.exists()) {
             String dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
             String newFileName = nameWithoutExtension +".jpg"+"_"+dateFormat;
             File newFileWithTimestamp = new File(filePath, newFileName);
             if (file.renameTo(newFileWithTimestamp)) {
                 System.out.println("Existing file renamed to: " + newFileName);
             }

         }
		}
	   
	   
		public Long folderSize(File directory) {
		    Long length = 0l;
		    for (File file : directory.listFiles()) {
		        if (file.isFile())
		            length += file.length();
		        else
		            length += folderSize(file);
		    }
		    return length;
		}

		public String decodeBase64ToFileForInvoice(String invoiceDirectory, String orderDocumentFile,
				String invoiceNumber, String fileName) {
		
				String extension = ".pdf";
				orderDocumentFile = cleanBase64(orderDocumentFile);
				fileName = fileName + "_" + invoiceNumber + "_"
						+ new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()) + extension;
				if (!new File(invoiceDirectory).exists()) {
					new File(invoiceDirectory).mkdirs();
				}
				String fileFullUrl = invoiceDirectory + File.separator + fileName;
				try (FileOutputStream fos = new FileOutputStream(fileFullUrl)) {
					byte[] decoder = Base64.decodeBase64(orderDocumentFile);
					fos.write(decoder);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return fileName;
			}
		
		public String decodeBase64ToFileForPaymentExtension(String filePath, String base64, String applicationNo, String fileName) {
			String extension = ".pdf";
			base64 = cleanBase64(base64);
			fileName = fileName + "_" + applicationNo + "_"
					+ new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()) + extension;
			if (!new File(filePath).exists()) {
				new File(filePath).mkdirs();
			}
			String fileFullUrl = filePath + File.separator + fileName;
			try (FileOutputStream fos = new FileOutputStream(fileFullUrl)) {
				byte[] decoder = Base64.decodeBase64(base64);
				fos.write(decoder);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileName;
		}
		
//		public String decodeBase64ToFile(String fileName,String filePath, String base64) {
//			String extension = ContentTypes.getExtensionFromMimeType(getMimeType(base64));
//			base64 = cleanBase64(base64);			
//			if(!new File(filePath).exists()) {
//				new File(filePath).mkdirs();
//			}
//			
//			String fileFullUrl = filePath + File.separator+ fileName;
//			try ( FileOutputStream fos = new FileOutputStream(fileFullUrl) ) {
//				byte[] decoder = Base64.decodeBase64(base64);
//				fos.write(decoder);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return fileName;
//		}
		
		public String decodeBase64ToFiles(String fileName,String filePath, String base64) {
		    base64 = cleanBase64(base64);
		    byte[] decoder = Base64.decodeBase64(base64);
			String extension = ".pdf";
		    if (!new File(filePath).exists()) {
		        new File(filePath).mkdirs();
		    }

		    String fileFullUrl = filePath+fileName+extension;
		    try (FileOutputStream fos = new FileOutputStream(fileFullUrl)) {
		        fos.write(decoder);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return fileName;
		}
		
		
		public String decodeBase64ToFile(String fileName,String filePath, String base64) {
		    base64 = cleanBase64(base64);

		    byte[] decoder = Base64.decodeBase64(base64);
		    String extension = determineFileType(decoder);
		    String NewFileName =  removeSpecialCharacterAndSpace(fileName)+ "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date())+ extension;
		    if (!new File(filePath).exists()) {
		        new File(filePath).mkdirs();
		    }

		    String fileFullUrl = filePath+NewFileName;
		    try (FileOutputStream fos = new FileOutputStream(fileFullUrl)) {
		        fos.write(decoder);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return NewFileName;
		}

		private String determineFileType(byte[] fileData) {
		    if (fileData.length > 4) {
		        if (fileData[0] == 0x25 && fileData[1] == 0x50 && fileData[2] == 0x44 && fileData[3] == 0x46) {
		            return ".pdf";
		        }
		        else if ((fileData[0] & 0xFF) == 0xFF && (fileData[1] & 0xFF) == 0xD8 && (fileData[2] & 0xFF) == 0xFF) {
		            return ".jpg";
		        }
		        else if (fileData[0] == (byte) 0x89 && fileData[1] == 0x50 && fileData[2] == 0x4E && fileData[3] == 0x47) {
		            return ".png";
		        }
		    }
		    return ".bin";
		}
		
		public String removeSpecialCharacterAndSpace(String fileName) {
	        String sanitized = fileName.replaceAll("[^a-zA-Z0-9\\s]", "");
	        fileName= sanitized.replaceAll("\\s+", "_");
			return fileName;
		}
		
//		public String encodeFileToBase64Binary(String filePath, String fileName) {
//		    try {
//		        if (fileName != null) {
//		            File file = new File(filePath );
//		            byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
//		            String base64EncodedString = new String(encoded, StandardCharsets.US_ASCII);
//
//		            // Detect MIME type
//		            Path path = file.toPath();
//		            String mimeType = Files.probeContentType(path);
//
//		            if (mimeType != null) {
//		                return "data:" + mimeType + ";base64," + base64EncodedString;
//		            } else {
//		                return base64EncodedString;
//		            }
//		        }
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//		    return null;
//		}
//		
		
		public String encodeFileToBase64Binary(String filePath, String fileName) {
		    if (filePath == null || filePath.isEmpty()) {
		        System.err.println("File path is null or empty");
		        return "";
		    }

		    File file = new File(filePath);
		    
		    if (!file.exists() || !file.isFile()) {
		        System.err.println("File not found: " + filePath);
		        return "";
		    }

		    try {
		        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		        String base64EncodedString = new String(encoded, StandardCharsets.US_ASCII);

		        // Detect MIME type
		        String mimeType = Files.probeContentType(file.toPath());

		        if (mimeType != null) {
		            return "data:" + mimeType + ";base64," + base64EncodedString;
		        } else {
		            return base64EncodedString;
		        }
		    } catch (IOException e) {
		        System.err.println("Error encoding file to Base64: " + e.getMessage());
		        e.printStackTrace();
		        return "";
		    }
		}

	    public static int countWeekends(int month, int year) {
	        YearMonth yearMonth = YearMonth.of(year, month);
	        int totalSaturdaysAndSundays = 0;

	        LocalDate date = yearMonth.atDay(1);

	        while (date.getMonthValue() == month) {
	            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
	                totalSaturdaysAndSundays++;
	            }
	            date = date.plusDays(1);
	        }

	        return totalSaturdaysAndSundays;
	    }
	    

		public String createChecksumSmsService(String data, String secret) throws NoSuchAlgorithmException {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(new String(data + "|" + secret).getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		}
		
		public byte[] convertFileToByteArray(String filePath) {
		    try {
		        Path path = Paths.get(filePath);
		        return Files.readAllBytes(path);
		    } catch (IOException e) {
		        e.printStackTrace();
		        return new byte[0];
		    }
		}
		
		
		public String getFileExtensions(String fileName) {
		    if (fileName == null || fileName.lastIndexOf('.') == -1) {
		        return "";
		    }
		    return fileName.substring(fileName.lastIndexOf('.') + 1);
		}
		
		
//		public byte[] convertImageToPdf(String imagePath) {
//		    try (PDDocument document = new PDDocument()) {
//		        PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
//		        
//		        PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
//		        document.addPage(page);
//		        
//		        try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page)) {
//		            contentStream.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
//		        }
//
//		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		        document.save(outputStream);
//		        return outputStream.toByteArray();
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		        return null;
//		    }
//		}
		
		public static byte[] convertImageToPdf(BufferedImage image) {
		    try (PDDocument document = new PDDocument()) {
		        PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
		        document.addPage(page);
		        
		        var imageXObject = LosslessFactory.createFromImage(document, image);
		        
		        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
		            float imageX = 0;
		            float imageY = 0;
		            float imageWidth = page.getMediaBox().getWidth();
		            float imageHeight = page.getMediaBox().getHeight();
		            
		            float scaleX = imageWidth / image.getWidth();
		            float scaleY = imageHeight / image.getHeight();
		            float scale = Math.min(scaleX, scaleY);
		            
		            contentStream.drawImage(imageXObject, imageX, imageY, image.getWidth() * scale, image.getHeight() * scale);
		        }

		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        document.save(outputStream);
		        return outputStream.toByteArray();
		    } catch (IOException e) {
		        e.printStackTrace();
		        return null;
		    }
		}
		
		
		  public String convertImageToBase64Pdf(String imagePath) {
			    try {
			        PDDocument document = new PDDocument();
			        PDPage page = new PDPage();
			        document.addPage(page);
			        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
			        String mimeType = new File(imagePath).toURL().openConnection().getContentType();
			        String fileExtension = ContentTypes.getExtensionFromMimeType(mimeType);//FilenameUtils.getExtension(imagePath).toLowerCase();   
//			        String fileExtension = FilenameUtils.getExtension(imagePath).toLowerCase();
			        fileExtension = fileExtension.substring(1);
			        PDImageXObject pdImage = null;
			        switch (fileExtension) {
			            case "jpg":
			            case "jpeg":
			                pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image/jpeg");
			                break;
			            case "png":
			                pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image/png");
			                break;
			            default:
			                throw new IllegalArgumentException("Unsupported image format: " + fileExtension);
			        }

			        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
			            float scale = 0.2f;
			            
			            float imageWidth = pdImage.getWidth() * scale;
			            float imageHeight = pdImage.getHeight() * scale;
			            
			            float x = (page.getMediaBox().getWidth() - imageWidth) / 2;
			            float y = (page.getMediaBox().getHeight() - imageHeight) / 2;
			            
//			            contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
			            contentStream.drawImage(pdImage, x, y, imageWidth, imageHeight);

			        }

			        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			        document.save(byteArrayOutputStream);
			        document.close();

			        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
			        return java.util.Base64.getEncoder().encodeToString(pdfBytes);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			    return null;
			}



}

