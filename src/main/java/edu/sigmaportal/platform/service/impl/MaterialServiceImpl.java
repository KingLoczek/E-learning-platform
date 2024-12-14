package edu.sigmaportal.platform.service.impl;

import edu.sigmaportal.platform.dto.MaterialDto;
import edu.sigmaportal.platform.exception.DependentEntityNotFoundException;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.model.MaterialFileModel;
import edu.sigmaportal.platform.model.MaterialModel;
import edu.sigmaportal.platform.repository.MaterialFileRepository;
import edu.sigmaportal.platform.repository.MaterialRepository;
import edu.sigmaportal.platform.service.FileService;
import edu.sigmaportal.platform.service.MaterialService;
import edu.sigmaportal.platform.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository repo;
    private final MaterialFileRepository matFileRepo;
    private final TopicService topics;
    private final FileService files;

    public MaterialServiceImpl(MaterialRepository repo, MaterialFileRepository matFileRepo, TopicService topics, FileService files) {
        this.repo = repo;
        this.matFileRepo = matFileRepo;
        this.topics = topics;
        this.files = files;
    }

    @Override
    public MaterialDto create(MaterialDto material, String authorId) {
        if (!topics.exists(material.topicId))
            throw new DependentEntityNotFoundException("Topic does not exist");

        if (!files.allExist(material.fileIds))
            throw new DependentEntityNotFoundException("Not all files exist");

        int uid = strToUserId(authorId);
        int tid = strToTopicId(material.topicId);
        MaterialModel model = new MaterialModel(0, material.name, material.content, uid, tid);
        MaterialModel saved = repo.save(model);
        List<MaterialFileModel> materialFiles = material.fileIds.stream()
                .mapToInt(this::strToFileId)
                .mapToObj(fid -> new MaterialFileModel(0, saved.materialId(), fid))
                .toList();
        matFileRepo.saveAll(materialFiles);
        return new MaterialDto(idToStr(saved.materialId()), saved.name(), saved.content(), material.fileIds, idToStr(saved.authorId()), idToStr(saved.topicId()));
    }

    @Override
    public String owningCourseId(String id) {
        int mid = strToMaterialId(id);
        return idToStr(repo.findOwningCourseIdByMaterialId(mid).orElseThrow(() -> new EntityNotFoundException("Material does not exist")));
    }

    @Override
    public MaterialDto update(String materialId, MaterialDto material) {
        if (!files.allExist(material.fileIds))
            throw new DependentEntityNotFoundException("Not all files exist");

        int mid = strToMaterialId(materialId);
        MaterialModel old = repo.findById(mid).orElseThrow(() -> new EntityNotFoundException("Material not found"));
        String name = Objects.isNull(material.name) ? old.name() : material.name;
        String content = Objects.isNull(material.content) ? old.content() : material.content;
        int topicId = Objects.isNull(material.topicId) ? old.topicId() : strToTopicId(material.topicId);
        MaterialModel merged = new MaterialModel(old.materialId(), name, content, old.authorId(), topicId);
        MaterialModel saved = repo.save(merged);
        Set<Integer> fileIds = material.fileIds.stream().map(this::strToMaterialId).collect(Collectors.toSet());
        Set<Integer> oldFileIds = matFileRepo.findAllByMaterialId(mid).map(MaterialFileModel::fileId).collect(Collectors.toSet());
        List<MaterialFileModel> freshFiles = fileIds.stream()
                .filter(id -> !oldFileIds.contains(id))
                .map(id -> new MaterialFileModel(0, mid, id)).toList();
        List<Integer> staleFiles = oldFileIds.stream()
                .filter(id -> !fileIds.contains(id))
                .toList();
        if (!freshFiles.isEmpty())
            matFileRepo.saveAll(freshFiles);
        if (!staleFiles.isEmpty())
            matFileRepo.deleteStaleFiles(mid, staleFiles);
        List<String> savedFileIds = matFileRepo.findAllByMaterialId(mid).map(m -> idToStr(m.fileId())).toList();
        return new MaterialDto(idToStr(saved.materialId()), saved.name(), saved.content(), savedFileIds, idToStr(saved.authorId()), idToStr(saved.topicId()));
    }

    @Override
    public void delete(String materialId) {
        int mid = strToMaterialId(materialId);
        MaterialModel model = repo.findById(mid).orElseThrow(() -> new EntityNotFoundException("Material not found"));
        matFileRepo.removeAllByMaterialId(mid);
        repo.delete(model);
    }

    @Override
    public MaterialDto find(String materialId) {
        int mid = strToMaterialId(materialId);
        MaterialModel model = repo.findById(mid).orElseThrow(() -> new EntityNotFoundException("Material not found"));
        List<String> fileIds = matFileRepo.findAllByMaterialId(mid).map(m -> idToStr(m.fileId())).toList();
        return new MaterialDto(idToStr(model.materialId()), model.name(), model.content(), fileIds, idToStr(model.topicId()), idToStr(model.authorId()));
    }

    @Override
    public Collection<String> files(String materialId) {
        int mid = strToMaterialId(materialId);
        return matFileRepo.findAllByMaterialId(mid).map(m -> idToStr(m.fileId())).toList();
    }

    private String idToStr(int id) {
        return Integer.toString(id);
    }

    private int strToMaterialId(String materialId) {
        try {
            return Integer.parseInt(materialId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid material id");
        }
    }

    private int strToFileId(String fileId) {
        try {
            return Integer.parseInt(fileId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid file id");
        }
    }

    private int strToTopicId(String topicId) {
        try {
            return Integer.parseInt(topicId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid topic id");
        }
    }

    private int strToUserId(String authorId) {
        try {
            return Integer.parseInt(authorId);
        } catch (NumberFormatException e) {
            throw new DependentEntityNotFoundException("Invalid user id");
        }
    }
}
