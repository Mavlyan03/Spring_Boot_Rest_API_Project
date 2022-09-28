package com.example.spring_boot_rest_api_project.service;

import com.example.spring_boot_rest_api_project.dto.request.VideoRequest;
import com.example.spring_boot_rest_api_project.dto.response.VideoResponse;
import com.example.spring_boot_rest_api_project.dto.view.VideoResponseView;
import com.example.spring_boot_rest_api_project.exception.NotFoundException;
import com.example.spring_boot_rest_api_project.model.Lesson;
import com.example.spring_boot_rest_api_project.model.Video;
import com.example.spring_boot_rest_api_project.repository.LessonRepository;
import com.example.spring_boot_rest_api_project.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoServiceImpl {

    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;

    public VideoResponse saveVideo(VideoRequest request) {
        Video video = new Video();
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Lesson with id - %s not found", request.getLessonId())));
        video.setVideoName(request.getVideoName());
        video.setLink(request.getLink());
        lesson.setVideo(video);
        video.setLesson(lesson);
        Video video1 = videoRepository.save(video);
        return mapToResponse(video1);
    }

    public VideoResponseView getVideosPagination(String text, int page, int size) {
        VideoResponseView view = new VideoResponseView();
        Pageable pageable = PageRequest.of(page - 1, size);
        view.setVideoResponses(getVideos(search(text, pageable)));
        return view;
    }

    private List<Video> search(String name, Pageable pageable) {
        String text = name == null ? "" : name;
        return videoRepository.searchByVideo(text.toUpperCase(), pageable);
    }

    public List<VideoResponse> getVideos(List<Video> videos) {
        List<VideoResponse> responses = new ArrayList<>();
        for (Video video : videos) {
            responses.add(mapToResponse(video));
        }
        return responses;
    }

    public VideoResponse getById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Video with id - %s not found", id))));
        return mapToResponse(video);
    }

    public VideoResponse updateVideo(Long id, VideoRequest request) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Video with id - %s not found", id))));
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Lesson with id - %s not found", request.getLessonId())));
        video.setVideoName(request.getVideoName());
        video.setLink(request.getLink());
        lesson.setVideo(video);
        video.setLesson(lesson);
        videoRepository.save(video);
        return mapToResponse(video);
    }

    public VideoResponse deleteById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new NotFoundException((String.format("Video with id - %s not found", id))));
        video.setLesson(null);
        videoRepository.delete(video);
        return new VideoResponse(video.getVideoId(), video.getVideoName(),
                video.getLink(), null);
    }

    public List<Video> findAllVideos() {
        return videoRepository.findAll();
    }

    private VideoResponse mapToResponse(Video video) {
        return new VideoResponse(
                video.getVideoId(),
                video.getVideoName(),
                video.getLink(),
                video.getLesson().getLessonId());
    }
}