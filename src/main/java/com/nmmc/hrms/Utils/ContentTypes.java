package com.nmmc.hrms.Utils;


import java.util.HashMap;
import java.util.Map;

public class ContentTypes {

	private static Map<String, String> mpMimeTypeToExtension = new HashMap<>();
	
	private static Map<String, String> mpExtensionToMimeType = new HashMap<>();
	
	static {
		mpMimeTypeToExtension.put("image/png", ".png");
		mpMimeTypeToExtension.put("image/jpg", ".jpg");
		mpMimeTypeToExtension.put("image/jpeg", ".jpeg");
		mpMimeTypeToExtension.put("text/csv", ".csv");
		mpMimeTypeToExtension.put("application/vnd.ms-excel", ".xls");
		mpMimeTypeToExtension.put("application/octet-stream", ".DMP.enc");
		mpMimeTypeToExtension.put("application/pdf", ".pdf");
		mpMimeTypeToExtension.put("application/x-zip-compressed", ".zip");
		mpMimeTypeToExtension.put("text/html", ".html");
		
		mpExtensionToMimeType.put(".png", "image/png");
		mpExtensionToMimeType.put(".jpg", "image/jpg");
		mpExtensionToMimeType.put(".jpeg", "image/jpeg");
		mpExtensionToMimeType.put(".csv", "text/csv");
		mpExtensionToMimeType.put(".xls", "application/vnd.ms-excel");
		mpExtensionToMimeType.put(".pdf", "application/pdf");
		mpExtensionToMimeType.put(".DMP.enc", "application/octet-stream");
		mpExtensionToMimeType.put(".zip", "application/x-zip-compressed");
		mpExtensionToMimeType.put(".zip", "application/zip");
		mpExtensionToMimeType.put(".html", "text/html");
		
	}
	
	public static String getExtensionFromMimeType(String mimeType) {
		return mpMimeTypeToExtension.get(mimeType);
	}
	
	public static String getMimeTypeFromExtension(String extension) {
		return mpExtensionToMimeType.get(extension);
	}
}

