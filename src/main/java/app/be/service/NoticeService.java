package app.be.service;

import app.be.dao.service.NoticeDaoService;
import app.be.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeDaoService noticeDaoService;

    public NoticeService() {
    }

    public Notice insertNotice(final String id,
                               final String name,
                               final byte[] data) {
        final Notice notice = new Notice();
        notice.setId(id);
        notice.setName(name);
        notice.setData(data);
        return noticeDaoService.createNotice(notice);
    }

    public Notice findPoster(String id) {
        return noticeDaoService.findNotice(id);
    }

    public Notice findNoticeByName(String name) {
        return noticeDaoService.findNoticeByName(name);
    }

    public List<Notice> findAll() {
        return noticeDaoService.findAllNotice();
    }
}
