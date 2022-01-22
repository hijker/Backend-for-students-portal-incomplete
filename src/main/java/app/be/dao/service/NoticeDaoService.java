package app.be.dao.service;

import app.be.dao.repository.NoticeRepository;
import app.be.model.Notice;
import app.be.model.Poster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeDaoService {

    @Autowired
    private NoticeRepository noticeRepository;

    public NoticeDaoService() {
    }

    public Notice createNotice(final Notice notice) {
        return noticeRepository.save(notice);
    }

    public Notice findNotice(String id) {
        return noticeRepository.findById(id).orElse(null);
    }

    public Notice findNoticeByName(String name) {
        return noticeRepository.findByName(name);
    }

    public List<Notice> findAllNotice() {
        return noticeRepository.findAll();
    }
}
