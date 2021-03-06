package com.capitalone.dashboard.rest;

import com.capitalone.dashboard.config.TestConfig;
import com.capitalone.dashboard.config.WebMVCConfig;
import com.capitalone.dashboard.model.Build;
import com.capitalone.dashboard.model.BuildStatus;
import com.capitalone.dashboard.model.DataResponse;
import com.capitalone.dashboard.model.SCM;
import com.capitalone.dashboard.request.BuildRequest;
import com.capitalone.dashboard.service.BuildService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class, WebMVCConfig.class})
@WebAppConfiguration
public class BuildControllerTest {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext wac;
    @Autowired private BuildService buildService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void builds() throws Exception {
        Build build = makeBuild();
        Iterable<Build> builds = Arrays.asList(build);
        DataResponse<Iterable<Build>> response = new DataResponse<>(builds, 1);
        SCM scm = build.getSourceChangeSet().get(0);

        when(buildService.search(Mockito.any(BuildRequest.class))).thenReturn(response);

        mockMvc.perform(get("/build?componentId=" + ObjectId.get()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$result", hasSize(1)))
                .andExpect(jsonPath("$result[0].id", is(build.getId().toString())))
                .andExpect(jsonPath("$result[0].collectorItemId", is(build.getCollectorItemId().toString())))
                .andExpect(jsonPath("$result[0].timestamp", is(intVal(build.getTimestamp()))))
                .andExpect(jsonPath("$result[0].number", is(build.getNumber())))
                .andExpect(jsonPath("$result[0].buildUrl", is(build.getBuildUrl())))
                .andExpect(jsonPath("$result[0].artifactVersionNumber", is(build.getArtifactVersionNumber())))
                .andExpect(jsonPath("$result[0].startTime", is(intVal(build.getStartTime()))))
                .andExpect(jsonPath("$result[0].endTime", is(intVal(build.getEndTime()))))
                .andExpect(jsonPath("$result[0].duration", is(intVal(build.getDuration()))))
                .andExpect(jsonPath("$result[0].buildStatus", is(build.getBuildStatus().toString())))
                .andExpect(jsonPath("$result[0].startedBy", is(build.getStartedBy())))
                .andExpect(jsonPath("$result[0].sourceChangeSet", hasSize(1)))
                .andExpect(jsonPath("$result[0].sourceChangeSet[0].scmUrl", is(scm.getScmUrl())))
                .andExpect(jsonPath("$result[0].sourceChangeSet[0].scmRevisionNumber", is(scm.getScmRevisionNumber())))
                .andExpect(jsonPath("$result[0].sourceChangeSet[0].numberOfChanges", is(intVal(scm.getNumberOfChanges()))))
                .andExpect(jsonPath("$result[0].sourceChangeSet[0].scmCommitTimestamp", is(intVal(scm.getScmCommitTimestamp()))))
                .andExpect(jsonPath("$result[0].sourceChangeSet[0].scmCommitLog", is(scm.getScmCommitLog())))
                .andExpect(jsonPath("$result[0].sourceChangeSet[0].scmAuthor", is(scm.getScmAuthor())));
    }

    @Test
    public void  builds_noComponentId_badRequest() throws Exception {
        mockMvc.perform(get("/build")).andExpect(status().isBadRequest());
    }

    private Build makeBuild() {
        Build build = new Build();
        build.setId(ObjectId.get());
        build.setCollectorItemId(ObjectId.get());
        build.setTimestamp(1);
        build.setNumber("1");
        build.setBuildUrl("buildUrl");
        build.setArtifactVersionNumber("2");
        build.setStartTime(3);
        build.setEndTime(8);
        build.setDuration(5);
        build.setBuildStatus(BuildStatus.Success);
        build.setStartedBy("foo");
        build.getSourceChangeSet().add(makeScm());
        return build;
    }

    private SCM makeScm() {
        SCM scm = new SCM();
        scm.setScmUrl("scmUrl");
        scm.setScmRevisionNumber("revNum");
        scm.setNumberOfChanges(20);
        scm.setScmCommitTimestamp(200);
        scm.setScmCommitLog("Log message");
        scm.setScmAuthor("bob");
        return scm;
    }

    private int intVal(long value) {
        return Long.valueOf(value).intValue();
    }

}
