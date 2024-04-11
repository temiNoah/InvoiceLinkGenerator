package com.company.reports.utils;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public class ResponseBuilder {
    public JSONObject buildSuccessResponse(String documentId, String message ,String url) {
        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("documentId", documentId);
        response.put("message", message);
        response.put("url" , url);
        return response;
    }

    public JSONObject buildErrorResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
    
    public ResponseEntity<byte[]> buildResponseEntity(String displayFormat, byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",getMediaTypeForFormat(displayFormat));
        headers.setContentLength(data.length);
       
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    private String getMediaTypeForFormat(String format) {
        switch (format) {
            case JasperReportConstants.DISPLAY_FORMAT_PDF:
                return MediaType.APPLICATION_PDF.toString();
            case JasperReportConstants.DISPLAY_FORMAT_HTML:
                return MediaType.TEXT_HTML.toString();
            case JasperReportConstants.DISPLAY_FORMAT_XLS:
                return  "application/vnd.ms-excel";
            case JasperReportConstants.DISPLAY_FORMAT_XLSX:
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case JasperReportConstants.DISPLAY_FORMAT_CSV:
                return "text/csv";
            default:
                throw new IllegalArgumentException("Unsupported display format: " + format);
        }
        
    }
}