package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.GradeDto;
import edu.sigmaportal.platform.exception.BadRequestException;
import edu.sigmaportal.platform.exception.DependentEntityNotFoundException;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.exception.InvalidOperationException;
import edu.sigmaportal.platform.model.GradeModel;
import edu.sigmaportal.platform.repository.GradeRepository;
import edu.sigmaportal.platform.service.GradeService;
import edu.sigmaportal.platform.service.SubmissionService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository repo;
    private final SubmissionService submissions;

    public GradeServiceImpl(GradeRepository repo, SubmissionService submissions) {
        this.repo = repo;
        this.submissions = submissions;
    }

    @Override
    public GradeDto create(String instructorId, GradeDto grade) {
        int uid = strToInstructorId(instructorId);
        if (grade.grade == null)
            throw new BadRequestException("Missing grade");
        if (!submissions.exists(grade.submissionId))
            throw new DependentEntityNotFoundException("Submission does not exist");
        int sid = strToSubmissionId(grade.submissionId);
        if (!submissions.isSubmitted(sid))
            throw new InvalidOperationException("Submission is not yet submitted");
        if (repo.existsBySubmissionId(sid))
            throw new InvalidOperationException("Submission is already graded");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        GradeModel model = new GradeModel(0, sid, grade.grade, grade.feedback, now, uid);
        GradeModel saved = repo.save(model);
        return new GradeDto(idToStr(saved.gradeId()), idToStr(saved.submissionId()), model.grade(), model.feedback(), model.gradedAt(), idToStr(saved.instructorId()));
    }

    @Override
    public GradeDto update(String id, GradeDto grade) {
        int gid = strToGradeId(id);
        GradeModel old = repo.findById(gid).orElseThrow(() -> new EntityNotFoundException("Grade not found"));
        int value = Objects.isNull(grade.grade) ? old.grade() : grade.grade;
        String feedback = Objects.isNull(grade.feedback) ? old.feedback() : grade.feedback;
        GradeModel merged = new GradeModel(old.gradeId(), old.submissionId(), value, feedback, old.gradedAt(), old.instructorId());
        GradeModel saved = repo.save(merged);
        return new GradeDto(idToStr(saved.gradeId()), idToStr(saved.submissionId()), saved.grade(), saved.feedback(), saved.gradedAt(), idToStr(saved.instructorId()));
    }

    @Override
    public void delete(String id) {
        int gid = strToGradeId(id);
        GradeModel model = repo.findById(gid).orElseThrow(() -> new EntityNotFoundException("Grade not found"));
        repo.delete(model);
    }

    @Override
    public String owningCourseId(String id) {
        return idToStr(repo.findOwningCourseIdByGradeId(strToGradeId(id)));
    }

    @Override
    public GradeDto find(String id) {
        int gid = strToGradeId(id);
        GradeModel model = repo.findById(gid).orElseThrow(() -> new EntityNotFoundException("Grade not found"));
        return new GradeDto(idToStr(model.gradeId()), idToStr(model.submissionId()), model.grade(), model.feedback(), model.gradedAt(), idToStr(model.instructorId()));
    }

    @Override
    public boolean hasAccess(String id, String userId) {
        int gid = strToGradeId(id);
        int uid = strToUserId(userId);
        if (repo.existsByGradeIdAndInstructorId(gid, uid)) {
            return true;
        }

        GradeModel model = repo.findById(gid).orElseThrow(() -> new EntityNotFoundException("Grade not found"));
        return submissions.owns(idToStr(model.submissionId()), userId);
    }

    private int strToUserId(String userId) {
        try {
            return Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid userId");
        }
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }

    private int strToSubmissionId(String submissionId) {
        try {
            return Integer.parseInt(submissionId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid submissionId");
        }
    }

    private int strToInstructorId(String instructorId) {
        try {
            return Integer.parseInt(instructorId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid instructorId");
        }
    }

    private int strToGradeId(String gradeId) {
        try {
            return Integer.parseInt(gradeId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid gradeId");
        }
    }
}
