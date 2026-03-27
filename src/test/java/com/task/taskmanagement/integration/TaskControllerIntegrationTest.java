package com.task.taskmanagement.integration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.task.taskmanagement.dto.TaskRequest;
import com.task.taskmanagement.entity.TaskStatus;
import com.task.taskmanagement.repository.TaskRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    private final tools.jackson.databind.ObjectMapper objectMapper = new tools.jackson.databind.ObjectMapper();

    @Autowired
    private TaskRepository taskRepository;


    @BeforeEach
    void cleanup() {
        taskRepository.deleteAll();
    }

    @Test
    void fullCrudFlowShouldWork() throws Exception {
        TaskRequest createRequest = new TaskRequest();
        createRequest.setTitle("Integration Task");
        createRequest.setDescription("Integration test run");
        createRequest.setStatus(TaskStatus.PENDING);
        createRequest.setDueDate(LocalDate.now().plusDays(10));

        HttpResponse<String> createResp = client.send(createPost(createRequest), HttpResponse.BodyHandlers.ofString());
        assert createResp.statusCode() == 200;

        Map<String, Object> body = objectMapper.readValue(createResp.body(), Map.class);
        assert "SUCCESS".equals(body.get("status"));

        Map<String, Object> data = (Map<String, Object>) body.get("data");
        String id = (String) data.get("id");

        HttpResponse<String> getResp = client.send(createGet("/tasks/" + id), HttpResponse.BodyHandlers.ofString());
        assert getResp.statusCode() == 200;

        TaskRequest updateRequest = new TaskRequest();
        updateRequest.setTitle("Updated Task");

        HttpResponse<String> updateResp = client.send(createPut(id, updateRequest), HttpResponse.BodyHandlers.ofString());
        assert updateResp.statusCode() == 200;

        client.send(createDelete(id), HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> notFoundResp = client.send(createGet("/tasks/" + id), HttpResponse.BodyHandlers.ofString());
        assert notFoundResp.statusCode() == 404;
    }

    @Test
    void getAllAndFilterByStatusShouldWork() throws Exception {
        TaskRequest t1 = new TaskRequest();
        t1.setTitle("Pending task");
        t1.setDueDate(LocalDate.now().plusDays(5));
        t1.setStatus(TaskStatus.PENDING);

        TaskRequest t2 = new TaskRequest();
        t2.setTitle("Done task");
        t2.setDueDate(LocalDate.now().plusDays(3));
        t2.setStatus(TaskStatus.DONE);

        client.send(createPost(t1), HttpResponse.BodyHandlers.ofString());
        client.send(createPost(t2), HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> allResp = client.send(createGet("/tasks?page=0&size=10"), HttpResponse.BodyHandlers.ofString());
        assert allResp.statusCode() == 200;

        Map<String, Object> allBody = objectMapper.readValue(allResp.body(), Map.class);
        Map<String, Object> allData = (Map<String, Object>) allBody.get("data");
        assert allData.get("totalItems").equals(2);

        HttpResponse<String> filterResp = client.send(createGet("/tasks?page=0&size=10&status=DONE"), HttpResponse.BodyHandlers.ofString());
        Map<String, Object> filterBody = objectMapper.readValue(filterResp.body(), Map.class);
        Map<String, Object> filterData = (Map<String, Object>) filterBody.get("data");
        assert filterData.get("totalItems").equals(1);
    }

    @Test
    void createWithInvalidDataShouldReturnBadRequest() throws Exception {
        TaskRequest invalid = new TaskRequest();
        invalid.setTitle(null);
        invalid.setDueDate(LocalDate.now().minusDays(1));

        HttpResponse<String> resp = client.send(createPost(invalid), HttpResponse.BodyHandlers.ofString());
        assert resp.statusCode() == 400;
    }

    private HttpRequest createPost(TaskRequest request) throws Exception {
        String json = objectMapper.writeValueAsString(request);

        return HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private HttpRequest createGet(String path) throws Exception {
        return HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + path))
                .GET()
                .build();
    }

    private HttpRequest createPut(String id, TaskRequest request) throws Exception {
        String json = objectMapper.writeValueAsString(request);
        return HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/tasks/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private HttpRequest createDelete(String id) throws Exception {
        return HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/tasks/" + id))
                .DELETE()
                .build();
    }
}

