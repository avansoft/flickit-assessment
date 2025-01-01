package org.flickit.assessment.core.application.service.subjectinsight;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.MessageBundle;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.AssessmentResult;
import org.flickit.assessment.core.application.domain.SubjectInsight;
import org.flickit.assessment.core.application.port.in.subjectinsight.InitSubjectInsightUseCase;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentResultPort;
import org.flickit.assessment.core.application.port.out.subject.LoadSubjectReportInfoPort;
import org.flickit.assessment.core.application.port.out.subjectinsight.CreateSubjectInsightPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.flickit.assessment.core.common.ErrorMessageKey.INIT_SUBJECT_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND;
import static org.flickit.assessment.core.common.MessageKey.SUBJECT_DEFAULT_INSIGHT;

@Service
@Transactional
@RequiredArgsConstructor
public class InitSubjectInsightService implements InitSubjectInsightUseCase {

    private final CreateSubjectInsightPort createSubjectInsightPort;
    private final LoadAssessmentResultPort loadAssessmentResultPort;
    private final LoadSubjectReportInfoPort loadSubjectReportInfoPort;

    @Override
    public void initSubjectInsight(Param param) {
        var assessmentResultId = loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())
            .map(AssessmentResult::getId)
            .orElseThrow(() -> new ResourceNotFoundException(INIT_SUBJECT_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND));

        String defaultInsight = createDefaultInsight(param.getAssessmentId(), param.getSubjectId());
        var subjectInsight = new SubjectInsight(assessmentResultId,
            param.getSubjectId(),
            defaultInsight,
            LocalDateTime.now(),
            null,
            false);

        createSubjectInsightPort.persist(subjectInsight);
    }

    private String createDefaultInsight(UUID assessmentId, long subjectId) {
        var subjectReport = loadSubjectReportInfoPort.load(assessmentId, subjectId);
        var subject = subjectReport.subject();

        if (subject.maturityLevel() == null)
            return "";

        return MessageBundle.message(SUBJECT_DEFAULT_INSIGHT,
            subject.title(),
            subject.description(),
            subject.confidenceValue() != null ? (int) Math.ceil(subject.confidenceValue()) : 0,
            subject.title(),
            subject.maturityLevel().getIndex(),
            subjectReport.maturityLevels().size(),
            subject.maturityLevel().getTitle(),
            subjectReport.attributes().size(),
            subject.title());
    }
}