package app.be.service;

import app.be.dao.service.PosterDaoService;
import app.be.dao.service.UserDaoService;
import app.be.model.Poster;
import app.be.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PosterService {

    @Autowired
    private PosterDaoService posterDaoService;

    public PosterService() {
    }

    public Poster insertPoster(final String id,
                               final String name,
                               final byte[] data) {
        final Poster poster = new Poster();
        poster.setId(id);
        poster.setName(name);
        poster.setData(data);
        return posterDaoService.createPoster(poster);
    }

    public Poster findPoster(String id) {
        return posterDaoService.findPoster(id);
    }

    public Poster findPosterByName(String name) {
        return posterDaoService.findPosterByName(name);
    }

    public List<Poster> findAll() {
        return posterDaoService.findAllPoster();
    }
}
