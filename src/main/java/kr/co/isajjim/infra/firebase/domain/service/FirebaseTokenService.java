package kr.co.isajjim.infra.firebase.domain.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import kr.co.isajjim.global.common.ResponseCode;
import kr.co.isajjim.global.exception.BaseException;
import org.springframework.stereotype.Service;

@Service
public class FirebaseTokenService {

    public String createCustomToken(Long userId) {
        try {
            return FirebaseAuth.getInstance().createCustomToken(String.valueOf(userId));
        } catch (FirebaseAuthException e) {
            throw new BaseException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
