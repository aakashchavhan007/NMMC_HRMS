package com.nmmc.hrms.Exceptions;


import java.util.UUID;

import org.apache.poi.ss.formula.functions.T;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse<T> {
	public MessageResponse(String message) {}

	
	private String message;
	private String status;
	private Object[] errors;
	private UUID id;
    private String errorMessages;
	private String data;
	private T anyData;



    
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);

  }
    
	public MessageResponse(String message, String status, Object[] errors,String data) {
		super();
		this.message = message;
		this.status = status;
		this.errors = errors;
		this.data =data;

	}
}
