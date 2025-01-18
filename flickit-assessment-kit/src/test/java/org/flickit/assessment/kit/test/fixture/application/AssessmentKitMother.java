package org.flickit.assessment.kit.test.fixture.application;

import org.flickit.assessment.kit.application.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public class AssessmentKitMother {

    public static final String CODE = "code";
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String ABOUT = "about";
    public static final long EXPERT_GROUP_ID = 1L;
    private static long id = 134L;

    public static AssessmentKit simpleKit() {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            null,
            null,
            null,
            null,
            id++);
    }

    public static AssessmentKit notPublishedKit() {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.FALSE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            null,
            null,
            null,
            null,
            id++);
    }

    public static AssessmentKit completeKit(List<Subject> subjects,
                                            List<MaturityLevel> maturityLevels,
                                            List<Questionnaire> questionnaires,
                                            List<AnswerRange> reusableAnswerRanges) {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            subjects,
            maturityLevels,
            questionnaires,
            reusableAnswerRanges,
            id++);
    }

    public static AssessmentKit kitWithMaturityLevels(List<MaturityLevel> maturityLevels) {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            null,
            maturityLevels,
            null,
            null,
            id++);
    }

    public static AssessmentKit kitWithQuestionnaires(List<Questionnaire> questionnaires) {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            null,
            null,
            questionnaires,
            null,
            id++);
    }

    public static AssessmentKit kitWithSubjects(List<Subject> subjects) {
        return kitWithSubjects(subjects, false);
    }

    public static AssessmentKit kitWithAnswerRanges(List<AnswerRange> answerRanges) {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            null,
            null,
            null,
            answerRanges,
            id++);
    }

    public static AssessmentKit kitWithSubjects(List<Subject> subjects, boolean isPrivate) {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            isPrivate,
            EXPERT_GROUP_ID,
            subjects,
            null,
            null,
            null,
            id++);
    }

    public static AssessmentKit kitWithSubjectsAndQuestionnaires(List<Subject> subjects, List<Questionnaire> questionnaires) {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            subjects,
            null,
            questionnaires,
            null,
            id++);
    }

    public static AssessmentKit privateKit() {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.TRUE,
            EXPERT_GROUP_ID,
            null,
            null,
            null,
            null,
            id++);
    }

    public static AssessmentKit kitWithKitVersionId(Long activeVersionId) {
        return new AssessmentKit(
            id++,
            CODE + id,
            TITLE + id,
            SUMMARY,
            ABOUT,
            "EN",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Boolean.TRUE,
            Boolean.FALSE,
            EXPERT_GROUP_ID,
            null,
            null,
            null,
            null,
            activeVersionId);
    }
}
