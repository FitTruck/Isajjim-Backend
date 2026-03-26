package kr.co.isajjim.global.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.isajjim.global.common.ApiResponse;
import kr.co.isajjim.global.common.ResponseCode;
import org.springframework.http.MediaType;

import java.io.IOException;

import static kr.co.isajjim.global.common.Constants.CHARACTER_ENCODING;

public class ResponseUtil {

    public static void responseError(HttpServletResponse response, ObjectMapper objectMapper, ResponseCode responseCode) throws IOException {
        response.setStatus(responseCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.ofFail(responseCode)));
    }
}