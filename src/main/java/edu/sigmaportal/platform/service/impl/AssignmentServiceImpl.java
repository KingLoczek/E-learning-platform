package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.AssignmentDto;
import edu.sigmaportal.platform.exception.BadRequestException;
import edu.sigmaportal.platform.exception.DependentEntityNotFoundException;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.model.AssignmentFileModel;
import edu.sigmaportal.platform.model.AssignmentModel;
import edu.sigmaportal.platform.repository.AssignmentFileRepository;
import edu.sigmaportal.platform.repository.AssignmentRepository;
import edu.sigmaportal.platform.service.AssignmentService;
import edu.sigmaportal.platform.service.FileService;
import edu.sigmaportal.platform.service.TopicService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository repo;
    private final AssignmentFileRepository assignFileRepo;
    private final TopicService topics;
    private final FileService files;

    public AssignmentServiceImpl(AssignmentRepository repo, AssignmentFileRepository assignFileRepo, TopicService topics, FileService files) {
        this.repo = repo;
        this.assignFileRepo = assignFileRepo;
        this.topics = topics;
        this.files = files;
    }

    @Override
    public AssignmentDto create(String assignerId, AssignmentDto assignment) {
        if (!topics.exists(assignment.topicId))
            throw new DependentEntityNotFoundException("Topic does not exist");

        if (!files.allExist(assignment.fileIds))
            throw new DependentEntityNotFoundException("Some files do not exist");

        int uid = strToUserId(assignerId);
        int tid = strToTopicId(assignment.topicId);
        OffsetDateTime dueDate = checkDate(assignment.dueDate, "due_date is in the past");
        OffsetDateTime closeDate = checkDate(assignment.closeDate, "close_date is in the past");
        if (dueDate.isAfter(closeDate)) throw new BadRequestException("due_date is after close_date");
        AssignmentModel model = new AssignmentModel(0, assignment.name, assignment.content, dueDate, closeDate, uid, tid);
        AssignmentModel saved = repo.save(model);
        List<AssignmentFileModel> files = assignment.fileIds.stream()
                .mapToInt(this::strToFileId)
                .mapToObj(id -> new AssignmentFileModel(0, saved.assignmentId(), id))
                .toList();
        List<String> savedFiles = assignFileRepo.saveAll(files).stream().map(m -> idToStr(m.fileId())).toList();
        return new AssignmentDto(idToStr(saved.assignmentId()), saved.name(), saved.content(), savedFiles, saved.dueDate(), saved.closeDate(), idToStr(saved.assignedBy()), idToStr(saved.topicId()));
    }

    @Override
    public AssignmentDto update(String assignmentId, AssignmentDto assigment) {
        if (!files.allExist(assigment.fileIds))
            throw new DependentEntityNotFoundException("Some files do not exist");

        if (assigment.topicId != null && !topics.exists(assigment.topicId))
                throw new DependentEntityNotFoundException("Topic does not exist");

        int aid = strToAssignmentId(assignmentId);
        AssignmentModel old = repo.findById(aid).orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        if (assigment.topicId != null) {
            String newTopicCourseId = topics.owningCourseId(assigment.topicId);
            String oldTopicCourseId = topics.owningCourseId(idToStr(old.topicId()));
            if (!newTopicCourseId.equals(oldTopicCourseId))
                throw new BadRequestException("Cannot assign to topic outside of the current course");
        }
        String name = Objects.isNull(assigment.name) ? old.name() : assigment.name;
        String content = Objects.isNull(assigment.content) ? old.content() : assigment.content;
        OffsetDateTime dueDate = Objects.isNull(assigment.dueDate) ? old.dueDate() : checkDate(assigment.dueDate, "due_date is in the past");
        OffsetDateTime closeDate = Objects.isNull(assigment.closeDate) ? old.closeDate() : checkDate(assigment.closeDate, "close_date is in the past");
        if (dueDate.isAfter(closeDate)) throw new BadRequestException("due_date is after close_date");
        int topicId = Objects.isNull(assigment.topicId) ? old.topicId() : strToTopicId(assigment.topicId);
        AssignmentModel merged = new AssignmentModel(old.assignmentId(), name, content, dueDate, closeDate, old.assignedBy(), topicId);
        AssignmentModel saved = repo.save(merged);
        Set<Integer> fileIds = assigment.fileIds.stream().map(this::strToAssignmentId).collect(Collectors.toSet());
        Set<Integer> oldFileIds = assignFileRepo.findAllByAssignmentId(aid).map(AssignmentFileModel::fileId).collect(Collectors.toSet());
        List<AssignmentFileModel> freshFiles = fileIds.stream()
                .filter(id -> !oldFileIds.contains(id))
                .map(id -> new AssignmentFileModel(0, aid, id)).toList();
        List<Integer> unlinkedFiles = oldFileIds.stream()
                .filter(id -> !fileIds.contains(id))
                .toList();
        if (!freshFiles.isEmpty())
            assignFileRepo.saveAll(freshFiles);
        if (!unlinkedFiles.isEmpty())
            assignFileRepo.deleteUnlikedFiles(aid, unlinkedFiles);
        List<String> savedFileIds = assignFileRepo.findAllByAssignmentId(aid).map(m -> idToStr(m.fileId())).toList();
        return new AssignmentDto(idToStr(saved.assignmentId()), saved.name(), saved.content(), savedFileIds, saved.dueDate(), saved.closeDate(), idToStr(saved.assignedBy()), idToStr(saved.topicId()));
    }

    @Override
    public String owningCourseId(String assignmentId) {
        int aid = strToAssignmentId(assignmentId);
        return idToStr(repo.findOwningCourseIdByAssignmentId(aid).orElseThrow(() -> new EntityNotFoundException("Assignment not found")));
    }

    @Override
    public void delete(String id) {
        int aid = strToAssignmentId(id);
        AssignmentModel model = repo.findById(aid).orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        assignFileRepo.removeAllByAssignmentId(model.assignmentId());
        repo.delete(model);
    }

    @Override
    public AssignmentDto find(String assignmentId) {
        int aid = strToAssignmentId(assignmentId);
        AssignmentModel model = repo.findById(aid).orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
        List<String> fileIds = assignFileRepo.findAllByAssignmentId(aid).map(m -> idToStr(m.fileId())).toList();
        return new AssignmentDto(idToStr(model.assignmentId()), model.name(), model.content(), fileIds, model.dueDate(), model.closeDate(), idToStr(model.assignedBy()), idToStr(model.topicId()));
    }

    private OffsetDateTime checkDate(OffsetDateTime dateTime, String error) {
        OffsetDateTime trunc = dateTime.truncatedTo(ChronoUnit.SECONDS);
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (trunc.isBefore(now)) {
            throw new BadRequestException(error);
        }

        return trunc;
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }

    private int strToAssignmentId(String assignmentId) {
        try {
            return Integer.parseInt(assignmentId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid assignment id");
        }
    }

    private int strToFileId(String fileId) {
        try {
            return Integer.parseInt(fileId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid topic id");
        }
    }

    private int strToTopicId(String topicId) {
        try {
            return Integer.parseInt(topicId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid topic id");
        }
    }

    private int strToUserId(String assignerId) {
        try {
            return Integer.parseInt(assignerId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid assigner id");
        }
    }
}
