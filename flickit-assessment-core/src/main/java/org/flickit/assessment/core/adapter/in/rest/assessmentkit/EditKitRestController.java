package org.flickit.assessment.core.adapter.in.rest.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.core.application.port.in.assessmentkit.EditKitUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EditKitRestController {

    private final EditKitUseCase useCase;

    @PutMapping("assessment-kits/dsl/{kitId}")
    public ResponseEntity editKit(@PathVariable("kitId") UUID kitId, @RequestBody String content) {
        useCase.edit(toParam(kitId, content));
        return new ResponseEntity(HttpStatus.OK);
    }

    private EditKitUseCase.Param toParam(UUID kitId, String content) {
        return new EditKitUseCase.Param(kitId, content);
    }
}
