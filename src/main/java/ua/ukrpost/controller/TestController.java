package ua.ukrpost.controller;

import java.net.UnknownHostException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ukrpost.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
public class TestController {
    private TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("post/document1")
    public ResponseEntity<?> indexDocument1() {
        testService.indexDocument1();
        return new ResponseEntity<>("success", OK);
    }

    @GetMapping("put/document1")
    public ResponseEntity<?> updateDocument1() {
        testService.updateDocument1();
        return new ResponseEntity<>("success", OK);
    }

    @GetMapping("put-script/document1")
    public ResponseEntity<?> updateDocument1WithScript() {
        testService.updateDocument1WithScript();
        return new ResponseEntity<>("success", OK);
    }

    @GetMapping("get/document1")
    public ResponseEntity<?> fetchDocument1() {
        testService.fetchDocument1();
        return new ResponseEntity<>("success", OK);
    }

    @GetMapping("post")
    public ResponseEntity<?> postData(@RequestParam String text) {
        try {
            testService.postData(text);
            return new ResponseEntity<>("success", OK);
        } catch (UnknownHostException e) {
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }
    }

    @GetMapping("get")
    public ResponseEntity<?> getData() {
        try {
            String data = testService.getData();
            return new ResponseEntity<>(data, OK);
        } catch (UnknownHostException e) {
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }
    }
}
