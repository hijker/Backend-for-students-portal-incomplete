package app.be.resource;

import app.be.model.Poster;
import app.be.response.PosterListResponse;
import app.be.service.PosterService;
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
@RequestMapping("/poster")
public class PosterUploadResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PosterUploadResource.class);

    @Autowired
    private PosterService posterService;

    @PostMapping(value = "/upload",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Poster> upload(final HttpServletRequest request,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        String id = String.valueOf(Arrays.hashCode(file.getBytes()));
        String altName = file.getOriginalFilename();
        Poster poster = posterService.insertPoster(id, altName, file.getBytes());

        return ResponseEntity.ok().body(poster);
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

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PosterListResponse> all(final HttpServletRequest request) {
        List<Poster> posters = posterService.findAll();
        Map<String, String> names = posters.stream()
                .collect(Collectors.toMap(Poster::getId, Poster::getName, (a, b) -> a));
        return ResponseEntity.ok().body(new PosterListResponse(posters.size(), names));
    }

}
