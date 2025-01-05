package org.flickit.assessment.common.exception.handler;

import org.flickit.assessment.common.application.MessageBundle;
import org.flickit.assessment.common.exception.UpgradeRequiredException;
import org.flickit.assessment.common.exception.api.ErrorResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.flickit.assessment.common.exception.api.ErrorCodes.UPGRADE_REQUIRED;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UpgradeRequiredExceptionHandler {

    @ResponseBody
    @ExceptionHandler(UpgradeRequiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorResponseDto handle(UpgradeRequiredException ex) {
        return new ErrorResponseDto(UPGRADE_REQUIRED, MessageBundle.message(ex.getMessage()));
    }
}
