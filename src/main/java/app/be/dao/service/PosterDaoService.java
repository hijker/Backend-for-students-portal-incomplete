package app.be.dao.service;

import app.be.dao.repository.PosterRepository;
import app.be.dao.repository.UsersRepository;
import app.be.model.Poster;
import app.be.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PosterDaoService {

    @Autowired
    private PosterRepository posterRepository;

    public PosterDaoService() {
    }

    public Poster createPoster(final Poster poster) {
        return posterRepository.save(poster);
    }

    public Poster findPoster(String id) {
        return posterRepository.findById(id).orElse(null);
    }

    public Poster findPosterByName(String name) {
        return posterRepository.findByName(name);
    }
}
