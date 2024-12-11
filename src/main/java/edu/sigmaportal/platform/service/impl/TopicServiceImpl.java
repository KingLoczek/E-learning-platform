package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.TopicDto;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.model.TopicModel;
import edu.sigmaportal.platform.repository.TopicsRepository;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicsRepository repo;
    private final CourseService courses;

    public TopicServiceImpl(TopicsRepository repo, CourseService courses) {
        this.repo = repo;
        this.courses = courses;
    }

    @Override
    public TopicDto create(TopicDto topic) {
        if (!courses.exists(topic.courseId))
            throw new EntityNotFoundException("Course not found");

        int cid = strToTopicId(topic.courseId);
        TopicModel model = new TopicModel(0, topic.title, topic.description, cid);
        TopicModel saved = repo.save(model);
        return new TopicDto(idToStr(saved.topicId()), saved.title(), saved.description(), idToStr(saved.courseId()));
    }

    @Override
    public String owningCourseId(String id) {
        int tid = strToTopicId(id);
        int owningCid = repo.findById(tid).orElseThrow(() -> new EntityNotFoundException("Topic not found")).courseId();
        return idToStr(owningCid);
    }

    @Override
    public TopicDto update(String id, TopicDto topic) {
        int tid = strToTopicId(id);
        TopicModel model = repo.findById(tid).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        String title = Objects.isNull(topic.title) ? model.title() : topic.title;
        String description = Objects.isNull(topic.description) ? model.description() : topic.description;
        TopicModel merged = new TopicModel(model.topicId(), title, description, model.courseId());
        TopicModel saved = repo.save(merged);
        return new TopicDto(idToStr(saved.topicId()), title, description, idToStr(saved.courseId()));
    }

    @Override
    public void delete(String id) {
        int tid = strToTopicId(id);
        TopicModel model = repo.findById(tid).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        repo.delete(model);
    }

    @Override
    public TopicDto find(String id) {
        int tid = strToTopicId(id);
        TopicModel model = repo.findById(tid).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        return new TopicDto(idToStr(model.topicId()), model.title(), model.description(), idToStr(model.courseId()));
    }

    @Override
    public Collection<String> findOwnedBy(String id) {
        if (!courses.exists(id))
            throw new EntityNotFoundException("Course not found");

        int cid = strToCourseId(id);
        return repo.findAllByCourseId(cid).map(t -> Integer.toString(t.topicId())).toList();
    }

    @Override
    public boolean exists(String topicId) {
        int tid = strToTopicId(topicId);
        return repo.existsById(tid);
    }

    private int strToCourseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Invalid course id");
        }
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }

    private int strToTopicId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Invalid topic id");
        }
    }
}
