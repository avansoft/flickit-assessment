package org.flickit.assessment.kit.application.port.out.subject;

public interface DeleteSubjectPort {

    void deleteByIdAndKitVersionId(long id, long kitVersionId);
}