package app.be.resource;

import app.be.model.Notice;
import app.be.response.NoticeListResponse;
import app.be.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notice")
public class NoticeUploadResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeUploadResource.class);

    @Autowired
    private NoticeService noticeService;

    @PostMapping(value = "/upload",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notice> upload(final HttpServletRequest request,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        String id = String.valueOf(Arrays.hashCode(file.getBytes()));
        String altName = file.getOriginalFilename();
        Notice notice = noticeService.insertNotice(id, altName, file.getBytes());

        return ResponseEntity.ok().body(notice);
    }

    @GetMapping(value = "/download",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> download(final HttpServletRequest request,
                                             final String name) {
        Notice notice = noticeService.findNoticeByName(name);

        if (notice == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Resource resource = new ByteArrayResource(notice.getData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(resource);
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoticeListResponse> all(final HttpServletRequest request) {
        List<Notice> posters = noticeService.findAll();
        Map<String, String> names = posters.stream()
                .collect(Collectors.toMap(Notice::getId, Notice::getName, (a, b) -> a));
        return ResponseEntity.ok().body(new NoticeListResponse(posters.size(), names));
    }

}
