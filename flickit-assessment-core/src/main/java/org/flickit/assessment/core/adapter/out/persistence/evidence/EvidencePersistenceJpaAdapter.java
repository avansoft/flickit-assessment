package org.flickit.assessment.core.adapter.out.persistence.evidence;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.Evidence;
import org.flickit.assessment.core.application.port.out.evidence.*;
import org.flickit.assessment.data.jpa.core.assessment.AssessmentJpaRepository;
import org.flickit.assessment.data.jpa.core.assessmentresult.AssessmentResultJpaRepository;
import org.flickit.assessment.data.jpa.core.evidence.*;
import org.flickit.assessment.data.jpa.kit.question.QuestionJpaRepository;
import org.flickit.assessment.data.jpa.users.user.UserJpaEntity;
import org.flickit.assessment.data.jpa.users.user.UserJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.flickit.assessment.core.adapter.out.persistence.evidence.EvidenceMapper.toEvidenceListItem;
import static org.flickit.assessment.core.common.ErrorMessageKey.*;

@Component
@RequiredArgsConstructor
public class EvidencePersistenceJpaAdapter implements
    CreateEvidencePort,
    LoadEvidencesPort,
    UpdateEvidencePort,
    DeleteEvidencePort,
    LoadEvidencePort,
    ResolveCommentPort,
    CountEvidencesPort {

    private final EvidenceJpaRepository repository;
    private final AssessmentJpaRepository assessmentRepository;
    private final QuestionJpaRepository questionRepository;
    private final UserJpaRepository userRepository;
    private final AssessmentResultJpaRepository assessmentResultRepository;

    @Override
    public UUID persist(CreateEvidencePort.Param param) {
        if (!assessmentRepository.existsByIdAndDeletedFalse(param.assessmentId()))
            throw new ResourceNotFoundException(ADD_EVIDENCE_ASSESSMENT_ID_NOT_FOUND);

        var assessmentKitVersionId = assessmentResultRepository.findFirstByAssessment_IdOrderByLastModificationTimeDesc(param.assessmentId())
            .orElseThrow(() -> new ResourceNotFoundException(ADD_EVIDENCE_ASSESSMENT_ID_NOT_FOUND))
            .getKitVersionId();
        var question = questionRepository.findByIdAndKitVersionId(param.questionId(), assessmentKitVersionId)
            .orElseThrow(() -> new ResourceNotFoundException(ADD_EVIDENCE_QUESTION_ID_NOT_FOUND));
        if (!Objects.equals(assessmentKitVersionId, question.getKitVersionId()))
            throw new ResourceNotFoundException(ADD_EVIDENCE_QUESTION_ID_NOT_FOUND);
        var unsavedEntity = EvidenceMapper.mapCreateParamToJpaEntity(param);
        EvidenceJpaEntity entity = repository.save(unsavedEntity);
        return entity.getId();
    }

    @Override
    public PaginatedResponse<EvidenceListItem> loadNotDeletedEvidences(Long questionId, UUID assessmentId, int page, int size) {
        if (!assessmentRepository.existsByIdAndDeletedFalse(assessmentId))
            throw new ResourceNotFoundException(ASSESSMENT_ID_NOT_FOUND);

        var order = EvidenceJpaEntity.Fields.lastModificationTime;
        var sort = Sort.Direction.DESC;
        var pageResult = repository.findByQuestionIdAndAssessmentId(questionId, assessmentId, PageRequest.of(page, size, sort, order));
        var userIds = pageResult.getContent().stream()
            .map(EvidenceWithAttachmentsCountView::getCreatedBy)
            .toList();
        var userIdToUserMap = userRepository.findAllById(userIds).stream()
            .collect(toMap(UserJpaEntity::getId, Function.identity()));
        var items = pageResult.getContent().stream()
            .map(e -> toEvidenceListItem(e, userIdToUserMap.get(e.getCreatedBy())))
            .toList();
        return new PaginatedResponse<>(
            items,
            pageResult.getNumber(),
            pageResult.getSize(),
            order,
            sort.name().toLowerCase(),
            (int) pageResult.getTotalElements()
        );
    }

    @Override
    public UpdateEvidencePort.Result update(UpdateEvidencePort.Param param) {
        repository.update(
            param.id(),
            param.description(),
            param.type(),
            param.lastModificationTime(),
            param.lastModifiedById()
        );
        return new UpdateEvidencePort.Result(param.id());
    }

    @Override
    public void deleteById(UUID id) {
        repository.delete(id);
    }

    @Override
    public Evidence loadNotDeletedEvidence(UUID id) {
        return repository.findByIdAndDeletedFalse(id)
            .map(EvidenceMapper::mapToDomainModel)
            .orElseThrow(() -> new ResourceNotFoundException(EVIDENCE_ID_NOT_FOUND));
    }

    @Override
    public void resolveComment(UUID commentId, UUID lastModifiedBy, LocalDateTime lastModificationTime) {
        repository.resolveComment(commentId, lastModifiedBy, lastModificationTime);
    }

    @Override
    public int countAnsweredQuestionsHavingEvidence(UUID assessmentId) {
        return repository.countAnsweredQuestionsHavingEvidence(assessmentId);
    }

    @Override
    public int countUnresolvedComments(UUID assessmentId) {
        return repository.countUnresolvedComments(assessmentId);
    }

    @Override
    public Map<Long, Integer> countUnresolvedComments(UUID assessmentId, Set<Long> questionnaireIds) {
        return repository.countQuestionnairesUnresolvedComments(assessmentId, questionnaireIds).stream()
            .collect(toMap(
                EvidencesQuestionnaireAndCountView::getQuestionnaireId,
                EvidencesQuestionnaireAndCountView::getCount));
    }

    @Override
    public Map<Long, Integer> countAnsweredQuestionsHavingEvidence(UUID assessmentId, long questionnaireId) {
        return repository.countQuestionnaireQuestionsHavingEvidence(assessmentId, questionnaireId).stream()
            .collect(toMap(
                EvidencesQuestionAndCountView::getQuestionId,
                EvidencesQuestionAndCountView::getCount
            ));
    }

    @Override
    public Map<Long, Integer> countUnresolvedComments(UUID assessmentId, long questionnaireId) {
        return repository.countQuestionnaireQuestionsUnresolvedComments(assessmentId, questionnaireId).stream()
            .collect(toMap(
                EvidencesQuestionAndCountView::getQuestionId,
                EvidencesQuestionAndCountView::getCount
            ));
    }

    @Override
    public Map<Long, Integer> countAnsweredQuestionsHavingEvidence(UUID assessmentId, Set<Long> questionnaireIds) {
        return repository.countQuestionnairesQuestionsHavingEvidence(assessmentId, questionnaireIds).stream()
            .collect(toMap(
                EvidencesQuestionnaireAndCountView::getQuestionnaireId,
                EvidencesQuestionnaireAndCountView::getCount));
    }
}
