package org.flickit.flickitassessmentcore.application.service.assessmentresult;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flickit.flickitassessmentcore.application.port.out.answer.FindAnswerOptionIdByResultAndQuestionInAnswerPort;
import org.flickit.flickitassessmentcore.application.port.out.answeroptionimpact.LoadAnswerOptionImpactsByAnswerOptionPort;
import org.flickit.flickitassessmentcore.application.port.out.levelcompetence.LoadLevelCompetenceByMaturityLevelPort;
import org.flickit.flickitassessmentcore.application.port.out.maturitylevel.LoadMaturityLevelByKitPort;
import org.flickit.flickitassessmentcore.application.port.out.question.LoadQuestionsByQualityAttributePort;
import org.flickit.flickitassessmentcore.application.port.out.questionImpact.LoadQuestionImpactPort;
import org.flickit.flickitassessmentcore.domain.*;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.*;

@Transactional
@RequiredArgsConstructor
@Component
@Slf4j
public class CalculateQualityAttributeMaturityLevel {

    private final LoadQuestionsByQualityAttributePort loadQuestionsByQualityAttributePort;
    private final LoadAnswerOptionImpactsByAnswerOptionPort loadAnswerOptionImpactsByAnswerOptionPort;
    private final LoadMaturityLevelByKitPort loadMaturityLevelByKitPort;
    private final FindAnswerOptionIdByResultAndQuestionInAnswerPort findAnswerOptionIdByResultAndQuestionInAnswerPort;
    private final LoadLevelCompetenceByMaturityLevelPort loadLevelCompetenceByMaturityLevelPort;
    private final LoadQuestionImpactPort loadQuestionImpactPort;

    public MaturityLevel calculateQualityAttributeMaturityLevel(AssessmentResult assessmentResult, QualityAttribute qualityAttribute, Long assessmentKitId) {
        Set<Question> questions = loadQuestionsByQualityAttributePort.loadByQualityAttributeId(new LoadQuestionsByQualityAttributePort.Param(qualityAttribute.getId())).questions();
        Map<Long, Integer> maturityLevelValueSumMap = new HashMap<>();
        Map<Long, Integer> maturityLevelValueCountMap = new HashMap<>();
        for (Question question : questions) {
            Long questionAnswerId = findQuestionAnswer(assessmentResult, question);
            if (questionAnswerId != null) {
                Set<AnswerOptionImpact> answerOptionImpacts = loadAnswerOptionImpactsByAnswerOptionPort.loadByAnswerOptionId(questionAnswerId).optionImpacts();
                for (AnswerOptionImpact impact : answerOptionImpacts) {
                    if (impact.getOptionId().equals(questionAnswerId)) {
                        Long questionImpactId = impact.getQuestionImapctId();
                        QuestionImpact questionImpact = loadQuestionImpactPort.load(new LoadQuestionImpactPort.Param(questionImpactId)).questionImpact();
                        Integer value = impact.getValue().setScale(0, RoundingMode.HALF_UP).intValue() * questionImpact.getWeight();
                        Long maturityLevelId = questionImpact.getMaturityLevelId();
                        log.debug("Question: [{}] with Option: [{}] as answer, has value: [{}], on ml: [{}]",
                            question.getTitle(), questionAnswerId, value, maturityLevelId);
                        maturityLevelValueSumMap.put(maturityLevelId, maturityLevelValueSumMap.getOrDefault(maturityLevelId, 0) + value);
                        maturityLevelValueCountMap.put(maturityLevelId, maturityLevelValueCountMap.getOrDefault(maturityLevelId, 0) + questionImpact.getWeight());
                    }
                }
            }
        }
        Map<Long, Integer> qualityAttributeImpactScoreMap = new HashMap<>();
        for (Long maturityLevelId : maturityLevelValueSumMap.keySet()) {
            qualityAttributeImpactScoreMap.put(maturityLevelId, maturityLevelValueSumMap.get(maturityLevelId) / maturityLevelValueCountMap.get(maturityLevelId));
        }
        // We have to create a new list, because the output of called method is immutable list, so we can't do anything on it further.
        List<MaturityLevel> maturityLevels = new ArrayList<>(loadMaturityLevelByKitPort.loadByKitId(assessmentKitId).maturityLevels());
        MaturityLevel qualityAttMaturityLevel = findMaturityLevelBasedOnCalculations(qualityAttributeImpactScoreMap, maturityLevels);

        return qualityAttMaturityLevel;
    }

    private Long findQuestionAnswer(AssessmentResult assessmentResult, Question question) {
        return findAnswerOptionIdByResultAndQuestionInAnswerPort.findAnswerOptionIdByResultIdAndQuestionId(
            new FindAnswerOptionIdByResultAndQuestionInAnswerPort.Param(assessmentResult.getId(), question.getId())
        );
    }

    /**
     * This method sorts maturity level list of desired kit by its value.
     * Then iterates over level competences and compares through thresholds.
     * If no threshold fulfills, it will return first and least maturity level.
     */
    private MaturityLevel findMaturityLevelBasedOnCalculations(Map<Long, Integer> qualityAttImpactScoreMap, List<MaturityLevel> maturityLevels) {
        maturityLevels.sort(Comparator.comparingInt(MaturityLevel::getValue));
        MaturityLevel result = maturityLevels.get(0);
        for (MaturityLevel maturityLevel : maturityLevels) {
            List<LevelCompetence> levelCompetences = loadLevelCompetenceByMaturityLevelPort.loadByMaturityLevelId(maturityLevel.getId()).levelCompetences();
            for (LevelCompetence levelCompetence : levelCompetences) {
                Long id = levelCompetence.getMaturityLevelCompetenceId();
                if (qualityAttImpactScoreMap.containsKey(id) && qualityAttImpactScoreMap.get(id) >= levelCompetence.getValue()) {
                    Optional<MaturityLevel> resultMaturityLevel = maturityLevels.stream().filter(ml -> ml.getId().equals(id)).findFirst();
                    result = resultMaturityLevel.orElseGet(() -> maturityLevels.get(0));
                } else {
                    break;
                }
            }
        }
        return result;
    }
}
