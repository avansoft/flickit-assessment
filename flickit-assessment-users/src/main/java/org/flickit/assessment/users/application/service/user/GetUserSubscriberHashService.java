package org.flickit.assessment.users.application.service.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.flickit.assessment.common.config.NotificationSenderProperties;
import org.flickit.assessment.users.application.port.in.user.GetUserSubscriberHashUseCase;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class GetUserSubscriberHashService implements GetUserSubscriberHashUseCase {

    private static final String HMAC_SHA_256 = "HmacSHA256";

    private final NotificationSenderProperties notificationSenderProperties;

    @SneakyThrows
    @Override
    public Result getUserSubscriberHash(Param param) {
        String key = notificationSenderProperties.getNovu().getApiKey();
        String subscriberHash = encode(key, param.getCurrentUserId().toString());
        return new Result(subscriberHash);
    }

    private String encode(String key, String subscriberId) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac encryptor = Mac.getInstance(HMAC_SHA_256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(UTF_8), HMAC_SHA_256);
        encryptor.init(secretKeySpec);
        encryptor.update(subscriberId.getBytes());
        return Hex.encodeHexString(encryptor.doFinal());
    }
}
