package vttp.batch5.ssf.noticeboard.services;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

import java.io.StringReader;
import java.util.Set;

@Service
public class NoticeService {

    // TODO: Task 3
    // You can change the signature of this method by adding any number of parameters
    // and return any type

    @Autowired
    private NoticeRepository noticeRepo;

    @Value("${publish.server.url}")
    private String publisherUrl;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public ResponseEntity<String> postToNoticeServer(Notice notice) {

        //convert categories list into Json Array
        JsonArrayBuilder categories = Json.createArrayBuilder();
        for (String category : notice.getCategories()) {
            categories.add(category);
        }

        JsonObject obj = Json.createObjectBuilder()
                .add("title", notice.getTitle())
                .add("poster", notice.getPoster())
                .add("postDate", notice.getPostDate().getTime())
                .add("categories", categories.build())
                .add("text", notice.getText())
                .build();

        RequestEntity<String> requestEntity = RequestEntity
                .post(publisherUrl + "/notice")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .body(obj.toString());

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);


            if (!response.getStatusCode().isError()) {
                JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
                JsonObject jsonObject = jsonReader.readObject();

                String id = jsonObject.getString("id");
                long timestamp = jsonObject.getJsonNumber("timestamp").longValue();

                System.out.println("Notice succesfully posted");

                System.out.println("id: " + id);
                System.out.println("timestamp: " + timestamp);

                //store notice payload into redis
                noticeRepo.insertNotices(jsonObject);

                return ResponseEntity.status(HttpStatus.CREATED).body(id);

            } else {

                JsonObject errorResponse = Json.createObjectBuilder()
                        .add("message", "Notice posting unsuccessful")
                        .add("timestamp", notice.getPostDate().getTime())
                        .build();

                return new ResponseEntity<>(errorResponse.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.err.println("Error occurred while posting notice: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<?> checkHealth() {
        try {
            // Try to fetch a random key from Redis
            String randomKey = (String) redisTemplate.randomKey();

            if (randomKey != null && !randomKey.isEmpty()) {
                // if Redis is healthy

                JsonObject healthy = Json.createObjectBuilder()
                        .add("status", "UP")
                        .add("message", "Application is healthy")
                        .build();

                return ResponseEntity.status(200).body(healthy.toString());
            } else {
                // Redis is running but empty
                JsonObject unhealthy = Json.createObjectBuilder()
                        .add("status", "DOWN")
                        .add("message", "Application is unhealthy")
                        .build();

                return ResponseEntity.status(503).body(unhealthy.toString());
            }
        } catch (Exception e) {
            // Redis operation failed
            JsonObject unhealthy = Json.createObjectBuilder()
                    .add("status", "DOWN")
                    .add("message", "Application is unhealthy")
                    .build();

            return ResponseEntity.status(503).body(unhealthy.toString());
        }
    }


}
