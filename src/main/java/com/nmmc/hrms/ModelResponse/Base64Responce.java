package com.nmmc.hrms.ModelResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Base64Responce {
	
	private String base64Data;
	private String fileName;
	private String status;
	private String message;


}
