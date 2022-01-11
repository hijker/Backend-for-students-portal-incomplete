package app.be.resource;

import app.be.model.Poster;
import app.be.service.PosterService;
import app.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/file")
public class UploadResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadResource.class);

    @Autowired
    private PosterService posterService;

    @PostMapping(value = "/upload",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(final HttpServletRequest request,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        String id = String.valueOf(Arrays.hashCode(file.getBytes()));
        String altName = file.getOriginalFilename();
        posterService.insertPoster(id, altName, file.getBytes());

        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = "/download",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> download(final HttpServletRequest request,
                                             final String name) {
        Poster poster = posterService.findPosterByName(name);

        if (poster == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Resource resource = new ByteArrayResource(poster.getData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(resource);
    }
}
