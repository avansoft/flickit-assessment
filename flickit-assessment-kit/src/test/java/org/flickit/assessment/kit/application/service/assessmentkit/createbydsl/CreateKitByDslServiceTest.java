package org.flickit.assessment.kit.application.service.assessmentkit.createbydsl;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.kit.application.domain.KitVersionStatus;
import org.flickit.assessment.kit.application.port.in.assessmentkit.CreateKitByDslUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.CreateAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.assessmentkit.UpdateKitActiveVersionPort;
import org.flickit.assessment.kit.application.port.out.assessmentkittag.CreateKitTagRelationPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupMemberIdsPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupOwnerPort;
import org.flickit.assessment.kit.application.port.out.kitdsl.LoadDslJsonPathPort;
import org.flickit.assessment.kit.application.port.out.kitdsl.UpdateKitDslPort;
import org.flickit.assessment.kit.application.port.out.kituseraccess.GrantUserAccessToKitPort;
import org.flickit.assessment.kit.application.port.out.kitversion.CreateKitVersionPort;
import org.flickit.assessment.kit.application.port.out.minio.LoadKitDSLJsonFilePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.common.error.ErrorMessageKey.FILE_STORAGE_FILE_NOT_FOUND;
import static org.flickit.assessment.common.util.SlugCodeUtil.generateSlugCode;
import static org.flickit.assessment.kit.common.ErrorMessageKey.CREATE_KIT_BY_DSL_KIT_DSL_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateKitByDslServiceTest {

    private static final UUID EXPERT_GROUP_OWNER_ID = UUID.randomUUID();
    private static final UUID DSL_JSON_VERSION_ID = UUID.randomUUID();
    private static final String DSL_JSON = "sample/json/file/path/" + DSL_JSON_VERSION_ID;
    private static final Long KIT_ID = 1L;

    @InjectMocks
    private CreateKitByDslService service;
    @Mock
    private LoadExpertGroupOwnerPort loadExpertGroupOwnerPort;
    @Mock
    private LoadDslJsonPathPort loadDslJsonPathPort;
    @Mock
    private LoadKitDSLJsonFilePort loadKitDSLJsonFilePort;
    @Mock
    private CreateAssessmentKitPort createAssessmentKitPort;
    @Mock
    private CompositeCreateKitPersister persister;
    @Mock
    private CreateKitTagRelationPort createKitTagRelationPort;
    @Mock
    private UpdateKitDslPort updateKitDslPort;
    @Mock
    private GrantUserAccessToKitPort grantUserAccessToKitPort;
    @Mock
    private LoadExpertGroupMemberIdsPort loadExpertGroupMemberIdsPort;
    @Mock
    private CreateKitVersionPort createKitVersionPort;
    @Mock
    private UpdateKitActiveVersionPort updateKitActiveVersionPort;

    @Test
    void testCreateKitByDsl_ValidInputs_CreateAndSaveKit() {
        long kitVersionId = 123L;
        var param = createParam(b -> b.currentUserId(EXPERT_GROUP_OWNER_ID));
        String dslContent = """
                {
                    "questionnaireModels": [
                        {
                            "code": "CleanArchitecture",
                            "index": 1,
                            "title": "Clean Architecture",
                            "description": "desc"
                        },
                        {
                            "code": "CodeQuality",
                            "index": 2,
                            "title": "Code Quality",
                            "description": "desc"
                        }
                    ],
                    "attributeModels": [],
                    "questionModels": [],
                    "subjectModels": [
                        {
                            "code": "Software",
                            "index": 1,
                            "title": "Software",
                            "description": "desc",
                            "weight": 0,
                            "questionnaireCodes": null
                        },
                        {
                            "code": "Team",
                            "index": 2,
                            "title": "Team",
                            "description": "desc",
                            "weight": 0,
                            "questionnaireCodes": null
                        }
                    ],
                    "levelModels": [],
                    "hasError": false
                }
            """;
        UUID currentUserId = EXPERT_GROUP_OWNER_ID;

        when(loadExpertGroupOwnerPort.loadOwnerId(param.getExpertGroupId())).thenReturn(EXPERT_GROUP_OWNER_ID);
        when(loadDslJsonPathPort.loadJsonPath(param.getKitDslId())).thenReturn(DSL_JSON);
        when(loadKitDSLJsonFilePort.loadDslJson(DSL_JSON)).thenReturn(dslContent);
        when(createAssessmentKitPort.persist(any())).thenReturn(KIT_ID);
        when(createKitVersionPort.persist(any())).thenReturn(kitVersionId);
        doNothing().when(updateKitActiveVersionPort).updateActiveVersion(KIT_ID, kitVersionId);
        doNothing().when(persister).persist(any(), anyLong(), any());
        doNothing().when(updateKitDslPort).update(anyLong(), anyLong(), any(), any());

        UUID expertGroupMemberId = UUID.randomUUID();
        List<LoadExpertGroupMemberIdsPort.Result> expertGroupMembers = List.of(
            new LoadExpertGroupMemberIdsPort.Result(currentUserId),
            new LoadExpertGroupMemberIdsPort.Result(expertGroupMemberId));
        List<UUID> expertGroupMemberIds = List.of(currentUserId, expertGroupMemberId);
        when(loadExpertGroupMemberIdsPort.loadMemberIds(param.getExpertGroupId())).thenReturn(expertGroupMembers);
        doNothing().when(grantUserAccessToKitPort).grantUsersAccess(KIT_ID, expertGroupMemberIds);

        Long savedKitId = service.create(param);
        assertEquals(KIT_ID, savedKitId);

        ArgumentCaptor<CreateAssessmentKitPort.Param> createKitPortParamCaptor = ArgumentCaptor.forClass(CreateAssessmentKitPort.Param.class);
        verify(createAssessmentKitPort).persist(createKitPortParamCaptor.capture());
        assertEquals(generateSlugCode(param.getTitle()), createKitPortParamCaptor.getValue().code());
        assertEquals(param.getTitle(), createKitPortParamCaptor.getValue().title());
        assertEquals(param.getSummary(), createKitPortParamCaptor.getValue().summary());
        assertEquals(param.getAbout(), createKitPortParamCaptor.getValue().about());
        assertFalse(createKitPortParamCaptor.getValue().published());
        assertEquals(param.getIsPrivate(), createKitPortParamCaptor.getValue().isPrivate());
        assertEquals(param.getExpertGroupId(), createKitPortParamCaptor.getValue().expertGroupId());
        assertEquals(param.getCurrentUserId(), createKitPortParamCaptor.getValue().createdBy());

        ArgumentCaptor<CreateKitVersionPort.Param> createVersionPortParamCaptor = ArgumentCaptor.forClass(CreateKitVersionPort.Param.class);
        verify(createKitVersionPort).persist(createVersionPortParamCaptor.capture());
        assertEquals(KIT_ID, createVersionPortParamCaptor.getValue().kitId());
        assertEquals(KitVersionStatus.ACTIVE, createVersionPortParamCaptor.getValue().status());
        assertEquals(param.getCurrentUserId(), createVersionPortParamCaptor.getValue().createdBy());

        verify(createKitTagRelationPort, times(1)).persist(param.getTagIds(), KIT_ID);
    }

    @Test
    void testCreateKitByDsl_CurrentUserIsNotExpertGroupOwner_ThrowException() {
        var param = createParam(CreateKitByDslUseCase.Param.ParamBuilder::build);

        when(loadExpertGroupOwnerPort.loadOwnerId(param.getExpertGroupId())).thenReturn(EXPERT_GROUP_OWNER_ID);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.create(param));
        assertThat(throwable).hasMessage(COMMON_CURRENT_USER_NOT_ALLOWED);
    }

    @Test
    void testCreateKitByDsl_KitDslDoesNotExist_ThrowException() {
        var param = createParam(b -> b.currentUserId(EXPERT_GROUP_OWNER_ID));

        when(loadExpertGroupOwnerPort.loadOwnerId(param.getExpertGroupId())).thenReturn(EXPERT_GROUP_OWNER_ID);
        when(loadDslJsonPathPort.loadJsonPath(param.getKitDslId())).thenThrow(new ResourceNotFoundException(CREATE_KIT_BY_DSL_KIT_DSL_NOT_FOUND));

        var throwable = assertThrows(ResourceNotFoundException.class, () -> service.create(param));
        assertThat(throwable).hasMessage(CREATE_KIT_BY_DSL_KIT_DSL_NOT_FOUND);
    }

    @Test
    void testCreateKitByDsl_JsonFileIsNotUploaded_ThrowException() {
        var param = createParam(b -> b.currentUserId(EXPERT_GROUP_OWNER_ID));

        when(loadExpertGroupOwnerPort.loadOwnerId(param.getExpertGroupId())).thenReturn(EXPERT_GROUP_OWNER_ID);
        when(loadDslJsonPathPort.loadJsonPath(param.getKitDslId())).thenReturn(DSL_JSON);
        when(loadKitDSLJsonFilePort.loadDslJson(any())).thenThrow(new ResourceNotFoundException(FILE_STORAGE_FILE_NOT_FOUND));

        var throwable = assertThrows(ResourceNotFoundException.class, () -> service.create(param));
        assertThat(throwable).hasMessage(FILE_STORAGE_FILE_NOT_FOUND);
    }

    private CreateKitByDslUseCase.Param createParam(Consumer<CreateKitByDslUseCase.Param.ParamBuilder> changer) {
        var param = paramBuilder();
        changer.accept(param);
        return param.build();
    }

    private CreateKitByDslUseCase.Param.ParamBuilder paramBuilder() {
        return CreateKitByDslUseCase.Param.builder()
            .kitDslId(1L)
            .isPrivate(false)
            .expertGroupId(1L)
            .title("title")
            .summary("summary")
            .about("about")
            .lang("EN")
            .tagIds(List.of(1L))
            .currentUserId(UUID.randomUUID());
    }
}
