package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.SubmissionDto;
import edu.sigmaportal.platform.exception.BadRequestException;
import edu.sigmaportal.platform.exception.DependentEntityNotFoundException;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.exception.InvalidOperationException;
import edu.sigmaportal.platform.model.SubmissionFileModel;
import edu.sigmaportal.platform.model.SubmissionModel;
import edu.sigmaportal.platform.repository.SubmissionFileRepository;
import edu.sigmaportal.platform.repository.SubmissionRepository;
import edu.sigmaportal.platform.service.AssignmentService;
import edu.sigmaportal.platform.service.FileService;
import edu.sigmaportal.platform.service.SubmissionService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository repo;
    private final SubmissionFileRepository subFileRepo;
    private final AssignmentService assignments;
    private final FileService files;

    public SubmissionServiceImpl(SubmissionRepository repo, SubmissionFileRepository subFileRepo, AssignmentService assignments, FileService files) {
        this.repo = repo;
        this.subFileRepo = subFileRepo;
        this.assignments = assignments;
        this.files = files;
    }

    @Override
    public SubmissionDto create(String studentId, SubmissionDto submission) {
        int sid = strToStudentId(studentId);
        if (!assignments.exists(submission.assignmentId))
            throw new DependentEntityNotFoundException("Assignment does not exist");
        if (!files.allExist(submission.fileIds))
            throw new DependentEntityNotFoundException("Some files do not exist");
        int aid = Integer.parseInt(submission.assignmentId);
        if (repo.existsByStudentIdAndAssignmentId(sid, aid))
            throw new BadRequestException("Submission already exists for this assignment");

        if (assignments.isClosed(aid))
            throw new BadRequestException("Assignment is closed");

        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        SubmissionModel model = new SubmissionModel(0, sid, aid, false, now);
        SubmissionModel saved = repo.save(model);
        List<SubmissionFileModel> files = submission.fileIds.stream()
                .mapToInt(this::strToFileId)
                .mapToObj(id -> new SubmissionFileModel(0, saved.submissionId(), id))
                .toList();
        List<String> fileIds = subFileRepo.saveAll(files).stream().map(m -> idToStr(m.fileId())).toList();
        return new SubmissionDto(idToStr(saved.submissionId()), idToStr(saved.studentId()), idToStr(saved.assignmentId()), fileIds, saved.submittedAt(), saved.isSubmitted());
    }

    @Override
    public boolean owns(String id, String studentId) {
        int sid = strToStudentId(studentId);
        int subid = strToSubmissionId(id);
        return repo.existsByStudentIdAndSubmissionId(sid, subid);
    }

    @Override
    public SubmissionDto update(String id, SubmissionDto submission) {
        if (!files.allExist(submission.fileIds))
            throw new DependentEntityNotFoundException("Some files do not exist");

        int sid = strToSubmissionId(id);
        SubmissionModel model = repo.findById(sid).orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        if (model.isSubmitted())
            throw new InvalidOperationException("Cannot edit submission that is submitted");

        Set<Integer> fileIds = submission.fileIds.stream().map(this::strToFileId).collect(Collectors.toSet());
        Set<Integer> oldFiles = subFileRepo.findAllBySubmissionId(sid).map(SubmissionFileModel::fileId).collect(Collectors.toSet());
        List<Integer> unlinkedFiles = oldFiles.stream()
                .filter(o -> !fileIds.contains(o))
                .toList();
        List<SubmissionFileModel> freshFiles = fileIds.stream()
                .filter(fid -> !oldFiles.contains(fid))
                .map(fid -> new SubmissionFileModel(0, sid, fid))
                .toList();
        if (!unlinkedFiles.isEmpty())
            subFileRepo.deleteUnlinkedFiles(sid, unlinkedFiles);
        if (!freshFiles.isEmpty())
            subFileRepo.saveAll(freshFiles);
        List<String> savedFileIds = subFileRepo.findAllBySubmissionId(sid)
                .map(m -> idToStr(m.fileId())).toList();
        return new SubmissionDto(idToStr(model.assignmentId()), idToStr(model.studentId()), idToStr(model.assignmentId()), savedFileIds, model.submittedAt(), model.isSubmitted());
    }

    @Override
    public boolean submit(String id) {
        int sid = strToSubmissionId(id);
        SubmissionModel model = repo.findById(sid).orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        if (model.isSubmitted())
            return false;
        if (assignments.isClosed(model.assignmentId()))
            throw new BadRequestException("Assignment is closed");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        SubmissionModel submitted = new SubmissionModel(model.submissionId(), model.studentId(), model.assignmentId(), true, now);
        repo.save(submitted);
        return true;
    }

    @Override
    public String owningCourseId(String id) {
        int subid = strToSubmissionId(id);
        return idToStr(repo.findOwningCourseId(subid).orElseThrow(() -> new EntityNotFoundException("Submission not found")));
    }

    @Override
    public void delete(String id) {
        int subid = strToSubmissionId(id);
        SubmissionModel model = repo.findById(subid).orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        subFileRepo.deleteAllBySubmissionId(model.submissionId());
        repo.delete(model);
    }

    @Override
    public SubmissionDto find(String id) {
        int subid = strToSubmissionId(id);
        SubmissionModel model = repo.findById(subid).orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        List<String> fileIds = subFileRepo.findAllBySubmissionId(model.submissionId())
                .map(m -> idToStr(m.fileId())).toList();
        return new SubmissionDto(idToStr(model.assignmentId()), idToStr(model.studentId()), idToStr(model.assignmentId()), fileIds, model.submittedAt(), model.isSubmitted());
    }

    private int strToSubmissionId(String submissionId) {
        try {
            return Integer.parseInt(submissionId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid submissionId");
        }
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }

    private int strToFileId(String fileId) {
        try {
            return Integer.parseInt(fileId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid fileId");
        }
    }

    private int strToStudentId(String studentId) {
        try {
            return Integer.parseInt(studentId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid studentId");
        }
    }
}
